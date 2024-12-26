import Drawable.Shape;
import Drawable.Vector3;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class Scene {
    private List<Shape> shapes;
    private Camera camera;

    public Scene(Camera camera) {
        this.shapes = new ArrayList<>();
        this.camera = camera;
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void handleCameraInput(int keyCode) {
        Vector3 delta = new Vector3(0, 0, 0);
        double moveSpeed = 10;
        double rotateSpeed = 5;

        switch (keyCode) {
            case KeyEvent.VK_W -> delta = new Vector3(0, 0, moveSpeed);  // Вперёд
            case KeyEvent.VK_S -> delta = new Vector3(0, 0, -moveSpeed); // Назад
            case KeyEvent.VK_A -> delta = new Vector3(-moveSpeed, 0, 0); // Влево
            case KeyEvent.VK_D -> delta = new Vector3(moveSpeed, 0, 0);  // Вправо
            case KeyEvent.VK_SPACE -> delta = new Vector3(0, moveSpeed, 0); // Вверх
            case KeyEvent.VK_SHIFT -> delta = new Vector3(0, -moveSpeed, 0); // Вниз

            case KeyEvent.VK_UP -> camera.rotate(-rotateSpeed, 0, 0);   // Вверх
            case KeyEvent.VK_DOWN -> camera.rotate(rotateSpeed, 0, 0); // Вниз
            case KeyEvent.VK_LEFT -> camera.rotate(0, -rotateSpeed, 0); // Влево
            case KeyEvent.VK_RIGHT -> camera.rotate(0, rotateSpeed, 0); // Вправо
            case KeyEvent.VK_Q -> camera.rotate(0, 0, -rotateSpeed);   // Крен влево
            case KeyEvent.VK_E -> camera.rotate(0, 0, rotateSpeed);    // Крен вправо
        }

        camera.moveRelative(delta);
    }
}
