package app.game.gamefield.drawable;

import app.supportclasses.GameValues;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.geom.Point2D;

/**
 * BTNode
 * 
 *
 */
public class Drawable  {
    protected Point2D.Double location;
    protected BufferedImage image;
    protected Point2D.Double sizeInBlocks;
    
    protected GameValues gameValues;

    public Drawable(GameValues gameValues, Point2D.Double location) {
        super();
        this.gameValues = gameValues;
        this.location = location;
    }

    public void render(Graphics g) {
        g.drawImage(getImage(), DrawingCalculator.findPixelLocation(getLocation().getX(), getSizeInBlocks().getX(), gameValues.fieldXZero, gameValues.singleSquareX), 
                        DrawingCalculator.findPixelLocation(getLocation().getY(), getSizeInBlocks().getY(), gameValues.fieldYZero, gameValues.singleSquareY), 
                        DrawingCalculator.findPixelSize(getSizeInBlocks().getX(), gameValues.singleSquareX), 
                        DrawingCalculator.findPixelSize(getSizeInBlocks().getY(), gameValues.singleSquareY), 
                                null);
    }




    protected Point2D.Double getLocation() {
        return location;
    }

    protected Point2D.Double getSizeInBlocks() {
        return sizeInBlocks;
    }

    protected BufferedImage getImage() {
        return image;
    }

    protected void printDebug(String toPrint) {
        if (gameValues.debugMode) {
            System.out.println(toPrint);
        }
    }

}