package Drawable.ThreeDimObj;

import Drawable.Shape;
import Drawable.Vector3;

import java.awt.*;

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
    public double intersect(Vector3 origin, Vector3 direction) {
        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;

        Vector3 min = position.subtract(new Vector3(width / 2, height / 2, depth / 2));
        Vector3 max = position.add(new Vector3(width / 2, height / 2, depth / 2));

        for (int i = 0; i < 3; i++) {
            double invD = 1.0 / direction.getComponent(i);
            double t0 = (min.getComponent(i) - origin.getComponent(i)) * invD;
            double t1 = (max.getComponent(i) - origin.getComponent(i)) * invD;

            if (invD < 0) {
                double temp = t0;
                t0 = t1;
                t1 = temp;
            }

            tMin = Math.max(tMin, t0);
            tMax = Math.min(tMax, t1);

            if (tMax <= tMin) return -1;
        }

        return tMin;
    }

    @Override
    public void rotate(double angleX, double angleY, double angleZ) {
        for (int i = 0; i < corners.length; i++) {
            corners[i] = corners[i].rotateX(angleX).rotateY(angleY).rotateZ(angleZ);
        }
    }
}
