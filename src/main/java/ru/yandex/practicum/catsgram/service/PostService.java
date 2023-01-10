package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.PostNotFoundException;
import ru.yandex.practicum.catsgram.exception.UserNotExistException;
import ru.yandex.practicum.catsgram.model.Post;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final Map<Integer, Post> posts = new HashMap<>();
    private final UserService userService;

    public List<Post> findAll(String sortingOrder, Integer size, Integer page) {
        Comparator<Post> comparator = (p1, p2) -> {
            int compare = p1.getCreationDate().compareTo(p2.getCreationDate());
            if (sortingOrder.equals("desc")) {
                compare *= -1;
            }
            return compare;
        };
        return posts.values().stream()
                .sorted(comparator)
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
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

    public Post findPostById(Integer id) {
        if (posts.containsKey(id)) {
            return posts.get(id);
        }
        throw new PostNotFoundException("Пост с ID = " + id + " не найден");
    }
}