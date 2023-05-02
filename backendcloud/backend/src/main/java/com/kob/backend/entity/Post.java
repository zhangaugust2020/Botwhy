package com.kob.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author August
 * @create 2022-09-02 10:33
 */
@Data
public class Post implements Serializable {
    private static final long serialVersionUID = 1408332139045362785L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String photo;
    private Integer userId;
    @NotBlank(message = "帖子内容不能为空")
    @Size(max = 10000, message = "帖子长度超出限制")
    @JsonProperty("content")
    private String content;
    @NotBlank(message = "帖子标题不能为空")
    @JsonProperty("title")
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createtime;
}
