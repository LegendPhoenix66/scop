package fortytwo.luxembourg

import org.lwjgl.opengl.GL33.*

class Mesh(model: Model) {
    private val vaoId: Int
    private val vertexCount: Int

    init {
        vertexCount = model.indices.size

        // Generate VAO and bind it
        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)

        // Vertex positions
        val vboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, model.vertices, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(0)

        // Normals
        val vboNormalsId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalsId)
        glBufferData(GL_ARRAY_BUFFER, model.normals, GL_STATIC_DRAW)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(1)

        // Texture coordinates
        val vboTexCoordsId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboTexCoordsId)
        glBufferData(GL_ARRAY_BUFFER, model.textureCoords, GL_STATIC_DRAW)
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(2)

        // Indices
        val eboId = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, model.indices, GL_STATIC_DRAW)

        // Unbind the VAO
        glBindVertexArray(0)
    }

    fun render() {
        // Bind VAO
        glBindVertexArray(vaoId)
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }

    fun cleanup() {
        glDeleteVertexArrays(vaoId)
    }
}
