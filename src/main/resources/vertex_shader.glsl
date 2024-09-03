#version 330 core

// Input vertex data, different for all executions of this shader.
layout(location = 0) in vec3 inPosition;  // Position of the vertex
layout(location = 1) in vec3 inNormal;    // Normal at the vertex
layout(location = 2) in vec2 inTexCoord;  // Texture coordinate of the vertex

// Output data; will be interpolated for each fragment.
out vec3 fragNormal;      // Normal to be used in the fragment shader
out vec2 fragTexCoord;    // Texture coordinate to be used in the fragment shader

// Uniforms (global variables set from outside the shader)
uniform mat4 model;       // Model matrix
uniform mat4 view;        // View matrix
uniform mat4 projection;  // Projection matrix

void main() {
    // Apply transformations to the vertex position
    gl_Position = projection * view * model * vec4(inPosition, 1.0);

    // Pass the normal and texture coordinate to the fragment shader
    fragNormal = mat3(transpose(inverse(model))) * inNormal; // Adjust normal based on the model matrix
    fragTexCoord = inTexCoord;
}