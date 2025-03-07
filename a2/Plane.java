package a2;

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
