package com.qingshixun.project.eshop.module.promo.controller;


import com.qingshixun.project.eshop.dto.MemberDTO;
import com.qingshixun.project.eshop.module.promo.controller.viewobject.PromoVO;
import com.qingshixun.project.eshop.module.promo.error.BusinessException;
import com.qingshixun.project.eshop.module.promo.error.EmBusinessError;
import com.qingshixun.project.eshop.module.promo.response.CommonReturnType;
import com.qingshixun.project.eshop.module.promo.service.PromoService;
import com.qingshixun.project.eshop.module.promo.service.model.PromoModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.stream.Collectors;


@Controller("promo")
@RequestMapping("/promo")
//@CrossOrigin(allowCredentials = "true",origins = "*")
public class PromoController extends BaseController{
    @Autowired
    PromoService promoService;
    /**
     * 返回促销商品页面
     */
    @RequestMapping("/promoPage")
    public String promosPage(){
        return "/promo/listitem";
    }

    @RequestMapping("/getPage")
    public String getPage(){
        return "/promo/getitem";
    }

    /**
     *返回所有商品的信息
     */
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listPromo(Model model){
        List<PromoModel> promoModelList=promoService.listPromoModel();
        List<PromoVO> promoVOList = promoModelList.stream().map(promoModel -> {
            PromoVO promoVO = this.convertFromModel(promoModel);
            return promoVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(promoVOList);
    }

    /**
     *返回商品的信息byId
     */
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public String getPromo(Model model, @RequestParam("id")Long id){
        PromoModel promoModel=promoService.getPromoModelById(id);
        PromoVO promoVO =this.convertFromModel(promoModel);
        model.addAttribute("promoVO",promoVO);
        return "/promo/getitem";
    }

    @RequestMapping(value = "/createorder",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType createOrder(@RequestBody PromoVO promoVO)throws BusinessException {
        //System.out.println(promoVO.getPromoItemNum());
        if (promoVO==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //获取用户信息-->调用baseController方法得到用户信息
        MemberDTO memberDTO = getCurrentUser();
        if (memberDTO==null){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        //将用户信息和活动信息传给servicec处理
        promoService.createOrder(promoVO,memberDTO);
        return CommonReturnType.create(null);
    }

    /**
     * 将促销领域模型的可见数据传给VO模型
     * @param promoModel
     * @return
     */
    private PromoVO convertFromModel(PromoModel promoModel){
        if (promoModel==null){
            return null;
        }
        PromoVO promoVO = new PromoVO();
        BeanUtils.copyProperties(promoModel,promoVO);
        promoVO.setPromoStatus(promoModel.getStatus());
        promoVO.setStartDate(promoModel.getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        return promoVO;
    }


}
