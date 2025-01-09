package pacman;

import java.awt.*;
import java.util.Random;

public class GhostBlock extends DynamicBlocks {
    private final Random random = new Random();

    public GhostBlock(Image image, int x, int y, int size) {
        super(image, x, y, size);
        this.direction = Direction.values()[random.nextInt(4)];
        updateVelocity();
    }
    
    @Override
    public void updateDirection(Direction newDirection) {
    	if (newDirection == null) {
            //If no direction given, choose a random value
            newDirection = Direction.values()[random.nextInt(4)];
        }
        super.updateDirection(newDirection);
    }
}
    

