package org.example.chessserver;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class GameSocketHandler extends TextWebSocketHandler {

    private static final int ELO_DIFF_LIMIT = 100;
    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(GameSocketHandler.class);
    private final Map<String, ConcurrentLinkedQueue<PlayerInfo>> waitingQueue = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, String> sessionToGame = new ConcurrentHashMap<>();


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject json = new JSONObject(message.getPayload());
        String action = json.getString("action");

        if(action.equals("search"))
        {
            handleNewPlayer(session, json);
        }
        if(action.equals("cancel"))
        {
            String time = json.getString("time");
            ConcurrentLinkedQueue<PlayerInfo> queue = waitingQueue.get(time);
            if (queue != null) {
                queue.removeIf(p -> p.session.equals(session));
            }
        }
//        if (action.equals("join")) {
//            log.info("Player joined game");
//            String gameId = json.getString("gameId");
//            Game game = games.computeIfAbsent(gameId, id -> new Game());
//
////            String color = game.addPlayer(session);
////            if (color == null) {
////                session.sendMessage(new TextMessage("{\"event\":\"error\",\"message\":\"Game full\"}"));
////                return;
////            }
//
//            // Send assigned color to player
//            JSONObject response = new JSONObject();
//            response.put("event", "colorAssigned");
////            response.put("color", color);
//            session.sendMessage(new TextMessage(response.toString()));
//        }
        if(action.equals("move"))
        {
            String move = json.getString("move");
            String gameId = sessionToGame.get(session);

            log.info("Move received: " + move);
            Game game = games.get(gameId);
            if (game != null) {
                for (PlayerInfo player : game.getPlayers()) {
                    player.session.sendMessage(new TextMessage("{\"event\":\"move\",\"move\":\"" + move + "\"}"));
                }
            }
        }
    }

    private void handleNewPlayer(WebSocketSession session, JSONObject json) throws IOException {
        int elo = Integer.parseInt(json.getString("elo"));
        String time = json.getString("time");
        PlayerInfo currentPlayer = new PlayerInfo(session, elo);
        waitingQueue.putIfAbsent(time, new ConcurrentLinkedQueue<>());
        ConcurrentLinkedQueue<PlayerInfo> queue = waitingQueue.get(time);

        PlayerInfo compatibleOpponent = null;

        for (PlayerInfo p : queue) {
            if (Math.abs(p.elo - elo) <= ELO_DIFF_LIMIT) {
                compatibleOpponent = p;
                break;
            }
        }

        if (compatibleOpponent == null) {
            queue.add(currentPlayer);
            return;
        }

        queue.remove(compatibleOpponent);
        startGame(currentPlayer, compatibleOpponent, time);
    }

    public void startGame(PlayerInfo player1, PlayerInfo player2, String time) throws IOException {
        String gameId = UUID.randomUUID().toString();
        Game game = new Game(player1, player2, time);
        sessionToGame.put(player1.session, gameId);
        sessionToGame.put(player2.session, gameId);
        games.put(gameId, game);

        JSONObject response1 = new JSONObject();
        response1.put("event", "colorAssigned");
        response1.put("color", game.getColor(player1));
        player1.session.sendMessage(new TextMessage(response1.toString()));

        JSONObject response2 = new JSONObject();
        response2.put("event", "colorAssigned");
        response2.put("color", game.getColor(player2));
        player2.session.sendMessage(new TextMessage(response2.toString()));
    }
}
