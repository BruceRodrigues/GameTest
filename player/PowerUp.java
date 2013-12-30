package player;

import gameloop.GamePanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import collision.Obj;

public class PowerUp extends Obj {

	private int type;

	private Color color;

	public PowerUp(int type, double x, double y) {
		super(x, y, 3);

		this.type = type;

		switch (type) {
		case 1: // speed
			this.color = Color.GREEN;
			break;
		case 2: // power
			this.color = Color.yellow;
			break;
		default:
		}
	}

	public int getType() {
		return this.type;
	}

	public boolean update() {
		this.y += 2;

		if (this.y < GamePanel.HEIGHT) {
			return true;
		}

		return false;
	}

	public void draw(Graphics2D graphics) {
		graphics.setColor(this.color);
		graphics.fillRect((int) (this.x - this.width),
				(int) (this.y - this.height), (int) (2 * this.width),
				(int) (2 * this.height));
		graphics.setStroke(new BasicStroke(3));
		graphics.setColor(this.color.darker());
		graphics.setStroke(new BasicStroke(1));
	}

}
