package app.game.gamefield.movable;

import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.Color;

import app.game.gamefield.touchable.Touchable;
import app.supportclasses.GameValues;

public class Movable extends Touchable {
    protected Point2D.Double percentVelocity;
    protected Point percentAcceleration;
	protected double accelerationRate;
	protected double maxSpeed;

    public Movable(GameValues gameValues, Point2D.Double location, Color c) {
        super(gameValues, location, c);
        percentVelocity = new Point2D.Double();
        percentAcceleration = new Point();
    }

    public void accelerate(boolean up, boolean down, boolean left, boolean right) {
        accelerate(up?1:0, down?-1:0, left?1:0, right?-1:0);
    }

    public void accelerate(int up, int down, int left, int right) {
        accelerate(up+down, left+right);
    }

    public void accelerate(int yAcc, int xAcc) {
        percentAcceleration.x = (int)Math.signum(xAcc);
        percentAcceleration.y = (int)Math.signum(yAcc);
    }

    private Point2D.Double getNextVelocity() {
        //Converts from blocks/second to blocks/tick
        final double acceleration = accelerationRate/gameValues.goalTicksPerSecond;
        final double friction = gameValues.friction/gameValues.goalTicksPerSecond;
        final double changeWhenFull = .1;

        Point2D.Double tempPercentVelocity = (Point2D.Double) this.percentVelocity.clone();

        //Add friction and acceleration to the current velocity
        tempPercentVelocity.x -= Math.signum(tempPercentVelocity.x)*friction + acceleration*percentAcceleration.x;
        tempPercentVelocity.y -= Math.signum(tempPercentVelocity.y)*friction + acceleration*percentAcceleration.y;

        //Considers static and kinetic friction to be equal
        if (Math.abs(tempPercentVelocity.x) <= friction) {
            tempPercentVelocity.x = 0;
        }
        if (Math.abs(tempPercentVelocity.y) <= friction) {
            tempPercentVelocity.y = 0;
        }

        //When the resultant of x and y are too large, lower them both 
        //this will result in the initial direction always being slightly more than the other
        double resultant = Math.sqrt((Math.pow(tempPercentVelocity.x, 2) + Math.pow(tempPercentVelocity.y, 2)));
        if (resultant > 1+changeWhenFull) {
            //Lower both
            tempPercentVelocity.x *= (1-changeWhenFull);
            tempPercentVelocity.y *= (1-changeWhenFull);
        }

        return tempPercentVelocity;
    }

    public Point2D.Double getNextLocation() {
        Point2D.Double nextVelocity = getNextVelocity();
        return calculateNextLocation(nextVelocity);
    }

    private Point2D.Double calculateNextLocation(Point2D.Double nextVelocity) {
        return new Point2D.Double(this.location.x + nextVelocity.x, this.location.y + nextVelocity.y);
    }

    public void updateVelocityAndLocation() {
        this.percentVelocity = getNextVelocity();
        this.location = calculateNextLocation(this.percentVelocity);
    }

    public void centerScreen() {
        gameValues.fieldXZeroOffset = this.location.x - gameValues.FIELD_X_SPACES/2.0;
        gameValues.fieldYZeroOffset = this.location.y - gameValues.FIELD_Y_SPACES/2.0;
    }
}
