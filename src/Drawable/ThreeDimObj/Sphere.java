package Drawable.ThreeDimObj;

import Drawable.Face;
import Drawable.Vector3;
import Drawable.Shape;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class Sphere extends Shape {
    private int latitudeBands;
    private int longitudeBands;
    private double radius;
    private Vector3[] vertices;

    public Sphere(Vector3 position, Color color, double radius, int latitudeBands, int longitudeBands, Vector3 rotation) {
        super(position, color, rotation);
        this.radius = radius;
        this.latitudeBands = latitudeBands;
        this.longitudeBands = longitudeBands;
        initializeVertices();
    }

    private void initializeVertices() {
        int totalVertices = (latitudeBands + 1) * (longitudeBands + 1);
        vertices = new Vector3[totalVertices];
        int index = 0;

        for (int lat = 0; lat <= latitudeBands; lat++) {
            double theta = Math.PI * lat / latitudeBands;
            double sinTheta = Math.sin(theta);
            double cosTheta = Math.cos(theta);

            for (int lon = 0; lon <= longitudeBands; lon++) {
                double phi = 2 * Math.PI * lon / longitudeBands;
                double sinPhi = Math.sin(phi);
                double cosPhi = Math.cos(phi);

                double x = radius * sinTheta * cosPhi;
                double y = radius * sinTheta * sinPhi;
                double z = radius * cosTheta;

                vertices[index++] = new Vector3(x, y, z);
            }
        }
        rotate(rotation.x, rotation.y, rotation.z);
    }

    @Override
    public void draw(Graphics2D g, double fov, int screenWidth, int screenHeight,
                     Vector3 cameraPosition, Vector3 cameraForward, Vector3 cameraUp, Vector3 cameraRight) {
        int pointsPerLongitude = longitudeBands + 1;
        g.setColor(color);

        for (int lat = 0; lat < latitudeBands; lat++) {
            for (int lon = 0; lon < longitudeBands; lon++) {
                int idx0 = lat * pointsPerLongitude + lon;
                int idx1 = idx0 + 1;
                int idx2 = idx0 + pointsPerLongitude;
                int idx3 = idx2 + 1;

                drawFace(g, new Vector3[]{vertices[idx0], vertices[idx1], vertices[idx3], vertices[idx2]},
                        fov, screenWidth, screenHeight, cameraPosition, cameraForward, cameraUp, cameraRight);
            }
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
        int pointsPerLongitude = longitudeBands + 1;

        List<Face> faces = new ArrayList<>();
        for (int lat = 0; lat < latitudeBands; lat++) {
            for (int lon = 0; lon < longitudeBands; lon++) {
                int idx0 = lat * pointsPerLongitude + lon;
                int idx1 = idx0 + 1;
                int idx2 = idx0 + pointsPerLongitude;
                int idx3 = idx2 + 1;

                double averageZ = (transformedVertices[idx0].z + transformedVertices[idx1].z +
                        transformedVertices[idx2].z + transformedVertices[idx3].z) / 4.0;

                faces.add(new Face(new int[]{idx0, idx1, idx3, idx2}, averageZ, color, transformedVertices));
            }
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
