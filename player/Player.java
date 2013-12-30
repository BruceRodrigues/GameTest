package player;

import gameloop.GamePanel;
import image.Sprite;

import java.awt.Graphics2D;

import collision.Obj;

public class Player extends Obj {

	public enum Movement {
		LEFT, RIGHT, DOWN, UP
	}

	private int dx, dy, speed;

	private int lives;

	private boolean firing;
	private long firingTimer, firingDelay;

	private boolean left, right, up, down;

	private boolean recovering;
	private long recoveryTimer;

	private int power;
	private int powerLevel;
	private int[] requiredPower = { 1, 2, 3, 4, 5 };

	private Sprite image;
	private Sprite recoveringImage;

	public Player() {
		super(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 10, 10);
		this.image = new Sprite("/hero/pkmnFRHero.png");
		this.recoveringImage = new Sprite("/hero/recHero.png");

		this.dx = 0;
		this.dy = 0;
		this.speed = 5;

		this.lives = 3;

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
		if (this.x < this.width) {
			this.x = this.width;
		}
		if (this.y < this.height) {
			this.y = this.height;
		}
		if (this.x > GamePanel.WIDTH) {
			this.x = GamePanel.WIDTH;
		}
		if (this.y > GamePanel.HEIGHT) {
			this.y = GamePanel.HEIGHT;
		}
		//

		this.dx = 0;
		this.dy = 0;

		if (this.firing) {
			long elapsed = (System.nanoTime() - this.firingTimer) / 1000000;
			if (elapsed > this.firingDelay) {
				this.firingTimer = System.nanoTime();

				if (this.powerLevel == 0) {
					GamePanel.bullets.add(new Bullet(270, this.x + this.width
							/ 2, this.y));
				} else if (this.powerLevel < 3) {
					GamePanel.bullets.add(new Bullet(270, this.x + 5, this.y));
					GamePanel.bullets.add(new Bullet(270, this.x - 5, this.y));
				} else {
					GamePanel.bullets.add(new Bullet(270, this.x + 14, this.y));
					GamePanel.bullets.add(new Bullet(270, this.x + 7, this.y));
					GamePanel.bullets.add(new Bullet(270, this.x, this.y));
					GamePanel.bullets.add(new Bullet(270, this.x - 7, this.y));
				}
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
			this.recoveringImage.draw(graphics, (int) this.x, (int) this.y);
		} else {
			this.image.draw(graphics, (int) this.x, (int) this.y);
		}
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
		this.speed = 5;
		this.power = 0;
		this.powerLevel = 0;

	}

	public void increasePower(int i) {
		this.power += i;
		if (this.power >= this.requiredPower[this.powerLevel]) {
			this.power -= this.requiredPower[this.powerLevel];
			this.powerLevel++;
		}
	}

	public int getPowerLevel() {
		return this.powerLevel;
	}

	public int getRequiredPower() {
		return this.requiredPower[this.powerLevel];
	}

	public void increaseSpeed() {
		this.speed += 2;
	}
}
