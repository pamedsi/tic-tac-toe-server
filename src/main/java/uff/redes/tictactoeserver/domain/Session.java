package uff.redes.tictactoeserver.domain;

import uff.redes.tictactoeserver.dto.Player;

import java.util.UUID;

public class Session {
    private final UUID id;
    private final Player player;
    private boolean WSConnected;

    public Session(UUID id, Player player){
        this.id = id;
        this.player = player;
        this.WSConnected = false;
    }

    public UUID getID() {
        return id;
    }

    public Player getPlayer () {
        return player;
    }

    public boolean isWSConnected() {
        return WSConnected;
    }

    public void setWSConnected(boolean WSConnected) {
        this.WSConnected = WSConnected;
    }
}