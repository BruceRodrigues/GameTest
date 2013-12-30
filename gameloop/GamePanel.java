package gameloop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import player.Bullet;
import player.Player;
import player.Player.Movement;
import enemy.Enemy;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;

	public static final int HEIGHT = 400;
	public static final int WIDTH = 400;

	private Thread gameThread;

	private Graphics2D graphics;

	private BufferedImage image;

	private boolean running;

	public static Player player;
	public static List<Bullet> bullets;
	public static List<Enemy> enemies;

	private long waveStartTimer;
	private long waveStartTimerDiff;
	private int waveNumber;
	private boolean waveStart;
	private int waveDelay = 5000;

	private int FPS = 30;
	private double averageFPS;

	private Font infoFont;

	public GamePanel() {
		super();
		this.running = false;
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.setFocusable(true);
		this.requestFocus();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (this.gameThread == null) {
			this.gameThread = new Thread(this);
		}
		this.gameThread.start();
		this.addKeyListener(this);
	}

	@Override
	public void run() {
		this.running = true;
		this.image = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		this.graphics = (Graphics2D) this.image.getGraphics();
		this.graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		this.infoFont = this.graphics.getFont();

		// FPS Control
		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;

		int frameCount = 0;
		int maxFrameCount = 30;
		long targetTime = 1000 / this.FPS;

		this.player = new Player();
		this.bullets = new ArrayList<Bullet>();
		this.enemies = new ArrayList<Enemy>();

		this.waveStartTimer = 0;
		this.waveStartTimerDiff = 0;
		this.waveNumber = 0;
		this.waveStart = true;

		// GAME LOOP
		while (this.running) {

			startTime = System.nanoTime();

			this.gameUpdate();
			this.gameRender();
			this.gameDraw();

			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;

			waitTime = targetTime - URDTimeMillis;

			try {
				Thread.sleep(Math.abs(waitTime));
			} catch (Exception e) {
				e.printStackTrace();
			}

			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if (frameCount == maxFrameCount) {
				this.averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}
		}
	}

	private void gameUpdate() {

		// enemies wave
		if (this.waveStartTimer == 0 && enemies.size() == 0) {
			this.waveNumber++;
			this.waveStart = false;
			this.waveStartTimer = System.nanoTime();
		} else {
			this.waveStartTimerDiff = (System.nanoTime() - this.waveStartTimer) / 1000000;
			if (this.waveStartTimerDiff > this.waveDelay) {
				this.waveStart = true;
				this.waveStartTimer = 0;
				this.waveStartTimerDiff = 0;
			}
		}

		// create new enemies
		if (this.waveStart && enemies.size() == 0) {
			createNewEnemies();
		}

		// update player state
		this.player.update();

		// update bullets
		for (int i = 0; i < this.bullets.size(); i++) {
			if (this.bullets.get(i).update()) {
				this.bullets.remove(i);
				i--;
			}
		}

		// update enemies
		for (Enemy enemy : this.enemies) {
			enemy.update();
		}

		// Collision test
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			double bx = bullet.getX();
			double by = bullet.getY();
			double br = bullet.getR();

			for (int j = 0; j < enemies.size(); j++) {
				Enemy enemy = enemies.get(j);
				double ex = enemy.getX();
				double ey = enemy.getY();
				double er = enemy.getR();

				double dx = bx - ex;
				double dy = by - ey;
				double dist = Math.sqrt(dx * dx + dy * dy);

				if (dist < br + er) {
					enemy.hit();
					bullets.remove(i);
					i--;
					break;
				}
			}
		}

		// Test if enemy is dead
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).isDead()) {
				this.enemies.remove(i);
				i--;
			}
		}
	}

	// Create a wave of enemies
	private void createNewEnemies() {
		enemies.clear();
		for (int i = 0; i < this.waveNumber * 3; i++) {
			enemies.add(new Enemy(1, 1));
		}
	}

	private void gameRender() {
		// Background and info
		this.graphics.setFont(this.infoFont);
		this.graphics.setColor(new Color(0, 100, 255));
		this.graphics.fillRect(0, 0, WIDTH, HEIGHT);
		this.graphics.setColor(Color.black);
		this.graphics.drawString("FPS: " + this.averageFPS, 10, 10);
		this.graphics.drawString("Bullets: " + this.bullets.size(), 10, 20);

		// render player
		this.player.draw(this.graphics);

		// render bullets
		for (int i = 0; i < this.bullets.size(); i++) {
			this.bullets.get(i).draw(this.graphics);
		}

		// render enemies
		for (Enemy enemy : this.enemies) {
			enemy.draw(this.graphics);
		}

		if (this.waveStartTimer != 0) {
			this.graphics.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "- W A V E  " + this.waveNumber + "  -";
			int length = (int) this.graphics.getFontMetrics()
					.getStringBounds(s, this.graphics).getWidth();
			int alpha = (int) (255 * Math.sin(3.14 * this.waveStartTimerDiff
					/ this.waveDelay));
			if (alpha > 255) {
				alpha = 255;
			}
			this.graphics.setColor(new Color(255, 255, 255, alpha));
			this.graphics.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
		}
	}

	private void gameDraw() {
		this.getGraphics().drawImage(this.image, 0, 0, null);
		this.getGraphics().dispose();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			this.player.move(Movement.LEFT, true);
			;
			break;
		case KeyEvent.VK_RIGHT:
			this.player.move(Movement.RIGHT, true);
			break;
		case KeyEvent.VK_DOWN:
			this.player.move(Movement.DOWN, true);
			break;
		case KeyEvent.VK_UP:
			this.player.move(Movement.UP, true);
			break;
		case KeyEvent.VK_SPACE:
			this.player.setFiring(true);
			break;
		default:
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			this.player.move(Movement.LEFT, false);
			break;
		case KeyEvent.VK_RIGHT:
			this.player.move(Movement.RIGHT, false);
			break;
		case KeyEvent.VK_DOWN:
			this.player.move(Movement.DOWN, false);
			break;
		case KeyEvent.VK_UP:
			this.player.move(Movement.UP, false);
			break;
		case KeyEvent.VK_SPACE:
			this.player.setFiring(false);
			break;
		default:
		}
	}

}
