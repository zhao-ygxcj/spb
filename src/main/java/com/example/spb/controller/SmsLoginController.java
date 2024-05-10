package com.example.spb.controller;

import com.example.spb.entity.User;
import com.example.spb.service.SmsLoginService;
import com.example.spb.service.UserService;
import com.example.spb.utils.JwtUtil;
import com.example.spb.utils.SendSMSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/smsLogin")
    @ApiOperation("/验证码验证登录")
    @ResponseBody
    public ResponseEntity<Object> SmsLogin(@RequestParam("phone") String phoneNumber,
                                           @RequestParam("smsCode") Integer smsCode,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        if (StringUtils.isAnyBlank(phoneNumber,String.valueOf(smsCode))) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("手机号/验证码不能为空");
        }
        if(!smsLoginService.verifyCode(phoneNumber,smsCode)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("手机号或验证码不正确");
        }
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken(userService.queryUserByPhone(phoneNumber).getJobId());
        response.setHeader(JwtUtil.HEADER, token);
        response.setHeader("Access-control-Expost-Headers", JwtUtil.HEADER);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResponseEntity.ok(map);


    }

}
