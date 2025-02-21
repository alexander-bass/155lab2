package a2;

public class Cube {
    private float[] vertices = {
        -1.0f,  1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, -1.0f, 1.0f,  1.0f, -1.0f, -1.0f,  1.0f, -1.0f,
        1.0f, -1.0f, -1.0f, 1.0f, -1.0f,  1.0f, 1.0f,  1.0f, -1.0f,
        1.0f, -1.0f,  1.0f, 1.0f,  1.0f,  1.0f, 1.0f,  1.0f, -1.0f,
        1.0f, -1.0f,  1.0f, -1.0f, -1.0f,  1.0f, 1.0f,  1.0f,  1.0f,
        -1.0f, -1.0f,  1.0f, -1.0f,  1.0f,  1.0f, 1.0f,  1.0f,  1.0f,
        -1.0f, -1.0f,  1.0f, -1.0f, -1.0f, -1.0f, -1.0f,  1.0f,  1.0f,
        -1.0f, -1.0f, -1.0f, -1.0f,  1.0f, -1.0f, -1.0f,  1.0f,  1.0f,
        -1.0f, -1.0f,  1.0f,  1.0f, -1.0f,  1.0f,  1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,  1.0f,
        -1.0f,  1.0f, -1.0f, 1.0f,  1.0f, -1.0f, 1.0f,  1.0f,  1.0f,
        1.0f,  1.0f,  1.0f, -1.0f,  1.0f,  1.0f, -1.0f,  1.0f, -1.0f
    };

    private float[] texCoords = {
        1.0f, 1.0f,  1.0f, 0.0f,  0.0f, 0.0f, // back face
        0.0f, 0.0f,  0.0f, 1.0f,  1.0f, 1.0f,
        1.0f, 0.0f,  0.0f, 0.0f,  1.0f, 1.0f, // right face
        0.0f, 0.0f,  0.0f, 1.0f,  1.0f, 1.0f,
        1.0f, 0.0f,  0.0f, 0.0f,  1.0f, 1.0f, // front face
        0.0f, 0.0f,  0.0f, 1.0f,  1.0f, 1.0f,
        1.0f, 0.0f,  0.0f, 0.0f,  1.0f, 1.0f, // left face
        0.0f, 0.0f,  0.0f, 1.0f,  1.0f, 1.0f,
        0.0f, 1.0f,  1.0f, 1.0f,  1.0f, 0.0f, // bottom face
        1.0f, 0.0f,  0.0f, 0.0f,  0.0f, 1.0f,
        0.0f, 1.0f,  1.0f, 1.0f,  1.0f, 0.0f, // top face
        1.0f, 0.0f,  0.0f, 0.0f,  0.0f, 1.0f
    };



    public Cube() {}
    public float[] getVertices() { return vertices; }
    public float[] getTexCoords() { return texCoords; }
}
