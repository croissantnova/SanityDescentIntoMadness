#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 Resolution;
uniform float Factor;

in vec2 texCoord;

out vec4 fragColor;

void main() {
	float pi = 3.1415;
	float ratio = Resolution.x / Resolution.y;
	
	vec2 centerCoord = vec2(0.5, 0.5 / ratio);
	vec2 fragCoord = texCoord.xy;
	fragCoord.y = (texCoord.y * Resolution.y) / Resolution.x;
	vec2 centerToFrag = fragCoord - centerCoord;
	
	float dist = sqrt(dot(centerToFrag, centerToFrag));
	float centerDist = sqrt(dot(centerCoord, centerCoord));
	float power = (2.0 * pi / (2.0 * centerDist)) * Factor;

	float bind;
	if (power > 0.0)
		bind = centerDist;
	else
	{
		if (ratio < 1.0)
			bind = centerCoord.x;
		else
			bind = centerCoord.y;
	}
	
	vec2 uv;
	if (power > 0.0)
		uv = centerCoord + normalize(centerToFrag) * tan(dist * power) * bind / tan(bind * power);
	else if (power < 0.0)
		uv = centerCoord + normalize(centerToFrag) * atan(dist * -power * 10.0) * bind / atan(-power * bind * 10.0);
	else uv = fragCoord;

	uv.y *= ratio;
    fragColor = texture(DiffuseSampler, uv);
}