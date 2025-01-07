package pacman;

import javax.swing.JFrame;

public class App {

	public static void main(String[] args) {
		//Define grid/frame
		int rowCount = 21; // index begins at 0 until 20
		int columunCount = 19;
		int boxSize = 32; //pixel
		int boardWidth = columunCount * boxSize;
		int boardHeight = rowCount * boxSize;
		
		JFrame frame = new JFrame("Pac Man");
		//frame.setVisible(true);
		frame.setSize(boardWidth,boardHeight);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		PacMan pacmanGame= new PacMan();
		frame.add(pacmanGame);
		frame.pack();
		pacmanGame.requestFocus(true);
		frame.setVisible(true);
	}

}
