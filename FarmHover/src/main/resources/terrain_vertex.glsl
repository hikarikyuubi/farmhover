#version 150 core

in vec3 a_position;
in vec3 a_normal;
in vec2 a_texcoord;

out vec2 v_texcoord;
out vec3 surfaceNormal;
out vec3 toLightVector;

out vec3 toLightVectorCone;

uniform mat4 u_modelMatrix;
uniform mat4 u_projectionMatrix;
uniform mat4 u_viewMatrix;
uniform vec3 lightPosition;
uniform vec3 coneLightPosition;


void main()
{
    vec4 worldPosition = u_modelMatrix * vec4(a_position, 1.0);
    gl_Position =   u_projectionMatrix * u_viewMatrix * worldPosition;
    v_texcoord = a_texcoord;

    surfaceNormal = (u_modelMatrix * vec4(a_normal,0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;

    toLightVectorCone = coneLightPosition - worldPosition.xyz;
}
/*
#version 150

uniform mat4 u_modelMatrix;
uniform mat4 u_projectionMatrix;
uniform mat4 u_viewMatrix;

in vec3 a_position;
in vec3 a_normal;
in vec2 a_texcoord;

out vec3 v_normal;
out vec3 v_eye;
out vec2 v_texcoord;
out vec4 v_position;

void main(void)
{
  v_position = u_viewMatrix * u_modelMatrix * vec4(a_position, 1.0);

  v_eye = -vec3(v_position);

  v_normal = transpose(inverse(mat3(u_viewMatrix * u_modelMatrix))) * a_normal;
  //v_normal = mat3(u_viewMatrix * u_modelMatrix) * a_normal;

  v_texcoord = a_texcoord;

  gl_Position = u_projectionMatrix * v_position;
}*/