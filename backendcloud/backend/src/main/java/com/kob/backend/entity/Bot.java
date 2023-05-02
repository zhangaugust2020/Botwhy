package com.kob.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author August
 * @create 2022-08-31 14:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bot implements Serializable {
    private static final long serialVersionUID = -1278533214084536390L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;

    @NotBlank(message = "游戏类型不能为空")
    private String game;
    @NotBlank(message = "语言类型不能为空")
    private String language;
    @Size(min = 1, max = 50, message = "标题长度必须在[1,50]之间")
    @JsonProperty("title")
    private String title;

    @Size(max = 100, message = "Bot简介长度过长")
    @JsonProperty("description")
    private String description = "这个用户很懒, 什么都没有留下~";

    @JsonProperty("content")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updatetime;
}
