package com.happymall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by onegx on 17-7-13.
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化时候 如果是null的对象 其key也为null 比u如说当没有data时
// 如果没有这个注释,在序列化的时候会返回一个KEY为data的空字段 而实际我们并不需要这个空字段
public class ServerResponse<T> implements Serializable {
    private  int  status;
    private  String msg;
    private T data;

    private ServerResponse(int status){
        this.status = status;
    }

    private ServerResponse(int status, T data){
        this.status = status;
        this.data = data;
    }
    private ServerResponse(int status, String msg){
        this.status = status;
        this.msg = msg;
    }
    private ServerResponse(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.Success.getCode();
    }

    public static<T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.Success.getCode());
    }

    public static<T> ServerResponse<T> createBySuccessMsg(String msg){
        return new ServerResponse<T>(ResponseCode.Success.getCode(),msg);
    }

    public static<T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.Success.getCode(),data);
    }

    public static<T> ServerResponse<T> createBySuccess(String msg, T data){
        return new ServerResponse<T>(ResponseCode.Success.getCode(),msg,data);
    }

    public static<T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.error.getCode(),ResponseCode.error.getDesc());
    }

    public static<T> ServerResponse<T> createByError(String msg){
        return new ServerResponse<T>(ResponseCode.error.getCode(),msg);
    }

    public static <T> ServerResponse<T> createByError(int code, String msg){
        return new ServerResponse<T>(code,msg);
    }
}
