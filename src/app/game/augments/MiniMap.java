package app.game.augments;

import java.awt.Color;
import java.awt.Graphics;

import app.game.gamefield.map.Map;


public class MiniMap {
	private double sizeRatio = 1.0 / 8.0;
	private Map map;

	public MiniMap(Map m) {
		map = m;
	}
	
	public void render(Graphics g, Entity e) {
		// Print out the entire map
		double xStart, yStart;
		double size;
		//double tilesToRender;

		size = Math.min(Game.WIDTH, Game.HEIGHT) * sizeRatio;
		xStart = Game.WIDTH - size - 7;
		yStart = Game.HEIGHT - size - 12;

		// System.out.println(Game.WIDTH + ", " + Game.HEIGHT);
		// System.out.println("FillRect: " + xStart + ", " + yStart + ", " + xSize + ",
		// " + ySize);
		// g.fillRect(10, 10, 100, 100);

		// Background of Minimap
		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect((int) (xStart * Game.SCALE), (int) (yStart * Game.SCALE), (int) (size * Game.SCALE),
				(int) (size * Game.SCALE));


		//Either print all entities, or focus on certain entities
		for (int i = 0; i < map.getEntities().size(); i++) {
			if (e == null) //Print out everything (with different colors)
			{
				if (map.getEntities().get(i).getMiniMapColor() != null) 
				{
					g.setColor(map.getEntities().get(i).getMiniMapColor());
				}	else
				{
					g.setColor(Color.WHITE);
				}
				double xPrint = (Game.SCALE * xStart + (size * Game.SCALE * map.getEntities().get(i).getXMap() / Map.MAPSIZE)) + 2;
				double yPrint = (Game.SCALE * yStart + (size * Game.SCALE * map.getEntities().get(i).getYMap() / Map.MAPSIZE)) + 2;
				double printSize = (size*Game.SCALE/Map.MAPSIZE);
				g.fillRect((int) (xPrint-printSize/2), (int) (yPrint-printSize/2), (int)printSize, (int)printSize);
			}	else	
			{
				if (map.getEntities().get(i).isShowOnMap())
				{
					if (e.equals(map.getEntities().get(i)))
					{
						g.setColor(Color.GREEN);
					}	else
					{
						g.setColor(map.getEntities().get(i).getMiniMapColor());
					}
					double xPrint = (Game.SCALE * xStart + (size * Game.SCALE * map.getEntities().get(i).getXMap() / Map.MAPSIZE)) + 2;
					double yPrint = (Game.SCALE * yStart + (size * Game.SCALE * map.getEntities().get(i).getYMap() / Map.MAPSIZE)) + 2;
					double printSize = (size*Game.SCALE/Map.MAPSIZE);
					g.fillRect((int) (xPrint-printSize/2), (int) (yPrint-printSize/2), (int)printSize, (int)printSize);
				}
			}
		}

	}

	/**
	 * @return A fraction representing how much of the screen the minimap takes up
	 */
	public double getSizeRatio() {
		return sizeRatio;
	}

	/**
	 * The bigger the number, the smaller the minimap will be
	 * 
	 * @param sizeRatio - size multiplier
	 */
	public void setSizeRatio(double sizeRatio) {
		this.sizeRatio = 1.0 / sizeRatio;
	}

}
