package Drawable;

import java.awt.*;
import java.util.List;

public abstract class Shape {
    protected Vector3 position;
    protected Color color;
    protected Vector3 rotation;

    public Shape(Vector3 position, Color color, Vector3 rotation) {
        this.position = position;
        this.color = color;
        this.rotation = rotation;
    }

    public abstract void draw(Graphics2D g, double fov, int screenWidth, int screenHeight,
                              Vector3 cameraPosition, Vector3 cameraForward, Vector3 cameraUp, Vector3 cameraRight);

    public abstract Vector3[] getTransformedVertices(Vector3 cameraPosition, Vector3 cameraForward, Vector3 cameraUp, Vector3 cameraRight);

    public abstract List<Face> getFaces(Vector3[] transformedVertices);

    public void rotate(double angleX, double angleY, double angleZ) {
        rotation = rotation.add(new Vector3(angleX, angleY, angleZ));
    }
}
