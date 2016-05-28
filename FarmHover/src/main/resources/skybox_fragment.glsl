#version 150 core
in vec3 v_texcoord;
out vec4 fragColor;

uniform samplerCube u_texture;

void main()
{
    fragColor = texture(u_texture, v_texcoord);
}