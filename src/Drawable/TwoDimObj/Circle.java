package Drawable.TwoDimObj;

import Drawable.Shape;
import Drawable.Vector3;

import java.awt.*;

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
    public double intersect(Vector3 origin, Vector3 direction) {
        // Плоскость круга: z = position.z
        double t = (position.z - origin.z) / direction.z;

        if (t < 0) return -1; // Пересечение сзади камеры

        // Точка пересечения на плоскости
        Vector3 point = origin.add(direction.multiply(t));

        // Проверка попадания в круг
        Vector3 relative = point.subtract(position);
        if (relative.x * relative.x + relative.y * relative.y <= radius * radius) {
            return t;
        }

        return -1;
    }

    @Override
    public void rotate(double angleX, double angleY, double angleZ) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].rotateX(angleX).rotateY(angleY).rotateZ(angleZ);
        }
    }
}