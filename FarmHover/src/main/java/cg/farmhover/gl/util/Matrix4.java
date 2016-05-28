package cg.farmhover.gl.util;

import cg.farmhover.objects.SceneObject;
import java.util.Arrays;
import java.util.Stack;
import javax.media.opengl.GL3;

public class Matrix4 {

    private Stack<float[]> stack;
    private GL3 gl;
   // private int handle;
    private float[] matrix;

    public Stack<float[]> getStack() {
        return stack;
    }

  //====================== funções personalizadas ==============================
    public static void print(float[] matrix) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.printf("%.2f\t\t",matrix[i + j * 4]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public float[] getMatrix() {
        return matrix;
    }

    public void setMatrix(float[] matrix) {
        this.matrix = Arrays.copyOf(matrix, 16);
    }

    public void scale(SceneObject object, float sx, float sy, float sz) {
        this.scale(sx, sy, sz);
        float[] inverse = Arrays.copyOf(object.getInverseModelMatrix().scale(1 / sx, 1 / sy, 1 / sz), 16);
        object.getInverseModelMatrix().getStack().push(inverse);
    }

    public void translate(SceneObject object, float tx, float ty, float tz) {
        this.translate(tx, ty, tz);
        float[] inverse = Arrays.copyOf(object.getInverseModelMatrix().translate(-tx, -ty, -tz), 16);
        object.getInverseModelMatrix().getStack().push(inverse);
    }

    public void rotate(SceneObject object, float theta, float x, float y, float z) {
        this.rotate(theta, x, y, z);
        float[] inverse = Arrays.copyOf(object.getInverseModelMatrix().rotate(-theta, x, y, z), 16);
        object.getInverseModelMatrix().getStack().push(inverse);
    }

    // ============================================================================

    public Matrix4() {
        matrix = new float[16];
        stack = new Stack<float[]>();
        loadIdentity();
    }

    public void init(final GL3 gl) {
        this.gl = gl;
        //this.handle = handle;
    }

    public void bind(int handle) {
        gl.glUniformMatrix4fv(handle, 1, false, this.matrix, 0);
    }

    public void print() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.printf("%.2f\t\t",matrix[i + j * 4]); 
            }
            System.out.println();
        }
        System.out.println();
    }

    public final void loadIdentity() {
        this.matrix[0] = 1.0f;
        this.matrix[1] = 0.0f;
        this.matrix[2] = 0.0f;
        this.matrix[3] = 0.0f;
        this.matrix[4] = 0.0f;
        this.matrix[5] = 1.0f;
        this.matrix[6] = 0.0f;
        this.matrix[7] = 0.0f;
        this.matrix[8] = 0.0f;
        this.matrix[9] = 0.0f;
        this.matrix[10] = 1.0f;
        this.matrix[11] = 0.0f;
        this.matrix[12] = 0.0f;
        this.matrix[13] = 0.0f;
        this.matrix[14] = 0.0f;
        this.matrix[15] = 1.0f;
    }

    public float[] translate(float tx, float ty, float tz) {
        float[] translate = new float[16];
        translate[0] = 1.0f;
        translate[5] = 1.0f;
        translate[10] = 1.0f;
        translate[15] = 1.0f;
        translate[12] = tx;
        translate[13] = ty;
        translate[14] = tz;
        multiply(translate);
        return translate;
    }

    public float[] scale(float sx, float sy, float sz) {
        float[] scale = new float[16];
        scale[0] = sx;
        scale[5] = sy;
        scale[10] = sz;
        scale[15] = 1.0f;
        multiply(scale);
        return scale;
    }

    public float[] rotate(float theta, float x, float y, float z) {
        float len = (float) Math.sqrt(x * x + y * y + z * z);

        if (len != 1) {
            len = 1 / len;
            x *= len;
            y *= len;
            z *= len;
        }

        float s = (float) Math.sin(Math.toRadians(theta));
        float c = (float) Math.cos(Math.toRadians(theta));
        float t = 1 - c;

        float a00, a01, a02, a03,
                a10, a11, a12, a13,
                a20, a21, a22, a23,
                b00, b01, b02,
                b10, b11, b12,
                b20, b21, b22;

        a00 = this.matrix[0];
        a01 = this.matrix[1];
        a02 = this.matrix[2];
        a03 = this.matrix[3];
        a10 = this.matrix[4];
        a11 = this.matrix[5];
        a12 = this.matrix[6];
        a13 = this.matrix[7];
        a20 = this.matrix[8];
        a21 = this.matrix[9];
        a22 = this.matrix[10];
        a23 = this.matrix[11];

        // Construct the elements of the rotation matrix
        b00 = x * x * t + c;
        b01 = y * x * t + z * s;
        b02 = z * x * t - y * s;
        b10 = x * y * t - z * s;
        b11 = y * y * t + c;
        b12 = z * y * t + x * s;
        b20 = x * z * t + y * s;
        b21 = y * z * t - x * s;
        b22 = z * z * t + c;

        // Perform rotation-specific matrix multiplication
        this.matrix[0] = a00 * b00 + a10 * b01 + a20 * b02;
        this.matrix[1] = a01 * b00 + a11 * b01 + a21 * b02;
        this.matrix[2] = a02 * b00 + a12 * b01 + a22 * b02;
        this.matrix[3] = a03 * b00 + a13 * b01 + a23 * b02;

        this.matrix[4] = a00 * b10 + a10 * b11 + a20 * b12;
        this.matrix[5] = a01 * b10 + a11 * b11 + a21 * b12;
        this.matrix[6] = a02 * b10 + a12 * b11 + a22 * b12;
        this.matrix[7] = a03 * b10 + a13 * b11 + a23 * b12;

        this.matrix[8] = a00 * b20 + a10 * b21 + a20 * b22;
        this.matrix[9] = a01 * b20 + a11 * b21 + a21 * b22;
        this.matrix[10] = a02 * b20 + a12 * b21 + a22 * b22;
        this.matrix[11] = a03 * b20 + a13 * b21 + a23 * b22;

        //================== Personalizado =====================
        float[] rotation = new float[16];
        rotation[0] = b00;
        rotation[1] = b01;
        rotation[2] = b02;
        rotation[4] = b10;
        rotation[5] = b11;
        rotation[6] = b12;
        rotation[8] = b20;
        rotation[9] = b21;
        rotation[10] = b22;
        rotation[15] = 1.0f;
        return rotation;
        //======================================================
    }

