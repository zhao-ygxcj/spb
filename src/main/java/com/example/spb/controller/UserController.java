package com.example.spb.controller;


import com.example.spb.entity.User;
import com.example.spb.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zff
 * @since 2024-04-29
 */
@Controller
@Api("用户控制类")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

//    添加单个用户
    @PostMapping("/add")
    @ApiOperation("添加单个用户")
    public ResponseEntity<Object> saveAdmin(@RequestBody User user) {
        System.out.print(user);
        int isSuccess = userService.saveUser(user);
        if (isSuccess == 1) {
            return ResponseEntity.ok("用户添加成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户添加失败");
        }
    }
    @PostMapping("/test")
    public ResponseEntity<Object> test(){
        return ResponseEntity.ok("hello");
    }

//    批量添加用户
    @PostMapping("/uploadAdminFile")
    public ResponseEntity<String> uploadUserFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文件不能为空");
        }
        try {
            System.out.print(file.isEmpty());
            userService.saveUserFromExcel(file);
            return ResponseEntity.ok("用户上传成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户上传失败");
        }
    }

//    修改用户信息
    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        try {
            String job_id = user.getJobId();
            userService.updateUser(job_id, user);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating user: " + e.getMessage());
        }
    }

//    删除用户信息
    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam("job_id") String job_id) {
        try {
            userService.deleteUser(job_id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting user: " + e.getMessage());
        }
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestParam("inputPwd") String inputPwd, @RequestParam("newPwd") String newPwd,@RequestBody User user) {
        try {
            String prePwd = userService.findPwdByJobID(user.getJobId());
            if (!inputPwd.equals(prePwd)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("原密码错误");
            }
            else {
                String JobID = user.getJobId();
                userService.updatePasswordByJobID(JobID,newPwd);
                return ResponseEntity.ok("Password updated successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error update changepwd: " + e.getMessage());
        }
    }






//



}

