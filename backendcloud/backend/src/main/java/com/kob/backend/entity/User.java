package com.kob.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author August
 * @create 2022-08-30 9:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String photo;
    private Integer rating;
    private String openid;
    private String email;
}
