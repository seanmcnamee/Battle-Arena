package app.game.gamefield.movable.player;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import app.game.gamefield.drawable.DrawingCalculator;
import app.game.gamefield.map.Map;
import app.game.gamefield.movable.Movable;
import app.game.gamefield.movable.projectile.Projectile;
import app.supportclasses.GameValues;
import app.supportclasses.SpriteSheet;
import app.game.gamefield.touchable.HitBox;
import app.game.gamefield.touchable.Touchable;

public class Player extends Movable {
    private boolean moveUp, moveDown, moveLeft, moveRight;
    private boolean isRunning;
    private ArrayList<Projectile> bullets;

    public Player(GameValues gameValues, Point2D.Double location) {
        super(gameValues, location, gameValues.PLAYER_COLOR);
        this.bullets = new ArrayList<Projectile>();
        setStats();
        setSizings();
        System.out.println(this.location.toString());
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
        } else if (e.getKeyCode() == KeyEvent.VK_1) {
            this.centerScreen();
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

    public void mouseClicked(MouseEvent e, Map gameMap){
        System.out.println(this.bullets.size());
        double mouseX = DrawingCalculator.estimateLocationFromPixel(e.getX(), gameValues.fieldXZeroOffset, gameValues.singleSquareX);
        double mouseY = DrawingCalculator.estimateLocationFromPixel(e.getY(), gameValues.fieldYZeroOffset, gameValues.singleSquareY);
        
        double dY = (mouseY - location.y) + percentVelocity.y * maxSpeed;
        double dX = (mouseX - location.x) + percentVelocity.x * maxSpeed;
        double theta = Math.atan(dY/dX);

        double xVel = Math.signum(dX) * Math.cos(theta);
        double yVel = ((dX<0)? -1:1) * Math.sin(theta);
        Point2D.Double projectileVelocity = new Point2D.Double(xVel, yVel);

        double resultantVelocity = Math.sqrt(Math.pow(percentVelocity.x, 2) + Math.pow(percentVelocity.y, 2));
        double projectileMaxSpeed = resultantVelocity*maxSpeed+gameValues.MAX_PROJECTILE_SPEED;

        Projectile ball = new Projectile(this, gameValues, location, projectileVelocity, projectileMaxSpeed, image);

        this.bullets.add(ball);
        gameMap.addMovable(ball);
	}

    @Override
    public void updateLocation(Map m) {
        double previousX = this.location.x;
        double previousY = this.location.y;
        super.updateLocation(m);
        updateScreenPosition(this.location.x - previousX, this.location.y - previousY);
    }

    @Override
    public void updateFromCollision(Touchable t, Map m) {
        if (this.bullets.contains(t)) {
            updateLocation(m);
        } else {
            double previousX = this.location.x;
            double previousY = this.location.y;
            super.updateFromCollision(t, m);
            updateScreenPosition(this.location.x - previousX, this.location.y - previousY);
        }
    }

    private void updateScreenPosition(double xChange, double yChange) {
        centerScreen();
        
        //gameValues.fieldXZeroOffset += xChange;
        //gameValues.fieldYZeroOffset += yChange;
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
