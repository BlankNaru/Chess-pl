package com.example.chesspl.puzzle;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ChessApi
{

    @POST("api/puzzles")
    Call<PuzzleDTO> addPuzzle(@Body PuzzleDTO puzzle);

    @GET("api/puzzles")
    Call<List<PuzzleDTO>> getAllPuzzles();

    @DELETE("api/puzzles/clear")
    Call<Void> clearPuzzles();

    @GET("api/puzzles/{id}")
    Call<PuzzleDTO> getPuzzleById(@Path("id") long id);

}
