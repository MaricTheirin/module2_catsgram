package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.InvalidEmailException;
import ru.yandex.practicum.catsgram.exception.UserAlreadyExistException;
import ru.yandex.practicum.catsgram.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final Map<String, User> users = new HashMap<>();

    public User createUser(User user) {
        if (users.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с такой почтой уже существует.");
        }
        return addUser(user);
    }

    public User updateUser(User user) {
        return addUser(user);
    }

    private User addUser(User user) {
          if (user.getEmail() == null || user.getEmail().isBlank()) {
              throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
          }
          users.put(user.getEmail(), user);
          return user;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User findUserByEmail(String email) {
        return users.get(email);
    }

}
