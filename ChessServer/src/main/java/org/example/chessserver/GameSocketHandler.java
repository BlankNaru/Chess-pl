package org.example.chessserver;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class GameSocketHandler extends TextWebSocketHandler {

    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(GameSocketHandler.class);


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject json = new JSONObject(message.getPayload());
        String action = json.getString("action");

        if (action.equals("join")) {
            log.info("Player joined game");
            String gameId = json.getString("gameId");
            Game game = games.computeIfAbsent(gameId, id -> new Game());

            String color = game.addPlayer(session);
            if (color == null) {
                session.sendMessage(new TextMessage("{\"event\":\"error\",\"message\":\"Game full\"}"));
                return;
            }

            // Send assigned color to player
            JSONObject response = new JSONObject();
            response.put("event", "colorAssigned");
            response.put("color", color);
            session.sendMessage(new TextMessage(response.toString()));
        }
        if(action.equals("move"))
        {
            String gameId = json.getString("gameId");
            String move = json.getString("move");

            log.info("Move received: " + move);
            Game game = games.get(gameId);
            if (game != null) {
                for (WebSocketSession player : game.getPlayers()) {
                    player.sendMessage(new TextMessage("{\"event\":\"move\",\"move\":\"" + move + "\"}"));
                }
            }
        }
    }
}
