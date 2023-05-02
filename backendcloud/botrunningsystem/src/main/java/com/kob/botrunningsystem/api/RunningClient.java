package com.kob.botrunningsystem.api;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;

import java.util.Map;

/**
 * @author August
 * @create 2022-09-25 13:42
 */

public interface RunningClient {
    @Post(
            value = "http://120.27.232.248:8000/bot/",
            headers = {
                    "X-ECC-Current-Tenant: 10000",
                    "Accept-Language: zh-CHS"
            }
    )
    Map<String, String> run(@JSONBody Map data);
}
