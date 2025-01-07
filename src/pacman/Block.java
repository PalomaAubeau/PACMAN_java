package pacman;

import java.awt.*;

public abstract class Block {
    protected int x, y;
    protected int width, height;
    protected final int startX, startY;
    protected Image image;

    public Block(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }
    
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
    }

    public void reset() {
        this.x = startX;
        this.y = startY;
    }
}
