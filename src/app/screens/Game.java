package app.screens;

import java.awt.Graphics;

import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import app.game.GameField;
import app.game.gamefield.movable.player.Player;
import app.supportclasses.DisplayScreen;
import app.supportclasses.GameValues;

/**
 * Main playing screen of the App
 */
public class Game extends DisplayScreen{
    
    private GameValues gameValues;
    private GameField gameField;

    public Game(JFrame frame, GameValues gameValues) {
        super(frame);
        this.gameValues = gameValues;
    }

    public void initialize() {
        Player player = new Player(this.gameValues, 
                        new Point2D.Double((2*gameValues.WALL_THICKNESS+gameValues.MAPSIZE.x-1)/2.0, 
                                            (2*gameValues.WALL_THICKNESS+gameValues.MAPSIZE.y-1)/2.0));
        gameField = new GameField(this.gameValues, player);
    }


    public void tick() {
        gameField.tick();
    }

    public void render(Graphics g) {
        this.gameValues.fieldXSize = gameValues.WIDTH_SCALE_1*(gameValues.gameScale);
        this.gameValues.fieldYSize = gameValues.HEIGHT_SCALE_1*(gameValues.gameScale);

        this.gameValues.singleSquareX = (gameValues.fieldXSize)/(gameValues.FIELD_X_SPACES);
        this.gameValues.singleSquareY = (gameValues.fieldYSize)/(gameValues.FIELD_Y_SPACES);

        double excessWidth = gameValues.frameWidth-(gameValues.WIDTH_SCALE_1*gameValues.gameScale);
        double excessHeight = gameValues.frameHeight-(gameValues.HEIGHT_SCALE_1*gameValues.gameScale);
        this.gameValues.fieldXStart = excessWidth/2.0;
        this.gameValues.fieldYStart = excessHeight/2.0;

        gameField.render(g);
    }

    public void mousePressed(MouseEvent e){
        gameField.mousePressed(e);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        gameField.mouseWheelMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e){
        gameField.keyPressed(e);
        //System.out.println(e.getKeyChar() + " Key Pressed");
    
    }

    @Override
    public void keyReleased(KeyEvent e){
        gameField.keyReleased(e);
        //.out.println(e.getKeyChar() + " Key Released");
    }
    
}