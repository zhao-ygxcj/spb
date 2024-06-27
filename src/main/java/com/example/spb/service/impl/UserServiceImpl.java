package com.example.spb.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.spb.entity.User;
import com.example.spb.mapper.UserMapper;
import com.example.spb.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zff
 * @since 2024-04-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${avatar.save.path}")
    private String avatarPath;

    @Value("${upload.dir.avatar}")
    private String uploadDir;
//    添加用户
    public int saveUser(User user){
        return userMapper.insert(user);
    }
//    批量导入用户
    @Override
    public boolean saveUserFromExcel(MultipartFile file) throws IOException {
        try (InputStream in = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<User> users = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 假设第一行是表头
                User user = new User();

                int authority = (int)(row.getCell(0).getNumericCellValue());
                user.setAuthority(authority);
                System.out.println(authority);

                String id_job = row.getCell(1).getStringCellValue();
                user.setJobId(id_job);
                System.out.println(id_job);

                Cell cell = row.getCell(2); // Get the password cell
                String password = null;
                if (cell.getCellType() == CellType.STRING) {
                    password = cell.getStringCellValue();
                }else {
                    password = String.valueOf((int) row.getCell(2).getNumericCellValue());
                }
                user.setPassword(password);
                System.out.println(password);

                System.out.println(user);
                users.add(user);
            }
            return this.saveBatch(users); // 使用 saveBatch 来提高性能
        }
    }

    @Override
    public void updateUser(String job_id,User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("job_id",job_id);
        userMapper.update(user,wrapper);
    }

    @Override
    public void deleteUser(String job_id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("job_id",job_id);
        userMapper.delete(wrapper);
    }

    @Override
    public User findByJobID(String job_id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("job_id",job_id);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public int updatePasswordByJobID(String jobID, String newPwd) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("job_id",jobID).set("password", newPwd);
        return userMapper.update(null, updateWrapper);
    }

    @Override
    public String findPwdByJobID(String job_id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("job_id",job_id);
        return userMapper.selectOne(wrapper).getPassword();
    }

    @Override
    public int updateAvatarByJobID(String jobID, MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String path = uploadDir + File.separator + filename;
        System.out.println(path);
        File destfile = new File(path);
//                 如果目录不存在，创建目录
        if (!destfile.getParentFile().exists()) {
            destfile.getParentFile().mkdirs();
        }
        file.transferTo(destfile.getAbsoluteFile());

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("job_id",jobID).set("portrait", path);
        return userMapper.update(null, updateWrapper);
    }

    @Override
    public User queryUserByPhone(String phone) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("phone",phone);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public Boolean isPhoneBounded(String jobID) {
        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.eq("job_id",jobID);
        if (userMapper.selectOne(wrapper).getPhone() == null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public String getPortraitByJobId(String job_id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("job_id",job_id);
        String url = userMapper.selectOne(wrapper).getPortrait();
        int lastSlashIndex = url.lastIndexOf("\\");
        String name = " ";
        if (lastSlashIndex != -1) {
            name =  url.substring(lastSlashIndex);
        }
        String path = serverUrl + avatarPath + name;
        return path;
    }

    public void boundPhoneNumber(String job_id, String phone) {
        UpdateWrapper<User> wrapper = new UpdateWrapper();
        wrapper.eq("job_id",job_id).set("phone",phone);
        userMapper.update(null,wrapper);
    }


}
