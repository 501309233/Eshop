package com.qingshixun.project.eshop.module.promo.error;

public interface CommonError {
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
