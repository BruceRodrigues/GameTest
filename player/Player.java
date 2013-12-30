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

	private boolean left, right, up, down;

	public Player() {
		this.x = GamePanel.WIDTH / 2;
		this.y = GamePanel.HEIGHT / 2;
		this.r = 5;

		this.dx = 0;
		this.dy = 0;
		this.speed = 5;

		this.lives = 3;

		this.color1 = Color.BLACK;
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

		this.dx = 0;
		this.dy = 0;
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

	public void draw(Graphics2D graphics) {
		graphics.setColor(this.color1);
		graphics.fillOval(this.x - this.r, this.y - this.r, 2 * this.r,
				2 * this.r);
		graphics.setStroke(new BasicStroke(3));
		graphics.setColor(this.color1.darker());
		graphics.setStroke(new BasicStroke(1));
	}
}
