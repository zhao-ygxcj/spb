package com.example.spb.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Test
    void saveUser() {
    }

    @Test
    void saveUserFromExcel() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void findByJobID() {
        System.out.print(userService.findByJobID("123106010735"));
    }
}
