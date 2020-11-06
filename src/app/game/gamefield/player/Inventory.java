package app.game.server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import game.BufferedImageLoader;
import game.Game;
import game.SpriteSheet;

public class Inventory {
	private double ySizeRatio = 1.0 / 16.0, xSizeRatio = 3.0/4.0;
	private InvSlot[][] inv; //The [0] is considered the 'hotbar' (bottom)
	private int selected = 0;
	private boolean open = false;
	private InvSlot holding;
	//private double xPos, yPos, xSize, ySize, rim = 1;

	public Inventory(int r, int c)	{
		inv = new InvSlot[r][c];
		setSlots(false);
	}
	public Inventory(int slots) {
		inv = new InvSlot[1][slots];
		setSlots(false);
	}
	
	public Inventory() {
		inv = new InvSlot[1][9];
		setSlots(false);
	}
	
	public void setSlots(boolean reset)	{
		
		SpriteSheet ss = null;
		if (!reset)	{
			BufferedImageLoader loader = new BufferedImageLoader();
			BufferedImage spriteSheet = null;
			try {
				spriteSheet = loader.loadImage("/sprite_sheet.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			ss = new SpriteSheet(spriteSheet);
		}
		
		double xStart, yStart;
		double xS, yS;

		//Values for the Largest Possible
		xS = Game.WIDTH * xSizeRatio;
		yS = Game.HEIGHT * ySizeRatio;
		//Center that shiiiit
		xStart = Game.WIDTH/2 - (xS/2);
		
		int dif = 1;
				
		while (xStart + xS > Game.WIDTH - Math.min(Game.WIDTH, Game.HEIGHT)/8.0 - 7)	{
			xSizeRatio *= .99;
			xS = Game.WIDTH * xSizeRatio;
		}
		
		double smaller = Math.min((xS/inv.length), yS);
		xStart = Game.WIDTH/2 - (smaller*inv[0].length/2);
		yStart = Game.HEIGHT - smaller - 12;
		xS = smaller*inv[0].length;
		yS = smaller-dif;
		//System.out.println("WOOOWWW "inv.length);
		for (int r = 0; r < inv.length; r++)	{
			for (int c = 0; c < inv[r].length; c++)	{
				if (reset)	{
					inv[r][c].setPos(xStart + c*(smaller), yStart - r*(smaller), (smaller-dif) /2.0, (smaller-dif)/2.0);
				}	else	{
					if (r == 0 && c == 0)	{
						holding = new InvSlot(xStart + c*(smaller), yStart - r*(smaller), smaller-dif, smaller-dif);
					}
					if (c == 0)	{
						inv[r][c] = new InvSlot(xStart + c*(smaller), yStart - r*(smaller), smaller-dif, smaller-dif, ss.grabImage((int)(Math.random()*3), (int)(Math.random()*3), 1, 1));
					}	else	{
						inv[r][c] = new InvSlot(xStart + c*(smaller), yStart - r*(smaller), smaller-dif, smaller-dif);
					}
				}
			}
		}
		
		
		//Border info
		//xPos = xStart;
		//yPos = yStart;
		//xSize = xS-dif;
		//ySize = yS;
	}

	public void render(Graphics g) {
		// Print out the entire map
		
		g.setColor(new Color(0, 0, 0, 200));
		//g.draw3
		//g2.drawRect((int)(xPos*Game.SCALE), (int)(yPos*Game.SCALE), (int)(xSize*Game.SCALE), (int)(ySize*Game.SCALE));
		if (open)	{
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		}
		
		for (int r = 0; r < ((open)? inv.length:1); r++)	{
			for (int c = 0; c < inv[r].length; c++)	{
				inv[r][c].render(g, (r == (selected/inv[r].length) && c == (selected%inv[r].length)), open);
			}
		}
		
		if (holding.getItem() != null)	{
			holding.render(g, false, open);
		}
		
	}

	/**
	 * @return A fraction representing how much of the width of the screen the inventory takes up
	 */
	public double getXSizeRatio() {
		return xSizeRatio;
	}
	
	/**
	 * 
	 * @return A fraction representing how much of the height of the screen the inventory takes up
	 */
	public double getYSizeRatio() {
		return ySizeRatio;
	}

	/**
	 * @return The bigger the number, the smaller the Inventory Width will be
	 * @param sizeRatio - size multiplier
	 */
	public void setXSizeRatio(double sizeRatio) {
		this.xSizeRatio = 1.0 / sizeRatio;
	}
	
	/**
	 * @return The bigger the number, the smaller the Inventory Height will be
	 * @param sizeRatio - size multiplier
	 */
	public void setYSizeRatio(double sizeRatio) {
		this.ySizeRatio = 1.0 / sizeRatio;
	}
	
	public void setSelected(int s)	{
		if (s < 0)	{
			s = open? inv.length*inv[0].length -1:inv[0].length-1;
		}
		if (s > (open? inv.length*inv[0].length -1:inv[0].length-1))	{
			s = 0;
		}
		selected = s;
		
		//Set where the holding item is
		holding.setPos(getSelectedSlot().getxPos(), getSelectedSlot().getyPos(), getSelectedSlot().getxSize()/2.0, getSelectedSlot().getySize()/2.0);
	}
	
	public int getSelected()	{
		return selected;
	}
	
	public int getHotBarSize()	{
		return inv[0].length;
	}
	
	public int getInvSize()	{
		return inv.length*inv[0].length;
	}
	
	public boolean isOpen()	{
		return open;
	}
	
	public void setOpen(boolean o)	{
		open = o;
		if (selected > inv[0].length-1)	{
			selected /= inv[0].length;
		}
		if (!o && (holding.getItem() != null))	{
			System.out.println("Inv just closed");
			pickUp(holding.getItem());
			holding.setItem(null);
				//TODO Throw down item
		}
	}
	
	/**
	 * 
	 * @info Places item starting from first element in inventory
	 * @return returns true if an item was successfully added
	 */
	public boolean pickUp(Item i)	{
		for (int r = 0; r < ((open)? inv.length:1); r++)	{
			for (int c = 0; c < inv[r].length; c++)	{
				if (inv[r][c].getItem() == null)	{
					System.out.println("Setting item to " + r + ", " + c);
					inv[r][c].setItem(i);
					return true;
				}
			}
		}
		return false;
	}
	
	public void switchItemWithHand() {
		Item temp = holding.getItem();
		holding.setItem(getSelectedItem());
		setSelectedItem(temp);
	}
	
	public InvSlot getSelectedSlot()	{
		return inv[(selected/inv[0].length)][(selected%inv[0].length)];
	}
	
	public Item getSelectedItem()	{
		return inv[(selected/inv[0].length)][(selected%inv[0].length)].getItem();
	}
	
	public void setSelectedItem(Item i)	{
		inv[(selected/inv[0].length)][(selected%inv[0].length)].setItem(i);
	}
	
	public Item getHolding()	{
		return holding.getItem();
	}
	
	public void setHolding(Item i)	{
		holding.setItem(i);
	}
	
}
