package collision;

import java.awt.Rectangle;

public class Obj {

	protected double x;
	protected double y;
	protected double height;
	protected double width;

	public Obj(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public Obj(double x, double y, double r) {
		this(x, y, r, r);
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getHeight() {
		return this.height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return this.width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) this.x, (int) this.y, (int) this.width,
				(int) this.height);
	}

}
