package ru.yandex.practicum.catsgram.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/feed/friends")
public class PostFeedController {

    private final PostService postService;

    @Autowired
    public PostFeedController (PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public List<Post> getFriendFeed(@RequestBody String params) {
        ObjectMapper mapper = new ObjectMapper();
        List<Post> friendFeed= new ArrayList<>();

        try {
            String stringParams = mapper.readValue(params, String.class);
            FriendFeedConfig config = mapper.readValue(stringParams, FriendFeedConfig.class);
            config.getFriends().forEach(
                email -> friendFeed.addAll(
                        postService.findAllByUserEmail(email, config.getSort(), config.getSize())
                )
            );
        } catch (JsonProcessingException e) {
            System.out.println("Ошибка при преобразовании строки: " + params);
        }

        return friendFeed;
    }

    static class FriendFeedConfig {

        private String sort;
        private Integer size;
        private List<String> friends;

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public List<String> getFriends() {
            return friends;
        }

        public void setFriends(List<String> friends) {
            this.friends = friends;
        }

    }

}
