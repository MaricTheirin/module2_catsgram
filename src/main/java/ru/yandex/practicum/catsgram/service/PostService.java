package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.PostNotFoundException;
import ru.yandex.practicum.catsgram.exception.UserNotExistException;
import ru.yandex.practicum.catsgram.model.Post;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final Map<Integer, Post> posts = new HashMap<>();
    private final UserService userService;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll(String sortingOrder, Integer size, Integer page) {

        return posts.values().stream()
                .sorted(getCreationDateComparator((sortingOrder)))
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Post> findAllByUserEmail(String email, String sortingOrder, Integer size) {
        return posts.values().stream()
                .filter(post -> post.getAuthor().equals(email))
                .sorted(getCreationDateComparator(sortingOrder))
                .limit(size)
                .collect(Collectors.toList());
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

    private Comparator<Post> getCreationDateComparator(String sortingOrder) {
        return (p1, p2) -> p1.getCreationDate().compareTo(p2.getCreationDate()) * (sortingOrder.equals("desc") ? -1 : 1);
    }
}