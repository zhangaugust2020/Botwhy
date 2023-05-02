package com.kob.backend.common.constants;

/**
 * 全局常量
 *
 * @author August
 * @create 2022-08-30 9:11
 */
public class KobConstants {
    public static final String PROJECT_NAME = "Botwhy";
    /**
     * 第三方登录常量
     */
    public static final String APPID = "2604"; // 459
    public static final String APPSECRET = "d853f066af914cb09de8e7b40c866f9c";  // c6cad26d057f4394a6d996d743a6adb3
    public static final String ACAPP_REDIRECT_URI = "https://app3426.acapp.acwing.com.cn/api/user/account/acwing/acapp/receive_code/";
    public static final String WEB_REDIRECT_URI = "https://app3426.acapp.acwing.com.cn/user/account/acwing/web/receive_code/";
    public static final String APPLY_ACWING_ACCESS_TOKEN_URL = "https://www.acwing.com/third_party/api/oauth2/access_token/";
    public static final String APPLY_ACWING_USERINFO_URL = "https://www.acwing.com/third_party/api/meta/identity/getinfo/";

    public static final String ADD_PLAYER_URL = "http://127.0.0.1:10301/player/add";
    public static final String REMOVE_PLAYER_URL = "http://127.0.0.1:10301/player/remove";
    public static final String ADD_BOT_URL = "http://127.0.0.1:10302/bot/add";

    public static final String END = "http://120.27.232.248:8000/bot/";
    public static final String COMPILE = "http://120.27.232.248:8000/bot/compile/";

    public static final String PHOTO = "https://cdn.acwing.com/media/user/profile/photo/14816_lg_8874ea8ed6.webp";
    public static final Integer RATING = 1500;
    public static final Integer BOT_USER_ID = 4;

}
