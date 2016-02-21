// Emma Kopf (ek4wy) & LeiLei Secor (lfs3bp)
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;


public class JoustScreen extends KeyAdapter implements ActionListener {

	/**
	 * A simple method to make the game runnable. You should not modify 
	 * this main method: it should print out a list of extras you added
	 * and then say "new JoustScreen();" -- nothing more than that.
	 */
	public static void main(String[] args) {
		// add a list of all extras you did, such as
		// System.out.println("Moving obstacles");
		// System.out.println("Birds leave trails of glowing faerie dust");
		// System.out.println("Press left-right-left-left-down to open a game of Mahjong");
		new JoustScreen();
	}

	
	// DO NOT CHANGE the next four fields (the window and timer)
	private JFrame window;         // the window itself
	private BufferedImage content; // the current game graphics
	private Graphics2D paintbrush; // for drawing things in the window
	private Timer gameTimer;       // for keeping track of time passing
	// DO NOT CHANGE the previous four fields (the window and timer)
	
	
	// TODO: add your own fields here
	
	private Bird bird1;
	private Bird bird2;
	private ArrayList<Rectangle> walls; 
	private Rectangle screen;
	private int score1;
	private int score2;
	private boolean gamePaused;
	private boolean gameOver;
	private Rectangle obstacle1;
	private Rectangle obstacle2;
	private CollisionBox obstacle1b;
	private CollisionBox obstacle2b;
	private CollisionBox hitbox1;
	private CollisionBox hitbox2;

	
	public JoustScreen() {
		// DO NOT CHANGE the window, content, and paintbrush lines below
		this.window = new JFrame("Joust Clone");
		this.content = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		this.paintbrush = (Graphics2D)this.content.getGraphics();
		this.window.setContentPane(new JLabel(new ImageIcon(this.content)));
		this.window.pack();
		this.window.setVisible(true);
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.window.addKeyListener(this);
		// DO NOT CHANGE the window, content, and paintbrush lines above

		// TODO: add anything else you might need (e.g., a couple of Bird objects, some walls)
		this.bird1 = new Bird("birdg", 100, 200, 0);
		this.bird2 = new Bird("birdr", 700, 200, 3);
		this.screen = new Rectangle(800, 600);
		this.score1 = 0;
		this.score2 = 0;
		this.obstacle1 = new Rectangle(100, 100, 300, 40);
		this.obstacle2 = new Rectangle(600, 200, 40, 250);
		this.obstacle1b = new CollisionBox(obstacle1);
		this.obstacle2b = new CollisionBox(obstacle2);
		this.hitbox1 = new CollisionBox(this.bird1.getHitbox());
		this.hitbox2 = new CollisionBox(this.bird2.getHitbox());
		this.gamePaused = false;
		this.gameOver = false;

		// DO NOT CHANGE the next two lines nor add lines after them
		this.gameTimer = new Timer(20, this); // tick at 1000/20 fps
		this.gameTimer.start();               // and start ticking now
		// DO NOT CHANGE the previous two lines nor add lines after them
	}
	

	/**
	 * This method gets called each time a player presses a key.
	 * You can find out what key the pressed by comparing event.getKeyCode() with KeyEvent.VK_...
	 */
	public void keyPressed(KeyEvent event) {
		
		// TODO: handle the keys you want to use to run your game

		if (event.getKeyCode() == KeyEvent.VK_A) { // example
			this.bird1.moveLeft();
			this.bird1.flapWings(event);
		}
	
		if (event.getKeyCode() == KeyEvent.VK_S) { // example
			this.bird1.moveRight();
			this.bird1.flapWings(event);
		}
		
		if(event.getKeyCode() == KeyEvent.VK_K) {
			this.bird2.moveLeft();
			this.bird2.flapWings(event);
		}
		
		if(event.getKeyCode() == KeyEvent.VK_L) {
			this.bird2.moveRight();
			this.bird2.flapWings(event);
			
		}
		
		if(event.getKeyCode() == KeyEvent.VK_P) {
			this.gamePaused = true;
			this.gameTimer.stop();
		}
		
		if(event.getKeyCode() == KeyEvent.VK_C) {
			this.gamePaused = false;
			this.gameTimer.start();
		}
		
	} 

