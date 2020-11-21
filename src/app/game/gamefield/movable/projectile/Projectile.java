package app.game.gamefield.movable.projectile;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.time.temporal.TemporalUnit;

import app.game.gamefield.map.Map;
import app.game.gamefield.movable.Movable;
import app.game.gamefield.touchable.Touchable;
import app.supportclasses.GameValues;

public class Projectile extends Movable {
    private Touchable owner;

    public Projectile(Touchable owner, GameValues gameValues, Point2D.Double location, Point2D.Double velocity, double maxSpeed, BufferedImage image) {
        super(gameValues, location, velocity, image, gameValues.PROJECTILE_SIZE);
        this.owner = owner;
        this.friction = gameValues.projectileFriction;
        this.maxSpeed = maxSpeed;
    }
    
    @Override
    public void updateFromCollision(Touchable t, Map m) {
        
        if (t.equals(owner)) {
            this.updateLocation(m);
        } else {
            //TODO delete the projectile and damage the touchable
            t.gotHit(this);
            m.removeMovable(this);
            
            //super.updateFromCollision(t, m);
        }
    }

    @Override
    public void updateLocation(Map m) {
        super.updateLocation(m);
        if (isStopped()) {
            m.removeMovable(this);
        }
    }

    public boolean isStopped() {
        return this.percentVelocity.x == 0 && this.percentVelocity.y == 0;
    }   
}
