package app.game.server;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.Game;

public class Item {
	BufferedImage image;
	
	public Item(BufferedImage i)	{
		image = i;
	}
	
	public void render(Graphics g, double xPos, double yPos, double xSize, double ySize)	{
		g.drawImage(image, (int)(xPos*Game.SCALE), (int)(yPos*Game.SCALE), (int)(xSize*Game.SCALE), (int)(ySize*Game.SCALE), null);
	}
	
	public BufferedImage getImg()	{
		return image;
	}
}
