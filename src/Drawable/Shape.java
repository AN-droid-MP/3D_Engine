package Drawable;

import java.awt.*;

public abstract class Shape {
    protected Vector3 position;
    protected Color color;
    protected Vector3 rotation;

    public Shape(Vector3 position, Color color, Vector3 rotation) {
        this.position = position;
        this.color = color;
        this.rotation = rotation;
    }

    public void rotate(double angleX, double angleY, double angleZ) {
        rotation = rotation.add(new Vector3(angleX, angleY, angleZ));
    }

    public Color getColor() {
        return color;
    }

    public abstract double intersect(Vector3 origin, Vector3 direction);

}
