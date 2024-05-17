package com.example.spb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spb.entity.User;
import com.example.spb.mapper.UserMapper;
import com.example.spb.service.SmsLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SmsLoginServiceImpl extends ServiceImpl<UserMapper, User> implements SmsLoginService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public String saveCodeToRedis(String phoneNumber, int code) {

//        Map<String,Object> map=new HashMap<>();
//        // 将数据存入redis
//        map.put(phoneNumber,code+"");
        //用phoneNumber来做键，可以做到唯一性
        stringRedisTemplate.opsForValue().append(phoneNumber, String.valueOf(code));
//        stringRedisTemplate.opsForHash().putAll(phoneNumber,map);
        // 设置redis过期时间,这个时间是秒为单位的，我现在设置5分钟之内有效，过了就会自动删除
        stringRedisTemplate.expire(phoneNumber, 60*5, TimeUnit.SECONDS);

        return "OK";
    }
    @Override
    public boolean verifyCode(String phoneNumber, Integer smsCode){
        String code = stringRedisTemplate.opsForValue().get(phoneNumber);
//        String code = "685869";
        if (String.valueOf(smsCode).equals(code)){
            return true;
        }else {
            return false;
        }
    }
}
