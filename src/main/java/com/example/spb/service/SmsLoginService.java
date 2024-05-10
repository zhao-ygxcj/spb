package com.example.spb.service;

import org.springframework.stereotype.Service;


public interface SmsLoginService {
    String saveCodeToRedis(String phoneNumber, int code);

    boolean verifyCode(String phoneNumber, Integer smsCode);
}
