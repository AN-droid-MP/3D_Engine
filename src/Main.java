import Drawable.ThreeDimObj.Parallelepiped;
import Drawable.ThreeDimObj.Sphere;
import Drawable.ThreeDimObj.Tetrahedron;
import Drawable.TwoDimObj.Circle;
import Drawable.TwoDimObj.Triangle;
import Drawable.TwoDimObj.Rectangle;
import Drawable.Vector3;
import Drawable.Shape;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        Camera camera = new Camera(new Vector3(0, 0, 0), 500);

        Scene scene = new Scene(camera);

        Vector3[] triangleVertices = {
                new Vector3(0, 50, 0),
                new Vector3(-50, -50, 0),
                new Vector3(50, -50, 0)
        };


        scene.addShape(new Parallelepiped(new Vector3(10, 15, 60), Color.CYAN, 15, 15, 15, new Vector3(0, 0, 0)));
        scene.addShape(new Sphere(new Vector3(-10, 10, 50), Color.MAGENTA, 10, 20, 20, new Vector3(0, 0, 0)));
        scene.addShape(new Circle(new Vector3(-100, 10, 50), Color.ORANGE, 10, 20, new Vector3(0, 0, 0)));
        scene.addShape(new Triangle(new Vector3(100, 100, 50), Color.PINK, triangleVertices, new Vector3(0, 0, 0)));
        scene.addShape(new Tetrahedron(new Vector3(50, 100, 200), Color.YELLOW, 200, new Vector3(0, 0, 0)));

        scene.addShape(new Rectangle(new Vector3(0, 100, 50), Color.GREEN, 50, 50, new Vector3(90, 0, 0)));
        scene.addShape(new Rectangle(new Vector3(0, 0, 50), Color.LIGHT_GRAY, 50, 50, new Vector3(90, 0, 0)));
        scene.addShape(new Rectangle(new Vector3(0, 50, 75), Color.GRAY, 100, 50, new Vector3(0, 0, 90)));
        scene.addShape(new Rectangle(new Vector3(-25, 50, 50), Color.RED, 50, 100, new Vector3(0, 90, 0)));
        scene.addShape(new Rectangle(new Vector3(25, 50, 50), Color.BLUE, 50, 100, new Vector3(0, 90, 0)));

        Renderer renderer = new Renderer(scene);

        JFrame frame = new JFrame("3D Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(renderer);
        frame.setVisible(true);

        // Обработчик клавиш для управления камерой
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                scene.handleCameraInput(e.getKeyCode());
                renderer.repaint(); // Обновляем отрисовку
            }
        });

        // Анимация вращения объектов
        new Timer(16, e -> {
            for (Shape shape : scene.getShapes()) {
                if (!(shape instanceof Rectangle) ) {
                    shape.rotate(1, 1, 0);
                }
            }
            renderer.repaint();
        }).start();
    }
}