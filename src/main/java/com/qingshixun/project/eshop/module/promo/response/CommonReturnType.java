package com.qingshixun.project.eshop.module.promo.response;

public class CommonReturnType {
    //表明对应请求的返回处理结果
    //有success或fail
    //若status=success,则data返回前端需要的json数据
    //若status=fail,则data内使用通用的错误码格式
    private String status;
    private Object data;

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }
    public static CommonReturnType create(Object result,String status){
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
