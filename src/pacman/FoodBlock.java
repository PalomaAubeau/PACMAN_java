package pacman;

import java.awt.*;

public class FoodBlock extends Block {
    public FoodBlock(int x, int y) {
        super(null, x, y, 4, 4); // Small size for food
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }
}

