package com.example.spb.service.impl;


import com.example.spb.entity.User;
import com.example.spb.mapper.UserMapper;
import com.example.spb.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
//    添加用户
    public boolean saveUser(User user){
        return this.save(user);
    }
//    批量导入用户
    @Override
    public void saveUserFromExcel(MultipartFile file) throws IOException {
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

                int id_job = (int) row.getCell(1).getNumericCellValue();
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
            this.saveBatch(users); // 使用 saveBatch 来提高性能
        }
    }

    @Override
    public void updateUser(long job_id, User user) {
        updateById(user);
    }

    @Override
    public void deleteUser(long job_id) {
        removeById(job_id);
    }


}
