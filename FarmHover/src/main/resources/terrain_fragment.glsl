#version 150 core

struct coneLight {
   vec3 lightColour;
   float attenuation;
   float coneAngle;
   vec3 coneDirection;
};

in vec2 v_texcoord;
in vec3 surfaceNormal;
in vec3 toLightVector;

in vec3 toLightVectorCone;

//in vec3 v_normal;
//in vec4 v_position;

out vec4 fragColor;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform	coneLight conelight;
uniform vec3 lightColour;


void main()
{
    // multitexture
    vec4 blendMapColour = texture(blendMap,v_texcoord);
    float backTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
    vec2 tiledCoords = v_texcoord * 80.0;

    vec4 backGroundTextureColour = texture(backgroundTexture,tiledCoords) * backTextureAmount;
    vec4 rTextureColour = texture(rTexture,tiledCoords) * blendMapColour.r;
    vec4 gTextureColour = texture(gTexture,tiledCoords) * blendMapColour.g;
    vec4 bTextureColour = texture(bTexture,tiledCoords) * blendMapColour.b;

    vec4 totalColour = backGroundTextureColour + 0.2*rTextureColour + 0.2*gTextureColour + 0.2*bTextureColour;

    //cone light
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVectorCone = normalize(toLightVectorCone);
    float distanceToLight = length(toLightVectorCone);
    float attenuation = 1.0;
    attenuation = 1.0 / (1.0 + conelight.attenuation * distanceToLight);
    float lightToSurfaceAngle = degrees(acos(dot(-unitLightVectorCone, normalize(conelight.coneDirection))));

    if(lightToSurfaceAngle > conelight.coneAngle){
       attenuation = 0.0;
    }

    float nDotl = dot(unitNormal, unitLightVectorCone);
    float brightness = max(nDotl,0.0);
    vec3 diffuseCone = brightness * conelight.lightColour;

    // global light
    unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    nDotl = dot(unitNormal, unitLightVector);
    brightness = max(nDotl,0.1);
    vec3 diffuse = brightness * lightColour;

    fragColor = (attenuation * vec4(diffuseCone,1.0) + vec4(diffuse,1.0)) * totalColour;
    //fragColor = texture(u_texture, v_texcoord);
}
/*
#version 150

struct LightProperties
{
	vec4 position;
	vec4 ambientColor;
	vec4 diffuseColor;
	vec4 specularColor;
};

struct MaterialProperties
{
	vec4 ambientColor;
	vec4 diffuseColor;
	vec4 specularColor;
	float specularExponent;
};

uniform	LightProperties u_light;
uniform	MaterialProperties u_material;
uniform mat4 u_viewMatrix;
uniform mat4 u_modelMatrix;

uniform sampler2D u_texture;
uniform bool u_is_texture;

in vec3 v_normal;
in vec3 v_eye;
in vec2 v_texcoord;
in vec4 v_position;

out vec4 fragColor;

void main(void)
{
    vec4 color = u_light.ambientColor * u_material.ambientColor;

	vec3 normal = normalize(v_normal);

    vec3 direction = normalize(vec3((u_viewMatrix * u_modelMatrix * u_light.position) - v_position));

	float nDotL = max(dot(direction, normal), 0.0);

	if (nDotL > 0.0)
	{
        if(u_is_texture) {
            color += texture(u_texture, v_texcoord) * nDotL;
        } else {
            color += u_light.diffuseColor * u_material.diffuseColor * nDotL;
        }

        vec3 eye = normalize(v_eye);

        // Incident vector is opposite light direction vector.
        vec3 reflection = reflect(-direction, normal);

        float eDotR = max(dot(eye, reflection), 0.0);

		float specularIntensity = 0.0;
		if (eDotR > 0.0)
		{
			specularIntensity = pow(eDotR, u_material.specularExponent);
		}

		color += u_light.specularColor * u_material.specularColor * specularIntensity;
	}

	fragColor = color;
}
*/