package gameloop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import player.Bullet;
import player.Player;
import player.Player.Movement;

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

	private int FPS = 30;
	private double averageFPS;

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
		this.player.update();
		for (int i = 0; i < this.bullets.size(); i++) {
			if (this.bullets.get(i).update()) {
				this.bullets.remove(i);
				i--;
			}
		}
	}

	private void gameRender() {
		this.graphics.setColor(Color.LIGHT_GRAY);
		this.graphics.fillRect(0, 0, WIDTH, HEIGHT);
		this.graphics.setColor(Color.black);
		this.graphics.drawRect(100, 50, 100, 100);
		this.graphics.drawString("FPS: " + this.averageFPS, 10, 10);
		this.graphics.drawString("Bullets: " + this.bullets.size(), 10, 20);

		this.player.draw(this.graphics);
		for (int i = 0; i < this.bullets.size(); i++) {
			this.bullets.get(i).draw(this.graphics);
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
