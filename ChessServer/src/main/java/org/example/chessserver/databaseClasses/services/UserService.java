package org.example.chessserver.databaseClasses.services;

import jakarta.transaction.Transactional;
import org.example.chessserver.databaseClasses.entities.User;
import org.example.chessserver.databaseClasses.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void makeFriends(String username1, String username2) {
        User user1 = userRepository.findByUsername(username1)
                .orElseThrow(() -> new RuntimeException("User not found: " + username1));

        User user2 = userRepository.findByUsername(username2)
                .orElseThrow(() -> new RuntimeException("User not found: " + username2));

        user1.addFriend(user2);

        userRepository.save(user1);
        userRepository.save(user2);
    }
}
