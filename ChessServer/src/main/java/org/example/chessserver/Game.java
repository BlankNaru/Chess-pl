package org.example.chessserver;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private final List<WebSocketSession> players = Collections.synchronizedList(new ArrayList<>());
    private final Map<WebSocketSession, String> colors = new ConcurrentHashMap<>();

    public synchronized String addPlayer(WebSocketSession session) {
        if (players.size() >= 2) {
            return null;
        }

        players.add(session);

        String color = players.size() == 1 ? "white" : "black";
        colors.put(session, color);

        return color;
    }

    public List<WebSocketSession> getPlayers() {
        return players;
    }

    public String getColor(WebSocketSession session) {
        return colors.get(session);
    }
}

