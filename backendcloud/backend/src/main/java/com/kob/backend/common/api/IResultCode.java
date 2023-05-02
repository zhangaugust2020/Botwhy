package com.kob.backend.common.api;

/**
 * @author August
 * @create 2022-08-30 9:36
 */
import java.io.Serializable;

public interface IResultCode extends Serializable {
    String getMessage();

    int getCode();
}

