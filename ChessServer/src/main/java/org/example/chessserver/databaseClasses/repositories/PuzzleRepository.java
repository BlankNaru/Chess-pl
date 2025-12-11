package org.example.chessserver.databaseClasses.repositories;

import org.example.chessserver.databaseClasses.entities.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzleRepository extends JpaRepository<Puzzle, Long> {}
