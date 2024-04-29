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

    boolean saveUser(User user);

   void saveUserFromExcel(MultipartFile file) throws IOException;

    void updateUser(long job_id, User user);

    void deleteUser(long job_id);
}
