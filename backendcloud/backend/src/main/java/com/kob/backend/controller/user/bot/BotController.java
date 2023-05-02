package com.kob.backend.controller.user.bot;

import com.kob.backend.common.api.R;
import com.kob.backend.entity.Bot;
import com.kob.backend.service.user.bot.BotService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author August
 * @create 2022-08-31 15:31
 */
@RestController
@RequestMapping("/api/user/bot/")
@AllArgsConstructor
public class BotController {
    private final BotService service;

    @PostMapping("add")
    public R add(@Validated @RequestBody Bot bot) {
        return service.add(bot);
    }

    @PostMapping("update")
    public R update(@Validated @RequestBody Bot bot) {
        return service.update(bot);
    }

    @PostMapping("remove")
    public R remove(@RequestParam Integer id) {
        return service.remove(id);
    }

    @RequestMapping("list/{game}")
    public R list(@PathVariable String game) {
        return service.botList(game);
    }

    @RequestMapping("count")
    public R count(@RequestParam Integer id) {
        return service.count(id);
    }
}
