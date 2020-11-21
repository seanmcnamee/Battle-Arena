package app.game.gamefield.touchable;

import app.game.gamefield.movable.Movable;

public interface Destroyable {
    public boolean isDestroyed();
    public void gotHit(Movable m);
}
