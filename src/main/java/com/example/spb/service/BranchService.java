package com.example.spb.service;

import com.example.spb.entity.Branch;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spb.entity.User;

import java.util.ArrayList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zff
 * @since 2024-05-21
 */
public interface BranchService extends IService<Branch> {

    int addBranchManager(int branchID, String jobID);

    Boolean isExistBranch(String branchName);

    int queryBranchByJobID(String jobID);

    int queryBranchIDByBranchName(String branchName);

    void addBranch(String branchName);

    void deleteBranch(String branchName);

    void updateBranch(String branchName, String currentName);

    ArrayList<User> queryBranchMember(int branchID);

    void deleteBranchManager(int branchID);

    int queryBranchIDByJobID(String jobID);
}
