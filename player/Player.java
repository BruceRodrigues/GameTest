package player;

import gameloop.GamePanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Player {

	public enum Movement {
		LEFT, RIGHT, DOWN, UP
	}

	private int x, y, r;

	private int dx, dy, speed;

	private int lives;

	private Color color1, color2;

	private boolean firing;
	private long firingTimer, firingDelay;

	private boolean left, right, up, down;

	private boolean recovering;
	private long recoveryTimer;

	public Player() {
		this.x = GamePanel.WIDTH / 2;
		this.y = GamePanel.HEIGHT / 2;
		this.r = 5;

		this.dx = 0;
		this.dy = 0;
		this.speed = 5;

		this.lives = 3;

		this.color1 = Color.BLACK;
		this.color2 = Color.red;

		this.firing = false;
		this.firingTimer = System.nanoTime();
		this.firingDelay = 200;

		this.recovering = false;
		this.recoveryTimer = 0;
	}

	public void update() {
		if (this.left) {
			this.dx = -this.speed;
		}
		if (this.right) {
			this.dx = this.speed;
		}
		if (this.up) {
			this.dy = -this.speed;
		}
		if (this.down) {
			this.dy = this.speed;
		}

		this.x += this.dx;
		this.y += this.dy;

		// border control
		if (this.x < this.r) {
			this.x = this.r;
		}
		if (this.y < this.r) {
			this.y = this.r;
		}
		if (this.x > GamePanel.WIDTH - this.r) {
			this.x = GamePanel.WIDTH;
		}
		if (this.y > GamePanel.HEIGHT - this.r) {
			this.y = GamePanel.HEIGHT;
		}
		//

		this.dx = 0;
		this.dy = 0;

		if (this.firing) {
			long elapsed = (System.nanoTime() - this.firingTimer) / 1000000;
			if (elapsed > this.firingDelay) {
				GamePanel.bullets.add(new Bullet(270, this.x, this.y));
				this.firingTimer = System.nanoTime();
			}
		}

		long elapsed = (System.nanoTime() - this.recoveryTimer) / 1000000;
		if (elapsed > 2000) {
			this.recovering = false;
			this.recoveryTimer = 0;
		}
	}

	public void move(Movement m, boolean value) {
		switch (m) {
		case RIGHT:
			this.right = value;
			break;
		case LEFT:
			this.left = value;
			break;
		case UP:
			this.up = value;
			break;
		case DOWN:
			this.down = value;
			break;
		default:
		}
	}

	public void setFiring(boolean value) {
		this.firing = value;
	}

	public void draw(Graphics2D graphics) {
		if (this.recovering) {
			graphics.setColor(this.color2);
		} else {
			graphics.setColor(this.color1);
		}
		graphics.fillOval(this.x - this.r, this.y - this.r, 2 * this.r,
				2 * this.r);
		graphics.setStroke(new BasicStroke(3));
		graphics.setColor(this.color1.brighter());
		graphics.setStroke(new BasicStroke(1));
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getR() {
		return this.r;
	}

	public int getLives() {
		return this.lives;
	}

	public boolean isRecovering() {
		return this.recovering;
	}

	public void hit() {
		this.lives--;
		this.recovering = true;
		this.recoveryTimer = System.nanoTime();
	}
}
