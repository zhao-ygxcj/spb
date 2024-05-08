package com.example.spb.controller;

import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.extra.servlet.ServletUtil;
import com.example.spb.entity.Result;
import com.example.spb.entity.User;
import com.example.spb.enums.ResponseCodeEnum;
import com.example.spb.service.UserService;
import com.example.spb.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/login")
public class LoginController {
        @Autowired
        private UserService userService;
        /**
         * 账号密码登录
         *
         * @param jobID    学号/工号
         * @param password 密码
         */
//        @PostMapping("/jobIDLogin")
//        public Result LoginController(@RequestParam String jobID, @RequestParam String password,
//                                      HttpServletRequest request, HttpServletResponse response) {
//                if (StringUtils.isAnyBlank(jobID, password)) {
//                        return Result.fail("用户名和密码不能为空.");
//                }
//                User user = userService.findByJobID(jobID);
//                if (user == null) {
////                        user为空
//                        user = userService.findByJobID(jobID);
//                        return Result.fail("用户名不存在");
//                }
//                if (user!=null){
//                        JwtUtil jwtUtil = new JwtUtil();
//                        String token = jwtUtil.generateToken(jobID);
////                        return Result.succ(ResponseCodeEnum.OK,"登录成功",token);
//                        response.setHeader(JwtUtil.HEADER, token);
//                        response.setHeader("Access-control-Expost-Headers", JwtUtil.HEADER);
//                        Map<String, String> map = new HashMap<>();
//                        map.put("token", token);
//                        return Result.succ(ResponseCodeEnum.OK,"成功登录",map);
//                }
//            return null;
//        }
        @PostMapping("/jobIDLogin")
        public ResponseEntity<Object> jobIDLogin(@RequestParam("job_id") String jobID, @RequestParam("password") String password,
                                      HttpServletRequest request, HttpServletResponse response) {
                if (StringUtils.isAnyBlank(jobID, password)) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("学号/工号和密码不能为空");
                }
                User user = userService.findByJobID(jobID);
                if (user == null) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户不存在a");
                }
                if (user!=null){
                        JwtUtil jwtUtil = new JwtUtil();
                        String token = jwtUtil.generateToken(jobID);
//                        return Result.succ(ResponseCodeEnum.OK,"登录成功",token);
                        response.setHeader(JwtUtil.HEADER, token);
                        response.setHeader("Access-control-Expost-Headers", JwtUtil.HEADER);
                        Map<String, String> map = new HashMap<>();
                        map.put("token", token);
                        return ResponseEntity.ok(map);
//                        return Result.succ(ResponseCodeEnum.OK,"成功登录",map);
                }
                return null;
        }


        @PostMapping("/phoneLogin")
        public ResponseEntity<Object> phoneLogin(@RequestParam("job_id") String jobID, @RequestParam("password") String password,
                                               HttpServletRequest request, HttpServletResponse response){


        }

        @PostMapping("/test")
        public ResponseEntity<Object> test(){
                return ResponseEntity.ok("hello");
        }


}
