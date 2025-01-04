import Drawable.Shape;
import Drawable.Vector3;

import javax.swing.*;
import java.awt.*;


public class Renderer extends JPanel {
    private Scene scene;
    private int width;
    private int height;

    public Renderer(Scene scene) {
        this.scene = scene;
        this.width = 800;
        this.height = 600;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Camera camera = scene.getCamera();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double aspectRatio = (double) width / height;
                double px = (2 * (x + 0.5) / width - 1) * Math.tan(Math.toRadians(camera.getFov()) / 2) * aspectRatio;
                double py = (1 - 2 * (y + 0.5) / height) * Math.tan(Math.toRadians(camera.getFov()) / 2);

                Vector3 rayDirection = camera.getOrientation().transform(new Vector3(px, py, -1)).normalize();

                Vector3 rayOrigin = camera.getPosition();
                Color pixelColor = traceRay(rayOrigin, rayDirection);

                g2d.setColor(pixelColor);
                g2d.fillRect(x, y, 1, 1);
            }
        }
    }

    private Color traceRay(Vector3 origin, Vector3 direction) {
        double closestDistance = Double.MAX_VALUE;
        Color hitColor = Color.BLACK;

        for (Shape shape : scene.getShapes()) {
            double distance = shape.intersect(origin, direction);
            if (distance > 0 && distance < closestDistance) {
                closestDistance = distance;
                hitColor = shape.getColor();
            }
        }

        return hitColor;
    }
}