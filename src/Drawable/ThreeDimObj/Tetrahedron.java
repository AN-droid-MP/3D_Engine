package Drawable.ThreeDimObj;

import Drawable.Face;
import Drawable.Vector3;
import Drawable.Shape;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class Tetrahedron extends Shape {
    private Vector3[] vertices;

    public Tetrahedron(Vector3 position, Color color, double size, Vector3 rotation) {
        super(position, color, rotation);
        initializeVertices(size);
    }

    private void initializeVertices(double size) {
        double h = Math.sqrt(2.0 / 3.0) * size;
        double r = size / Math.sqrt(3);
        vertices = new Vector3[]{
                new Vector3(0, h / 2, 0),
                new Vector3(-r, -h / 2, -r),
                new Vector3(r, -h / 2, -r),
                new Vector3(0, -h / 2, r)
        };
    }

    @Override
    public void draw(Graphics2D g, double fov, int screenWidth, int screenHeight,
                     Vector3 cameraPosition, Vector3 cameraForward, Vector3 cameraUp, Vector3 cameraRight) {
        int[][] faces = {
                {0, 1, 2},
                {0, 2, 3},
                {0, 3, 1},
                {1, 2, 3}
        };

        g.setColor(color);

        for (int[] face : faces) {
            Vector3[] faceVertices = {vertices[face[0]], vertices[face[1]], vertices[face[2]]};
            drawFace(g, faceVertices, fov, screenWidth, screenHeight, cameraPosition, cameraForward, cameraUp, cameraRight);
        }
    }

    private void drawFace(Graphics2D g, Vector3[] faceVertices, double fov, int screenWidth, int screenHeight,
                          Vector3 cameraPosition, Vector3 cameraForward, Vector3 cameraUp, Vector3 cameraRight) {
        int[] xPoints = new int[faceVertices.length];
        int[] yPoints = new int[faceVertices.length];

        for (int i = 0; i < faceVertices.length; i++) {
            Vector3 vertex = faceVertices[i]
                    .add(position)
                    .transform(cameraPosition, cameraForward, cameraUp, cameraRight);

            if (vertex.z <= 0) return;

            double scale = fov / vertex.z;
            xPoints[i] = (int) ((vertex.x * scale) + screenWidth / 2);
            yPoints[i] = (int) ((-vertex.y * scale) + screenHeight / 2);
        }

        Path2D path = new Path2D.Double();
        path.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < xPoints.length; i++) {
            path.lineTo(xPoints[i], yPoints[i]);
        }
        path.closePath();
        g.fill(path);
        g.draw(path);
    }

    @Override
    public List<Face> getFaces(Vector3[] transformedVertices) {
        int[][] faceIndices = {
                {0, 1, 2},
                {0, 2, 3},
                {0, 3, 1},
                {1, 2, 3}
        };

        List<Face> faces = new ArrayList<>();
        for (int[] indices : faceIndices) {
            double averageZ = 0;
            for (int index : indices) {
                averageZ += transformedVertices[index].z;
            }
            averageZ /= indices.length;

            faces.add(new Face(indices, averageZ, color, transformedVertices));
        }

        return faces;
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
    public void rotate(double angleX, double angleY, double angleZ) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].rotateX(angleX).rotateY(angleY).rotateZ(angleZ);
        }
    }
}

