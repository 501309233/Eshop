package com.qingshixun.project.eshop.module.promo.error;

public interface CommonError {
     int getErrCode();
     String getErrMsg();
     CommonError setErrMsg(String errMsg);
}
