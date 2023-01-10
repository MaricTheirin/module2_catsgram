package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.List;

@RestController
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<Post> findAll(
            @RequestParam(required = false, defaultValue = "asc", name = "sort") String sortingOrder,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false, defaultValue = "1") Integer page

    ) {
        if (!sortingOrder.equals("asc") && !sortingOrder.equals("desc")) {
            throw new IllegalArgumentException("Некорректно задан параметр сортировки");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Некорректно задан параметр size");
        }
        if (page < 0) {
            throw new IllegalArgumentException("Некорректно задан параметр page");
        }

        return postService.findAll(sortingOrder, size, page);
    }

    @GetMapping("/posts/{id}")
    public Post getPostById(@PathVariable Integer id) {
        return postService.findPostById(id);
    }

    @PostMapping(value = "/post")
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }
}