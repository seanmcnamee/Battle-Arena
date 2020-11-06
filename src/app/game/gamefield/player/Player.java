package app.game.clientside;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import app.game.serverside.Movable;
import app.game.serverside.Inventory;


//This is a player class. Its just an example of how these classes would work-ish
//A Player object would have to be created in the Game class and instantiated.
//tick() and render() methods would have to be called in those methods from Game
public class Player extends Movable {

	private double xSPos, ySPos; // Position on the screen
	private double xSVel, ySVel;
	private boolean up = false, down = false, left = false, right = false; //booleans for movings
	private double runFactor = 1.4;
	private String name;
	
	private double edgeOverLap = .2;
	
	private boolean rockBound = false;
	private int exploreStyle = 1;
	private boolean run = false;
	
	private Inventory inventory;
	
	// The screen position is default the center. The map position is based on the
	// character's CENTER
	public Player(double centerX, double centerY, BufferedImage img, String n) {
		image = img;
		xSPos = Game.WIDTH / 2;
		ySPos = Game.HEIGHT / 2;
		xMPos = centerX;
		yMPos = centerY;
		Acceleration = 1;
		maxSpeed = 12;
		name = n;
		showOnMiniMap = true;
		miniMapColor = Color.YELLOW;
		inventory = new Inventory(4, 9);
	}

	@Override
	public void tick() {
		double tempxVel = getxVel();
		double tempyVel = getyVel();
		double runMult = 1;
		if (run)
		{
			runMult = runFactor;
		}

		// Booleans designating direction for movement
		//Accelerate based on booleans
		if (up) {
			tempyVel -= getCalcAcceleration();
		}
		if (down) {
			tempyVel += getCalcAcceleration();
		}
		if (up ^ down)	{ //Make sure maxSpeed is never surpassed
			if (Math.abs(tempyVel) > getCalcSpeed()*runMult)	{
				tempyVel = Math.signum(tempyVel)*getCalcSpeed()*runMult;
			}
		}	else	{ //Automatically slow down (possibly a friction acceleration?)
			if (Math.abs(tempyVel) > getCalcAcceleration())	{
				tempyVel += Math.signum(-tempyVel)*getCalcAcceleration();
			}	else	{
				tempyVel = 0;
			}
		}
		
		if (left) {
			tempxVel -= getCalcAcceleration();
		}
		if (right) {
			tempxVel += getCalcAcceleration();
		}
		if (left ^ right) {
			if (Math.abs(tempxVel) > getCalcSpeed()*runMult)	{
				tempxVel = Math.signum(tempxVel)*getCalcSpeed()*runMult;
			}
		}	else	{
			if (Math.abs(tempxVel) > getCalcAcceleration())	{
				tempxVel += Math.signum(-tempxVel)*getCalcAcceleration();
			}	else	{
				tempxVel = 0;
			}
		}
		

		if (Math.abs(tempxVel) >= getCalcSpeed()*runMult && Math.abs(tempyVel) >= getCalcSpeed()*runMult) {
			tempxVel /= Math.sqrt(2);
			tempyVel /= Math.sqrt(2);
		}

		// Stay on the map
		int xUnitSize = image.getWidth()/SpriteSheet.SQUARE;
		int yUnitSize = image.getHeight()/SpriteSheet.SQUARE;
		if (xMPos + tempxVel > Map.MAPSIZE -xUnitSize/2 + edgeOverLap || xMPos + tempxVel < xUnitSize/2 - edgeOverLap) {
			tempxVel = 0;
		}
		if (yMPos + tempyVel > Map.MAPSIZE -yUnitSize/2 + edgeOverLap || yMPos + tempyVel < xUnitSize/2 - edgeOverLap) {
			tempyVel = 0;
		}

		xVel = tempxVel;
		yVel = tempyVel;
		//updateScreenPosTick();
	}
	
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
	
	
	public void render(Graphics g) {
		double xDraw = (xSPos - image.getWidth() / 2) * Game.SCALE;
		double yDraw = (ySPos - image.getHeight() / 2) * Game.SCALE;
		g.drawImage(image, (int) xDraw, (int) yDraw, (int) image.getWidth() * Game.SCALE,
		(int) image.getHeight() * Game.SCALE, null);
		// System.out.println((int)(xSPos-image.getWidth()/2)*Game.SCALE + ", " +
		// (int)(ySPos-image.getHeight()/2)*Game.SCALE + " : Player Screen");
		// System.out.println((int)xMPos*Game.SCALE + ", " + (int)yMPos*Game.SCALE + " :
		// Player Map Pos");

		// g.drawImage(image, (int)xSPos*Game.SCALE, (int)ySPos*Game.SCALE,
		// (int)image.getWidth()*Game.SCALE, (int)image.getHeight()*Game.SCALE, null);
		// g.drawImage(image, (int)(Game.SCALE*(xPos-p.getXM())),
		// (int)(Game.SCALE*(yPos-p.getYM())), (int)image.getWidth()*Game.SCALE,
		// (int)image.getHeight()*Game.SCALE, null);

		if (Game.DEBUG) {
			g.setColor(Color.BLACK);
			// g.fillRect(20, 20, 200, 200);
			g.setFont(new Font("Arial", Font.BOLD, 30));

			g.drawString("Map Position (x,y): " + String.format("%.3f", xMPos) + ", " + String.format("%.3f", yMPos),
					10, 30);
			g.drawString("Screen Position (x,y): " + String.format("%.3f", xSPos / 8) + ", "
					+ String.format("%.3f", ySPos / 8), 10, 60);
			// Distance from center X and Y
			double xSM = xSPos / 8 - (Game.WIDTH / 16.0);
			double ySM = ySPos / 8 - (Game.HEIGHT / 16.0);
			g.drawString(
					"Distance from center (x,y): " + String.format("%.3f", xSM) + ", " + String.format("%.3f", ySM), 10,
					90);
			g.drawString("ExploreMode: " + exploreStyle, 10, 150);

			g.drawString("Pixel Position (x,y): " + String.format("%.3f", xSPos) + ", "
					+ String.format("%.3f", ySPos), 10, 120);
			g.drawString("Player: " + name, 10, 180);
			// System.out.println("Debug Mode");
		}
		inventory.render(g);
	}
	

