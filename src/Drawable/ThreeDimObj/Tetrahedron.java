package Drawable.ThreeDimObj;

import Drawable.Shape;
import Drawable.Vector3;

import java.awt.*;

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
        rotate(rotation.x, rotation.y, rotation.z);
    }

    @Override
    public double intersect(Vector3 origin, Vector3 direction) {
        double closestT = Double.MAX_VALUE;

        int[][] faces = {
                {0, 1, 2},
                {0, 1, 3},
                {1, 2, 3},
                {0, 2, 3}
        };

        for (int[] face : faces) {
            Vector3[] triangle = {
                    vertices[face[0]],
                    vertices[face[1]],
                    vertices[face[2]]
            };

            double t = intersectTriangle(origin, direction, triangle);
            if (t > 0 && t < closestT) {
                closestT = t;
            }
        }

        return closestT == Double.MAX_VALUE ? -1 : closestT;
    }

    private double intersectTriangle(Vector3 origin, Vector3 direction, Vector3[] triangle) {
        Vector3 edge1 = triangle[1].subtract(triangle[0]);
        Vector3 edge2 = triangle[2].subtract(triangle[0]);
        Vector3 h = direction.cross(edge2);
        double a = edge1.dot(h);

        if (Math.abs(a) < 1e-6) return -1;

        double f = 1.0 / a;
        Vector3 s = origin.subtract(triangle[0]);
        double u = f * s.dot(h);

        if (u < 0.0 || u > 1.0) return -1;

        Vector3 q = s.cross(edge1);
        double v = f * direction.dot(q);

        if (v < 0.0 || u + v > 1.0) return -1;

        double t = f * edge2.dot(q);

        return (t > 1e-6) ? t : -1;
    }


    @Override
    public void rotate(double angleX, double angleY, double angleZ) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].rotateX(angleX).rotateY(angleY).rotateZ(angleZ);
        }
    }
}

