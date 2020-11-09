package app.game.gamefield.map;

import java.util.ArrayList;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.geom.Point2D;

import app.supportclasses.GameValues;
import app.supportclasses.SpriteSheet;
import app.game.gamefield.drawable.Drawable;
import app.game.gamefield.touchable.Touchable;
import app.game.gamefield.movable.Movable;

public class Map {
    private Drawable[][] map; //These drawables will always be present
    private ArrayList<Drawable> drawables; //These drawables can be removed
    private ArrayList<Touchable> touchables; //All touchables (for collision detection), not all can be removed
    private ArrayList<Movable> movables; //All movables (for tick update)
    private GameValues gameValues;

    public Map(GameValues gameValues) {
        this.map = new Drawable[gameValues.MAPSIZE.x+2*gameValues.WALL_THICKNESS][gameValues.MAPSIZE.y+2*gameValues.WALL_THICKNESS];
        this.drawables = new ArrayList<Drawable>();
        this.touchables = new ArrayList<Touchable>();
        this.movables = new ArrayList<Movable>();
        this.gameValues = gameValues;
        initializeMap();
    }

    private void initializeMap() {
        SpriteSheet ss = new SpriteSheet(gameValues.SPRITE_SHEET);

		BufferedImage insideImg = ss.grabImage(gameValues.SS_INNER_TILE_LOCATION, gameValues.SS_TILE_SIZE, gameValues.SINGLE_BOX_SIZE);
        BufferedImage outsideImg = ss.grabImage(gameValues.SS_OUTTER_TILE_LOCATION, gameValues.SS_TILE_SIZE, gameValues.SINGLE_BOX_SIZE);
        
        //TopLeft regular tile is (0,0)
		for (int x = 0; x < this.map.length; x++) {
			for (int y = 0; y < this.map[x].length; y++) {
                int xPos = x - gameValues.WALL_THICKNESS;
                int yPos = y - gameValues.WALL_THICKNESS;

				if (x < gameValues.WALL_THICKNESS || x >= gameValues.MAPSIZE.x + gameValues.WALL_THICKNESS || y < gameValues.WALL_THICKNESS || y >= gameValues.MAPSIZE.y + gameValues.WALL_THICKNESS) {
                    Touchable wall = new Touchable(gameValues, xPos, yPos, outsideImg, gameValues.INGAME_TILE_SIZE);
                    map[x][y] = wall;
                    this.touchables.add(wall); //Walls will be used for collision
				} else {
					map[x][y] = new Drawable(gameValues, xPos, yPos, insideImg, gameValues.INGAME_TILE_SIZE);
				}
			}
		}
    }

    public void render(Graphics g) {
        renderMap(g);
        renderDrawables(g);
    }

    private void renderMap(Graphics g) {
        //TODO add a margin so that walking looks smooth?
        int margin = 0;
        int startXIndex = Math.max((int) gameValues.fieldXZeroOffset - margin, 0);
        int startYIndex = Math.max((int) gameValues.fieldYZeroOffset - margin, 0);
        int endXIndex = Math.min(startXIndex+gameValues.FIELD_X_SPACES + 2*margin, map.length);
        int endYIndex = Math.min(startYIndex+gameValues.FIELD_Y_SPACES + 2*margin, map.length);

        //Only render elements based on the screen offset
        for (int x = startXIndex; x < endXIndex; x++) {
            for (int y = startYIndex; y < endYIndex; y++) {
                map[x][y].render(g);
            }
        }
    }

    private void renderDrawables(Graphics g) {
        for (Drawable d : drawables) {
            d.render(g);
        }
    }

    public void tick() {
        for (Movable m : this.movables) {
            Touchable collidingTouchable = collidesWithTouchable(m);
            if (collidingTouchable == null) {
                m.updateVelocityAndLocation();
            }   else {
                //TODO allow the movable to deal with the collision
            }
        }
    }

    private Touchable collidesWithTouchable(Movable m) {
        Point2D.Double nextLocation = m.getNextLocation();
        Touchable collidingTouchable = null;
        for (Touchable t : this.touchables) {
            if (!m.equals(t) && m.contains(nextLocation, t)) {
                collidingTouchable = t;
                break;
            }
        }
        return collidingTouchable;
    }

    public void addMovable(Movable m) {
        this.movables.add(m);
        addTouchable(m);
    }

    public void addTouchable(Touchable t) {
        this.touchables.add(t);
        addDrawable(t);
    }

    public void addDrawable(Drawable d) {
        this.drawables.add(d);
    }

    public void removeMovable(Movable m) {
        this.movables.remove(m);
        removeTouchable(m);
    }
    /**
     * Never remove a tile!! If you do, removing it from the Matrix will have to happen
     * @param t
     */
    public void removeTouchable(Touchable t) {
        this.touchables.remove(t);
        removeDrawable(t);
    }

    public void removeDrawable(Drawable d) {
        this.drawables.remove(d);
    }
}