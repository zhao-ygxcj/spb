package com.example.spb.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.spb.entity.Branch;
import com.example.spb.entity.User;
import com.example.spb.mapper.BranchMapper;
import com.example.spb.mapper.UserMapper;
import com.example.spb.service.BranchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zff
 * @since 2024-05-21
 */
@Service
public class BranchServiceImpl extends ServiceImpl<BranchMapper, Branch> implements BranchService {
    @Autowired
    private BranchMapper branchMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int addBranchManager(int branchID, String jobID) {

        Branch branch = new Branch();
        branch.setBranchId(branchID);
        branch.setManagerId(jobID);
        return branchMapper.updateById(branch);
    }

    @Override
    public Boolean isExistBranch(String branchName) {
//        branchID作为主键被查询
        QueryWrapper<Branch> queryWrapper = new QueryWrapper();
        queryWrapper.eq("branch_name",branchName);
        Branch branch = branchMapper.selectOne(queryWrapper);
        if (branch == null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public int queryBranchByJobID(String jobID) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("job_id",jobID);
        return userMapper.selectOne(wrapper).getBranchId();
    }

    @Override
    public int queryBranchIDByBranchName(String branchName) {
        QueryWrapper wrapper= new QueryWrapper();
        wrapper.eq("branch_name",branchName);
        return branchMapper.selectOne(wrapper).getBranchId();
    }

    @Override
    public void addBranch(String branchName) {
        Branch branch = new Branch();
        branch.setBranchName(branchName);
        Date date = new Date();
        branch.setCreateTime(DateTime.of(date.getTime()));
        branchMapper.insert(branch);
    }

    @Override
    public void deleteBranch(String branchName) {
        QueryWrapper<Branch> wrapper = new QueryWrapper<>();
        wrapper.eq("branch_name",branchName);
        branchMapper.delete(wrapper);
    }

    @Override
    public void updateBranch(String branchName, String currentName) {
        UpdateWrapper<Branch> wrapper = new UpdateWrapper<>();
        wrapper.eq("branch_name",branchName).set("branch_name",currentName);
        branchMapper.update(null,wrapper);
    }

    @Override
    public ArrayList<User> queryBranchMember(int branchID) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        ArrayList<User> list;
        wrapper.ge("branch_id",branchID);
        list = (ArrayList) userMapper.selectList(wrapper);
        return list;
    }

    @Override
    public void deleteBranchManager(int branchID) {
        UpdateWrapper<Branch> wrapper = new UpdateWrapper<>();
        wrapper.eq("branch_id",branchID).set("branch_id",null);
        branchMapper.update(null,wrapper);
    }

    @Override
    public int queryBranchIDByJobID(String jobID) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("job_id",jobID);
        return userMapper.selectOne(wrapper).getBranchId();
    }
}
