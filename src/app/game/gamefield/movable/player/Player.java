package app.game.gamefield.movable.player;

import java.awt.Color;
import java.awt.geom.Point2D.Double;
import java.awt.event.KeyEvent;

import app.game.gamefield.movable.Movable;
import app.supportclasses.GameValues;
import app.supportclasses.SpriteSheet;
import app.game.gamefield.touchable.HitBox;

public class Player extends Movable {
    private boolean moveUp, moveDown, moveLeft, moveRight;
    private boolean isRunning;

    public Player(GameValues gameValues, Double location, Color c) {
        super(gameValues, location, Color.GREEN);
        setStats();
        setSizings();
    }

    private void setStats() {
        this.accelerationRate = gameValues.ACCELERATION_RATE;
        this.maxSpeed = gameValues.MAX_SPEED;
        this.isRunning = false;
    }

    private void setSizings() {
        SpriteSheet ss = new SpriteSheet(gameValues.SPRITE_SHEET);
        this.image = ss.grabImage(gameValues.SS_PLAYER_LOCATION, gameValues.SS_PLAYER_SIZE, gameValues.SINGLE_BOX_SIZE);
        this.hitbox = new HitBox();
        this.sizeInBlocks = gameValues.PLAYER_SIZE;
    }
    
    public void keyPressed(KeyEvent e) {
        // System.out.println("Key Pressed");
        if (e.getKeyCode() == gameValues.moveUpKey) {
            this.moveUp = true;
        } else if (e.getKeyCode() == gameValues.moveDownKey) {
            this.moveDown = true;
        } else if (e.getKeyCode() == gameValues.moveLeftKey) {
            this.moveLeft = true;
        } else if (e.getKeyCode() == gameValues.moveRightKey) {
            this.moveRight = true;
        } else if (e.getKeyCode() == gameValues.runKey && !this.isRunning) {
            this.isRunning = true;
            this.maxSpeed *= this.gameValues.RUN_FACTOR;
        }
        accelerate(moveUp, moveDown, moveLeft, moveRight);
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == gameValues.moveUpKey) {
            this.moveUp = false;
        } else if (e.getKeyCode() == gameValues.moveDownKey) {
            this.moveDown = false;
        } else if (e.getKeyCode() == gameValues.moveLeftKey) {
            this.moveLeft = false;
        } else if (e.getKeyCode() == gameValues.moveRightKey) {
            this.moveRight = false;
        } else if (e.getKeyCode() == gameValues.runKey && this.isRunning) {
            this.isRunning = false;
            this.maxSpeed /= this.gameValues.RUN_FACTOR;
        }
        accelerate(moveUp, moveDown, moveLeft, moveRight);
    }

    @Override
    public void updateVelocityAndLocation() {
        double previousX = this.location.x;
        double previousY = this.location.y;
        super.updateVelocityAndLocation();
        updateScreenPosition(this.location.x - previousX, this.location.y - previousY);
    }

    private void updateScreenPosition(double xChange, double yChange) {
        gameValues.fieldXZeroOffset += xChange;
        gameValues.fieldYZeroOffset += yChange;
    }

    /*
    public void updateScreenPosTick()
	{
		double xSVel = 0;
		double ySVel = 0;
		// Distance from center of screen
		// Only moving on the screen when not at its' edge
		// When 1/6 from the edge, push
		double edgeFract = 10;
		if (exploreStyle == 1) {
			if ((xVel < 0 && (xSPos > (Game.WIDTH)/edgeFract)) || (xVel > 0 && xSPos+2 < ((edgeFract-1)*Game.WIDTH/edgeFract))) {
				xSVel = xVel * SpriteSheet.SQUARE;
			}

			if ((yVel < 0 && ySPos > Game.HEIGHT/edgeFract) || (yVel > 0 && ySPos+13 < (edgeFract-1)*Game.HEIGHT/edgeFract)) {
					ySVel = yVel * SpriteSheet.SQUARE;
			}
		}

		// Exploring through chunks
		if (exploreStyle == 2) {
			if ((xVel < 0 && xSPos > 0) || (xVel > 0 && xSPos < Game.WIDTH) || (xVel == 0)) {
				xSVel = xVel * SpriteSheet.SQUARE;
			} else {
				double xSM = xSPos / 8 - (Game.WIDTH / 16.0);
				if (xSM > 0) {
					xSPos = Game.WIDTH / 2 - Game.WIDTH / 2;
				} else {
					xSPos = Game.WIDTH / 2 + Game.WIDTH / 2;
				}
			}

			if ((yVel < 0 && ySPos > 0) || (yVel > 0 && ySPos+9 < Game.HEIGHT) || (yVel == 0)) {
				ySVel = yVel * SpriteSheet.SQUARE;
			} else {
				double ySM = ySPos / 8 - (Game.HEIGHT / 16.0);
				if (ySM > 0) {
					ySPos = Game.HEIGHT / 2 - Game.HEIGHT / 2;
				} else {
					ySPos = Game.HEIGHT / 2 + Game.HEIGHT / 2;
				}
			}
		}
		
		// Stuck at the center, setting position when changing to this mode
		if (exploreStyle == 0 && (xSPos != Game.WIDTH / 2 || ySPos != Game.HEIGHT / 2)) {
			xSPos = Game.WIDTH / 2;
			ySPos = Game.HEIGHT / 2;
		}
		this.xSVel = xSVel;
		this.ySVel = ySVel;
    }  
    */
}
