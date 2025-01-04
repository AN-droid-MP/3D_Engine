package Drawable.TwoDimObj;

import Drawable.Shape;
import Drawable.Vector3;

import java.awt.*;


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
    public double intersect(Vector3 origin, Vector3 direction) {
        Vector3 edge1 = vertices[1].subtract(vertices[0]);
        Vector3 edge2 = vertices[2].subtract(vertices[0]);
        Vector3 h = direction.cross(edge2);
        double a = edge1.dot(h);

        if (Math.abs(a) < 1e-6) return -1;

        double f = 1.0 / a;
        Vector3 s = origin.subtract(vertices[0]);
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


