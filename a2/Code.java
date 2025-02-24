package a2;

import javax.swing.JFrame;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TEXTURE0;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TRIANGLES;

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

public class Code extends JFrame implements GLEventListener {
    // game initialization variables
    private GLCanvas myCanvas;
    private int renderingProgram;
    private int vao[] = new int[1];
    private int vbo[] = new int[9];
    private Camera cam;
    private float cameraX, cameraY, cameraZ;
    private Vector3f cubeInitialLocation = new Vector3f(0, 0, 0f);
    private Vector3f modelInitialLocation = new Vector3f(0f, 0, 0.5f);
    private float pyrLocX, pyrLocY, pyrLocZ;
    private float pyr2LocX, pyr2LocY, pyr2LocZ;
    private int brickTexture, iceTexture;
    private InputManager inputManager;

    private int shuttleTexture;
    private int numObjVertices;
    private ImportedModel myModel;


    // display function variables
    private FloatBuffer vals = Buffers.newDirectFloatBuffer(16);
    private Matrix4f pMat = new Matrix4f();
    private Matrix4f vMat = new Matrix4f();
    private Matrix4f mMat = new Matrix4f();
    private Matrix4f mvMat = new Matrix4f();
    private int mvLoc, pLoc;
    private float aspect;
    private long prevDisplayTime, currDisplayTime;
    private float deltaTime;
    private float angle = 0.0f;
    private Quaternionf rotateX = new Quaternionf().rotateX((float) Math.toRadians(90.0f));
    private Quaternionf rotateX2 = new Quaternionf().rotateX((float) Math.toRadians(-90.0f));
    private Quaternionf rotateY = new Quaternionf();
    private Quaternionf rotateCube = new Quaternionf().rotateAxis((float) Math.toRadians(90.0f), 0, 1, 1);
    private Quaternionf cubeOrbit = new Quaternionf();


    public Code() {
        // setup window
        setTitle("CSC155 - Assignment 2");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // setup GLCanvas
        myCanvas = new GLCanvas();
        myCanvas.addGLEventListener(this);
        inputManager = new InputManager(this);
        myCanvas.addKeyListener(inputManager);

        this.add(myCanvas);
        this.setVisible(true);

        prevDisplayTime = System.nanoTime();
        Animator animtr = new Animator(myCanvas);
        animtr.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        currDisplayTime = System.nanoTime();
        deltaTime = (currDisplayTime - prevDisplayTime) / 1e9f;
        prevDisplayTime = currDisplayTime;
        
        handleInput(deltaTime);
        
        renderScene();
    }

