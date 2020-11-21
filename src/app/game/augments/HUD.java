package app.game.augments;

import java.awt.Graphics;

import app.game.gamefield.map.Map;
import app.game.gamefield.movable.player.Player;
import app.supportclasses.GameValues;

public class HUD {
    	
	private GameValues gameValues;
    private MiniMap miniMap;
    private ProjectileBar projectileBar;

	public HUD(GameValues gameValues, Map m, Player p) {
		this.gameValues = gameValues;
        this.miniMap = new MiniMap(gameValues, m);
        this.projectileBar = new ProjectileBar(gameValues, p.getProjectiles());
    }
    
    public void render(Graphics g) {
        double miniMapxStart, miniMapyStart, projBarXStart, projBarYStart;
        double miniMapSize, singleProjSize;
        double projSeperator;

		//Since fieldSize accounts for Scaling, multiplying by gameScale is unneeded.
		miniMapSize = Math.min(gameValues.fieldXSize, gameValues.fieldYSize) * gameValues.MINIMAP_SIZE;
		miniMapxStart = gameValues.fieldXSize - miniMapSize*gameValues.HUD_EDGE_SEPERATOR;
        miniMapyStart = gameValues.fieldYSize - miniMapSize*gameValues.HUD_EDGE_SEPERATOR;

        singleProjSize = Math.min(gameValues.fieldXSize, gameValues.fieldYSize) * gameValues.PROJECTILEBAR_SIZE;
        projSeperator = singleProjSize*gameValues.PROJECTILE_SEPERATION;
        projBarXStart = gameValues.fieldXSize - (singleProjSize + miniMapSize*(gameValues.HUD_EDGE_SEPERATOR-1));
        projBarYStart = miniMapyStart - (singleProjSize+projSeperator);
        


        miniMap.render(g, miniMapxStart, miniMapyStart, miniMapSize);
        projectileBar.render(g, projBarXStart, projBarYStart, singleProjSize, projSeperator);
    }
    
}
