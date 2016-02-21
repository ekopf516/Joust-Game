import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;



public class Bird {
	
	/// imgs: default storage for the pictures of the bird
	private BufferedImage[] imgs;
	private double x;
	private double y;
	private double dx;
	private double dy;
	private int i;
	private double gravity;
	private double drag;
	private Rectangle hitBox;
	private Rectangle screen;
	
	/**
	 * Creates a bird object with the given image set 
	 * @param basename should be "birdg" or "birdr" (assuming you use the provided images)
	 */
	
	//changes picture to flap wings
	public void flapWings(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_A) { // example
			this.i = 4;
		}
		
		if (event.getKeyCode() == KeyEvent.VK_S) { // example
			this.i = 2;
		}
		
		if(event.getKeyCode() == KeyEvent.VK_K) {
			this.i = 4;
		}
		
		if(event.getKeyCode() == KeyEvent.VK_L) {
			this.i = 2;
			
		}
	}
	
	public void move() {

		gravity = 0.6;
		drag = 2.0;
		dy = dy + gravity;
		y = y + dy;
		x+= dx;
		if(this.hitBox.getY() >= 555.2) {
			bounceIfOutsideOf(screen, .50);
		}

		if(this.hitBox.getY()<= 4.2) {
			bounceIfOutsideOf(screen, .30);
		}

		if (this.hitBox.getX() >= 800) {
			bounceIfOutsideOf(screen, .30);
		}

		if (this.hitBox.getMinX() < 0) {
			bounceIfOutsideOf(screen, .30);
		}
		
		hitBox.setLocation((int) (this.x - 25), (int) (this.y - 25));
	}

	
	public void moveRight() {
		dy = -10;
		dx = 5;
		this.y = y + dy;
		this.x = x + dx;
		hitBox.setLocation((int) (this.x - 25), (int) (this.y - 25));
	}
	
	public void moveLeft() {
		dy = -10;
		dx = -5;
		this.y = y + dy;
		this.x = x + dx;
		hitBox.setLocation((int) (this.x - 25), (int) (this.y - 25));
	}
	
	public double getSpeed() {
		return Math.sqrt((dx * dx) + (dy * dy));
	}
	
	
	public void applyForce(double fx, double fy, double dt) {
		this.dx = dx + ((fx * dt));
		this.dy = dy + ((fy * dt));
	}
	
	public void applyDrag(double drag, double dt) {
		applyForce(-dx * getSpeed() * drag, -dy * getSpeed() * drag, dt);
	}
	
	public void refreshBird(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDX(double dx) {
		this.dx = dx;
	}
	
	public void setDY(double dy) {
		this.dy = dy;
	}
	
	public double getDY() {
		return this.dy;
	}
	
	
	
	public Bird(String basename, double x, double y, int i) {
		// You may change this method if you wish, including adding 
		// parameters if you want; however, the existing image code works as is.
		this.imgs = new BufferedImage[6];
		this.x = x;
		this.y = y;
		this.i = i;
		this.screen = new Rectangle(800, 600);
		hitBox = new Rectangle((int) (x-25), (int) (y-25), 50, 55);
		
		try {
			// 0-2: right-facing (folded, back, and forward wings)
			this.imgs[0] = ImageIO.read(new File(basename+".png"));  
			this.imgs[1] = ImageIO.read(new File(basename+"f.png"));
			this.imgs[2] = ImageIO.read(new File(basename+"b.png"));
			// 3-5: left-facing (folded, back, and forward wings)
			this.imgs[3] = Bird.makeFlipped(this.imgs[0]);
			this.imgs[4] = Bird.makeFlipped(this.imgs[1]);
			this.imgs[5] = Bird.makeFlipped(this.imgs[2]);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}

	public void bounceIfOutsideOf(Rectangle screen, double bounciness) {
		if(hitBox.getMinX() < screen.getMinX()) {
			this.x = screen.getMinX()+40; 
			this.dx = -Math.abs(dx * bounciness);
		}
		if(hitBox.getMaxX() > (screen.getMaxX())) {
			this.x = screen.getMaxX()-30;
			this.dx = -Math.abs(dx * bounciness);
		}
		if(hitBox.getMaxY() > screen.getMaxY()) {
			this.y = screen.getMaxY()-20.5;
			this.dy = -Math.abs(dy * bounciness);
		}
		if(hitBox.getMinY() < screen.getMinY()) {
			this.y = screen.getMinY()+30;
			this.dy = Math.abs(dy * bounciness);
		}
	}
	
	public void bounceOutsideOf(CollisionBox other, double bounciness) {
		if(hitBox.getMinX() < other.getRectangle().getMinX()) {
			this.x = other.getRectangle().getMinX() + 0.5;
			this.dx = -Math.abs(dx * bounciness);
		}
		if(hitBox.getMaxX() < other.getRectangle().getMaxX()) {
			this.x = other.getRectangle().getMaxX() - 0.5;
			this.dx = -Math.abs(dx * bounciness);
		}
		if(hitBox.getMaxY() > other.getRectangle().getMaxY()) {
			this.y = other.getRectangle().getMaxY() - 0.5;
			this.dy = -Math.abs(dy * bounciness);
		}
		if(hitBox.getMaxY() > other.getRectangle().getMinY()) {
			this.y = other.getRectangle().getMinY() + 0.5;
			this.dy = Math.abs(dy * bounciness);
		}
	}
	
	
	public Rectangle getHitbox() {
		return this.hitBox;
	}
	
	/**
	 * A helper method for flipping in image left-to-right into a mirror image.
	 * There is no reason to change this method.
	 * 
	 * @param original The image to flip
	 * @return A left-right mirrored copy of the original image
	 */
	private static BufferedImage makeFlipped(BufferedImage original) {
		AffineTransform af = AffineTransform.getScaleInstance(-1, 1);
		af.translate(-original.getWidth(), 0);
		BufferedImage ans = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		Graphics2D g = (Graphics2D)ans.getGraphics();
		g.drawImage(original, af, null);
		return ans;
	}
	
	
	
	/**
	 * Draws this bird
	 * @param g the paintbrush to use for the drawing
	 */
	public void draw(Graphics g) {

		int i = this.i; // between 0 and 6, depending on facing and wing state
		double x = this.x; // where to center the picture 
		double y = this.y;
		
		// TODO: find the right x, y, and i instead of the examples given here
		
		g.drawImage(this.imgs[i], (int)x-this.imgs[i].getWidth()/2, (int)y-this.imgs[i].getHeight()/2, null);
	}
