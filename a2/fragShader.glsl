#version 430

in vec2 tc;
out vec4 color;

uniform mat4 mv_matric;
uniform mat4 p_matrix;
uniform int tileCount;
uniform int useTexture;
uniform vec3 axisColor;

layout (binding = 0) uniform sampler2D samp;

void main(void) {
    if (useTexture == 1) {
        color = texture(samp, tc);
    } else {
        color = vec4(axisColor, 1.0);
    }
}