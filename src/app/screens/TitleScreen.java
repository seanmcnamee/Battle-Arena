package app.screens;

import java.awt.Graphics;
import javax.swing.JFrame;

import java.awt.Color;


import app.supportclasses.BufferedImageLoader;
import app.supportclasses.Button;
import app.supportclasses.DisplayScreen;
import app.supportclasses.GameValues;
import app.supportclasses.Input;
import app.supportclasses.SpriteSheet;



import java.awt.event.MouseEvent;

/**
 * MainScreen of the App
 */
public class TitleScreen extends DisplayScreen {

    //private final BufferedImageLoader background;
    private Button btnStart;
    private GameValues gameValues;
    private DisplayScreen game;

    public TitleScreen(JFrame frame, GameValues gameValues, Input gameInputs, DisplayScreen game) {
        super(frame);
        //background = new BufferedImageLoader(gameValues.MAIN_MENU_FILE);
        //SpriteSheet buttons = new SpriteSheet(gameValues.MAIN_MENU_BUTTONS);
        
        btnStart = new Button(1.0, 1.0, (int)(gameValues.START_BUTTON_X*gameValues.WIDTH_SCALE_1), (int)(gameValues.START_BUTTON_Y*gameValues.HEIGHT_SCALE_1), Color.GRAY, gameValues);

        
        this.gameValues = gameValues;
        this.game = game;

        //font = setFont(gameValues);
    }

    /*
    private Font setFont(GameValues gameValues) {
        Font returningFont = null;
        try {
            InputStream myStream = new BufferedInputStream(new FileInputStream(gameValues.gameFontFile));
            Font temp = Font.createFont(Font.TRUETYPE_FONT, myStream);
            returningFont = temp.deriveFont(Font.PLAIN, 50);          
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Font not loaded.");
        }
        return returningFont;
    }
    */

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, (int)(gameValues.WIDTH_SCALE_1*gameValues.gameScale), (int)(gameValues.HEIGHT_SCALE_1*gameValues.gameScale));
        btnStart.render(g);

        //g.setFont(font);
        //g.drawString("START", mainGUI.getContentPane().getWidth()/2 - 60, (int)(mainGUI.getContentPane().getHeight()*.75));
    }

    public void mouseClicked(MouseEvent e){
        if (btnStart.contains(e.getPoint())) {
            startGame();
        }
        System.out.println("Mouse clicked at: " +e.getPoint());
        
    }

    public void mouseMoved(MouseEvent e) {
        //Set hovering effect for the following buttons...
        //btnStart
        if (!btnStart.isHovering() && btnStart.contains(e.getPoint())) {
            btnStart.setHovering(true);
        }   else if (btnStart.isHovering() && !btnStart.contains(e.getPoint())) {
            btnStart.setHovering(false);
        //btnCredits
        }
    }

    private void startGame() {
        System.out.println("Starting Game");
        System.out.println("Setting currentScreen to 'game'");
        System.out.println("Game: " + game);
        ((Game)game).initialize();
        gameValues.currentScreen = game;
    }

    private void exitGame() {
        gameValues.gameState = GameValues.GameState.QUIT;
        System.exit(0);
    }

}