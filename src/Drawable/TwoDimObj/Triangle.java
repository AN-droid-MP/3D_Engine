package Drawable.TwoDimObj;

import Drawable.Face;
import Drawable.Vector3;
import Drawable.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Triangle extends Shape {
    private Vector3[] vertices;

    public Triangle(Vector3 position, Color color, Vector3[] vertices, Vector3 rotation) {
        super(position, color, rotation);
        if (vertices.length != 3) {
            throw new IllegalArgumentException("Triangle must have exactly 3 vertices");
        }
        this.vertices = vertices;
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
        int[] indices = {0, 1, 2};
        double averageZ = (transformedVertices[0].z + transformedVertices[1].z + transformedVertices[2].z) / 3.0;

        faces.add(new Face(indices, averageZ, color, transformedVertices));
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


