package org.example.chessserver;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    @Getter
    private final List<PlayerInfo> players = Collections.synchronizedList(new ArrayList<>());
    private final Map<PlayerInfo, String> colors = new ConcurrentHashMap<>();
    private final String time;

    public Game(PlayerInfo player1, PlayerInfo player2, String time)
    {
        this.time = time;
        addPlayer(player1);
        addPlayer(player2);
    }
    public synchronized String addPlayer(PlayerInfo player) {
        if (players.size() >= 2) {
            return null;
        }

        players.add(player);
        String color;

        if(players.size() == 2)
        {
            if(Objects.requireNonNull(colors.entrySet().stream().findFirst().orElse(null)).getValue().equals("white"))
                colors.put(player, "black");
            else
                colors.put(player, "white");
        }
        else
        {
            colors.put(player, Math.random() < 0.5 ? "white" : "black");
        }

        color = colors.get(player);
        return color;
    }

    public String getColor(PlayerInfo playerInfo) {
        return colors.get(playerInfo);
    }
}

