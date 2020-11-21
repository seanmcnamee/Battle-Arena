package app.game.gamefield.movable.enemies;

import java.awt.geom.Point2D.Double;
import java.awt.Color;
import java.awt.image.BufferedImage;

import app.game.gamefield.movable.Movable;
import app.game.gamefield.touchable.HitBox;
import app.supportclasses.GameValues;
import app.supportclasses.SpriteSheet;

public class Enemy extends Movable {
    public enum Type {
        Zombie
    }

    public static Color EnemyColor(Type t) {
        switch(t) {
            case Zombie:
                return Color.magenta;
            default:
                return Color.BLACK;
        }
        
    }


    public Enemy(GameValues gameValues, Double location, BufferedImage image, Type enemyType) {
        super(gameValues, location, EnemyColor(enemyType));
        // TODO Auto-generated constructor stub
        setSizings(enemyType);
    }

    private void setSizings(Type t) {
        SpriteSheet ss = new SpriteSheet(gameValues.SPRITE_SHEET);
        this.hitbox = new HitBox();

        this.image = ss.grabImage(gameValues.SS_PLAYER_LOCATION, gameValues.SS_PLAYER_SIZE, gameValues.SINGLE_BOX_SIZE);
        this.sizeInBlocks = gameValues.PLAYER_SIZE;
    }
    
}
