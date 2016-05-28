#version 150 core

in vec3 a_position;
out vec3 v_texcoord;

uniform mat4 u_modelMatrix;
uniform mat4 u_projectionMatrix;
uniform mat4 u_viewMatrix;


void main()
{
    gl_Position =   u_projectionMatrix * u_viewMatrix * u_modelMatrix * vec4(a_position, 1.0);
    v_texcoord = a_position;
}