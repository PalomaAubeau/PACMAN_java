package pacman;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {
	//X = wall, O = skip, P = pacman, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
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


	// Class Block pour définir/donner les informations de chaque élément aux Hashet
	class Block {
		int x;
		int y;
		int width;
		int height;
		Image image;
		
		int startX;
		int startY;
		
		char direction = 'U';
		int velocityX = 0;
		int velocityY = 0;
		
		// Constructor de Block	
		Block(Image image, int x, int y, int width, int height){
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.startX = x;
			this.startY = y;
		}
		
		void updateDirection(char direction) {
			char prevDirection = this.direction;
			this.direction = direction;
			updateVelocity();
			this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
		}
		
		void updateVelocity() {
			if(this.direction == 'U') {
				this.velocityX = 0;
				this.velocityY = -boxSize/4;
			}
			else if(this.direction == 'D') {
				this.velocityX = 0;
				this.velocityY = boxSize/4;
			}
			else if(this.direction == 'L') {
				this.velocityX = -boxSize/4;
				this.velocityY = 0;
			}
			else if(this.direction == 'R') {
				this.velocityX = boxSize/4;
				this.velocityY = 0;
			}
		}
		
		void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
	}
	
	private int rowCount = 21; 
	private int columunCount = 19;
	private int boxSize = 32; 
	private int boardWidth = columunCount * boxSize;
	private int boardHeight = rowCount * boxSize;

	private Image wallImage;
	private Image blueGhostImage;
	private Image orangeGhostImage;
	private Image pinkGhostImage;
	private Image redGhostImage;
	private Image pacManUpImage;
	private Image pacManDownImage;
	private Image pacManLeftImage;
	private Image pacManRightImage;
	
	
	// Tableaux avec nos éléments
	HashSet<Block> walls;
	HashSet<Block> foods;
	HashSet<Block> ghosts;
	Block pacman;
	
	
	Timer gameLoop; //gameLoop pour re-actualiser les images
	char[] directions = {'U','D','L','R'}; // tableau de 4 caractères
	Random random = new Random();
	int score = 0;
	int lives = 3;
	boolean gameOver = false;
	
	//Constructor
	PacMan(){
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setBackground(Color.BLACK);
		addKeyListener(this);
		setFocusable(true);
		
		//load images
		wallImage = new ImageIcon(getClass().getResource("/images/wall.png")).getImage();
		blueGhostImage = new ImageIcon(getClass().getResource("/images/blueGhost.png")).getImage();
		orangeGhostImage = new ImageIcon(getClass().getResource("/images/orangeGhost.png")).getImage();
		pinkGhostImage = new ImageIcon(getClass().getResource("/images/pinkGhost.png")).getImage();
		redGhostImage = new ImageIcon(getClass().getResource("/images/redGhost.png")).getImage();
		pacManUpImage = new ImageIcon(getClass().getResource("/images/pacmanUp.png")).getImage();
		pacManDownImage = new ImageIcon(getClass().getResource("/images/pacmanDown.png")).getImage();
		pacManLeftImage = new ImageIcon(getClass().getResource("/images/pacmanLeft.png")).getImage();
		pacManRightImage = new ImageIcon(getClass().getResource("/images/pacmanRight.png")).getImage();
	
		loadMap();
		for(Block ghost :ghosts) {
			char newDirection = directions[random.nextInt(4)]; // 4 direction possible.
			ghost.updateDirection(newDirection);
		}
		// gameLoop = how long it takes to start timer, milliseconds gone between frames.
		gameLoop = new Timer(50, this); //50=delay : 20frame per sec (1000/50) & this=refer to PacMan object
		gameLoop.start();
	}
	
	public void loadMap() {
		walls = new HashSet<Block>();
		foods = new HashSet<Block>();
		ghosts = new HashSet<Block>();
		
		for(int r= 0; r < rowCount; r++) {
			for(int c= 0; c < columunCount; c++) {
				String row = tileMap[r] ;
				char titleMapChar = row.charAt(c);
				
				int x = c*boxSize;
				int y = r*boxSize;
				
				if(titleMapChar == 'X') {
					Block wall = new Block(wallImage, x, y, boxSize, boxSize);
					walls.add(wall);
				}
				else if(titleMapChar == 'b') {
					Block ghost = new Block(blueGhostImage, x, y, boxSize, boxSize);
					ghosts.add(ghost);
				}
				else if(titleMapChar == 'o') {
					Block ghost = new Block(orangeGhostImage, x, y, boxSize, boxSize);
					ghosts.add(ghost);
				}
				else if(titleMapChar == 'p') {
					Block ghost = new Block(pinkGhostImage, x, y, boxSize, boxSize);
					ghosts.add(ghost);
				}
				else if(titleMapChar == 'r') {
					Block ghost = new Block(redGhostImage, x, y, boxSize, boxSize);
					ghosts.add(ghost);
				}
				else if(titleMapChar == 'P') {
					pacman = new Block(pacManRightImage, x, y, boxSize, boxSize);
				}
				else if(titleMapChar == ' ') {
					Block food = new Block(null, x+14, y+14, 4, 4);
					foods.add(food);
				}
			}
		}
	}
public void paintComponent(Graphics g) {
	super.paintComponent(g);
	draw(g);
}

public void draw(Graphics g) {
    g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

    for (Block ghost : ghosts) {
        g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
    }

    for (Block wall : walls) {
        g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
    }

    g.setColor(Color.WHITE);
    for (Block food : foods) {
        g.fillRect(food.x, food.y, food.width, food.height);
    }
    //score
    g.setFont(new Font("Arial",Font.PLAIN,18));
    if (gameOver) {
        g.drawString("Game Over: " + String.valueOf(score), boxSize/2, boxSize/2);
    }
    else {
        g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), boxSize/2, boxSize/2);
    }
}

