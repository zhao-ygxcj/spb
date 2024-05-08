package com.example.spb.entity;

import com.example.spb.enums.ResponseCodeEnum;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private ResponseCodeEnum code;
    private String msg;
    private Object data;
    private String stringData;

    public static Result succ(Object data) {
        return succ(ResponseCodeEnum.OK, "操作成功", data);
    }

    public static Result fail(String msg) {
        return fail(ResponseCodeEnum.BAD_REQUEST, msg, null);
    }

    public static Result succ (ResponseCodeEnum responseCodeEnum, String msg, Object data) {
        Result result = new Result();
        result.setCode(responseCodeEnum);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result fail (ResponseCodeEnum responseCodeEnum, String msg, Object data) {
        Result result = new Result();
        result.setCode(responseCodeEnum);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
