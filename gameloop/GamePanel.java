package gameloop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final int HEIGHT = 400;
	private static final int WIDTH = 400;

	private Thread gameThread;

	private Graphics2D graphics;

	private BufferedImage image;

	private boolean running;

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

	}

	private void gameRender() {
		this.graphics.setColor(Color.white);
		this.graphics.fillRect(0, 0, WIDTH, HEIGHT);
		this.graphics.setColor(Color.black);
		this.graphics.drawRect(100, 50, 100, 100);
		this.graphics.drawString("FPS: " + this.averageFPS, 10, 10);
	}

	private void gameDraw() {
		this.getGraphics().drawImage(this.image, 0, 0, null);
		this.getGraphics().dispose();
	}

}
