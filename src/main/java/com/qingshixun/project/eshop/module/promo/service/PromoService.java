package com.qingshixun.project.eshop.module.promo.service;

import com.qingshixun.project.eshop.dto.MemberDTO;
import com.qingshixun.project.eshop.module.promo.controller.viewobject.PromoVO;
import com.qingshixun.project.eshop.module.promo.error.BusinessException;
import com.qingshixun.project.eshop.module.promo.service.model.PromoModel;

import java.util.List;

public interface PromoService {
    /**
     * 返回单个活动商品
     * @param itemId
     * @return
     */
    PromoModel getPromoByItemId(Long itemId);

    List<PromoModel> listPromoModel();

    void decreasePromoItemNum(Long itemId);

    PromoModel getPromoModelById(Long id);

    void createOrder(PromoVO promoVO, MemberDTO memberDTO) throws BusinessException;
}
