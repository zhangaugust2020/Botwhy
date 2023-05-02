package com.kob.backend.service.user.post;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kob.backend.common.api.R;
import com.kob.backend.entity.Post;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.PostMapper;
import com.kob.backend.utils.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author August
 * @create 2022-09-02 10:32
 */
@Service
@AllArgsConstructor
public class PostService {
    private final PostMapper mapper;

    public R postList() {
        QueryWrapper<Post> query = new QueryWrapper<>();
        query.orderByDesc("createtime");
        return R.data(mapper.selectList(query));
    }

    public R userPostList(Integer id) {
        QueryWrapper<Post> query = new QueryWrapper<>();
        query.eq("user_id", id).orderByDesc("createtime");
        return R.data(mapper.selectList(query));
    }

    public R add(Post post) {
        User user = UserUtil.getUser();
        post.setUserId(user.getId());
        post.setUsername(user.getUsername());
        post.setPhoto(user.getPhoto());
        post.setCreatetime(DateUtil.date());
        return R.data(mapper.insert(post));
    }

    public R update(Post post) {
        User user = UserUtil.getUser();
        QueryWrapper<Post> query = new QueryWrapper<>();
        query.eq("id", post.getId()).eq("user_id", user.getId());
        Post post1 = mapper.selectOne(query);
        if (post1 == null) {
            return R.fail("当前帖子不存在或没有权限修改该帖子");
        }
        post.setCreatetime(DateUtil.date());
        return R.data(mapper.update(post, query));
    }

    public R remove(Integer id) {
        User user = UserUtil.getUser();
        QueryWrapper<Post> query = new QueryWrapper<>();
        query.eq("id", id).eq("user_id", user.getId());
        return R.data(mapper.delete(query));
    }

    public R count(Integer id) {
        QueryWrapper query = new QueryWrapper();
        query.eq("user_id", id);
        return R.data(mapper.selectCount(query));
    }

    public R updateAvatar(Integer id, String url) {
        UpdateWrapper<Post> updateWrapper = new UpdateWrapper();
        updateWrapper.set("photo", url).eq("user_id", id);
        return R.data(mapper.update(null, updateWrapper));
    }

    public R searchPostList(String content) {
        QueryWrapper<Post> query = new QueryWrapper<>();
        if ("".equals(content)) {
            content = "%";
        }
        query.like("content", content);
        query.orderByDesc("createtime");
        return R.data(mapper.selectList(query));
    }

    public R userSearchMyPostList(Integer id, String content) {
        QueryWrapper<Post> query = new QueryWrapper<>();
        if ("".equals(content)) {
            content = "%";
        }
        query.like("content", content).eq("user_id", id).orderByDesc("createtime");
        return R.data(mapper.selectList(query));
    }

    public R searchCheckPostList(String username ,String content) {
        QueryWrapper<Post> query = new QueryWrapper<>();
        if ("".equals(username)) {
            username = "%";
        }
        if ("".equals(content)) {
            content = "%";
        }

        query.like("username", username).like("content", content);
        query.orderByDesc("createtime");
        return R.data(mapper.selectList(query));
    }

}
