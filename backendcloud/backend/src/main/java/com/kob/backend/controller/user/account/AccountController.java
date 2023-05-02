package com.kob.backend.controller.user.account;

import com.kob.backend.common.api.R;
import com.kob.backend.entity.Post;
import com.kob.backend.entity.User;
import com.kob.backend.service.user.account.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author August
 * @create 2022-08-30 15:31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/user/account/")
public class AccountController {
    private final AccountService service;

    @PostMapping("token")
    public R getToken(@RequestParam Map<String, String> map) {
        String username = map.get("username"), password = map.get("password");
        return R.data(service.getToken(username, password));
    }

    @GetMapping("info")
    public R getInfo() {
        return R.data(service.getInfo());
    }

    @PostMapping("register")
    public R register(@RequestParam Map<String, String> map) {
        String username = map.get("username"), password = map.get("password"), confirmedPassword = map.get("confirmedPassword");
        return service.register(username, password, confirmedPassword);
    }

    @PostMapping("photo")
    public R photo(@RequestParam String url) {
        return service.changePhoto(url);
    }

    @RequestMapping("otherInfo")
    public R otherInfo(@RequestParam Integer id) {
        return R.data(service.otherInfo(id));
    }

    @PostMapping("update")
    public R updatrInfo(@RequestParam Map<String, String> map) {
        String username = map.get("username"), email = map.get("email");
        return service.updateInfo(username, email);
    }
}
