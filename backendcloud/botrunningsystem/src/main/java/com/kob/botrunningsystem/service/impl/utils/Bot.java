package com.kob.botrunningsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author August
 * @create 2022-09-14 9:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bot {
    Integer userId;
    String botCode;
    String input;
    String language;
    String container;
    String status;
    String game;
}
