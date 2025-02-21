package a2;

import javax.swing.JFrame;

import static com.jogamp.opengl.GL4.*;

import java.nio.FloatBuffer;
import java.lang.Math;
import java.awt.event.*;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import org.joml.*;

public class Code extends JFrame implements GLEventListener, KeyListener {
    // game initialization variables
    private GLCanvas myCanvas;
    private int renderingProgram;
    private int vao[] = new int[1];
    private int vbo[] = new int[2];
    private Camera cam;
    private float cameraX, cameraY, cameraZ;
    private float cubeLocX, cubeLocY, cubeLocZ;
    private int brickTexture;



    // display function variables
    private FloatBuffer vals = Buffers.newDirectFloatBuffer(16);
    private Matrix4f pMat = new Matrix4f();
    private Matrix4f vMat = new Matrix4f();
    private Matrix4f mMat = new Matrix4f();
    private Matrix4f mvMat = new Matrix4f();
    // private Quaternionf rotationX = new Quaternionf().rotateX((float) Math.toRadians(90.0));
    // private Quaternionf rotationY = new Quaternionf().rotateY((float) Math.toRadians(75.0));
    private int mvLoc, pLoc;
    private float aspect;

    public Code() {
        setTitle("CSC155 - Assignment 2");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        myCanvas = new GLCanvas();
        myCanvas.addGLEventListener(this);
        myCanvas.addKeyListener(this);
        this.add(myCanvas);
        this.setVisible(true);
        Animator animtr = new Animator(myCanvas);
        animtr.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glClear(GL_COLOR_BUFFER_BIT);
        gl.glClear(GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(renderingProgram);

        mvLoc = gl.glGetUniformLocation(renderingProgram, "mv_matrix");
        pLoc = gl.glGetUniformLocation(renderingProgram, "p_matrix");


        vMat = cam.getViewMatrix();

        // draw cube
        mMat.translation(cubeLocX, cubeLocY, cubeLocZ);

        mvMat.identity().mul(vMat).mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, brickTexture);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 36);

        // draw other
        /*
        mMat.translation(cubeLocX + 2.5f, cubeLocY + 3.0f, cubeLocZ);
        mMat.rotate(rotationX);
        mMat.rotate(rotationY);

        mvMat.identity().mul(vMat).mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 42);
        */
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        renderingProgram = Utils.createShaderProgram("a2/vertShader.glsl", "a2/fragShader.glsl");

        aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
        pMat.setPerspective((float) Math.toRadians(60.0f), aspect, 0.1f, 1000.0f);

        setupVertices();
        cam = new Camera();
        cameraX = 0.0f; cameraY = 0.0f; cameraZ = 8.0f;
        cubeLocX = 0.0f; cubeLocY = -2.0f; cubeLocZ = 0.0f;

        cam.setLocation(new Vector3f(cameraX, cameraY, cameraZ));
        brickTexture = Utils.loadTexture("assets/textures/brick1.jpg");
    }

    private void setupVertices() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        Cube cube = new Cube();
        // CrayzeeCube crayzeeCube = new CrayzeeCube();

        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
        gl.glGenBuffers(vbo.length, vbo, 0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
        FloatBuffer objBuf = Buffers.newDirectFloatBuffer(cube.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        FloatBuffer texBuf = Buffers.newDirectFloatBuffer(cube.getTexCoords());
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);
        /*
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        objBuf = Buffers.newDirectFloatBuffer(crayzeeCube.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);
        */

    }

    public static void main(String[] args) { new Code(); }
    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {}
    @Override
    public void dispose(GLAutoDrawable arg0) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Q:
                cam.MoveUp(0.1f);
                break;
            case KeyEvent.VK_E:
                cam.MoveUp(-0.1f);
                break;
            case KeyEvent.VK_W:
                cam.MoveForward(0.1f);
                break;
            case KeyEvent.VK_S:
                cam.MoveForward(-0.1f);
                break;
            case KeyEvent.VK_D:
                cam.MoveRight(0.1f);
                break;
            case KeyEvent.VK_A:
                cam.MoveRight(-0.1f);
                break;
            case KeyEvent.VK_LEFT:
                cam.Yaw(0.1f);
                break;
            case KeyEvent.VK_RIGHT:
                cam.Yaw(-0.1f);
                break;
            case KeyEvent.VK_UP:
                cam.Pitch(-0.1f);
                break;
            case KeyEvent.VK_DOWN:
                cam.Pitch(0.1f);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent e) {}

}