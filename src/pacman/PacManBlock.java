package pacman;

import java.awt.*;

public class PacManBlock extends Block {
    private Direction direction;
    private int velocityX = 0, velocityY = 0;
  

    public PacManBlock(Image image, int x, int y, int size) {
        super(image, x, y, size, size);
    }

    public void updateDirection(Direction direction) {
        this.direction = direction;
        velocityX = direction.getDeltaX() * 8;
        velocityY = direction.getDeltaY() * 8;
    }
    

    public void move() {
        x += velocityX;
        y += velocityY;
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
    
    @Override
    public void reset() {
        super.reset(); 
        this.direction = null; 
        this.velocityX = 0; 
        this.velocityY = 0; 
    }
    
}



