package app.game;

import java.awt.Graphics;

import app.game.serverside.Map;
import app.supportclasses.GameValues;
import app.game.clientside.Player;

public class GameField {
    private GameValues gameValues;
    private Map gameMap;
    private Player player;
    
    public GameField(GameValues gameValues, Player player) {
        this.gameValues = gameValues;
        this.player = player;
        this.gameMap = new Map();
        gameMap.addEntity(player);
    }

    public void tick() {
        gameMap.tick();
    }

    public void render(Graphics g) {
        
    }

}