	public double getXScreen() {
		return xSPos;
	}

	public double getYScreen() {
		return ySPos;
	}

	public void setXScreen(double p) {
		xSPos = p;
	}

	public void setYScreen(double p) {
		ySPos = p;
	}
	
	public void resetPos(double pX, double pY)	{
		xSPos = pX;
		ySPos = pY;
		inventory.setSlots(true);
	}

	public void setXMap(double p) {
		xMPos = p;
	}

	public void setYMap(double p) {
		yMPos = p;
	}

	// Movement booleans
	public boolean isUp() {
		return up;
	}

	public boolean isDown() {
		return down;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public void setUp(boolean b) {
		up = b;
	}

	public void setDown(boolean b) {
		down = b;
	}

	public void setLeft(boolean b) {
		left = b;
	}

	public void setRight(boolean b) {
		right = b;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setRockBound(boolean b)
	{
		rockBound = b;
		if (b == false)
		{
			xSPos = Game.WIDTH / 2;
			ySPos = Game.HEIGHT / 2;
		}
	}
	
	public boolean isRockBound()
	{
		return rockBound;
	}
	
	public void setName(String n)
	{
		name = n;
	}
	
	public String getName()	
	{
		return name;
	}

	public int getExplore() {
		return exploreStyle;
	}

	public void setExplore(int explore) {
		this.exploreStyle = explore;
	}
	
	public void updateScreenPos(double xV, double yV)
	{
		xSPos += xV;
		ySPos += yV;
	}

	public double getxSVel() {
		return xSVel;
	}

	public void setxSVel(double xSVel) {
		this.xSVel = xSVel;
	}

	public double getySVel() {
		return ySVel;
	}

	public void setySVel(double ySVel) {
		this.ySVel = ySVel;
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}
	
	public Inventory getInv()	{
		return inventory;
	}
	
}
