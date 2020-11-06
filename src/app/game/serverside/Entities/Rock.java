package app.game.server.Entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import ClientSide.Player;
import ServerSide.Entity;
import ServerSide.Map;
import game.Game;

public class Rock extends Entity{

	public Rock(double xCenter, double yCenter, BufferedImage img)
	{
		xMPos = xCenter;
		yMPos = yCenter;
		image = img;
		miniMapColor = Color.GRAY;
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
	}
	
	//Requires a player to know exactly who its displaying for

}
