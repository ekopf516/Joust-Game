import java.awt.Rectangle;


public class CollisionBox {

private Rectangle rect;
	
	public CollisionBox(Rectangle rect) {
		this.rect = rect;
	}
	
	public void moveTo(int x, int y) {
		rect.setLocation(x, y);
	}
	
	public CollisionBox(int x, int y, int width, int height) {
		rect = new Rectangle(x, y, width, height);
	}
	
	public Rectangle getRectangle() {
		return this.rect;
	}
	
	public boolean collidesWith(CollisionBox other) {
		if(rect.intersects(other.getRectangle())) {
			return true;
		}
		return false;
	}
	
	public boolean isHigherThan(CollisionBox other) {
		if(rect.getMaxY() < other.getRectangle().getMaxY()) {
			return true;
		}
		return false;
	}
	
	public boolean isLeftOf(CollisionBox other) {
		if(rect.getX() - (rect.width / 2) < other.getRectangle().getX() - (other.getRectangle().width / 2)) {
			return true;
		}
		return false;
	}
}
