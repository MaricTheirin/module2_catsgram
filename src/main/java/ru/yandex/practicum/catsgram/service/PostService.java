package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.PostNotFoundException;
import ru.yandex.practicum.catsgram.exception.UserNotExistException;
import ru.yandex.practicum.catsgram.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {
    private final Map<Integer, Post> posts = new HashMap<>();
    private final UserService userService;

    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Post create(Post post) {
        if (userService.findUserByEmail(post.getAuthor()) == null) {
            throw new UserNotExistException("Пользователь " + post.getAuthor() + " не найден");
        }
        post.setId(posts.size() + 1);
        posts.put(post.getId(), post);
        return post;
    }

    public Post getPostById(Integer id) {
        if (posts.containsKey(id)) {
            return posts.get(id);
        }
        throw new PostNotFoundException("Пост с ID = " + id + " не найден");
    }
}