	/**
	 * Java will call this every time the gameTimer ticks (50 times a second).
	 * If you want to stop the game, invoke this.gameTimer.stop() in this method.
	 */
	public void actionPerformed(ActionEvent event) {
		// DO NOT CHANGE the next four lines, and add nothing above them
		if (! this.window.isValid()) { // the "close window" button
			this.gameTimer.stop();     // should stop the timer
			return;                    // and stop doing anything else
		}                              
		// DO NOT CHANGE the previous four lines
		
		
		
		// TODO: add every-frame logic in here (gravity, momentum, collisions, etc)
		this.bird1.move();
		this.bird2.move();
		this.bird1.applyDrag(0.3, .04);
		this.bird2.applyDrag(0.3, .04);
		
		if(hitbox1.collidesWith(hitbox2)) {
			if(hitbox1.isHigherThan(hitbox2)) {
				if(Math.abs(this.bird1.getHitbox().getMinX() - this.bird2.getHitbox().getMinX()) > 30) {
					this.bird1.setDX(-5);
				} else {
				score1+=1;
				this.bird2.refreshBird(Math.random() * 800,0);
				}
			} 
			if(hitbox2.isHigherThan(hitbox1)) {
				if(Math.abs(this.bird1.getHitbox().getMinX() - this.bird2.getHitbox().getMinX()) > 30) {
					this.bird1.setDX(-5);
				} else {
				score2+=1;
				this.bird1.refreshBird(Math.random() * 800,0);
				}
			}
			
		}
		
		
		// obstacle 1 collision
		if (hitbox1.collidesWith(obstacle1b)) {
			if(hitbox1.isHigherThan(obstacle1b)) {
				this.bird1.setDY(-5);
			} else {
				this.bird1.setDY(5);
			}
			
			if(hitbox1.isLeftOf(obstacle1b)) {
				this.bird1.setDX(-5);
			} else {
				this.bird1.setDX(5);
			}
			
		}
		
		if(hitbox2.collidesWith(obstacle1b)) {
			if(hitbox2.isHigherThan(obstacle1b)) {
				this.bird2.bounceOutsideOf(obstacle1b, 0.2);
			} else {
				this.bird2.setDY(5);
			}
			if(hitbox2.isLeftOf(obstacle1b)) {
				this.bird2.setDX(-5);
			} else {
				this.bird2.setDX(5);
			}
		}

		
		//obstacle 2 collision
		
		if(hitbox1.collidesWith(obstacle2b)) {
			if(hitbox1.isHigherThan(obstacle2b)) {
				this.bird1.setDY(-5);
			} else {
				this.bird1.setDY(5);
			}
			
			if(hitbox1.isLeftOf(obstacle2b)) {
				this.bird1.setDX(-5);
			} else {
				this.bird1.setDX(5);
			}
		}
		
		if(hitbox2.collidesWith(obstacle2b)) {
			if(hitbox2.isHigherThan(obstacle2b)) {
				this.bird2.setDY(-5);
			} else {
				this.bird2.setDY(5);
			}
			if(hitbox2.isLeftOf(obstacle2b)) {
				this.bird2.setDX(-5);
			} else {
				this.bird2.setDX(5);
			}
			
		}
		
		// DO NOT CHANGE the next line; it must be last in this method
		this.refreshScreen(); // redraws the screen after things move
		// DO NOT CHANGE the above line; it must be last in this method
	}

	/**
	 * Re-draws the screen. You should erase the old image and draw a 
	 * new one, but you should not change anything in this method
	 * (use actionPerformed instead if you need something to change).
	 */
	

	
	private void refreshScreen() {
		this.paintbrush.setColor(new Color(150, 210, 255)); // pale blue
		this.paintbrush.fillRect(0, 0, this.content.getWidth(), this.content.getHeight()); // erases the previous frame

		
		// TODO: replace the following example code with code that does 
		// the right thing (i.e., draw the birds, walls, and score)
		
		// example bird drawing; replace with something sensible instead of making a new bird every frame
		this.bird1.draw(this.paintbrush);
		this.bird2.draw(this.paintbrush);

		// example wall drawing; replace with something sensible instead of making a new wall every frame
		this.paintbrush.setColor(Color.BLACK);
		this.paintbrush.fill(obstacle1);
		this.paintbrush.fill(obstacle2);
		
		// example text drawing, for scores and/or other messages
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		this.paintbrush.setFont(f);
		this.paintbrush.setColor(new Color(127,0,0)); // dark red
		this.paintbrush.drawString(Integer.toString(score2), 760, 30);
		this.paintbrush.setColor(new Color(0,127,0)); // dark green
		this.paintbrush.drawString(Integer.toString(score1), 30, 30);
		String msg = "First player to five wins!";
		String msg2 = "To pause the game press 'P,' to continue press 'C'";
		f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		Rectangle2D r = f.getStringBounds(msg, this.paintbrush.getFontRenderContext());
		Rectangle2D s = f.getStringBounds(msg2, this.paintbrush.getFontRenderContext());
		this.paintbrush.setFont(f);
		this.paintbrush.setColor(Color.BLUE);
		this.paintbrush.drawString(msg, 400-(int)r.getWidth()/2, 30);
		this.paintbrush.drawString(msg2, 170, 590);
		
		if(score1 == 5) {
			String win1 = "Game over! Player 1 wins!";
			f = new Font(Font.SANS_SERIF, Font.BOLD, 50);
			Rectangle2D r1 = f.getStringBounds(win1, this.paintbrush.getFontRenderContext());
			this.paintbrush.setFont(f);
			this.paintbrush.setColor(Color.BLUE);
			this.paintbrush.drawString(win1, 100, 300);
			this.gameTimer.stop();
		}
		
		if(score2 == 5) {
			String win2 = "Game over! Player 2 wins!";
			f = new Font(Font.SANS_SERIF, Font.BOLD, 50);
			Rectangle2D r2 = f.getStringBounds(win2, this.paintbrush.getFontRenderContext());
			this.paintbrush.setFont(f);
			this.paintbrush.setColor(Color.BLUE);
			this.paintbrush.drawString(win2, 100, 300);
			this.gameTimer.stop();
		}
		
		
		// DO NOT CHANGE the next line; it must be last in this method
		this.window.repaint();  // displays the frame to the screen
		// DO NOT CHANGE the above line; it must be last in this method
	

	}
}