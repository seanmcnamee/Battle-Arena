package app.game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.Color;

import app.game.augments.MiniMap;
import app.game.gamefield.movable.player.Player;
import app.game.gamefield.map.*;
import app.supportclasses.GameValues;

public class GameField {
    private GameValues gameValues;
    private Map gameMap;
    private MiniMap miniMap;
    private Player player;
    
    public GameField(GameValues gameValues, Player player) {
        this.gameValues = gameValues;
        this.player = player;
        this.gameMap = new Map(gameValues);
        this.miniMap = new MiniMap(this.gameMap);
        gameMap.addMovable(player);
    }

    public void tick() {
        gameMap.tick();
    }


    public void render(Graphics g) {
        this.gameValues.fieldXSize = gameValues.WIDTH_SCALE_1*(gameValues.gameScale);
        this.gameValues.fieldYSize = gameValues.HEIGHT_SCALE_1*(gameValues.gameScale);

        this.gameValues.singleSquareX = (gameValues.fieldXSize)/(gameValues.FIELD_X_SPACES+gameValues.WALL_THICKNESS*2);// - gameValues.WALL_THICKNESS*2;
        this.gameValues.singleSquareY = (gameValues.fieldYSize)/(gameValues.FIELD_Y_SPACES+gameValues.WALL_THICKNESS*2);// - gameValues.WALL_THICKNESS*2;

        double excessWidth = gameValues.frameWidth-(gameValues.WIDTH_SCALE_1*gameValues.gameScale);
        double excessHeight = gameValues.frameHeight-(gameValues.HEIGHT_SCALE_1*gameValues.gameScale);
        this.gameValues.fieldXStart = excessWidth/2.0;//gameValues.WIDTH_SCALE_1*(gameValues.gameScale-gameValues.GAME_BAR_WIDTH)*.5;
        this.gameValues.fieldYStart = excessHeight/2.0;

        //The 0,0 is at the center of the top left tile (accounting for wall thickness and half the tile size)
        //final double halfABlock = .5;
        //this.gameValues.fieldXZero = gameValues.fieldXStart+(gameValues.singleSquareX*(gameValues.WALL_THICKNESS+halfABlock));
        //this.gameValues.fieldYZero = gameValues.fieldYStart+(gameValues.singleSquareY*(gameValues.WALL_THICKNESS+halfABlock));

        gameMap.render(g);

        this.gameValues.fontSize = 15.1522*gameValues.gameScale - .4976;

        /*
        if (gameValues.debugMode) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.BOLD, (int)gameValues.fontSize));
            DecimalFormat df = new DecimalFormat("#,###,##0.00");
            g.drawString("(" + df.format(player.getX()) + ", " + df.format(player.getY()) + ")", (int)(gameValues.fieldXStart), (int)(gameValues.fieldYStart+.25*gameValues.singleSquareX));
        }*/

        g.setColor(Color.GRAY);
        //g.fillRect(100, 10, 500, 500);
        g.fillRect((int)(this.gameValues.fieldXStart), (int)(gameValues.fieldYStart), (int)(gameValues.fieldXSize), (int)(gameValues.fieldYSize));
        
    }

	public void mouseWheelMoved(MouseWheelEvent e) {
	}

	public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
	}

    /*
    	// When a key is preesed down, this is called
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_E)	{
			player.getInv().setOpen(!player.getInv().isOpen());
		}
		
		//Different things happen when the inventory is open!!
		if (player.getInv().isOpen())	{
			if (key == KeyEvent.VK_ENTER)	{
				player.getInv().switchItemWithHand();
				//player.getInv()
			}
		} 	else	{
			if (key == KeyEvent.VK_F3) {
				DEBUG = !DEBUG;
				// gameMap.removeEntity(player);
			} else if (key == KeyEvent.VK_F4) {
				player.setExplore((player.getExplore() + 1) % 3);
			}	else if(key == KeyEvent.VK_F7)	{
				player.setRockBound(!player.isRockBound());
				System.out.println("Casting Spell");
			}	else if(key == KeyEvent.VK_SHIFT)	{
				player.setRun(true);
			} else {
				setMovement(e, true);
			}

			for (int i = 0; i < player.getInv().getHotBarSize(); i++)	{
				if (key == i+KeyEvent.VK_1)	{
					player.getInv().setSelected(i);
				}
			}
		}
		
		
		
	}

	// When the key it finished being pressed, this is called
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SHIFT)
		{
			player.setRun(false);
		}	else
		{
			setMovement(e, false);
		}
	}

	private void setMovement(KeyEvent e, boolean b) {
		// TODO
		// At the top, figure out what player is playing so it can change the correct
		// one.
		// 'player' is the player running this instance of the Game
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
			player.setUp(b);
		}
		if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
			player.setDown(b);
		}
		if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
			player.setLeft(b);
		}
		if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
			player.setRight(b);
		}
	}

	public void mouseRotated(MouseWheelEvent e)	{
		player.getInv().setSelected(player.getInv().getSelected()+e.getWheelRotation());
    }
    */
}