    public void renderScene() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glClear(GL_COLOR_BUFFER_BIT);
        gl.glClearColor(0, 0, .2f, 1);
        gl.glClear(GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(renderingProgram);

        mvLoc = gl.glGetUniformLocation(renderingProgram, "mv_matrix");
        pLoc = gl.glGetUniformLocation(renderingProgram, "p_matrix");

        vMat = cam.getViewMatrix();

        angle += (2.0f * deltaTime);

        // draw cube
        mMat.translation(cubeInitialLocation);
        mMat.rotate(rotateCube);
        mMat.scale(0.15f);

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

        // draw manual object 1
        mMat.translation(pyrLocX, pyrLocY, pyrLocZ);

        rotateY.rotationY((float)Math.sin(angle));
        mMat.rotate(rotateY);

        mMat.rotate(rotateX);

        mvMat.identity().mul(vMat).mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, iceTexture);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 42);

        // draw manual object 2
        mMat.translation(pyr2LocX, pyr2LocY, pyr2LocZ);

        rotateY.rotationY((float)Math.cos(-angle));
        mMat.rotate(rotateY);

        mMat.rotate(rotateX2);
        
        mvMat.identity().mul(vMat).mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[5]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, iceTexture);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 42);

        // draw obj model
        mMat.identity();
		mMat.translate(modelInitialLocation);
        cubeOrbit.rotationY(1.5f * deltaTime);
        Vector3f modelNewLocation = modelInitialLocation.rotate(cubeOrbit);
        mMat.translate(modelNewLocation);
        rotateY.rotationTo(new Vector3f(1, 0, 0), modelNewLocation.normalize());
        mMat.rotate(rotateY);

		mvMat.identity();
		mvMat.mul(vMat);
		mvMat.mul(mMat);

		gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
		gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(1);

		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, shuttleTexture);

		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glDrawArrays(GL_TRIANGLES, 0, myModel.getNumVertices());


    }

    @Override
    public void init(GLAutoDrawable drawable) {
        myModel = new ImportedModel("assets/models/shuttle.obj");
        renderingProgram = Utils.createShaderProgram("a2/vertShader.glsl", "a2/fragShader.glsl");

        aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
        pMat.setPerspective((float) Math.toRadians(60.0f), aspect, 0.1f, 1000.0f);

        setupVertices();
        cam = new Camera();
        cameraX = 0.0f; cameraY = 0.0f; cameraZ = 6.0f;
        pyrLocX = 0.0f; pyrLocY = 0.25f; pyrLocZ = 0.0f;
        pyr2LocX = 0.0f; pyr2LocY = -0.25f; pyr2LocZ = 0.0f;

        cam.setLocation(new Vector3f(cameraX, cameraY, cameraZ));
        brickTexture = Utils.loadTexture("assets/textures/brick1.jpg");
        iceTexture = Utils.loadTexture("assets/textures/ice.jpg");
        shuttleTexture = Utils.loadTexture("assets/textures/spstob_1.jpg");
    }

    private void setupVertices() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        Cube cube = new Cube();
        CrayzeeCube pyr = new CrayzeeCube();
        CrayzeeCube pyr2 = new CrayzeeCube();

        // obj model setup stuff
        numObjVertices = myModel.getNumVertices();
        Vector3f[] vertices = myModel.getVertices();
        Vector2f[] texCoords = myModel.getTexCoords();
        Vector3f[] normals = myModel.getNormals();

        float[] pvalues = new float[numObjVertices*3];
        float[] tvalues = new float[numObjVertices*2];
        float[] nvalues = new float[numObjVertices*3];

        for (int i = 0; i < numObjVertices; i++) {
            pvalues[i*3] = (float) (vertices[i]).x();
            pvalues[i*3+1] = (float) (vertices[i]).y();
            pvalues[i*3+2] = (float) (vertices[i]).z();
            tvalues[i*2] = (float) (texCoords[i]).x();
            tvalues[i*2+1] = (float) (texCoords[i]).y();
            nvalues[i*3] = (float) (normals[i]).x();
            nvalues[i*3+1] = (float) (normals[i]).y();
            nvalues[i*3+2] = (float) (normals[i]).z();
        }
        // end of obj model setup stuff


        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
        gl.glGenBuffers(vbo.length, vbo, 0);

        // setup cube vert & tex
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
        FloatBuffer objBuf = Buffers.newDirectFloatBuffer(cube.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        FloatBuffer texBuf = Buffers.newDirectFloatBuffer(cube.getTexCoords());
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        // setup manual obj 1 vert & tex
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
        objBuf = Buffers.newDirectFloatBuffer(pyr.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
        texBuf = Buffers.newDirectFloatBuffer(pyr.getTexCoords());
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        // setup manual obj 2 vert & tex
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
        objBuf = Buffers.newDirectFloatBuffer(pyr2.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[5]);
        texBuf = Buffers.newDirectFloatBuffer(pyr2.getTexCoords());
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);
    
        // setup obj model vert tex & norms
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
        objBuf = Buffers.newDirectFloatBuffer(pvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
        texBuf = Buffers.newDirectFloatBuffer(tvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[8]);
        FloatBuffer normBuf = Buffers.newDirectFloatBuffer(nvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, normBuf.limit() * 4, normBuf, GL_STATIC_DRAW);
    }


    private void handleInput(float time) {
        float yaw = 0, pitch = 0;
        if (inputManager.isKeyPressed(KeyEvent.VK_LEFT)) yaw += 1;
        if (inputManager.isKeyPressed(KeyEvent.VK_RIGHT)) yaw -= 1;
        if (inputManager.isKeyPressed(KeyEvent.VK_UP)) pitch += 1;
        if (inputManager.isKeyPressed(KeyEvent.VK_DOWN)) pitch -= 1;
        cam.rotate(yaw, pitch, time);

        float forward = 0, right = 0, up = 0;
        if (inputManager.isKeyPressed(KeyEvent.VK_W)) forward += 1;
        if (inputManager.isKeyPressed(KeyEvent.VK_S)) forward -= 1;
        if (inputManager.isKeyPressed(KeyEvent.VK_D)) right += 1;
        if (inputManager.isKeyPressed(KeyEvent.VK_A)) right -= 1;
        if (inputManager.isKeyPressed(KeyEvent.VK_Q)) up += 1;
        if (inputManager.isKeyPressed(KeyEvent.VK_E)) up -= 1;
        cam.move(forward, right, up, time);
    }

    public static void main(String[] args) { new Code(); }
    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
        aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
        pMat.setPerspective((float) Math.toRadians(60.0f), aspect, 0.1f, 1000.0f);
    }
    @Override
    public void dispose(GLAutoDrawable arg0) {}


}