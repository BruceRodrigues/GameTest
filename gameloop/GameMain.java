package gameloop;

import javax.swing.JFrame;

public class GameMain {

	public static void main(String args[]) {
		JFrame window = new JFrame("Game Test");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new GamePanel());
		window.pack();
		window.setVisible(true);
	}

}
