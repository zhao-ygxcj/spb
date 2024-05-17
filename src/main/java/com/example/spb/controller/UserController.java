package com.example.spb.controller;


import com.example.spb.entity.User;
import com.example.spb.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    @ApiOperation("添加单一用户")
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
    @ApiOperation("测试通路")
    public ResponseEntity<Object> test(){
        return ResponseEntity.ok("hello");
    }

//    批量添加用户
    @PostMapping("/uploadAdminFile")
    @ApiOperation("批量添加用户")
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
    @ApiOperation("修改用户信息")
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
    @ApiOperation("删除用户信息")
    public ResponseEntity<String> deleteUser(@RequestParam("job_id") String job_id) {
        try {
            userService.deleteUser(job_id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting user: " + e.getMessage());
        }
    }

//    修改密码
    @PostMapping("/updatePasswordByPrePwd")
    @ApiOperation("通过原密码修改密码")
    public ResponseEntity<String> updatePasswordByPrePwd(@RequestParam("inputPwd") String inputPwd, @RequestParam("newPwd") String newPwd,@RequestBody User user) {
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error update pwd: " + e.getMessage());
        }
    }

    @PostMapping("/isBoundedPhone")
    @ApiOperation("判断当前用户是否绑定了手机号")
    public ResponseEntity<Boolean> isBoundedPhone(@RequestParam("jobID") String jobID){
        try {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.isPhoneBounded(jobID));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(!userService.isPhoneBounded(jobID));
        }

    }

    //    绑定手机号
    @PostMapping("/boundPhoneNumber")
    @ApiOperation("绑定手机号")
    public ResponseEntity<String> boundPhoneNumber(@RequestParam("phone") String phone, @RequestParam("jobID") String jobID) {
        try {
            userService.boundPhoneNumber(jobID,phone);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error bound phone " + e.getMessage());
        }
        return ResponseEntity.ok(" phone bounded successfully");
    }

    //    修改密码
    @PostMapping("/updatePasswordByphone")
    @ApiOperation("通过手机验证码修改密码")
    public ResponseEntity<String> updatePasswordByPhone(@RequestParam("phone") String phone, @RequestParam("newPwd") String newPwd) {
        try {
            User user = userService.queryUserByPhone(phone);
            String job_id = user.getJobId();
            userService.updatePasswordByJobID(job_id,newPwd);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error update pwd: " + e.getMessage());
        }
        return ResponseEntity.ok("Password updated successfully");
    }

//    上传/更新头像
    @PostMapping("/uploadAvatar")
    @ApiOperation("上传/更新头像")
    public ResponseEntity<String> updateAvatar(@RequestParam("avatar") MultipartFile file,@RequestParam("job_id") String job_id) throws IOException {
        if (file.isEmpty()){
            return ResponseEntity.badRequest().body("请选择上传的文件");
        }
        else {
                userService.updateAvatarByJobID(job_id,file);
                return ResponseEntity.ok("文件上传成功");
            }
        }
//    查询用户信息
    @GetMapping("/queryByJobID")
    @ApiOperation("查询用户信息")
    public ResponseEntity<Object> queryUserByJobID(@RequestParam("job_id") String job_id){
        User user = userService.findByJobID(job_id);
        if (user != null){
            return ResponseEntity.ok(user);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    //    查询用户信息
    @GetMapping("/queryByPhone")
    @ApiOperation("查询用户信息")
    public ResponseEntity<Object> queryUserByPhone(@RequestParam("phone") String phone){
        User user = userService.queryUserByPhone(phone);
        if (user != null){
            return ResponseEntity.ok(user);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    //    显示头像
    @GetMapping("/queryAvatar")
    @ApiOperation("显示头像")
    public ResponseEntity<Object> queryAvatar(@RequestParam("job_id") String job_id) throws IOException {
        User user = userService.findByJobID(job_id);
        String avatar = user.getPortrait();
        if (avatar != null){
            byte[] avatarData = Files.readAllBytes(Paths.get(avatar));
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(avatarData);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    }




//




