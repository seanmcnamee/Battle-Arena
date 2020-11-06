package app.game.gamefield.map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import ClientSide.Player;
import ServerSide.Entities.Rock;
import game.BufferedImageLoader;
import game.Game;
import game.KeyInput;
import game.SpriteSheet;

public class Map {

	private ArrayList<Entity> entities; //objects and players
	private Tile[][] map;
	MiniMap minimap;
	public static final int MAPSIZE = 50;
	public static final int MAPBORDER = 5;
	private int renderRadius = Game.WIDTH / 16 + 1;

	public Map() {
		map = new Tile[MAPSIZE + 2 * MAPBORDER][MAPSIZE + 2 * MAPBORDER];
		entities = new ArrayList<Entity>();
		init();
		System.out.println("RenderRadius: " + renderRadius);
		minimap = new MiniMap(this);
	}

	private void init() {
		BufferedImageLoader loader = new BufferedImageLoader();
		BufferedImage spriteSheet = null;
		try {
			spriteSheet = loader.loadImage("/sprite_sheet.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Uses the SpriteSheet class to get a specific image from the sprite sheet
		SpriteSheet ss = new SpriteSheet(spriteSheet);

		BufferedImage insideImg = ss.grabImage(0, 0, 1, 1);
		BufferedImage outsideImg = ss.grabImage(2, 0, 1, 1);
		BufferedImage tileImg;

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				if (x < MAPBORDER || x >= MAPSIZE + MAPBORDER || y < MAPBORDER || y >= MAPSIZE + MAPBORDER) {
					tileImg = outsideImg;
				} else {
					tileImg = insideImg;
				}
				map[x][y] = new Tile(x - MAPBORDER, y - MAPBORDER, tileImg);
			}
		}
		
		entities.add(new Rock(10, 10, ss.grabImage(3, 0, 1, 1)));
	}

	public void tick() {
		
		for (int i = 0; i < entities.size(); i++) {
			//Set the entity's velocity
			entities.get(i).tick();
			boolean canMove = true;
			for (int j = 0; j < entities.size(); j++) {
				if (entities.get(i) instanceof MovableOld && entities.get(i) != entities.get(j))
				{
					if (((MovableOld) entities.get(i)).checkCollision(entities.get(j), ((MovableOld)entities.get(i)).getxVel(), ((MovableOld)entities.get(i)).getyVel()))
					{
						canMove = false;
					}
				}
			}
			//If you can move and you're movable
			if (canMove && (entities.get(i) instanceof MovableOld))
			{
				((MovableOld)entities.get(i)).updatePosition( ((MovableOld)entities.get(i)).getxVel(), ((MovableOld)entities.get(i)).getyVel() );
				if (entities.get(i) instanceof Player)
				{
					((Player) entities.get(i)).updateScreenPosTick();
				}
				((Player) entities.get(i)).updateScreenPos(((Player) entities.get(i)).getxSVel(), ((Player) entities.get(i)).getySVel());
				//System.out.println("Velocity Clear: " + ((Movable)entities.get(i)).getxVel() + ", " + ((Movable)entities.get(i)).getyVel());
			}	else if((entities.get(i) instanceof Player))
			{
				((Player) entities.get(i)).updateScreenPos(((Player) entities.get(i)).getxSVel(), ((Player) entities.get(i)).getySVel());
				//((Player) entities.get(i)).setXScreen(((Player) entities.get(i)).getXScreen()+((Player) entities.get(i)));
				System.out.println("Velocity Hitting: " + ((MovableOld)entities.get(i)).getxVel() + ", " + ((MovableOld)entities.get(i)).getyVel());
			}
		}
	}

	//Render in relation to Who/What
	public void render(Graphics g, Entity e) {

		
		double eXM = e.getXMap();
		double eYM = e.getYMap();
		double eXS = Game.WIDTH/2;
		double eYS = Game.HEIGHT/2;
		if (e instanceof Player)
		{
			eXS = ((Player) e).getXScreen();
			eYS = ((Player) e).getYScreen();
		}
		
		
		double xSM;
		double ySM;

		int lowX;
		int lowY;
		int highX;
		int highY;

		// Setting render chunks
		xSM = eXS / 8.0 - (Game.WIDTH / 16.0);
		ySM = eYS / 8.0 - (Game.HEIGHT / 16.0);

		lowX = (int) (eXM - renderRadius + MAPBORDER - xSM);
		lowY = (int) (eYM - renderRadius + MAPBORDER - ySM);
		highX = (int) (eXM + renderRadius + MAPBORDER + 1 - xSM);
		highY = (int) (eYM + renderRadius + MAPBORDER - ySM);

		// Making sure those chunks are in range
		if (lowX < 0) {
			lowX = 0;
		}
		if (lowX > map.length) {
			lowX = map.length;
		}
		if (lowY < 0) {
			lowY = 0;
		}
		if (lowY > map.length) {
			lowY = map.length;
		}

		if (highX < 0) {
			highX = 0;
		}
		if (highX > map.length) {
			highX = map.length;
		}
		if (highY < 0) {
			highY = 0;
		}
		if (highY > map.length) {
			highY = map.length;
		}

		// Print out tiles
		for (int x = lowX; x < highX; x++) {
			for (int y = lowY; y < highY; y++) {
				map[x][y].render(g, e);
			}
		}

		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != e)
			{
				entities.get(i).render(g, e);
			}	else
			{
				entities.get(i).render(g);
			}
			
		}

		minimap.render(g, e);
	}

	public void render(Graphics g) {

		int lowX;
		int lowY;
		int highX;
		int highY;

		lowX = 0;
		lowY = 0;
		highX = MAPSIZE + MAPBORDER * 2 -1;
		highY = MAPSIZE + MAPBORDER * 2 -1;


		// Print out tiles
		for (int x = lowX; x < highX; x++) {
			for (int y = lowY; y < highY; y++) {
				map[x][y].render(g);
			}
		}

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(g);
		}

		minimap.render(g, null);
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void removeEntity(Entity e) {
		entities.remove(e);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public void setRenderRadius(int r) {
		renderRadius = r;
	}

}
