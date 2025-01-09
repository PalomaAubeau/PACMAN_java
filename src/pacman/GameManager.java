package pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;


public class GameManager extends JPanel implements ActionListener, KeyListener {
    private final int rowCount = 21;
    private final int columnCount = 19;
    private final int boxSize = 32;
    private final int boardWidth = columnCount * boxSize;
    private final int boardHeight = rowCount * boxSize;

    private Image wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage;
    private Image pacManUpImage, pacManDownImage, pacManLeftImage, pacManRightImage;

    private HashSet<Block> walls;
    private HashSet<Block> foods;
    private HashSet<GhostBlock> ghosts;
    private PacManBlock pacman;

    private final Timer gameLoop;
  

    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

    private final String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    public GameManager() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        loadImages();
        initializeGame();

        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    private void loadImages() {
        wallImage = new ImageIcon(getClass().getResource("/images/wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("/images/blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("/images/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("/images/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("/images/redGhost.png")).getImage();
        pacManUpImage = new ImageIcon(getClass().getResource("/images/pacmanUp.png")).getImage();
        pacManDownImage = new ImageIcon(getClass().getResource("/images/pacmanDown.png")).getImage();
        pacManLeftImage = new ImageIcon(getClass().getResource("/images/pacmanLeft.png")).getImage();
        pacManRightImage = new ImageIcon(getClass().getResource("/images/pacmanRight.png")).getImage();
    }

    private void initializeGame() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();
        pacman = null;

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                char tile = tileMap[r].charAt(c);
                int x = c * boxSize;
                int y = r * boxSize;

                switch (tile) {
                    case 'X':
                        walls.add(new WallBlock(wallImage, x, y, boxSize));
                        break;
                    case 'b':
                        ghosts.add(new GhostBlock(blueGhostImage, x, y, boxSize));
                        break;
                    case 'o':
                        ghosts.add(new GhostBlock(orangeGhostImage, x, y, boxSize));
                        break;
                    case 'p':
                        ghosts.add(new GhostBlock(pinkGhostImage, x, y, boxSize));
                        break;
                    case 'r':
                        ghosts.add(new GhostBlock(redGhostImage, x, y, boxSize));
                        break;
                    case 'P':
                        pacman = new PacManBlock(pacManRightImage, x, y, boxSize);
                        break;
                    case ' ':
                        foods.add(new FoodBlock(x + 14, y + 14));
                        break;
                }
            }
        }
    }

 

    private void draw(Graphics g) {
		
		  for (Block wall : walls) wall.draw(g); for (Block food : foods) food.draw(g);
		  for (GhostBlock ghost : ghosts) ghost.draw(g); pacman.draw(g);
		 

        // Display score and lives
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));

        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), boxSize/2, boxSize/2);
        }
        else {
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), boxSize/2, boxSize/2);
        }
    }

    private void playGame() {
    	
    // PACMAN
       pacman.move();
       //check pacman position on screen
       if (pacman.x + pacman.width >= boardWidth) {
           pacman.x = 0; 
       } else if (pacman.x <= 0) {
           pacman.x = boardWidth - pacman.width; 
       }

       //check wall collisions
       for (Block wall : walls) {
           if (collision(pacman, wall)) {
               pacman.x -= pacman.getVelocityX();
               pacman.y -= pacman.getVelocityY();
               break;
           }
       }
       
       	//GHOST
		  for (GhostBlock ghost : ghosts) { 
			  ghost.move();
			// action with pacman collisions
			  if (collision(pacman, ghost)) {
	               lives--;
	               if (lives == 0) {
	                   gameOver = true;
	               }
	               pacman.reset();
	               for (GhostBlock g : ghosts) {
	                   g.reset(); 
	               }
	               break;
	           }
			// at row 9, ghost shouldn't be stuck in x axe.
			  if (ghost.y == boxSize * 9 && ghost.getDirection() != Direction.UP && ghost.getDirection() != Direction.DOWN) {
				    ghost.updateDirection(Direction.UP);
				}
			//check wall collisions
			  for (Block wall : walls) {
		            if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
		                ghost.x -= ghost.getVelocityX();
		                ghost.y -= ghost.getVelocityY();
		                ghost.updateDirection(null);
		                break;
		            }
		        }
		  }	  
        
       //FOOD
       Block foodEaten = null;
       for(Block food: foods) {
       	if(collision(pacman,food)){
       		foodEaten = food;
       		score += 10;
       	}
       }
       foods.remove(foodEaten);
       
        if (foods.isEmpty()) {
            initializeGame();
        }
    }

    private boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }
    
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        playGame();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

 

    @Override
    public void keyReleased(KeyEvent e) {
    	 if (gameOver) {
         	initializeGame();
             lives = 3;
             score = 0;
             gameOver = false;
             gameLoop.start();
             return;
         }

      
             switch (e.getKeyCode()) {
                 case KeyEvent.VK_UP -> pacman.updateDirection(Direction.UP);
                 case KeyEvent.VK_DOWN -> pacman.updateDirection(Direction.DOWN);
                 case KeyEvent.VK_LEFT -> pacman.updateDirection(Direction.LEFT);
                 case KeyEvent.VK_RIGHT -> pacman.updateDirection(Direction.RIGHT);
             }
             
             switch (pacman.getDirection()) {
             case UP -> pacman.image = pacManUpImage;
             case DOWN -> pacman.image = pacManDownImage;
             case LEFT -> pacman.image = pacManLeftImage;
             case RIGHT -> pacman.image = pacManRightImage;
         }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {}
}


