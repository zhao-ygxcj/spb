package com.example.spb.controller;

import com.example.spb.utils.SendSMSUtil;
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
@RequestMapping("/smsLogin")
public class SmsLoginController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    /**
     * 发送手机验证码
     * @param phoneNumber 手机号码
     * @return 1表示成功，0表示失败
     */
    @PostMapping("/sendSms")
    @ResponseBody
    public String SmsTest(@RequestParam("phone") String phoneNumber){
        //发送短信
        String result = SendSMSUtil.sendSmsUtil(phoneNumber);


        if (result == null || !result.equals("OK")) {// 发送不成功
            return "0";
        }

        // 获取验证码
        int code = SendSMSUtil.getCode();
        Map<String,Object> map=new HashMap<>();
        // 将数据存入redis
        map.put(phoneNumber,code+"");
        //用phoneNumber来做键，可以做到唯一性
        stringRedisTemplate.opsForHash().putAll(phoneNumber,map);
        // 设置redis过期时间,这个时间是秒为单位的，我现在设置5分钟之内有效，过了就会自动删除
        stringRedisTemplate.expire(phoneNumber, 60*5, TimeUnit.SECONDS);

        return "OK";

    }
}