public void move() {
	pacman.x+=pacman.velocityX;
	pacman.y+=pacman.velocityY;
	// Vérifier si PacMan est hors de l'écran à gauche ou à droite et le repositionner
    if (pacman.x + pacman.width >= boardWidth) {
        pacman.x = 0;  // Revenir à la position la plus à gauche
    } else if (pacman.x <= 0) {
        pacman.x = boardWidth - pacman.width;  // Revenir à la position la plus à droite
    }
	
	//check wall collisions (step forwards if needed)
    for (Block wall : walls) {
        if (collision(pacman, wall)) {
            pacman.x -= pacman.velocityX;
            pacman.y -= pacman.velocityY;
            break;
        }
    }
    
    // check ghost collisions
    for (Block ghost : ghosts) {
    	if (collision(ghost, pacman)) {
            lives -= 1;
            if (lives == 0) {
                gameOver = true;
                return;
            }
            resetPositions();
        }
    	
    	
    	//pour les fantôme au row 9, forcer la direction Up s'ils bougent uniquement de L/R car pas de murs sur les côtés.
    	 if (ghost.y == boxSize*9 && ghost.direction != 'U' && ghost.direction != 'D') {
             ghost.updateDirection('U');
         }
    	
        ghost.x += ghost.velocityX;
        ghost.y += ghost.velocityY;
        
        for (Block wall : walls) {
            if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth){
                ghost.x -= ghost.velocityX;
                ghost.y -= ghost.velocityY;
                char newDirection = directions[random.nextInt(4)];
                ghost.updateDirection(newDirection);
            }
        }
       
    }
    
    // check food collision
    Block foodEaten = null;
    for(Block food: foods) {
    	if(collision(pacman,food)){
    		foodEaten = food;
    		score += 10;
    	}
    }
    foods.remove(foodEaten);
    
    if (foods.isEmpty()) {
        loadMap();
        resetPositions();
    }

    
}

public boolean collision(Block a,Block b) {
	return  a.x < b.x + b.width &&
            a.x + a.width > b.x &&
            a.y < b.y + b.height &&
            a.y + a.height > b.y;
}


public void resetPositions() {
    pacman.reset();
    pacman.velocityX = 0;
    pacman.velocityY = 0;
    for (Block ghost : ghosts) {
        ghost.reset();
        char newDirection = directions[random.nextInt(4)];
        ghost.updateDirection(newDirection);
    }
}

@Override
public void actionPerformed(ActionEvent e) {
	move();
	repaint(); // rappelle paintComponent, mais besoin d'un Timer pour que ça fonctionne => gameLoop
	if (gameOver) {
        gameLoop.stop();
    }
}

@Override
public void keyTyped(KeyEvent e) {}

@Override
public void keyPressed(KeyEvent e) {}

@Override
public void keyReleased(KeyEvent e) {
	
	if (gameOver) {
        loadMap();
        resetPositions();
        lives = 3;
        score = 0;
        gameOver = false;
        gameLoop.start();
    }
	
// definir direction et image utilisée
	if(e.getKeyCode()== KeyEvent.VK_DOWN) {pacman.updateDirection('D');}
	else if(e.getKeyCode()== KeyEvent.VK_UP) {pacman.updateDirection('U');}
	else if(e.getKeyCode()== KeyEvent.VK_LEFT) {pacman.updateDirection('L');}
	else if(e.getKeyCode()== KeyEvent.VK_RIGHT) {pacman.updateDirection('R');}
	
	if (pacman.direction == 'U') {
        pacman.image = pacManUpImage;
    }
    else if (pacman.direction == 'D') {
        pacman.image = pacManDownImage;
    }
    else if (pacman.direction == 'L') {
        pacman.image = pacManLeftImage;
    }
    else if (pacman.direction == 'R') {
        pacman.image = pacManRightImage;
    }
}


}
