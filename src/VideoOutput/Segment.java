package VideoOutput;


import java.awt.Color;

public class Segment {

    int xOffset;
    int yOffset;
    int width;
    int height;
    Color color;

    public Segment(int xOffset, int yOffset, int width, int height) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
        this.color = Color.BLACK;
    }

//    Getters and setters
    public int getxOffset() {
        return xOffset;
    }
    public int getyOffset() {
        return yOffset;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
