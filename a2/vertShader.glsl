#version 430

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
out vec2 tc;

uniform mat4 mv_matrix;
uniform mat4 p_matrix;
uniform int tileCount;
uniform int useTexture;
uniform int axisColor;

layout (binding = 0) uniform sampler2D samp;

void main(void) {
    gl_Position = p_matrix * mv_matrix * vec4(position, 1.0);
    tc = texCoord;
    tc = tc * tileCount;
}