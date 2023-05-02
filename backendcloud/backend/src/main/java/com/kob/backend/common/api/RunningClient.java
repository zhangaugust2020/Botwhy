package com.kob.backend.common.api;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.kob.backend.common.constants.KobConstants;

import java.util.Map;

/**
 * @author August
 * @create 2022-09-25 13:42
 */

public interface RunningClient {
    @Post(
            value = KobConstants.END,
            headers = {
                    "X-ECC-Current-Tenant: 10000",
                    "Accept-Language: zh-CHS"
            }
    )
    Map<String, String> end(@JSONBody Map data);

    @Post(
            value = KobConstants.COMPILE,
            headers = {
                    "X-ECC-Current-Tenant: 10000",
                    "Accept-Language: zh-CHS"
            }
    )
    Map<String, String> compile(@JSONBody Map data);
}
