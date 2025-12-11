package org.example.chessserver.databaseClasses.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "puzzles")
public class Puzzle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String playerToMove;
    private String moveHistory;

    @Column(columnDefinition = "LONGTEXT")
    private String fieldsJson;


    public Long getId() { return id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPlayerToMove() { return playerToMove; }
    public void setPlayerToMove(String playerToMove) { this.playerToMove = playerToMove; }

    public String getMoveHistory() { return moveHistory; }
    public void setMoveHistory(String moveHistory) { this.moveHistory = moveHistory; }

    public String getFieldsJson() { return fieldsJson; }
    public void setFieldsJson(String fieldsJson) { this.fieldsJson = fieldsJson; }
}
