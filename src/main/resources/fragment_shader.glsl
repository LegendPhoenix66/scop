#version 330 core

// Interpolated data from the vertex shader
in vec3 fragNormal;
in vec2 fragTexCoord;

// Output data
out vec4 FragColor;

// Uniforms
uniform sampler2D textureSampler;  // Texture sampler
uniform vec3 lightDirection;       // Direction of the light source
uniform vec3 lightColor;           // Color of the light
uniform vec3 objectColor;          // Base color of the object

void main() {
    // Normalize the normal
    vec3 norm = normalize(fragNormal);

    // Simple diffuse lighting (Lambertian reflection)
    float diff = max(dot(norm, -lightDirection), 0.0);

    // Apply the texture to the object color
    vec3 texColor = texture(textureSampler, fragTexCoord).rgb;
    vec3 finalColor = (diff * lightColor * texColor) + (0.1 * objectColor); // Adding some ambient light

    // Set the final color output
    FragColor = vec4(finalColor, 1.0);
}