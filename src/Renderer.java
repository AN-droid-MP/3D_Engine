import Drawable.Face;
import Drawable.Shape;
import Drawable.Vector3;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Renderer extends JPanel {
    private Scene scene;

    public Renderer(Scene scene) {
        this.scene = scene;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setComposite(AlphaComposite.Src);

        int screenWidth = getWidth();
        int screenHeight = getHeight();

        float[][] zBuffer = new float[screenWidth][screenHeight];
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                zBuffer[x][y] = Float.POSITIVE_INFINITY;
            }
        }

        Camera camera = scene.getCamera();

        for (Shape shape : scene.getShapes()) {
            Vector3[] transformedVertices = shape.getTransformedVertices(
                    camera.getPosition(),
                    camera.getForward(),
                    camera.getUp(),
                    camera.getRight()
            );

            List<Face> shapeFaces = shape.getFaces(transformedVertices);
            for (Face face : shapeFaces) {
                drawFace(g2d, face, camera.getFov(), screenWidth, screenHeight, zBuffer);
            }
        }

        g2d.setColor(Color.BLACK);
        g2d.drawString("Camera Orientation:", 10, 20);
        String[] orientation = camera.getOrientation().split("\n");
        for (int i = 0; i < orientation.length; i++) {
            g2d.drawString(orientation[i], 10, 40 + i * 15);
        }
    }


    private void drawFace(Graphics2D g, Face face, double fov, int screenWidth, int screenHeight, float[][] zBuffer) {
        int[] xPoints = new int[face.vertexIndices.length];
        int[] yPoints = new int[face.vertexIndices.length];
        float[] zPoints = new float[face.vertexIndices.length];

        for (int i = 0; i < face.vertexIndices.length; i++) {
            Vector3 vertex = face.transformedVertices[face.vertexIndices[i]];

            if (vertex.z <= 0) return;

            double scale = fov / vertex.z;
            xPoints[i] = (int) ((vertex.x * scale) + screenWidth / 2);
            yPoints[i] = (int) ((-vertex.y * scale) + screenHeight / 2);
            zPoints[i] = (float) vertex.z;
        }

        Color opaqueColor = new Color(face.color.getRed(), face.color.getGreen(), face.color.getBlue(), 255);

        for (int i = 1; i < face.vertexIndices.length - 1; i++) {
            int[] triangleX = {xPoints[0], xPoints[i], xPoints[i + 1]};
            int[] triangleY = {yPoints[0], yPoints[i], yPoints[i + 1]};
            float[] triangleZ = {zPoints[0], zPoints[i], zPoints[i + 1]};

            rasterizeTriangle(g, triangleX, triangleY, triangleZ, opaqueColor, zBuffer);
        }
    }


    private void rasterizeTriangle(Graphics2D g, int[] xPoints, int[] yPoints, float[] zPoints, Color color, float[][] zBuffer) {
        int screenWidth = zBuffer.length;
        int screenHeight = zBuffer[0].length;

        int minX = Math.max(0, Arrays.stream(xPoints).min().orElse(0));
        int maxX = Math.min(screenWidth - 1, Arrays.stream(xPoints).max().orElse(screenWidth - 1));
        int minY = Math.max(0, Arrays.stream(yPoints).min().orElse(0));
        int maxY = Math.min(screenHeight - 1, Arrays.stream(yPoints).max().orElse(screenHeight - 1));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (isInsideTriangle(x, y, xPoints, yPoints)) {
                    float z = interpolateZ(x, y, xPoints, yPoints, zPoints);

                    if (z < zBuffer[x][y]) {
                        zBuffer[x][y] = z;
                        g.setColor(color);
                        g.drawLine(x, y, x, y);
                    }
                }
            }
        }
    }

    private boolean isInsideTriangle(int x, int y, int[] xPoints, int[] yPoints) {
        double areaOrig = triangleArea(xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
        double area1 = triangleArea(x, y, xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
        double area2 = triangleArea(xPoints[0], yPoints[0], x, y, xPoints[2], yPoints[2]);
        double area3 = triangleArea(xPoints[0], yPoints[0], xPoints[1], yPoints[1], x, y);
        return Math.abs(areaOrig - (area1 + area2 + area3)) < 1e-5;
    }

    private double triangleArea(int x1, int y1, int x2, int y2, int x3, int y3) {
        return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
    }

    private float interpolateZ(int x, int y, int[] xPoints, int[] yPoints, float[] zPoints) {
        double area = triangleArea(xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
        double w1 = triangleArea(x, y, xPoints[1], yPoints[1], xPoints[2], yPoints[2]) / area;
        double w2 = triangleArea(xPoints[0], yPoints[0], x, y, xPoints[2], yPoints[2]) / area;
        double w3 = triangleArea(xPoints[0], yPoints[0], xPoints[1], yPoints[1], x, y) / area;
        return (float) (w1 * zPoints[0] + w2 * zPoints[1] + w3 * zPoints[2]);
    }

}