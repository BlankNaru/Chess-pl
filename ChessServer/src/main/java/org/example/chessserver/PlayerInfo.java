package org.example.chessserver;

import org.springframework.web.socket.WebSocketSession;

public class PlayerInfo {
    public WebSocketSession session;
    public int elo;

    public PlayerInfo(WebSocketSession session, int elo) {
        this.session = session;
        this.elo = elo;
    }
}
