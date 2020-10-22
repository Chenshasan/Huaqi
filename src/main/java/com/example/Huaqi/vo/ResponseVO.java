package com.example.Huaqi.vo;

import com.example.Huaqi.enums.ResponseCode;

/**
 * @author fjj
 * @date 2019/3/12 5:14 PM
 */
public class ResponseVO {

    /**
     * 调用是否成功
     */
    private Integer code;

    /**
     * 返回的提示信息
     */
    private String message;

    /**
     * 内容
     */
    private Object data;

    public static ResponseVO buildSuccess(){
        ResponseVO response=new ResponseVO();
        response.setCode(ResponseCode.Success.getCode());
        response.setData("");
        return response;
    }

    public static ResponseVO buildSuccess(Object content){
        ResponseVO response=new ResponseVO();
        response.setData(content);
        response.setCode(ResponseCode.Success.getCode());
        return response;
    }

    public static ResponseVO buildFailure(String message){
        ResponseVO response=new ResponseVO();
        response.setCode(ResponseCode.Failure.getCode());
        response.setMessage(message);
        System.out.println(message);
        return response;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
