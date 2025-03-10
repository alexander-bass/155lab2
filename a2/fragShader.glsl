#version 430

in vec2 tc;
out vec4 color;

uniform mat4 mv_matric;
uniform mat4 p_matrix;
uniform int tileCount;

layout (binding = 0) uniform sampler2D samp;

void main(void) {
    color = texture(samp, tc);
}