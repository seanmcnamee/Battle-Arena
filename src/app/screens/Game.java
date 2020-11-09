package app.screens;

import java.awt.Graphics;

import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import app.game.GameField;
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
        
        //TODO see if this will work once the button is pressed.
        //initialize();
    }

    public void initialize() {
        PlayerOld player = new PlayerOld(this.gameValues, new Point2D.Double((gameValues.FIELD_X_SPACES-1)/2.0, (gameValues.FIELD_Y_SPACES-1)/2.0));
        gameField = new GameField(this.gameValues, player);
    }


    public void tick() {
        gameField.tick();
    }

    public void render(Graphics g) {
        gameField.render(g);
    }

    public void mouseMoved(MouseEvent e) {
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