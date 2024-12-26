package Drawable;

import java.awt.*;

public class Face {
    public int[] vertexIndices;
    public double averageZ;
    public Color color;
    public Vector3[] transformedVertices;

    public Face(int[] vertexIndices, double averageZ, Color color, Vector3[] transformedVertices) {
        this.vertexIndices = vertexIndices;
        this.averageZ = averageZ;
        this.color = color;
        this.transformedVertices = transformedVertices;
    }
}


