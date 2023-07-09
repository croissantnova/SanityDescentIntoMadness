#version 150

uniform sampler2D DiffuseSampler;
uniform float DesaturateFactor;
uniform float SpreadFactor;
//uniform float SpreadThreshold;

in vec2 texCoord;

out vec4 fragColor;

vec3 spread(vec3 color, float factor)
{
	//return color * (1.0 + (color - threshold) * factor);
	float mul = 1.0 + ((color.r + color.g + color.b) * 1.0 - 1.0) * factor;
	return color * mul;
}

vec3 desaturate(vec3 color, float factor) {
	vec3 luma = vec3(0.299, 0.587, 0.114);
	vec3 gray = vec3(dot(luma, color));
	return vec3(mix(color, gray, factor));
}

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
	color.rgb = desaturate(spread(color.rgb, SpreadFactor), DesaturateFactor);
	//color.xyz *= vec3(0.308, 0.265, 0.242) * 2.3;
    fragColor = color;// * .91;
}