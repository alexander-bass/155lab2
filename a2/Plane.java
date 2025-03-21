package a2;

// Primitive shape with six vertices, used as ground and wall plane

public class Plane {
    float[] vertices = {
        -1.0f, 0.0f, 1.0f,
		1.0f, 0.0f, 1.0f,
		-1.0f, 0.0f, -1.0f,
		-1.0f, 0.0f, -1.0f,
		1.0f, 0.0f, 1.0f,
		1.0f, 0.0f, -1.0f
    };

    float[] texCoords = {
        0.0f, 0.0f,  1.0f, 0.0f,  0.0f, 1.0f,
		0.0f, 1.0f,  1.0f, 0.0f,  1.0f, 1.0f
    };

    public Plane(){}
    public float[] getVertices() { return vertices; }
    public float[] getTexCoords() { return texCoords; }
}
