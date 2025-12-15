package org.example.chessserver.databaseClasses.controllers;

import org.example.chessserver.databaseClasses.entities.User;
import org.example.chessserver.databaseClasses.repositories.UserRepository;
import org.example.chessserver.databaseClasses.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/by-username/{username}")
    public Map<String, Object> getUserByUsername(@PathVariable String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        Map<String, Object> json = new HashMap<>();
        json.put("username", user.getUsername());
        json.put("email", user.getEmail());
        json.put("elo", user.getElo());
        json.put("gamesPlayed", user.getGamesPlayed());

        return json;

    }

    @GetMapping("/by-username")
    public Map<String, Object> getUserByUsername(@RequestParam String username1, @RequestParam String username2) {
        Optional<User> optionalUser = userRepository.findByUsername(username1);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        boolean areFriends;
        Set<User> friends = user.getFriends();
        areFriends = !friends.stream().filter(friend -> friend.getUsername().equals(username2)).toList().isEmpty();

        Map<String, Object> json = new HashMap<>();
        json.put("friends", areFriends);

        return json;
    }

    @GetMapping("/get-players")
    public List<Map<String, Object>> getPlayers(@RequestParam String key) {

        List<User> users = userRepository.findAll();
        List<Map<String, Object>> jsonList = new ArrayList<>();

        for (User u : users) {
            if(!u.getUsername().toLowerCase().contains(key.toLowerCase()))
                continue;
            Map<String, Object> map = new HashMap<>();
            map.put("username", u.getUsername());
            map.put("elo", u.getElo());
            jsonList.add(map);
        }

        return jsonList;
    }

    @GetMapping("/get-friends")
    public List<Map<String, Object>> getFriendsOf(@RequestParam String player) {

        Optional<User> optionalUser = userRepository.findByUsername(player);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        List<Map<String, Object>> jsonList = new ArrayList<>();

        for (User friend : user.getFriends()) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", friend.getUsername());
            map.put("elo", friend.getElo());
            jsonList.add(map);
        }

        return jsonList;
    }

    @PostMapping("/add-friend")
    public ResponseEntity<Map<String, Object>> addFriend(@RequestParam("username1") String username1, @RequestParam("username2") String username2)
    {
        userService.makeFriends(username1, username2);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Users are now friends");

        return ResponseEntity.ok(response);
    }

}
