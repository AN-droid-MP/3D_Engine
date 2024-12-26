package Drawable;


public class Matrix4x4 {
    private double[][] values;

    public Matrix4x4(double[][] values) {
        this.values = values;
    }

    public static Matrix4x4 identity() {
        return new Matrix4x4(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    public static Matrix4x4 rotation(Vector3 axis, double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double x = axis.x, y = axis.y, z = axis.z;

        return new Matrix4x4(new double[][]{
                {cos + x * x * (1 - cos), x * y * (1 - cos) - z * sin, x * z * (1 - cos) + y * sin, 0},
                {y * x * (1 - cos) + z * sin, cos + y * y * (1 - cos), y * z * (1 - cos) - x * sin, 0},
                {z * x * (1 - cos) - y * sin, z * y * (1 - cos) + x * sin, cos + z * z * (1 - cos), 0},
                {0, 0, 0, 1}
        });
    }

    public Matrix4x4 multiply(Matrix4x4 other) {
        double[][] result = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    result[i][j] += this.values[i][k] * other.values[k][j];
                }
            }
        }
        return new Matrix4x4(result);
    }

    public Vector3 transform(Vector3 v) {
        double x = v.x * values[0][0] + v.y * values[0][1] + v.z * values[0][2] + values[0][3];
        double y = v.x * values[1][0] + v.y * values[1][1] + v.z * values[1][2] + values[1][3];
        double z = v.x * values[2][0] + v.y * values[2][1] + v.z * values[2][2] + values[2][3];
        return new Vector3(x, y, z);
    }
}