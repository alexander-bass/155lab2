package a2;

public class CrayzeeCube {
    private float[] vertices = {
        -1.0f, -1.0f, 1.0f,     0.0f, -1.0f, 1.0f,      -.25f, -.25f, 0.5f,
        0.0f, -1.0f, 1.0f,      .25f, -.25f, 0.5f,     -.25f, -.25f, 0.5f,
        0.0f, -1.0f, 1.0f,      1.0f, -1.0f, 1.0f,      .25f, -.25f, 0.5f,
        1.0f, -1.0f, 1.0f,      1.0f, 0.0f, 1.0f,       .25f, -.25f, 0.5f,
        1.0f, 0.0f, 1.0f,       .25f, .25f, 0.5f,       .25f, -.25f, 0.5f,
        1.0f, 0.0f, 1.0f,       1.0f, 1.0f, 1.0f,       .25f, .25f, 0.5f,
        .25f, .25f, 0.5f,       1.0f, 1.0f, 1.0f,       0.0f, 1.0f, 1.0f,
        .25f, .25f, 0.5f,       0.0f, 1.0f, 1.0f,       -.25f, .25f, 0.5f,  //8
        -.25f, .25f, 0.5f,      0.0f, 1.0f, 1.0f,       -1.0f, 1.0f, 1.0f,
        -.25f, .25f, 0.5f,     -1.0f, 1.0f, 1.0f,       -1.0f, 0.0f, 1.0f,
        -1.0f, 0.0f, 1.0f,     -.25f, -.25f, 0.5f,       -.25f, .25f, 0.5f,
        -1.0f, -1.0f, 1.0f,     -.25f, -.25f, 0.5f,     -1.0f, 0.0f, 1.0f,
        -.25f, -.25f, 0.5f,     .25f, -.25f, 0.5f,      .25f, .25f, 0.5f,
        -.25f, -.25f, 0.5f,     .25f, .25f, 0.5f,       -.25f, .25f, 0.5f
    };

    private float[] texCoords = {
        0.0f, 0.0f,   .5f, 0.0f,   .375f, .375f, 
        0.5f, 0.0f,   .625f, .375f,    .375f, .375f,
        0.5f, 0.0f,   1.0f, 0.0f,    .625f, .375f,
        1.0f, 0.0f,   1.0f, .5f,    .625f, .375f,     
        1.0f, .5f,   .625f, .625f,    .625f, .375f,     
        1.0f, .5f,   1.0f, 1.0f,    .625f, .625f,
        .625f, .625f,   1.0f, 1.0f,   0.5f, 1.0f, 
        .625f, .625f,   .5f, 1.0f,    .375f, 625f,  //8
        .375f, 625f,   .5f, 1.0f,    0.0f, 1.0f,
        .375f, 625f,   0.0f, 1.0f,    0.0f, .5f,     
        0.0f, .5f,   .375f, .375f,    .375f, 625f,     
        0.0f, 0.0f,   .375f, .375f,    0.0f, .5f,
        .375f, .375f,   .625f, .375f,    .625f, .625f,     
        .375f, .375f,  .625f, .625f,    .375f, 625f
    };

    public CrayzeeCube() {}
    public float[] getVertices() { return vertices; }
    public float[] getTexCoords() { return texCoords; }
}
