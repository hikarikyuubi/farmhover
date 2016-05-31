#version 150

in vec2 position;

uniform mat4 u_projectionMatrix;
uniform mat4 u_viewMatrix;

void main(void){

	gl_Position = u_projectionMatrix * u_viewMatrix * vec4(position, 0.0, 1.0);

}