package org.example.chessserver.databaseClasses.repositories;

import org.example.chessserver.databaseClasses.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
