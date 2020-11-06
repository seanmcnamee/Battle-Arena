package app.game.serverside;

import java.awt.Graphics;

public interface TickAction {
	public void tick();
	public void render(Graphics g);
	public void render(Graphics g, Entity e);
}
