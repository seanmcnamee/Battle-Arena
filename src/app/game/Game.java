package app.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
//import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import ClientSide.Player;
import ServerSide.Map;
//import ServerSide.Entities.Rock;

//Our main class is 'Game'. Canvas is the GUI type screen that is created, and
//allows for the game to be ran (run() and all those methods that are the brains)
public class Game extends Canvas implements Runnable {

	// Just sorta something, I always ignore it


	// The size and scaling of the game. SCALE is VERY useful (change to make
	// EVERYTHING it bigger/smaller)
	private static int oldWidth;
	private static int oldHeight;
	public static int WIDTH = 200;
	public static int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 4;
	public String NAME = "Explorer Environment";

	/*
	 * 0 - Player stuck at the center of the screen and the map moving around the, 1
	 * - Player moves to the edge then "pushes" the map to explore 2 - Player moves
	 * to the edge and the world 'loads' the next chunk
	 */
	public static boolean DEBUG = true;

	// The JFrame is the GUI itself that everything goes onto
	private JFrame frame;

	// Used to see if the game is still going on
	public boolean running = false;

	// This is the 'sprite sheet' that is used for all loaded images. Rather than
	// many images, all are
	// in one sheet.
	private BufferedImage spriteSheet = null;

	private Map gameMap;
	private Player player;

	public Game() // This is the constructor for this class. Here, we set up the GUI
	{
		// Most of this stuff does what it says... If you want to see what it does,
		// mess around with it a bit (but put it back when you're done)
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println(screenSize.getWidth() / SCALE + ", " + screenSize.getHeight() / SCALE + " :: Yuh");

		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		// Create the GUI itself
		frame = new JFrame(NAME);

		// Allow for trapclose (X button)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(this, BorderLayout.CENTER);
		frame.pack();

		frame.setResizable(true);
		frame.setLocationRelativeTo(null);

		//Unmaxmized to center of screen
		frame.setLocation((int)(screenSize.getWidth() - frame.getWidth())/2, (int)(screenSize.getHeight() - frame.getHeight())/2);
		frame.setVisible(true);
		
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		WIDTH = (int) frame.getWidth() / SCALE;
		HEIGHT = (int) frame.getHeight() / SCALE;
		oldWidth = WIDTH;
		oldHeight = HEIGHT;
	}

	private void init()// This is called when the game first starts to run.
						// It sets up the inital stuff for the game
	{

		// Load in the spritesheet (saved in res folder)
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			spriteSheet = loader.loadImage("/sprite_sheet.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Used for KeyInput (uses another class to do this)
		requestFocus();
		addKeyListener(new KeyInput(this));
		addMouseWheelListener(new KeyInput(this));

		frame.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				oldWidth = WIDTH;
				oldHeight = HEIGHT;
				WIDTH = (int) frame.getWidth() / SCALE;
				HEIGHT = (int) frame.getHeight() / SCALE;
				System.out.println("You resized me to : " + WIDTH + ", " + HEIGHT);
				int biggerSize = Game.WIDTH;
				if (Game.HEIGHT > Game.WIDTH)	biggerSize = Game.HEIGHT; 
				gameMap.setRenderRadius(biggerSize / 16 + 1);
				
				System.out.println("Ratio was... " + player.getXScreen()/oldWidth);
				//Fixing player's screen position on resize
				player.resetPos(WIDTH*(player.getXScreen()/oldWidth), HEIGHT*(player.getYScreen()/oldHeight));
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		// Uses the SpriteSheet class to get a specific image from the sprite sheet
		SpriteSheet ss = new SpriteSheet(spriteSheet);

		// In multiplayer it should be one or the other...
		startGame();
		joinPlayer(ss);
		//gameMap.addEntity(new Player(Map.MAPSIZE / 3, Map.MAPSIZE / 3, ss.grabImage(0, 1, 2, 2), "Secondary"));

		// This would return an image at the top left that is 1 'unit' x 1 'unit'
		// See SpriteSheet class for more info
		// ss.grabImage(0, 0, 1, 1);
	}

	// When making the map
	private void startGame() {
		gameMap = new Map();
	}

	private void joinPlayer(SpriteSheet ss) {
		// When a player joins
		player = new Player(Map.MAPSIZE / 2, Map.MAPSIZE / 2, ss.grabImage(0, 1, 2, 2), "Main");
		gameMap.addEntity(player);
	}

	public synchronized void start() // This is what's first called from the main method.
										// It makes sure running is true and creates a new 'Thread'
										// so that Runnable can begin
	{
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop()// Just stops the game by making running false...
	{
		running = false;
	}

	public void run() // This is what runs the game
	{
		init();

		// Sets the current time, and the amount of nanoseconds that should pass per
		// tick
		// This prevents super fast computers from having game speed be super fast
		// (process quicker)
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		double delta = 0;

		// Used to print out ticks per second (should be 60) and FPS
		int ticks = 0;
		int frames = 0;
		long lastTimer = System.currentTimeMillis();

		while (running) // This loop itself if the brain that
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;

			// Allows things to be printed out constantly without overprocessing the game
			while (delta >= 1) {
				ticks++;
				tick(); // Gameplay (brain)
				delta--;
			}

			frames++;
			render();// Gameplay (display)

			// Prints out FPS and TPS dta
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;

				// the more stuff moving in the game, the less the FPS will be
				System.out.println("fps: " + frames + ", tps: " + ticks);
				// System.out.println("PlayerPos: " + player.getXM() + ", " + player.getYM());
				// frame.setTitle("PlayerPos: " + (int)player.getXM() + ", " +
				// (int)player.getYM());

				// frame.hide();
				frames = 0;
				ticks = 0;
			}

		}
	}

