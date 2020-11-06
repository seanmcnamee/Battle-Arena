package app.game.server;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.Game;

public class InvSlot {
	private double xPos, yPos, xSize, ySize;
	private Item item;
	
	
	public InvSlot(double xP, double yP, double xS, double yS, BufferedImage i)	{
		xPos = xP;
		yPos = yP;
		xSize = xS;
		ySize = yS;
		item = new Item(i);
	}
	
	public InvSlot(double xP, double yP, double xS, double yS)	{
		xPos = xP;
		yPos = yP;
		xSize = xS;
		ySize = yS;
		item = null;
	}
	
	public void setPos(double xP, double yP, double xS, double yS)	{
		xPos = xP;
		yPos = yP;
		xSize = xS;
		ySize = yS;
	}
	
	public void render(Graphics g, boolean selected, boolean invOpen)	{
		if (invOpen)	{
			g.setColor(new Color(255, 255, 255, 200));
		}	else	{
			g.setColor(new Color(0, 0, 0, 100));
		}
		Graphics2D g2 = (Graphics2D) g;
		if (selected)	{
			g2.setStroke(new BasicStroke(6));
			g2.drawRect((int) (xPos * Game.SCALE), (int) (yPos * Game.SCALE), (int) (xSize * Game.SCALE),
					(int) (ySize * Game.SCALE));
		}
		g2.fillRect((int) (xPos * Game.SCALE), (int) (yPos * Game.SCALE), (int) (xSize * Game.SCALE),
				(int) (ySize * Game.SCALE));
		
		if (item != null)	{
			double ratio = 3.0/4.0;
			item.render(g, xPos + ((xSize-xSize*ratio)/2.0), yPos + ((ySize-ySize*ratio)/2.0), xSize*ratio, ySize*ratio);
		}
	}
	
	public Item getItem()	{
		return item;
	}
	
	public void setItem(Item i)	{
		item = i;
	}
	
	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public double getySize() {
		return ySize;
	}

	public void setySize(double ySize) {
		this.ySize = ySize;
	}

	public double getxPos() {
		return xPos;
	}

	public double getxSize() {
		return xSize;
	}
}
