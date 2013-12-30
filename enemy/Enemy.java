package enemy;

import gameloop.GamePanel;
import image.Sprite;

import java.awt.Graphics2D;

import collision.Obj;

public class Enemy extends Obj {

	private double dx, dy, rad;
	private double speed;

	private int health;
	private int type;
	private int rank;

	private Sprite image;

	private boolean ready;
	private boolean dead;

	public Enemy(int type, int rank) {
		super(Math.random() * GamePanel.WIDTH / 2 + GamePanel.WIDTH / 4, -5, 0);

		this.image = new Sprite("/enemy/ghost.png");

		this.setHeight(this.image.getHeight());
		this.setWidth(this.image.getWidth());

		this.type = type;
		this.rank = rank;

		if (this.type == 1) {
			if (rank == 1) {
				this.speed = 2;
				this.health = 1;
			}
		}

		double angle = Math.random() * 140 + 20;
		this.rad = Math.toRadians(angle);

		this.dx = Math.cos(this.rad) * this.speed;
		this.dy = Math.sin(this.rad) * this.speed;

		this.ready = false;
		this.dead = false;
	}

	public void hit() {
		this.health--;
		if (this.health <= 0) {
			this.dead = true;
		}
	}

	public void update() {
		this.x += this.dx;
		this.y += this.dy;

		if (!this.ready) {
			if (this.x > this.width && this.x < GamePanel.WIDTH - this.width
					&& this.y > this.height
					&& this.y < GamePanel.HEIGHT - this.height) {
				this.ready = true;
			}
		}

		if (this.x < this.width && this.dx < 0) {
			this.dx = -this.dx;
		}
		if (this.y < this.height && this.dy < 0) {
			this.dy = -this.dy;
		}
		if (this.x > GamePanel.WIDTH - this.width && this.dx > 0) {
			this.dx = -this.dx;
		}
		if (this.y > GamePanel.HEIGHT - this.height && this.dy > 0) {
			this.dy = -this.dy;
		}
	}

	public void draw(Graphics2D graphics) {
		this.image.draw(graphics, (int) this.x, (int) this.y);
	}

	public boolean isDead() {
		return this.dead;
	}
}
