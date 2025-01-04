import Drawable.Matrix4x4;
import Drawable.Shape;
import Drawable.ThreeDimObj.Parallelepiped;
import Drawable.ThreeDimObj.Sphere;
import Drawable.ThreeDimObj.Tetrahedron;
import Drawable.TwoDimObj.Circle;
import Drawable.TwoDimObj.Rectangle;
import Drawable.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        Camera camera = new Camera(
                new Vector3(0, 0, 0),
                Matrix4x4.identity(),
                90
        );

        Scene scene = new Scene(camera);


        scene.addShape(new Parallelepiped(new Vector3(10, 15, 60), Color.CYAN, 15, 15, 15, new Vector3(0, 0, 0)));
        scene.addShape(new Sphere(new Vector3(-10, 10, 50), Color.MAGENTA, 10, 20, 20, new Vector3(0, 0, 0)));
        scene.addShape(new Circle(new Vector3(-100, 10, 50), Color.ORANGE, 10, 20, new Vector3(0, 0, 0)));
        scene.addShape(new Tetrahedron(new Vector3(50, 0, 100), Color.YELLOW, 20, new Vector3(0, 0, 0)));

        scene.addShape(new Rectangle(new Vector3(100, -30, 50), Color.BLUE, 20, 20, new Vector3(0, 0, 0)));

        Renderer renderer = new Renderer(scene);

        JFrame frame = new JFrame("3D Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(renderer);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                scene.handleCameraInput(e.getKeyCode());
                renderer.repaint();
            }
        });

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