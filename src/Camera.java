import Drawable.Matrix4x4;
import Drawable.Vector3;

import java.util.List;

public class Camera {
    private Vector3 position;
    private Matrix4x4 orientation; // Матрица ориентации камеры
    private double fov;

    public Camera(Vector3 position, double fov) {
        this.position = position;
        this.fov = fov;
        this.orientation = Matrix4x4.identity(); // Начальная ориентация (единичная матрица)
    }

    public Vector3 getPosition() {
        return position;
    }

    public double getFov() {
        return fov;
    }

    public Vector3 getForward() {
        return orientation.transform(new Vector3(0, 0, 1)).normalize();
    }

    public Vector3 getUp() {
        return orientation.transform(new Vector3(0, 1, 0)).normalize();
    }

    public Vector3 getRight() {
        return orientation.transform(new Vector3(1, 0, 0)).normalize();
    }

    public void moveRelative(Vector3 delta) {
        Vector3 globalDelta = orientation.transform(delta);
        position = position.add(globalDelta);
    }

    public void rotate(double pitch, double yaw, double roll) {
        Matrix4x4 pitchMatrix = Matrix4x4.rotation(new Vector3(1, 0, 0), pitch);
        Matrix4x4 yawMatrix = Matrix4x4.rotation(new Vector3(0, 1, 0), yaw);
        Matrix4x4 rollMatrix = Matrix4x4.rotation(new Vector3(0, 0, 1), roll);

        orientation = yawMatrix.multiply(pitchMatrix).multiply(rollMatrix).multiply(orientation);
    }

    public String getOrientation() {
        return String.format("Position: %s\nForward: %s\nUp: %s\nRight: %s",
                position, getForward(), getUp(), getRight());
    }

    public void moveThroughPath(List<Vector3> path, double speed) {
        for (Vector3 target : path) {
            while (position.distanceTo(target) > 0.1) {
                Vector3 direction = target.subtract(position).normalize();
                position = position.add(direction.multiply(speed));
            }
        }
    }

}