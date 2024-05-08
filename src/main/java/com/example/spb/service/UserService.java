package com.example.spb.service;

import com.example.spb.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zff
 * @since 2024-04-29
 */
public interface UserService extends IService<User> {

    int saveUser(User user);

   boolean saveUserFromExcel(MultipartFile file) throws IOException;

    void updateUser(String job_id, User user);

    void deleteUser(String job_id);

    User findByJobID(String job_id);

    int updatePasswordByJobID(String jobID, String newPwd);

    String findPwdByJobID(String jobId);

    int updateAvatarByJobID(String jobID, MultipartFile file) throws IOException;


    User queryUserByPhone(String phone);


}
