package a2;

// A hollow, flat-topped pyramid type object, I think the technical name is a frustum?

public class ManualObject {
    private float[] vertices = {
        -1.0f, -1.0f, 1.0f,     0.0f, -1.0f, 1.0f,      -.25f, -.25f, 0.25f,
        0.0f, -1.0f, 1.0f,      .25f, -.25f, 0.25f,     -.25f, -.25f, 0.25f,
        0.0f, -1.0f, 1.0f,      1.0f, -1.0f, 1.0f,      .25f, -.25f, 0.25f,
        1.0f, -1.0f, 1.0f,      1.0f, 0.0f, 1.0f,       .25f, -.25f, 0.25f,
        1.0f, 0.0f, 1.0f,       .25f, .25f, 0.25f,       .25f, -.25f, 0.25f,
        1.0f, 0.0f, 1.0f,       1.0f, 1.0f, 1.0f,       .25f, .25f, 0.25f,
        .25f, .25f, 0.25f,       1.0f, 1.0f, 1.0f,       0.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f,       -.25f, .25f, 0.25f,       .25f, .25f, 0.25f,
        -.25f, .25f, 0.25f,      0.0f, 1.0f, 1.0f,       -1.0f, 1.0f, 1.0f,
        -.25f, .25f, 0.25f,     -1.0f, 1.0f, 1.0f,       -1.0f, 0.0f, 1.0f,
        -1.0f, 0.0f, 1.0f,     -.25f, -.25f, 0.25f,       -.25f, .25f, 0.25f,
        -1.0f, -1.0f, 1.0f,     -.25f, -.25f, 0.25f,     -1.0f, 0.0f, 1.0f,
        -.25f, -.25f, 0.25f,     .25f, -.25f, 0.25f,      .25f, .25f, 0.25f,
        -.25f, -.25f, 0.25f,     .25f, .25f, 0.25f,       -.25f, .25f, 0.25f
    };
    private float[] texCoords = {
        0.0f, 0.0f,   .5f, 0.0f,   .375f, .375f, 
        0.5f, 0.0f,   .625f, .375f,    .375f, .375f,
        0.5f, 0.0f,   1.0f, 0.0f,    .625f, .375f,
        1.0f, 0.0f,   1.0f, .5f,    .625f, .375f,     
        1.0f, .5f,   .625f, .625f,    .625f, .375f,     
        1.0f, .5f,   1.0f, 1.0f,    .625f, .625f,
        .625f, .625f,   1.0f, 1.0f,   0.5f, 1.0f, 
        .5f, 1.0f,   .375f, .625f,    .625f, .625f,
        .375f, .625f,   .5f, 1.0f,    0.0f, 1.0f,
        .375f, .625f,   0.0f, 1.0f,    0.0f, .5f,     
        0.0f, .5f,   .375f, .375f,    .375f, .625f,     
        0.0f, 0.0f,   .375f, .375f,    0.0f, .5f,
        .375f, .375f,   .625f, .375f,    .625f, .625f,     
        .375f, .375f,  .625f, .625f,    .375f, .625f
    };

    public ManualObject() {}
    public float[] getVertices() { return vertices; }
    public float[] getTexCoords() { return texCoords; }
}
