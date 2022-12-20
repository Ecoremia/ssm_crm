package com.corely.crm.common.domain;

public class ReturnObject {
    /**
     * 返回给前端的数据
     */
    /**
     * 状态码，0为失败，1为成功
     */
    private String code;
    /**
     * 提示信息
     */
    private String msg;

    private Object retData;

    public ReturnObject() {
    }

    public ReturnObject(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnObject(String code, String msg, Object retData) {
        this.code = code;
        this.msg = msg;
        this.retData = retData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }

    @Override
    public String toString() {
        return "ReturnObject{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", retData=" + retData +
                '}';
    }

}
