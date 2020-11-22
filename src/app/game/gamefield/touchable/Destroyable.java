package app.game.gamefield.touchable;

import app.game.gamefield.map.Map;
import app.game.gamefield.movable.Movable;

public interface Destroyable {
    public boolean isDestroyed();
    public void gotHit(Touchable m, Map map);
}
