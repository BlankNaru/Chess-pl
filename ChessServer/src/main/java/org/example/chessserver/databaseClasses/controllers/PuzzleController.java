package org.example.chessserver.databaseClasses.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.chessserver.databaseClasses.dto.ChessFieldDTO;
import org.example.chessserver.databaseClasses.dto.PuzzleDTO;
import org.example.chessserver.databaseClasses.entities.Puzzle;
import org.example.chessserver.databaseClasses.repositories.PuzzleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puzzles")
@CrossOrigin(origins = "*")
public class PuzzleController {

    private final PuzzleRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();
    @PersistenceContext
    private EntityManager entityManager;

    public PuzzleController(PuzzleRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Puzzle addPuzzle(@RequestBody PuzzleDTO dto) {
        Puzzle puzzle = new Puzzle();
        puzzle.setCategory(dto.getCategory());
        puzzle.setPlayerToMove(dto.getPlayerToMove());
        puzzle.setMoveHistory(dto.getMoveHistory());

        try {
            String json = mapper.writeValueAsString(dto.getFields());
            puzzle.setFieldsJson(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert fields to JSON", e);
        }

        return repo.save(puzzle);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuzzleDTO> getPuzzleById(@PathVariable Long id) {
        return repo.findById(id)
                .map(puzzle -> {
                    PuzzleDTO dto = new PuzzleDTO();
                    dto.setCategory(puzzle.getCategory());
                    dto.setPlayerToMove(puzzle.getPlayerToMove());
                    dto.setMoveHistory(puzzle.getMoveHistory());

                    try {
                        List<List<ChessFieldDTO>> fields = mapper.readValue(
                                puzzle.getFieldsJson(),
                                mapper.getTypeFactory().constructCollectionType(
                                        List.class,
                                        mapper.getTypeFactory().constructCollectionType(List.class, ChessFieldDTO.class)
                                )
                        );
                        dto.setFields(fields);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse fields JSON", e);
                    }

                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Puzzle> getAll() {
        return repo.findAll();
    }

    @DeleteMapping("/clear")
    @Transactional
    public void clearAllPuzzles() {
        repo.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE puzzles AUTO_INCREMENT = 1").executeUpdate();
    }


}
