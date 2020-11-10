package app.game.augments;

import java.awt.Color;
import java.awt.Graphics;

import app.game.gamefield.map.Map;
import app.game.gamefield.touchable.Touchable;
import app.supportclasses.GameValues;


public class MiniMap {
	
	private GameValues gameValues;
	private Map map;

	public MiniMap(GameValues gameValues, Map m) {
		this.gameValues = gameValues;
		map = m;
	}
	
	
	public void render(Graphics g) {
		// Print out the entire map
		double xStart, yStart;
		double size;

		//Since fieldSize accounts for Scaling, multiplying by gameScale is unneeded.
		size = Math.min(gameValues.fieldXSize, gameValues.fieldYSize) * gameValues.MINIMAP_SIZE;
		xStart = gameValues.fieldXSize - size*1.1;
		yStart = gameValues.fieldYSize - size*1.1;

		// Background of Minimap
		g.setColor(gameValues.MINIMAP_COLOR);
		g.fillRect((int) (xStart), (int) (yStart), (int) (size),
				(int) (size));


		for (Touchable t : map.getTouchables()) {
			if (t.isDisplaying()) {
				g.setColor(t.getColor());
				g.fillRect((int)(xStart + size*(t.getLocation().x/map.getMapSize().x)), 
							(int)(yStart + size*(t.getLocation().y/map.getMapSize().y)), 1, 1);
			}
		}
				/*
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
		*/

	}
}
