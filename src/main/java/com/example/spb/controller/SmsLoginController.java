package com.example.spb.controller;

import com.example.spb.entity.User;
import com.example.spb.service.SmsLoginService;
import com.example.spb.service.UserService;
import com.example.spb.utils.SendSMSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Controller
@Api("验证码功能控制类")
@RequestMapping("/sms")
public class SmsLoginController {

    @Autowired
    private SmsLoginService smsLoginService;

    @Autowired
    private UserService userService;
    /**
     * 发送手机验证码
     * @param phoneNumber 手机号码
     * @return 1表示成功，0表示失败
     */
    @PostMapping("/sendSms")
    @ApiOperation("/发送验证码")
    @ResponseBody
    public String SmsTest(@RequestParam("phone") String phoneNumber){
        //判断该手机号是否绑定了用户
        User user = userService.queryUserByPhone(phoneNumber);
        if (user == null){
            return "该手机号未绑定任何学号/工号";
        }
        //发送短信
        String result = SendSMSUtil.sendSmsUtil(phoneNumber);
        if (result == null || !result.equals("OK")) {// 发送不成功
            return "0";
        }
        else {
            int code = SendSMSUtil.getCode();
            return smsLoginService.saveCodeToRedis(phoneNumber,code);
        }
    }

}
