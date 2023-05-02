package com.kob.backend.controller.user.post;

import com.kob.backend.common.api.R;
import com.kob.backend.entity.Post;
import com.kob.backend.service.user.post.PostService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author August
 * @create 2022-09-02 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/user/post/")
public class PostController {
    private final PostService service;

    @PostMapping("add")
    public R add(@Validated @RequestBody Post post) {
        return service.add(post);
    }

    @PostMapping("update")
    public R update(@Validated @RequestBody Post post) {
        return service.update(post);
    }

    @PostMapping("remove")
    public R remove(@RequestParam Integer id) {
        return service.remove(id);
    }

    @RequestMapping("list")
    public R userPostlist(@RequestParam Integer id) {
        return service.userPostList(id);
    }

    @RequestMapping("allList")
    public R list() {
        return service.postList();
    }

    @RequestMapping("count")
    public R count(@RequestParam Integer id) {
        return service.count(id);
    }

    @RequestMapping("searchmyList")
    public R userSearchPostlist(@RequestParam Integer id, @RequestParam String content) {
        return service.userSearchMyPostList(id, content);
    }

    @RequestMapping("searchAllList")
    public R searchList(@RequestParam String content) {
        return service.searchPostList(content);
    }

    @RequestMapping("searchcheckpostlist")
    public R searchCheckPostList(@RequestParam String username, @RequestParam String content) {
        return service.searchCheckPostList(username, content);
    }
}
