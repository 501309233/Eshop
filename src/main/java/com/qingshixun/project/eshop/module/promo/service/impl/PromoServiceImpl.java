package com.qingshixun.project.eshop.module.promo.service.impl;

import com.qingshixun.project.eshop.dto.*;
import com.qingshixun.project.eshop.module.order.dao.OrderDaoMyBatis;
import com.qingshixun.project.eshop.module.order.service.OrderItemServiceImpl;
import com.qingshixun.project.eshop.module.product.dao.ProductDaoMyBatis;
import com.qingshixun.project.eshop.module.product.service.ProductServiceImpl;
import com.qingshixun.project.eshop.module.promo.controller.viewobject.PromoVO;
import com.qingshixun.project.eshop.module.promo.dao.PromoDOMapper;
import com.qingshixun.project.eshop.module.promo.dao.dataobject.PromoDO;
import com.qingshixun.project.eshop.module.promo.error.BusinessException;
import com.qingshixun.project.eshop.module.promo.error.EmBusinessError;
import com.qingshixun.project.eshop.module.promo.service.PromoService;
import com.qingshixun.project.eshop.module.promo.service.model.PromoModel;
import com.qingshixun.project.eshop.module.receiver.service.ReceiverServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    PromoDOMapper promoDOMapper;
    @Autowired
    ProductDaoMyBatis productDaoMyBatis;
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    OrderDaoMyBatis orderDaoMyBatis;
    @Autowired
    ReceiverServiceImpl receiverService;
    @Autowired
    OrderDaoMyBatis orderDao;
    @Autowired
    OrderItemServiceImpl orderItemService;

    @Override
    @Transactional
    public PromoModel getPromoByItemId(Long itemId) {
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);
        PromoModel promoModel = this.convertPromoFromDO(promoDO);
        return this.getModelPromoStatus(promoModel);
    }

    /**
     * 返回所有促销商品列表以及促销信息
     * @return
     */
    @Override
    public List<PromoModel> listPromoModel() {
        List<PromoDO> listPromoDO = promoDOMapper.listPromo();
        List<PromoModel> promoModelList = listPromoDO.stream().map(promoDO -> {
            ProductDTO productDTO = productDaoMyBatis.getProduct(promoDO.getItemId());
            PromoModel promoModel = this.convertModelFromDO(productDTO,promoDO);
            promoModel.setStatus(this.setStatus(promoModel));
            return promoModel;
        }).collect(Collectors.toList());

        return promoModelList;
    }

    @Override
    public void decreasePromoItemNum(Long itemId) {
        promoDOMapper.decreasePromoItemNum(itemId);
    }

    @Override
    public PromoModel getPromoModelById(Long id) {
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(id);
        ProductDTO productDTO = productDaoMyBatis.getProduct(promoDO.getItemId());
        PromoModel promoModel = this.convertModelFromDO(productDTO,promoDO);
        promoModel.setStatus(this.setStatus(promoModel));
        return promoModel;
    }

    /**
     * 创建促销活动订单
     * @param promoVO
     * @param memberDTO
     */
    @Override
    @Transactional
    public void createOrder(PromoVO promoVO, MemberDTO memberDTO) throws BusinessException {
        //下订单前先验证这个人是否已经参加过该活动了
        Long memberId = memberDTO.getId();
        Long promoId = promoVO.getId();
        List<OrderDTO> orderDTOList = orderDaoMyBatis.getOrdersByMember(memberId);
        for (OrderDTO order : orderDTOList) {
           String channel = order.getOrderChannel().getCode();
           if (this.verifyPromo(channel,promoVO)){
               throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"您已经参与过了");
           }
        }
        //活动是否开始
        PromoModel promoModel = new PromoModel();
        promoModel = this.convertPromoFromDO(promoDOMapper.selectByPrimaryKey(promoId));
        Integer status = this.getModelPromoStatus(promoModel).getStatus();
        if (status!=2){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动还未开始");
        }
        //活动商品是否售空
        if (promoModel.getPromoItemNum()<1){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动商品已经售空");
        }

        //确定用户没有参加过秒杀活动后，组装订单
        OrderDTO order = new OrderDTO();
        order.setStatus(new OrderStatus("ORDS_UnPay"));
        //将活动id组装进购买渠道上
        order.setOrderChannel(new OrderChannel("ORDC_Web-"+promoId));
        //用默认的收件人
        order.setReceiver(receiverService.getReceiver(memberDTO.getDefaultReceiverId()));
        order.setOrderNum(getOrderNum(memberDTO.getId()));
        order.setMember(memberDTO);

        //将商品详情添加到数据库中
        Map<Long,Integer> productCountMap = new HashMap<>();
        productCountMap.put(promoVO.getItemId(),1);
        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setProduct(productService.getProduct(promoVO.getItemId()));
        orderItem.setProductQuantity(productCountMap.get(promoVO.getItemId()));

        orderItem.setTotalPrice(promoVO.getPromoItemPrice().doubleValue());
        orderItem.setOrder(orderDao.getOrder(order.getId()));
        orderItem.setStatus(new OrderItemStatus("ORIS_UnGrant"));
        orderItemService.saveOrderItem(orderItem);
        orderDao.saveOrUpdateOrder(order);
        //活动商品数量减少
        promoDOMapper.decreasePromoItemNum(promoVO.getItemId());
    }

    /**
     * 生成订单号
     */
    public String getOrderNum(Long memberId) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        String time = format.format(calendar.getTime());
        return time + "1" + String.valueOf(memberId);
    }

    /**
     * 验证是否参与过秒杀
     * @param channel
     * @param promoVO
     * @return
     */
    private Boolean verifyPromo(String channel,PromoVO promoVO){
        Long promoId = promoVO.getId();
        String promoIdStr = promoId.toString();
        String channelId = channel.substring(channel.lastIndexOf('-')+1);
        return promoIdStr.equals(channelId);
    }


    /**
     * 产品数据对象和促销数据对象-->促销模型
     * @param productDTO
     * @param promoDO
     * @return
     */
    private PromoModel convertModelFromDO(ProductDTO productDTO,PromoDO promoDO){
        if (productDTO==null){
            return null;
        }
        PromoModel promoModel = convertPromoFromDO(promoDO);
        //让促销商品模型拿到商品市场价
        promoModel.setItemPrice(BigDecimal.valueOf(productDTO.getMarketPrice()));
        //顺便拿到商品图片
        promoModel.setProductImage(productDTO.getProductImage());
        //处理时间问题
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return this.getModelPromoStatus(promoModel);

    }

    /**
     * 给活动领域模型设置活动状态
     * @param promoModel
     * @return
     */
    private PromoModel getModelPromoStatus(PromoModel promoModel){
        if (promoModel==null){
            return null;
        }
        //判断秒杀活动当前的状态
        //1未开始，2进行中，3已结束
        if (promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if (promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else {
            promoModel.setStatus(2);
        }
        return promoModel;
    }
    //促销数据对象->促销模型
    private PromoModel convertPromoFromDO(PromoDO promoDO){
        if (promoDO==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setPromoItemPrice(BigDecimal.valueOf(promoDO.getPromoItemPrice()));
        return promoModel;
    }

    /**
     * 设置促销状态
     */
    private Integer setStatus(PromoModel promoModel){
        int status = 0;
        if (promoModel.getStartDate().isAfterNow()){
            status=1;
        }else if (promoModel.getEndDate().isBeforeNow()){
            status=3;
        }else {
            status=2;
        }
        return status;
    }
}
