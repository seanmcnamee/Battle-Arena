package app.game.gamefield.map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import ClientSide.Player;
import game.Game;
import game.SpriteSheet;

public class Tile {

	
	private double xPos;
	private double yPos;
	
	private BufferedImage image;
	
	
	//Give in a x and y position, sets the x and y based on the Image's size
	public Tile(double centerX, double centerY, BufferedImage img)
	{
		image = img;
		xPos = centerX;// - img.getWidth()/2;
		yPos = centerY;// - img.getHeight()/2;
	}
	
	public void tick()
	{
		
	}
	
	//Requires an entity to know exactly who its displaying for
	public void render(Graphics g, Entity e)
	{	
		double eXM = e.getXMap();
		double eYM = e.getYMap();
		double eXS = Game.WIDTH/2;
		double eYS = Game.HEIGHT/2;
		if (e instanceof Player)
		{
			eXS = ((Player) e).getXScreen();
			eYS = ((Player) e).getYScreen();
		}
		
		//Takes into account entity position in map AND on the screen
		int xUnitSize = image.getWidth()/SpriteSheet.SQUARE;
		int yUnitSize = image.getHeight()/SpriteSheet.SQUARE;
		double xDraw = Game.SCALE*(SpriteSheet.SQUARE*(xPos-eXM-xUnitSize/2)+eXS);
		double yDraw = Game.SCALE*(SpriteSheet.SQUARE*(yPos-eYM-yUnitSize/2)+eYS);
		
		g.drawImage(image, (int)xDraw, (int)yDraw, (int)image.getWidth()*Game.SCALE, (int)image.getHeight()*Game.SCALE, null);
		
		
		
		double xDrawText = (Game.SCALE*image.getWidth()*( xPos-eXM+eXS/8.0-.5));;
		double yDrawText = (Game.SCALE*image.getHeight()*( yPos-eYM+eYS/8.0));
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 8));
		g.drawString((int)xPos + "-" + (int)yPos, (int)xDrawText, (int)yDrawText);
		
		
		//g.drawImage(image, (int)(Game.SCALE*(xPos-p.getXM())), (int)(Game.SCALE*(yPos-p.getYM())), (int)image.getWidth()*Game.SCALE, (int)image.getHeight()*Game.SCALE, null);
		//g.drawImage(image, (int)(Game.SCALE*(xPos-p.getXM()+p.getXScreen())), (int)(Game.SCALE*(yPos-p.getYM()+p.getYScreen())), (int)image.getWidth()*Game.SCALE, (int)image.getHeight()*Game.SCALE, null);
	}
	
	
	//TODO rok
	public void render(Graphics g)
	{
		
		double xSPos = Game.WIDTH / 2.0;
		double ySPos = Game.HEIGHT / 2.0;
		
		double xDraw = (Game.SCALE*image.getWidth()*( xPos-Map.MAPSIZE/2+xSPos/8-.5));
		double yDraw = (Game.SCALE*image.getHeight()*( yPos-Map.MAPSIZE/2+ySPos/8-.5));
		g.drawImage(image, (int)xDraw, (int)yDraw, (int)image.getWidth()*Game.SCALE, (int)image.getHeight()*Game.SCALE, null);
		
		
	}
	
	public double getXCenter() {	return xPos + Game.SCALE*image.getWidth()/2 - (Game.SCALE*image.getWidth()/4.5);	}
	public double getYCenter() {	return yPos + Game.SCALE*image.getHeight()/2 - (Game.SCALE*image.getWidth()/4.5) ;	}
	
	public double getX() {	return xPos;	}
	public double getY() {	return yPos;	}
	
	public BufferedImage getImage() {	return image;	}
}