	public void tick() // Update Game Logic
	{
		// Here you could call object.tick(), with a tick method in each of the classes
		// you have for
		// The game like (Player, Monster, Bullet, Obstacle, etc.)
		// If certain methods need it, you can even have certain classes take in
		// parameters
		if (!player.getInv().isOpen()) {
			gameMap.tick();
		}
	}

	public void render() // Update Game Display
	{

		// Printing to screen
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3); // three buffers (usually 'doubleBuffer, this is 'TripleBuffer'
			return;
		}

		// Everything drawn will go through Graphics or some type of Graphics
		Graphics g = bs.getDrawGraphics();
		////////////// Start of Drawing Stuff to Screen ////////////////////////

		// Here you would call object.render(g), passing in the graphics so it can be
		// printed
		// In that object's class, but go onto the screen here.
		// Text can even be printed out with g.drawString(), (just have to set up a
		// Font/Color)

		g.setColor(Color.BLUE);// Go ahead and change this color (like CYAN or WHITE, etc)
		g.fillRect(0, 0, getWidth(), getHeight());// This just fills the screen in that color

		if (player != null) {
			if (player.isRockBound()) {
				//int rnd = (int)(Math.random()*(gameMap.getEntities().size()));
				gameMap.render(g, gameMap.getEntities().get(0));
			}	else	{
				gameMap.render(g, player);
			}
		} else {
			gameMap.render(g);
		}

		//////////// End of Drawing Stuff to screen ////////////////////////

		g.dispose();
		bs.show();
	}

	// When a key is preesed down, this is called
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_E)	{
			player.getInv().setOpen(!player.getInv().isOpen());
		}
		
		//Different things happen when the inventory is open!!
		if (player.getInv().isOpen())	{
			if (key == KeyEvent.VK_ENTER)	{
				player.getInv().switchItemWithHand();
				//player.getInv()
			}
		} 	else	{
			if (key == KeyEvent.VK_F3) {
				DEBUG = !DEBUG;
				// gameMap.removeEntity(player);
			} else if (key == KeyEvent.VK_F4) {
				player.setExplore((player.getExplore() + 1) % 3);
			}	else if(key == KeyEvent.VK_F7)	{
				player.setRockBound(!player.isRockBound());
				System.out.println("Casting Spell");
			}	else if(key == KeyEvent.VK_SHIFT)	{
				player.setRun(true);
			} else {
				setMovement(e, true);
			}

			for (int i = 0; i < player.getInv().getHotBarSize(); i++)	{
				if (key == i+KeyEvent.VK_1)	{
					player.getInv().setSelected(i);
				}
			}
		}
		
		
		
	}

	// When the key it finished being pressed, this is called
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SHIFT)
		{
			player.setRun(false);
		}	else
		{
			setMovement(e, false);
		}
	}

	private void setMovement(KeyEvent e, boolean b) {
		// TODO
		// At the top, figure out what player is playing so it can change the correct
		// one.
		// 'player' is the player running this instance of the Game
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
			player.setUp(b);
		}
		if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
			player.setDown(b);
		}
		if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
			player.setLeft(b);
		}
		if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
			player.setRight(b);
		}
	}

	public void mouseRotated(MouseWheelEvent e)	{
		player.getInv().setSelected(player.getInv().getSelected()+e.getWheelRotation());
	}
	// Main method of the program
	public static void main(String[] args) {
		new Game().start();
	}

}