//  public void push() {
//    stack.push(this.matrix.clone());
//  }
//  public void pop() {
//    if (stack.size() == 0) {
//      System.err.println("Matrix stack is empty.");
//    } else {
//      this.matrix = stack.pop();
//    }
//  }
    public void ortho2D(float xwmin, float xwmax, float ywmin, float ywmax) {
        float[] ortho = new float[16];
        ortho[0] = 2.0f / (xwmax - xwmin);
        ortho[5] = 2.0f / (ywmax - ywmin);
        ortho[10] = 1.0f;
        ortho[12] = -(xwmax + xwmin) / (xwmax - xwmin);
        ortho[13] = -(ywmax + ywmin) / (ywmax - ywmin);
        ortho[15] = 1.0f;
        multiply(ortho);
    }

    public void ortho(float xwmin, float xwmax, float ywmin, float ywmax, float dnear, float dfar) {
        float[] ortho = new float[16];
        ortho[0] = 2.0f / (xwmax - xwmin);
        ortho[5] = 2.0f / (ywmax - ywmin);
        ortho[10] = -2.0f / (dfar - dnear);
        ortho[12] = -(xwmax + xwmin) / (xwmax - xwmin);
        ortho[13] = -(ywmax + ywmin) / (ywmax - ywmin);
        ortho[14] = (dnear + dfar) / (dnear - dfar);
        ortho[15] = 1.0f;
        multiply(ortho);
    }

    public void lookAt(float x0, float y0, float z0,
            float xref, float yref, float zref,
            float Vx, float Vy, float Vz) {
        float[] n = new float[]{x0 - xref, y0 - yref, z0 - zref};
        normalize(n);

        float[] u = cross(new float[]{Vx, Vy, Vz}, n);
        normalize(u);

        float[] v = cross(n, u);
        normalize(v);

        float[] Mwc_vc = new float[16];
        Mwc_vc[0] = u[0];
        Mwc_vc[4] = u[1];
        Mwc_vc[8] = u[2];

        Mwc_vc[1] = v[0];
        Mwc_vc[5] = v[1];
        Mwc_vc[9] = v[2];

        Mwc_vc[2] = n[0];
        Mwc_vc[6] = n[1];
        Mwc_vc[10] = n[2];

        Mwc_vc[12] = -(u[0] * x0 + u[1] * y0 + u[2] * z0);
        Mwc_vc[13] = -(v[0] * x0 + v[1] * y0 + v[2] * z0);
        Mwc_vc[14] = -(n[0] * x0 + n[1] * y0 + n[2] * z0);
        Mwc_vc[15] = 1.0f;
        multiply(Mwc_vc);
    }

    public void perspective(float theta, float aspect, float dnear, float dfar) {
        float[] persp = new float[16];
        persp[0] = (float) (1 / (aspect * Math.tan(Math.toRadians(theta / 2))));
        persp[5] = (float) (1 / Math.tan(Math.toRadians(theta / 2)));
        persp[10] = (dnear + dfar) / (dnear - dfar);
        persp[14] = -2 * ((dnear * dfar) / (dfar - dnear));
        persp[11] = -1.0f;
        multiply(persp);
    }

    private float[] cross(float[] vector1, float[] vector2) {
        float[] newv = new float[3];
        newv[0] = vector1[1] * vector2[2] - vector1[2] * vector2[1];
        newv[1] = vector1[2] * vector2[0] - vector1[0] * vector2[2];
        newv[2] = vector1[0] * vector2[1] - vector1[1] * vector2[0];
        return newv;
    }

    private void normalize(float[] vector) {
        float norm = (float) Math.sqrt(vector[0] * vector[0]
                + vector[1] * vector[1]
                + vector[2] * vector[2]);
        vector[0] /= norm;
        vector[1] /= norm;
        vector[2] /= norm;
    }

    public void multiply(float[] mat2) {
        // Cache the matrix values (makes for huge speed increases!)
        float a00 = this.matrix[0], a01 = this.matrix[1], a02 = this.matrix[2], a03 = this.matrix[3];
        float a10 = this.matrix[4], a11 = this.matrix[5], a12 = this.matrix[6], a13 = this.matrix[7];
        float a20 = this.matrix[8], a21 = this.matrix[9], a22 = this.matrix[10], a23 = this.matrix[11];
        float a30 = this.matrix[12], a31 = this.matrix[13], a32 = this.matrix[14], a33 = this.matrix[15];

        // Cache only the current line of the second matrix
        float b0 = mat2[0], b1 = mat2[1], b2 = mat2[2], b3 = mat2[3];
        this.matrix[0] = b0 * a00 + b1 * a10 + b2 * a20 + b3 * a30;
        this.matrix[1] = b0 * a01 + b1 * a11 + b2 * a21 + b3 * a31;
        this.matrix[2] = b0 * a02 + b1 * a12 + b2 * a22 + b3 * a32;
        this.matrix[3] = b0 * a03 + b1 * a13 + b2 * a23 + b3 * a33;

        b0 = mat2[4];
        b1 = mat2[5];
        b2 = mat2[6];
        b3 = mat2[7];
        this.matrix[4] = b0 * a00 + b1 * a10 + b2 * a20 + b3 * a30;
        this.matrix[5] = b0 * a01 + b1 * a11 + b2 * a21 + b3 * a31;
        this.matrix[6] = b0 * a02 + b1 * a12 + b2 * a22 + b3 * a32;
        this.matrix[7] = b0 * a03 + b1 * a13 + b2 * a23 + b3 * a33;

        b0 = mat2[8];
        b1 = mat2[9];
        b2 = mat2[10];
        b3 = mat2[11];
        this.matrix[8] = b0 * a00 + b1 * a10 + b2 * a20 + b3 * a30;
        this.matrix[9] = b0 * a01 + b1 * a11 + b2 * a21 + b3 * a31;
        this.matrix[10] = b0 * a02 + b1 * a12 + b2 * a22 + b3 * a32;
        this.matrix[11] = b0 * a03 + b1 * a13 + b2 * a23 + b3 * a33;

        b0 = mat2[12];
        b1 = mat2[13];
        b2 = mat2[14];
        b3 = mat2[15];
        this.matrix[12] = b0 * a00 + b1 * a10 + b2 * a20 + b3 * a30;
        this.matrix[13] = b0 * a01 + b1 * a11 + b2 * a21 + b3 * a31;
        this.matrix[14] = b0 * a02 + b1 * a12 + b2 * a22 + b3 * a32;
        this.matrix[15] = b0 * a03 + b1 * a13 + b2 * a23 + b3 * a33;
    }

}
