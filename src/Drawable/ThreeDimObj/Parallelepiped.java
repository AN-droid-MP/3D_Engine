package Drawable.ThreeDimObj;

import Drawable.Face;
import Drawable.Vector3;
import Drawable.Shape;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class Parallelepiped extends Shape {
    public double width, height, depth;
    private Vector3[] corners;

    public Parallelepiped(Vector3 position, Color color, double width, double height, double depth, Vector3 rotation) {
        super(position, color, rotation);
        this.width = width;
        this.height = height;
        this.depth = depth;
        initializeCorners();
    }

    private void initializeCorners() {
        // Задаем вершины относительно центра
        corners = new Vector3[]{
                new Vector3(-width / 2, -height / 2, -depth / 2),
                new Vector3(width / 2, -height / 2, -depth / 2),
                new Vector3(width / 2, height / 2, -depth / 2),
                new Vector3(-width / 2, height / 2, -depth / 2),
                new Vector3(-width / 2, -height / 2, depth / 2),
                new Vector3(width / 2, -height / 2, depth / 2),
                new Vector3(width / 2, height / 2, depth / 2),
                new Vector3(-width / 2, height / 2, depth / 2)
        };
    }

    @Override
    public void draw(Graphics2D g, double fov, int screenWidth, int screenHeight,
                     Vector3 cameraPosition, Vector3 cameraForward, Vector3 cameraUp, Vector3 cameraRight) {
        Vector3[] transformedCorners = new Vector3[corners.length];
        for (int i = 0; i < corners.length; i++) {
            transformedCorners[i] = corners[i]
                    .add(position)
                    .transform(cameraPosition, cameraForward, cameraUp, cameraRight);
        }

        int[][] faces = {
                {0, 1, 2, 3}, // Front
                {4, 5, 6, 7}, // Back
                {0, 1, 5, 4}, // Bottom
                {2, 3, 7, 6}, // Top
                {0, 3, 7, 4}, // Left
                {1, 2, 6, 5}  // Right
        };

        g.setColor(color);
        for (int[] face : faces) {
            drawFace(g, transformedCorners, face, fov, screenWidth, screenHeight);
        }
    }

    private void drawFace(Graphics2D g, Vector3[] corners, int[] face, double fov, int screenWidth, int screenHeight) {
        int[] xPoints = new int[face.length];
        int[] yPoints = new int[face.length];
        for (int i = 0; i < face.length; i++) {
            Vector3 vertex = corners[face[i]];
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
                {0, 1, 2, 3}, // Front
                {4, 5, 6, 7}, // Back
                {0, 1, 5, 4}, // Bottom
                {2, 3, 7, 6}, // Top
                {0, 3, 7, 4}, // Left
                {1, 2, 6, 5}  // Right
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
        Vector3[] transformedCorners = new Vector3[corners.length];
        for (int i = 0; i < corners.length; i++) {
            transformedCorners[i] = corners[i]
                    .add(position)
                    .transform(cameraPosition, cameraForward, cameraUp, cameraRight);
        }
        return transformedCorners;
    }

    @Override
    public void rotate(double angleX, double angleY, double angleZ) {
        for (int i = 0; i < corners.length; i++) {
            corners[i] = corners[i].rotateX(angleX).rotateY(angleY).rotateZ(angleZ);
        }
    }
}
