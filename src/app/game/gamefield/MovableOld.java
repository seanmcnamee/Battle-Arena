package app.game.server;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.PixelGrabber;

import ClientSide.Player;
import game.Game;
import game.SpriteSheet;

public abstract class MovableOld extends Entity{
	protected double xVel, yVel;
	protected double Acceleration;
	protected double maxSpeed;

	public double getCalcSpeed() {
		return maxSpeed/60.0;
	}
	
	public double getCalcAcceleration() {
		return Acceleration/60.0;
	}
	
	public double getCalcXVel()	{
		return xVel/60.0;
	}
	
	public double getCalcYVel()	{
		return yVel/60.0;
	}
	
	public double getxVel() {
		return xVel;
	}

	public double getyVel() {
		return yVel;
	}
	
	public void updatePosition(double xV, double yV)
	{
		xMPos += xV;
		yMPos += yV;
	}
	
	public boolean checkCollision(Entity e, double xV, double yV)
	{
		//System.out.println("Starting checkCollision: " + xV + "|" + xVel + " - " + yV + "|" + yVel);
		int xUnitSize1 = image.getWidth()/SpriteSheet.SQUARE;
		int yUnitSize1 = image.getHeight()/SpriteSheet.SQUARE;
		Rectangle rect = new Rectangle((int)((xMPos+xV-xUnitSize1/2)*100), (int)((yMPos+yV-yUnitSize1/2)*100), 100*xUnitSize1, 100*yUnitSize1);
		int xUnitSize2 = e.getImage().getWidth()/SpriteSheet.SQUARE;
		int yUnitSize2 = e.getImage().getHeight()/SpriteSheet.SQUARE;
		Rectangle rect2 = new Rectangle((int)((e.getXMap()-xUnitSize2/2)*100), (int)((e.getYMap()-yUnitSize2/2)*100), 100*xUnitSize2, 100*yUnitSize2);
		Rectangle intersection = rect.intersection(rect2);
		
		//System.out.println("Rect1 " + rect.getMinX() + ", " + rect.getMinY() + " : " + rect.getWidth() + ", " + rect.getHeight());
		//System.out.println("Rect2 " + rect2.getMinX() + ", " + rect2.getMinY() + " : " + rect2.getWidth() + ", " + rect2.getHeight());
		//System.out.println("Rect1 " + intersection.getMinX() + ", " + intersection.getMinY() + " : " + intersection.getWidth() + ", " + intersection.getHeight());
		if (intersection.getWidth() > 0 && intersection.getHeight() > 0)
		{
			System.out.println("Hitting!");
			//System.out.println("Rect1 " + rect.getMinX() + ", " + rect.getMinY() + " : " + rect.getWidth() + ", " + rect.getHeight());
			//System.out.println("Rect2 " + rect2.getMinX() + ", " + rect2.getMinY() + " : " + rect2.getWidth() + ", " + rect2.getHeight());
			//System.out.println("Intersect " + intersection.getMinX() + ", " + intersection.getMinY() + " : " + intersection.getWidth() + ", " + intersection.getHeight());
			//System.out.println("Actual " + intersection.getMinX()/100.0 + ", " + intersection.getMinY()/100.0 + " : " + intersection.getWidth()/100.0 + ", " + intersection.getHeight()/100.0);
			
			//Figure out what pixels have to be taken
			int xStart1 = (int)(SpriteSheet.SQUARE*(intersection.getMinX()-rect.getMinX())/100 );
			int yStart1 = (int)(SpriteSheet.SQUARE*(intersection.getMinY()-rect.getMinY())/100);
			int xStart2 = (int)(SpriteSheet.SQUARE*(intersection.getMinX()-rect2.getMinX())/100);
			int yStart2 = (int)(SpriteSheet.SQUARE*(intersection.getMinY()-rect2.getMinY())/100);
			int xSize = (int)( SpriteSheet.SQUARE*(intersection.getWidth())/100);
			int ySize = (int)( SpriteSheet.SQUARE*(intersection.getHeight())/100);
			//System.out.println("Main pixel start: " + xStart1 + ", " + yStart1);
			//System.out.println("Alt pixel start: " + xStart2 + ", " + yStart2);
			//System.out.println("pixel size: " + xSize + ", " + ySize);
			
			int[] pixels = new int[xSize*ySize];
		    PixelGrabber pg = new PixelGrabber(image, xStart1, yStart1, xSize, ySize, pixels, 0, xSize);
		    int[] pixels2 = new int[xSize*ySize];
		    PixelGrabber pg2 = new PixelGrabber(image, xStart2, yStart2, xSize, ySize, pixels2, 0, xSize);
		    try {
				pg.grabPixels();
				pg2.grabPixels();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		    System.out.println("Checking pixels " + xSize*ySize);
		    System.out.println("Size: " + xUnitSize1 + ", " + yUnitSize1);
		    //Loop through pixels
		    for (int i = 0; i < xSize*ySize; i++)
		    {
		    	//Color color1 = new Color(pixels[i]);
				//Color color2 = new Color(pixels2[i]);
				System.out.println("1: " + pixels[i]);
				System.out.println("2: " + pixels2[i]);
				//If hitting
		    	if (((pixels[i] != 0) && (pixels2[i] != 0)))
		    	{
		    		System.out.println("Pixel collision ----------------------------------");
		    		//If actual pixel test
		    		if (xV == xVel && yV == yVel)
		    		{
		    			//If you want to move the other object too
			    		if (e instanceof MovableOld)
						{
			    			//System.out.println(((Player)e).getName() + "should be moving");
			    			
			    			//TODO turn pusing into a resistance formula
			    			((MovableOld) e).updatePosition(xVel/2.0, yVel/2.0);
			    			updatePosition(xVel/2.0, yVel/2.0);
			    			if (this instanceof PlayerOld)
			    			{
			    				((PlayerOld) this).setxSVel(((PlayerOld) this).getxVel()*SpriteSheet.SQUARE/2.0);
			    				((PlayerOld) this).setySVel(((PlayerOld) this).getyVel()*SpriteSheet.SQUARE/2.0);
			    			}
			    			//System.out.println("Checking between a movable and a movable.. " + e.getClass());
						}	else
						{
							//Moving Diagonally, seeing if moving in one direction will work, if so, do that
							if (xVel != 0 && yVel != 0)
				    		{
				    			if (!checkCollision(e, xVel, 0)) //If no yVel mean no collision
				    			{
				    				//System.out.println("Didn't Hit!");
				    				updatePosition(xVel, 0);
				    				yVel = 0;
				    				if (this instanceof PlayerOld)
				    				{
				    					((PlayerOld) this).setySVel(0);
				    					//((Player) this).setxSVel(0);
				    					((PlayerOld) this).setxSVel(((PlayerOld) this).getxVel()*SpriteSheet.SQUARE);
				    					//xVel * SpriteSheet.SQUARE
				    				}
				    				//System.out.println("Updated Position xWise");
				    				//System.out.println();
				    				//System.out.println();
				    			}	else if (!checkCollision(e, 0, yVel)) //No x means No collision
				    			{
				    				updatePosition(0, yVel);
				    				xVel = 0;
				    				if (this instanceof PlayerOld)
				    				{
				    					((PlayerOld) this).setxSVel(0);
				    					((PlayerOld) this).setySVel(((PlayerOld) this).getyVel()*SpriteSheet.SQUARE);
				    				}
				    				//System.out.println("Updated Position yWise");
				    				//System.out.println();
				    				//System.out.println();
				    			}	else
				    			{
				    				//updatePosition(-xVel, -yVel);
				    				xVel = 0;
				    				yVel = 0;
				    				if (this instanceof PlayerOld)
				    				{
				    					((PlayerOld) this).setxSVel(0);
				    					((PlayerOld) this).setySVel(0);
				    				}
				    				//System.out.println("Updated Position");
				    				//System.out.println();
				    				//System.out.println();
				    			}
				    		}	else //Moving in one direction, see if moving in two will work
				    		{
				    			System.out.println("moving one direction");
				    			if (xVel != 0) //Moving XWise
				    			{
				    				if (!checkCollision(e, xVel/Math.sqrt(2), xVel/Math.sqrt(2))) //If moving DR or UL mean no collision
					    			{
					    				updatePosition(xVel/Math.sqrt(2), xVel/Math.sqrt(2));
					    				yVel = xVel/Math.sqrt(2);
					    				xVel = xVel/Math.sqrt(2);
					    				if (this instanceof PlayerOld)
					    				{
					    					((PlayerOld) this).setySVel(yVel*SpriteSheet.SQUARE);
					    					((PlayerOld) this).setxSVel(xVel*SpriteSheet.SQUARE);
					    				}
					    				System.out.println("Was going x and adjusted y");
					    			}	else if (!checkCollision(e, xVel/Math.sqrt(2), -xVel/Math.sqrt(2))) //If moving UR or DL mean no collision
					    			{
					    				updatePosition(xVel/Math.sqrt(2), -xVel/Math.sqrt(2));
					    				yVel = -xVel/Math.sqrt(2);
					    				xVel = xVel/Math.sqrt(2);
					    				if (this instanceof PlayerOld)
					    				{
					    					((PlayerOld) this).setySVel(yVel*SpriteSheet.SQUARE);
					    					((PlayerOld) this).setxSVel(xVel*SpriteSheet.SQUARE);
					    				}
					    				System.out.println("Was going x and adjusted -y");
					    			}	else
					    			{
					    				xVel = 0;
					    				yVel = 0;
						    			if (this instanceof PlayerOld)
					    				{
						    				((PlayerOld) this).setxSVel(0);
					    					((PlayerOld) this).setySVel(0);
					    				}
						    			System.out.println("Was going x and couldn't adjust");
					    			}
				    			}	else //Moving YWise
				    			{
				    				if (!checkCollision(e, yVel/Math.sqrt(2), yVel/Math.sqrt(2))) //If moving DR or UL mean no collision
					    			{
					    				updatePosition(yVel/Math.sqrt(2), yVel/Math.sqrt(2));
					    				yVel = yVel/Math.sqrt(2);
					    				xVel = yVel/Math.sqrt(2);
					    				if (this instanceof PlayerOld)
					    				{
					    					((PlayerOld) this).setySVel(yVel*SpriteSheet.SQUARE);
					    					((PlayerOld) this).setxSVel(xVel*SpriteSheet.SQUARE);
					    				}
					    				System.out.println("Was going y and adjusted x");
					    			}	else if (!checkCollision(e, -yVel/Math.sqrt(2), yVel/Math.sqrt(2))) //If moving UR or DL mean no collision
					    			{
					    				updatePosition(-yVel/Math.sqrt(2), yVel/Math.sqrt(2));
					    				yVel = yVel/Math.sqrt(2);
					    				xVel = -yVel/Math.sqrt(2);
					    				if (this instanceof PlayerOld)
					    				{
					    					((PlayerOld) this).setySVel(yVel*SpriteSheet.SQUARE);
					    					((PlayerOld) this).setxSVel(xVel*SpriteSheet.SQUARE);
					    				}
					    				System.out.println("Was going y and adjusted -x");
					    			}	else
					    			{
					    				xVel = 0;
					    				yVel = 0;
						    			if (this instanceof PlayerOld)
					    				{
						    				((PlayerOld) this).setxSVel(0);
					    					((PlayerOld) this).setySVel(0);
					    				}
						    			System.out.println("Was going y and couldn't adjust");
					    			}
				    			}
				    			
				    			/*
				    			////////////
				    			if (!checkCollision(e, yVel/Math.sqrt(2), yVel/Math.sqrt(2))) //If moving DR or UL mean no collision
				    			{
				    				updatePosition(0, yVel);
				    				xVel = 0;
				    				if (this instanceof Player)
				    				{
				    					((Player) this).setxSVel(0);
				    					((Player) this).setySVel(((Player) this).getyVel()*SpriteSheet.SQUARE);
				    				}
				    				//System.out.println("Updated Position yWise");
				    				//System.out.println();
				    				//System.out.println();
				    			}	else
				    			{
				    				//////////////
					    			//updatePosition(-xVel, -yVel);
					    			xVel = 0;
				    				yVel = 0;
					    			if (this instanceof Player)
				    				{
					    				((Player) this).setxSVel(0);
				    					((Player) this).setySVel(0);
				    				}
				    				//System.out.println("Updated Position");
				    				//System.out.println();
				    				//System.out.println();
				    			}
				    			*/
				    		}
						}
		    		}
		    		return true;
		    	}
		    }
		    return false;
		}
		//System
		return false;
	
	}
}
