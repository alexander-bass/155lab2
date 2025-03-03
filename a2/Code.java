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
    private int vbo[] = new int[25];
    private Camera cam;
    private float cameraX, cameraY, cameraZ;
    private Vector3f croissantInitialLocation = new Vector3f(0, 0, 0);
    private Vector3f ratInitialLocation = new Vector3f(0, 0, 1.35f);
    private Vector3f frontInitial, backInitial, rightInitial, leftInitial, topInitial, bottomInitial;
    private InputManager inputManager;

    private int ratTexture, criossantTexture, garbageTexture;
    private int numObjVertices;
    private ImportedModel ratModel, croissantModel;


    // display function variables
    private FloatBuffer vals = Buffers.newDirectFloatBuffer(16);
    private Matrix4f pMat = new Matrix4f();
    private Matrix4f vMat = new Matrix4f();
    private Matrix4f mMat = new Matrix4f();
    private Matrix4f mvMat = new Matrix4f();
    private int mvLoc, pLoc;
    private float aspect;
    private long prevDisplayTime, currDisplayTime;
    private float posDelta, negDelta, deltaTime;
    private float angle = 0.0f;
    private Quaternionf rotateFaces = new Quaternionf();
    private Quaternionf rotateX = new Quaternionf();
    private Quaternionf rotateY = new Quaternionf();
    private Quaternionf rotateZ = new Quaternionf();
    private Quaternionf rotateCroissant = new Quaternionf().rotateAxis((float) Math.toRadians(90.0f), 0, 1, 1);
    private Quaternionf modelOrbit = new Quaternionf();


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
        gl.glClearColor(0, 0.2f, 0.2f, 1);
        gl.glClear(GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(renderingProgram);

        mvLoc = gl.glGetUniformLocation(renderingProgram, "mv_matrix");
        pLoc = gl.glGetUniformLocation(renderingProgram, "p_matrix");

        vMat = cam.getViewMatrix();

        angle += (1.0f * deltaTime);
        posDelta = ((float) Math.sin(angle) + 1) / 2.0f;
        negDelta = ((float) Math.sin(-angle) - 1) / 2.0f;

        // draw top
        rotateFaces.rotationX((float) Math.toRadians(-90.0f));
        mMat.identity();
        mMat.translation(topInitial);
        mMat.translate(0, posDelta, 0);
        rotateY.rotationY(posDelta*2);
        mMat.rotate(rotateY);
        
        mMat.rotate(rotateFaces);

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
        gl.glBindTexture(GL_TEXTURE_2D, garbageTexture);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 42);

        // draw bottom
        mMat.identity();
        rotateFaces.rotationX((float) Math.toRadians(90.0f));
        mMat.translation(bottomInitial);
        mMat.translate(0, negDelta, 0);

        rotateY.rotationY(negDelta*2);
        mMat.rotate(rotateY);

        mMat.rotate(rotateFaces);
        
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
        gl.glBindTexture(GL_TEXTURE_2D, garbageTexture);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 42);

        // draw front
        mMat.identity();
        mMat.translation(frontInitial);
        mMat.translate(0, 0, posDelta);
        
        rotateZ.rotationZ(posDelta*2);
        mMat.rotate(rotateZ);
        
        mvMat.identity().mul(vMat).mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, garbageTexture);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 42);

        // draw back
        rotateFaces.rotationY((float) Math.toRadians(180.0f));
        mMat.identity();
        mMat.translation(backInitial);
        mMat.translate(0, 0, negDelta);

        rotateZ.rotationZ(negDelta*2);
        mMat.rotate(rotateZ);

        mMat.rotate(rotateFaces);

        mvMat.identity().mul(vMat).mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[8]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[9]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, garbageTexture);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 42);

        // draw left
        rotateFaces.rotationY((float) Math.toRadians(90.0f));
        mMat.identity();
        mMat.translation(leftInitial);
        mMat.translate(posDelta, 0, 0);

        rotateX.rotationX(posDelta*2);
        mMat.rotate(rotateX);

        mMat.rotate(rotateFaces);
        
        mvMat.identity().mul(vMat).mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[10]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[11]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, garbageTexture);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 42);

        // draw right
        rotateFaces.rotationY((float) Math.toRadians(-90.0f));
        mMat.identity();
        mMat.translation(rightInitial);
        mMat.translate(negDelta, 0, 0);

        rotateX.rotationX(negDelta*2);
        mMat.rotate(rotateX);

        mMat.rotate(rotateFaces);
        
        mvMat.identity().mul(vMat).mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[12]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[13]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, garbageTexture);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        gl.glDrawArrays(GL_TRIANGLES, 0, 42);
        
        // draw rat model
        mMat.identity();
		mMat.translate(ratInitialLocation);

        modelOrbit.rotationY(1.5f * deltaTime);
        Vector3f modelNewLocation = ratInitialLocation.rotate(modelOrbit);
        mMat.translate(modelNewLocation);
        rotateY.rotationTo(new Vector3f(0, 0, -1), modelNewLocation);
        mMat.rotate(rotateY);
        mMat.scale(10.0f);

		mvMat.identity();
		mvMat.mul(vMat);
		mvMat.mul(mMat);

		gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
		gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[14]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[15]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(1);

		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, ratTexture);

		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glDrawArrays(GL_TRIANGLES, 0, ratModel.getNumVertices());

        // draw croissant model
        mMat.identity();
		mMat.translate(croissantInitialLocation);
        mMat.rotate(rotateCroissant);
        mMat.scale(2.5f);

		mvMat.identity();
		mvMat.mul(vMat);
		mvMat.mul(mMat);

		gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
		gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[17]);
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[18]);
		gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(1);

		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, criossantTexture);

		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glDrawArrays(GL_TRIANGLES, 0, croissantModel.getNumVertices());

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        ratModel = new ImportedModel("assets/models/street_rat_1k.obj");
        croissantModel = new ImportedModel("assets/models/croissant_1k.obj");
        renderingProgram = Utils.createShaderProgram("a2/vertShader.glsl", "a2/fragShader.glsl");

        aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
        pMat.setPerspective((float) Math.toRadians(60.0f), aspect, 0.1f, 1000.0f);

        setupVertices();
        cam = new Camera();
        cameraX = 0.0f; cameraY = 0.0f; cameraZ = 6.0f;

        frontInitial = new Vector3f(0, 0, 0); backInitial = new Vector3f(0, 0, 0);
        leftInitial = new Vector3f(0, 0, 0); rightInitial = new Vector3f(0, 0, 0);
        topInitial = new Vector3f(0, 0, 0); bottomInitial = new Vector3f(0, 0, 0);


        cam.setLocation(new Vector3f(cameraX, cameraY, cameraZ));
        
        garbageTexture = Utils.loadTexture("assets/textures/evgeny-karchevsky-k1tUxfs8JYY-unsplash.jpg");
        ratTexture = Utils.loadTexture("assets/textures/street_rat_diff_1k.jpg");
        criossantTexture = Utils.loadTexture("assets/textures/croissant_diff_1k.jpg");
    }

    private void setupVertices() {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        Cube cube = new Cube();
        ManualObject top = new ManualObject();
        ManualObject bottom = new ManualObject();
        ManualObject front = new ManualObject();

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
        
        // setup top vert & tex
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
        objBuf = Buffers.newDirectFloatBuffer(top.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
        texBuf = Buffers.newDirectFloatBuffer(top.getTexCoords());
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        // setup bottom vert & tex
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
        objBuf = Buffers.newDirectFloatBuffer(bottom.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[5]);
        texBuf = Buffers.newDirectFloatBuffer(bottom.getTexCoords());
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        // setup front vert & tex
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
        objBuf = Buffers.newDirectFloatBuffer(front.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
        texBuf = Buffers.newDirectFloatBuffer(front.getTexCoords());
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        // setup back vert & tex
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[8]);
        objBuf = Buffers.newDirectFloatBuffer(front.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[9]);
        texBuf = Buffers.newDirectFloatBuffer(front.getTexCoords());
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        // setup left vert & tex
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[10]);
        objBuf = Buffers.newDirectFloatBuffer(front.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[11]);
        texBuf = Buffers.newDirectFloatBuffer(front.getTexCoords());
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        // setup right vert & tex
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[12]);
        objBuf = Buffers.newDirectFloatBuffer(front.getVertices());
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[13]);
        texBuf = Buffers.newDirectFloatBuffer(front.getTexCoords());
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        // load in croissant obj model stuff
        numObjVertices = croissantModel.getNumVertices();
        Vector3f[] vertices = croissantModel.getVertices();
        Vector2f[] texCoords = croissantModel.getTexCoords();
        Vector3f[] normals = croissantModel.getNormals();

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
    
        // setup croissant model vert tex & norms
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[17]);
        objBuf = Buffers.newDirectFloatBuffer(pvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[18]);
        texBuf = Buffers.newDirectFloatBuffer(tvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[19]);
        FloatBuffer normBuf = Buffers.newDirectFloatBuffer(nvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, normBuf.limit() * 4, normBuf, GL_STATIC_DRAW);

        // load in rat obj model stuff
        numObjVertices = ratModel.getNumVertices();
        vertices = ratModel.getVertices();
        texCoords = ratModel.getTexCoords();
        normals = ratModel.getNormals();

        pvalues = new float[numObjVertices*3];
        tvalues = new float[numObjVertices*2];
        nvalues = new float[numObjVertices*3];

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


        // setup rat model vert tex & norms
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[14]);
        objBuf = Buffers.newDirectFloatBuffer(pvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, objBuf.limit() * 4, objBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[15]);
        texBuf = Buffers.newDirectFloatBuffer(tvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[16]);
        normBuf = Buffers.newDirectFloatBuffer(nvalues);
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