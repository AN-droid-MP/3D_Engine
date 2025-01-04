package Drawable.TwoDimObj;

import Drawable.Shape;
import Drawable.Vector3;

import java.awt.*;

public class Rectangle extends Shape {
    private double width, height;
    private Vector3[] vertices;

    public Rectangle(Vector3 position, Color color, double width, double height, Vector3 rotation) {
        super(position, color, rotation);
        this.width = width;
        this.height = height;
        initializeVertices();
    }

    private void initializeVertices() {
        vertices = new Vector3[]{
                new Vector3(-width / 2, -height / 2, 0),
                new Vector3(width / 2, -height / 2, 0),
                new Vector3(width / 2, height / 2, 0),
                new Vector3(-width / 2, height / 2, 0)
        };
        rotate(rotation.x, rotation.y, rotation.z);
    }

    @Override
    public double intersect(Vector3 origin, Vector3 direction) {
        // Нормаль плоскости прямоугольника
        Vector3 normal = new Vector3(0, 0, 1).rotateX(rotation.x).rotateY(rotation.y).rotateZ(rotation.z);

        // Скалярное произведение для проверки угла между лучом и нормалью
        double denom = normal.dot(direction);

        if (Math.abs(denom) < 1e-6) {
            return -1; // Луч параллелен плоскости
        }

        // Пересечение с плоскостью
        double t = position.subtract(origin).dot(normal) / denom;

        if (t < 0) {
            return -1; // Пересечение сзади камеры
        }

        // Точка пересечения
        Vector3 point = origin.add(direction.multiply(t));

        // Проверка, находится ли точка внутри прямоугольника
        Vector3 localPoint = point.subtract(position);
        double halfWidth = width / 2;
        double halfHeight = height / 2;

        if (Math.abs(localPoint.x) <= halfWidth && Math.abs(localPoint.y) <= halfHeight) {
            return t;
        }

        return -1; // Точка за пределами прямоугольника
    }


    @Override
    public void rotate(double angleX, double angleY, double angleZ) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].rotateX(angleX).rotateY(angleY).rotateZ(angleZ);
        }
    }
}