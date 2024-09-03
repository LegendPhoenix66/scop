package fortytwo.luxembourg

import org.lwjgl.opengl.GL33.*

class ShaderProgram(vertexShaderSource: String, fragmentShaderSource: String) {
    private val programId: Int

    init {
        val vertexShaderId = compileShader(vertexShaderSource, GL_VERTEX_SHADER)
        val fragmentShaderId = compileShader(fragmentShaderSource, GL_FRAGMENT_SHADER)

        // Create program and link shaders
        programId = glCreateProgram()
        glAttachShader(programId, vertexShaderId)
        glAttachShader(programId, fragmentShaderId)
        glLinkProgram(programId)

        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw RuntimeException("Failed to link shader program: " + glGetProgramInfoLog(programId))
        }

        // Delete shaders after linking
        glDeleteShader(vertexShaderId)
        glDeleteShader(fragmentShaderId)
    }

    private fun compileShader(shaderSource: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        glShaderSource(shaderId, shaderSource)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw RuntimeException("Failed to compile shader: " + glGetShaderInfoLog(shaderId))
        }

        return shaderId
    }

    fun use() {
        glUseProgram(programId)
    }

    fun cleanup() {
        glDeleteProgram(programId)
    }
}
