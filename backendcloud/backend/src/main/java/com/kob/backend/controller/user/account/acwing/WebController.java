package com.kob.backend.controller.user.account.acwing;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.user.account.acwing.WebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author August
 * @create 2022-10-09 20:33
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/user/account")
public class WebController {
    private final WebService service;

    @GetMapping("/acwing/web/apply_code/")
    public JSONObject applyCode() {
        return service.applyCode();
    }

    @GetMapping("/acwing/web/receive_code/")
    public JSONObject receiveCode(@RequestParam Map<String, String> data) {
        String code = data.get("code");
        String state = data.get("state");
        return service.receiveCode(code, state);
    }

}
