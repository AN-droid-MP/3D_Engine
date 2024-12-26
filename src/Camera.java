import Drawable.Matrix4x4;
import Drawable.Vector3;

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
        position = position.add(
                getForward().multiply(delta.z)
                        .add(getRight().multiply(delta.x))
                        .add(getUp().multiply(delta.y))
        );
    }

    public void rotate(double pitch, double yaw, double roll) {
        // Матрицы вращения
        Matrix4x4 pitchMatrix = Matrix4x4.rotation(getRight(), pitch);   // Вверх/вниз
        Matrix4x4 yawMatrix = Matrix4x4.rotation(getUp(), yaw);         // Влево/вправо
        Matrix4x4 rollMatrix = Matrix4x4.rotation(getForward(), roll);  // Крен

        // Обновление ориентации камеры
        orientation = pitchMatrix.multiply(yawMatrix).multiply(rollMatrix).multiply(orientation);
    }

    public String getOrientation() {
        return String.format("Position: %s\nForward: %s\nUp: %s\nRight: %s",
                position, getForward(), getUp(), getRight());
    }
}