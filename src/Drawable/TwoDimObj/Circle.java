package Drawable.TwoDimObj;

import Drawable.Face;
import Drawable.Vector3;
import Drawable.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Circle extends Shape {
    private double radius;
    private int segments;
    private Vector3[] vertices;

    public Circle(Vector3 position, Color color, double radius, int segments, Vector3 rotation) {
        super(position, color, rotation);
        this.radius = radius;
        this.segments = segments;
        initializeVertices();
    }
    private void initializeVertices() {
        vertices = new Vector3[segments];
        for (int i = 0; i < segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            vertices[i] = new Vector3(Math.cos(angle) * radius, Math.sin(angle) * radius, 0);
        }
        rotate(rotation.x, rotation.y, rotation.z);
    }

    @Override
    public Vector3[] getTransformedVertices(Vector3 cameraPosition, Vector3 cameraForward, Vector3 cameraUp, Vector3 cameraRight) {
        Vector3[] transformedVertices = new Vector3[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            transformedVertices[i] = vertices[i]
                    .add(position)
                    .transform(cameraPosition, cameraForward, cameraUp, cameraRight);
        }
        return transformedVertices;
    }

    @Override
    public List<Face> getFaces(Vector3[] transformedVertices) {
        List<Face> faces = new ArrayList<>();
        for (int i = 1; i < segments - 1; i++) {
            int[] indices = {0, i, i + 1};
            double averageZ = (transformedVertices[0].z + transformedVertices[i].z + transformedVertices[i + 1].z) / 3.0;

            faces.add(new Face(indices, averageZ, color, transformedVertices));
        }
        return faces;
    }

    @Override
    public void draw(Graphics2D g, double fov, int screenWidth, int screenHeight,
                     Vector3 cameraPosition, Vector3 cameraForward, Vector3 cameraUp, Vector3 cameraRight) {
        Vector3[] transformedVertices = getTransformedVertices(cameraPosition, cameraForward, cameraUp, cameraRight);

        List<Face> faces = getFaces(transformedVertices);

        for (Face face : faces) {
            drawFace(g, face, fov, screenWidth, screenHeight);
        }
    }


    private void drawFace(Graphics2D g, Face face, double fov, int screenWidth, int screenHeight) {
        int[] xPoints = new int[face.vertexIndices.length];
        int[] yPoints = new int[face.vertexIndices.length];

        for (int i = 0; i < face.vertexIndices.length; i++) {
            Vector3 vertex = face.transformedVertices[face.vertexIndices[i]];

            if (vertex.z <= 0) return;

            double scale = fov / vertex.z;
            xPoints[i] = (int) ((vertex.x * scale) + screenWidth / 2);
            yPoints[i] = (int) ((-vertex.y * scale) + screenHeight / 2);
        }

        Polygon polygon = new Polygon(xPoints, yPoints, face.vertexIndices.length);
        g.setColor(face.color);
        g.fillPolygon(polygon);
        g.drawPolygon(polygon);
    }


    @Override
    public void rotate(double angleX, double angleY, double angleZ) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].rotateX(angleX).rotateY(angleY).rotateZ(angleZ);
        }
    }
}