package com.kob.backend.service.user.account.acwing;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.common.constants.KobConstants;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.utils.HttpClientUtil;
import com.kob.backend.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * @author August
 * @create 2022-10-09 20:30
 */
@Service
@Slf4j
@AllArgsConstructor
public class WebService {
    private final RedisTemplate<String, String> redisTemplate;
    private final UserMapper userMapper;

    public JSONObject applyCode() {
        JSONObject resp = new JSONObject();
        String encodeUri = "";
        try {
            encodeUri = URLEncoder.encode(KobConstants.WEB_REDIRECT_URI, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            resp.put("result", "failed");
            return resp;
        }
        StringBuilder state = getState();
        resp.put("result", "success");
        redisTemplate.opsForValue().set(state.toString(), "true");
        redisTemplate.expire(state.toString(), Duration.ofMinutes(10));
        System.out.println("redis add key : " + state);
        String applyCodeUrl = "https://www.acwing.com/third_party/api/oauth2/web/authorize/?appid="
                + KobConstants.APPID
                + "&redirect_uri=" + encodeUri
                + "&scope=userinfo"
                + "&state=" + state;
        resp.put("apply_code_url", applyCodeUrl);
        return resp;
    }

    /**
     * 接收第三方返回结果
     */
    public JSONObject receiveCode(String code, String state) {
        // 处理acwing返回信息
        JSONObject resp = new JSONObject();
        resp.put("result", "failed");
        if (code == null || state == null) {
            log.error("code or state is null");
            return resp;
        }
        if (Boolean.FALSE.equals(redisTemplate.hasKey(state))) {
            log.error("redis has not state : " + state);
            return resp;
        }
        redisTemplate.delete(state);

        // 申请token
        List<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("appid", KobConstants.APPID));
        nameValuePairs.add(new BasicNameValuePair("secret", KobConstants.APPSECRET));
        nameValuePairs.add(new BasicNameValuePair("code", code));
        String getString = HttpClientUtil.get(KobConstants.APPLY_ACWING_ACCESS_TOKEN_URL, nameValuePairs);
        if (getString == null) {
            log.error("apply token false");
            return resp;
        }
        JSONObject getResp = JSONObject.parseObject(getString);
        String accessToken = getResp.getString("access_token");
        String openid = getResp.getString("openid");
        if (accessToken == null || openid == null) {
            log.error("accessToken or openid is null");
            return resp;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openid);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            User user = users.get(0);
            String jwt = JwtUtil.createJWT(user.getId().toString());
            resp.put("result", "success");
            resp.put("jwt_token", jwt);
            return resp;
        }

        // 不存在该用户, 继续申请用户信息
        nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", accessToken));
        nameValuePairs.add(new BasicNameValuePair("openid", openid));
        getString = HttpClientUtil.get(KobConstants.APPLY_ACWING_USERINFO_URL, nameValuePairs);
        if (getString == null) {
            log.error("apply info false");
            return resp;
        }
        getResp = JSONObject.parseObject(getString);
        String username = getResp.getString("username");
        String photo = getResp.getString("photo");

        if (username == null || photo == null) {
            log.error("username or photo is null");
            return resp;
        }
        // 处理用户名重复
        for (int i = 0; i < 100; i++) {
            QueryWrapper<User> query = new QueryWrapper<>();
            query.eq("username", username);
            if (userMapper.selectList(query).isEmpty()) {
                break;
            }
            username += (char) (Math.random() * 10 + '0');
            if (i == 99) {
                return resp;
            }
        }
        User user = User
                .builder()
                .username(username)
                .password("")
                .photo(photo)
                .rating(KobConstants.RATING)
                .openid(openid)
                .build();
        userMapper.insert(user);
        String jwt = JwtUtil.createJWT(user.getId().toString());
        resp.put("result", "success");
        resp.put("jwt_token", jwt);
        return resp;
    }

    private StringBuilder getState() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            res.append((char) (Math.random() * 10 + '0'));
        }
        return res;
    }
}
