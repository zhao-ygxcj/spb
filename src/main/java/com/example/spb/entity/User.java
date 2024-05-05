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

    /**
     * 用户手机号
     */
    private long phone;

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
     * 身份证号
     */
    private String cardId;

    /**
     * 岗位
     */
    private String post;

    /**
     * 绑定微信id
     */
    @TableField("Vx_id")
    private String vxId;

    /**
     * 用户头像
     */
    private String portrait;

    /**
     * 用户性别
     */
    private String sex;
    /**
     * 用户角色
     */
    private String role;


}
