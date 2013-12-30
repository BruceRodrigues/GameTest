package player;

import gameloop.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;

import collision.Obj;

public class Bullet extends Obj {

	private double dx, dy;
	private double rad, speed;

	private Color color;

	public Bullet(double angle, double x, double y) {
		super(x, y, 4);
		this.speed = 15;
		this.rad = Math.toRadians(angle);
		this.dx = Math.cos(this.rad) * this.speed;
		this.dy = Math.sin(this.rad) * this.speed;

		this.color = Color.YELLOW;
	}

	public boolean update() {
		this.x += this.dx;
		this.y += this.dy;

		// test if the bullet is inside the panel
		if (this.x < -this.rad || this.x > (GamePanel.WIDTH + this.width)
				|| this.y < -this.height
				|| this.y > GamePanel.HEIGHT + this.height) {
			return true;
		}

		return false;
	}

	public void draw(Graphics2D graphics) {
		graphics.setColor(this.color);
		graphics.fillOval((int) (this.x - this.width),
				(int) (this.y - this.height), (int) this.width,
				(int) this.height);
	}
}
