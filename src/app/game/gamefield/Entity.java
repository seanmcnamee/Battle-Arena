package app.game.server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import ClientSide.Player;
import ServerSide.Entities.Rock;
import game.Game;
import game.SpriteSheet;

public abstract class Entity implements TickAction {
	protected double xMPos, yMPos;
	protected BufferedImage image;
	protected boolean showOnMiniMap = false;
	protected Color miniMapColor = null;
	
	public double getXMap() {
		return xMPos;
	}

	public double getYMap() {
		return yMPos;
	}
	
	public void render(Graphics g, Entity e)
	{
		double eXM = e.getXMap();
		double eYM = e.getYMap();
		double eXS = Game.WIDTH/2;
		double eYS = Game.HEIGHT/2;
		if (e instanceof PlayerOld)
		{
			eXS = ((PlayerOld) e).getXScreen();
			eYS = ((PlayerOld) e).getYScreen();
			//System.out.println("PLAYER DATA:");
			//System.out.println("Map Pos: " + eXM + ", " + eYM);
			//System.out.println("Screen Pos: " + eXS + ", " + eYS);
		}
		
		//Takes into account entity position in map AND on the screen
		int xUnitSize = image.getWidth()/SpriteSheet.SQUARE;
		int yUnitSize = image.getHeight()/SpriteSheet.SQUARE;
		double xDraw = Game.SCALE*(SpriteSheet.SQUARE*(xMPos-eXM-xUnitSize/2)+eXS);
		double yDraw = Game.SCALE*(SpriteSheet.SQUARE*(yMPos-eYM-yUnitSize/2)+eYS);
		
		//System.out.println("Size in tiles: " + xUnitSize + ", " + yUnitSize);
		
		g.drawImage(image, (int)xDraw, (int)yDraw, (int)image.getWidth()*Game.SCALE, (int)image.getHeight()*Game.SCALE, null);
		
	}
	
	
	public void render(Graphics g)
	{
		/*if (this instanceof Rock)
		{
			System.out.println("Rock is main");
		}
		*/
		int xUnitSize = image.getWidth()/SpriteSheet.SQUARE;
		int yUnitSize = image.getHeight()/SpriteSheet.SQUARE;
		
		double xSPos = Game.WIDTH / 2.0;
		double ySPos = Game.HEIGHT / 2.0;
		if (this instanceof PlayerOld)
		{
			xSPos = ((PlayerOld)this).getXScreen();
			ySPos = ((PlayerOld)this).getYScreen();
		}
		
		double xDraw = (xSPos - xUnitSize/2) * Game.SCALE;
		double yDraw = (ySPos - yUnitSize/2) * Game.SCALE;
		g.drawImage(image, (int)xDraw, (int)yDraw, (int)image.getWidth()*Game.SCALE, 
				(int)image.getHeight()*Game.SCALE, null);
	}
	
	protected void setImage(BufferedImage i)
	{
		image = i;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}

	public boolean isShowOnMap() {
		return showOnMiniMap;
	}

	public void setShowOnMap(boolean showOnMap) {
		this.showOnMiniMap = showOnMap;
	}

	public Color getMiniMapColor() {
		return miniMapColor;
	}

}
