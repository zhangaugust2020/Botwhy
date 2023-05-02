package com.kob.backend.controller.ranklist;

import com.kob.backend.common.api.R;
import com.kob.backend.service.ranklist.RankListService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author August
 * @create 2022-09-16 14:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/ranklist")
public class RankListController {
    private final RankListService service;

    @RequestMapping("page")
    public R page(@RequestParam Integer page) {
        return service.page(page);
    }
}
