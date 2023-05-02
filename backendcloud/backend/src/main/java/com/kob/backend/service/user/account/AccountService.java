package com.kob.backend.service.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.common.api.R;
import com.kob.backend.common.constants.KobConstants;
import com.kob.backend.entity.User;
import com.kob.backend.entity.UserDetailsImpl;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.service.user.post.PostService;
import com.kob.backend.utils.JwtUtil;
import com.kob.backend.utils.RegularUtil;
import com.kob.backend.utils.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author August
 * @create 2022-08-30 15:17
 */
@Service
@AllArgsConstructor
public class AccountService {

    private final UserMapper mapper;
    private final PostService postService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String getToken(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        // 登录失败，会自动处理
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();
        String jwt = JwtUtil.createJWT(user.getId().toString());

        return jwt;
    }

    public User getInfo() {
        User user = UserUtil.getUser();
        user.setPassword("");
        return user;
    }

    public User otherInfo(Integer id) {
        User user = mapper.selectById(id);
        user.setPassword("");
        return user;
    }

    public R register(String username, String password, String confirmedpassword) {
        if (username == null) {
            return R.fail("用户名不能为空");
        }
        if (password == null || confirmedpassword == null) {
            return R.fail("密码不能为空");
        }
        if (!password.equals(confirmedpassword)) {
            return R.fail("两次输入密码不一致");
        }
        username = username.trim();
        if (username.length() == 0) {
            return R.fail("用户名不能为空");
        }
        if (password.length() == 0) {
            return R.fail("密码不能为空");
        }
        if (username.length() > 16) {
            return R.fail("用户名长度不能大于16");
        }
        if (password.length() > 16 ) {
            return R.fail("密码长度不能大于16");
        }
        if (password.length() < 6 ) {
            return R.fail("密码长度不能小于6");
        }

        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", username);
        List<User> userList = mapper.selectList(query);

        if (!userList.isEmpty()) {
            return R.fail("用户名已存在");
        }
        mapper.insert(User
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .photo(KobConstants.PHOTO)
                .rating(KobConstants.RATING)
                .openid(null)
                .email(null)
                .build());
        return R.success();
    }

    public R changePhoto(String url) {
        if (!RegularUtil.isAvatarUrl(url)) {
            return R.fail("图片路径错误, 请检查~");
        }
        User user = UserUtil.getUser();
        user.setPhoto(url);
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("id", user.getId());
        postService.updateAvatar(user.getId(), url);
        return R.data(mapper.update(user, query));
    }

    public R updateInfo(String username, String email) {
        User loginUser = UserUtil.getUser();
        loginUser.setUsername(username);
        loginUser.setEmail(email);
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("id", loginUser.getId());
        return R.data(mapper.update(loginUser, query));
    }

}
