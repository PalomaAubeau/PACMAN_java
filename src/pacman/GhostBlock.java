package pacman;

import java.awt.*;
import java.util.Random;

public class GhostBlock extends Block {
    private Direction direction;
    private final Random random = new Random();
    private final int startX, startY; 
    private int velocityX = 0, velocityY = 0;

    public GhostBlock(Image image, int x, int y, int size) {
        super(image, x, y, size, size);
        this.startX = x; 
        this.startY = y;
        this.direction = Direction.values()[random.nextInt(4)];
        updateVelocity();
    }
    
    public void updateDirection(Direction newDirection) {
        this.direction = newDirection;
        updateVelocity();
    }
    
    public void updateDirectionRandomly() {
        Direction newDirection;
        do {
            newDirection = Direction.values()[random.nextInt(4)];
        } while (newDirection == direction); 
        updateDirection(newDirection);
    }

   
	
	private void updateVelocity() { 
		this.velocityX = direction.getDeltaX() * 8;
		this.velocityY = direction.getDeltaY() * 8; 
	}
	 

    public void move() {
        x += direction.getDeltaX() * 8; 
        y += direction.getDeltaY() * 8;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

}

