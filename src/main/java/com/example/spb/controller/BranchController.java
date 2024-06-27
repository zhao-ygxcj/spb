package com.example.spb.controller;


import com.example.spb.entity.User;
import com.example.spb.service.BranchService;
import com.example.spb.utils.BranchUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zff
 * @since 2024-05-21
 */
@Controller
@Api("支部管理类")
@RequestMapping("/branch")
public class BranchController {
    @Autowired
    private BranchService branchService;
    private BranchUtil branchUtil;

    @PostMapping("/addBranch")
    @ApiOperation("添加支部")
    public ResponseEntity<String> addBranch(@RequestParam("branchName") String branchName){

        Boolean isExist = branchService.isExistBranch(branchName);
        if (isExist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("支部名称已存在！");
        }
        branchService.addBranch(branchName);
        return ResponseEntity.ok("成功添加了该党支部");
    }

    @PostMapping("/deleteBranch")
    @ApiOperation("删除支部")
    public ResponseEntity<String> deleteBranch(@RequestParam("branchName") String branchName){

        Boolean isExist = branchService.isExistBranch(branchName);
        if (!isExist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("支部名称不存在！");
        }
        branchService.deleteBranch(branchName);
        return ResponseEntity.ok("成功删除了该党支部");
    }

    @PostMapping("/updateBranch")
    @ApiOperation("编辑支部名称")
    public ResponseEntity<String> updateBranch(@RequestParam("branchName") String branchName,@RequestParam("currentName") String currentName){

        Boolean isExist = branchService.isExistBranch(branchName);
        if (!isExist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("支部名称不存在！");
        }
        branchService.updateBranch(branchName,currentName);
        return ResponseEntity.ok("成功修改了党支部名称");
    }

    @PostMapping("/addBranchManager")
    @ApiOperation("添加支部管理员")
    public ResponseEntity<String> addBranchManager(@RequestParam("branchName") String branchName,@RequestParam("jobID") String jobID){

        //先查询是否有该支部
        Boolean isExist = branchService.isExistBranch(branchName);
        if (!isExist){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("党支部不存在");
        }
        int branchID = branchService.queryBranchIDByBranchName(branchName);
        //再判断用户是否为该支部的成员
        int curBranchID = branchService.queryBranchByJobID(jobID);
        if (branchID != curBranchID){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("设置的用户不是该党支部的成员");
        }
        //最后添加用户为支部的管理员
        branchService.addBranchManager(branchID,jobID);
        return ResponseEntity.ok("成功添加了该党支部管理员");
    }

    @PostMapping("/deleteBranchManager")
    @ApiOperation("删除支部管理员")
    public ResponseEntity<String> deleteBranchManager(@RequestParam("branchName") String branchName){

        //先查询是否有该支部
        Boolean isExist = branchService.isExistBranch(branchName);
        if (!isExist){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("党支部不存在");
        }
        int branchID = branchService.queryBranchIDByBranchName(branchName);

        branchService.deleteBranchManager(branchID);

        return ResponseEntity.ok("成功删除了该党支部管理员");
    }


    @PostMapping("/queryBranchMember")
    @ApiOperation("查看支部成员")
    public ResponseEntity<Object> queryBranchMember(@RequestParam("branchName") String branchName){
        //先查询是否有该支部
        Boolean isExist = branchService.isExistBranch(branchName);
        if (!isExist){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("该支部不存在");
        }
        int branchID = branchService.queryBranchIDByBranchName(branchName);
        ArrayList<User> list;
        list = branchService.queryBranchMember(branchID);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/addActiveBranchManager")
    @ApiOperation("添加积极分子管理员")
    public ResponseEntity<String> addActiveBranchManager(@RequestParam("jobID") String jobID){
        String branchName="积极分子";
        int branchID = 0;
        //先查询是否有积极分子专属党支部
        Boolean isExist = branchService.isExistBranch(branchName);
        if (!isExist){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("积极分子党支部不存在");
        }
        branchService.addBranchManager(branchID,jobID);
        return ResponseEntity.ok("成功添加了积极分子党支部管理员");
    }

    @PostMapping("/queryBranchIDByJobID")
    @ApiOperation("查看用户所在支部")
    public ResponseEntity<Integer> queryBranchIDByJobID(@RequestParam("jobID") String jobID){

        int branchID = branchService.queryBranchIDByJobID(jobID);
        return ResponseEntity.ok(branchID);
    }





}

