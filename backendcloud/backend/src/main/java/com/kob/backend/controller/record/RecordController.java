package com.kob.backend.controller.record;

import com.kob.backend.common.api.R;
import com.kob.backend.service.record.RecordService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author August
 * @create 2022-09-15 16:15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/record/")
public class RecordController {
    private final RecordService service;

    @RequestMapping("page")
    public R page(@RequestParam Integer page) {
        return service.page(page);
    }

    @RequestMapping("search/page")
    public R page(@RequestParam String username, @RequestParam String game, @RequestParam Integer page) {
        return service.searchPage(username, game, page);
    }
}
