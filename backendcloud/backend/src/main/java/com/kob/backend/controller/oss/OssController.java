package com.kob.backend.controller.oss;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.common.api.R;
import com.kob.backend.service.oss.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author August
 * @date 2023/4/25 - 19:52
 */
@RestController
@RequestMapping("/api/")
public class OssController {

    @Autowired
    private OssService ossService;

    //上传头像的方法
    @PostMapping("photooss")
    public R uploadOssFile(@RequestParam(value = "photo", required = false) MultipartFile file) {
        //获取上传文件  MultipartFile
        //返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        return R.data(url);
    }
}
