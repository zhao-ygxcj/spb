package com.example.spb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author zff
 * @since 2024-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 管理权限
     */
    private Integer authority;

    /**
     * 工号/学号
     */
    private String jobId;

    /**
     * 用户密码
     */
    private String password;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 所属支部
所属支部
     */
    private Integer branchId;

    /**
     * 姓名
     */
    private String name;


    /**
     * 岗位
     */
    private String post;

    /**
     * 用户头像
     */
    private String portrait;

    /**
     * 用户角色
     */
    private String role;


}
