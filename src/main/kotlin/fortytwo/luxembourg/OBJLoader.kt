package fortytwo.luxembourg

import java.io.File

class OBJLoader {
    fun loadOBJ(filePath: String): Model {
        val vertices = mutableListOf<Float>()
        val normals = mutableListOf<Float>()
        val textureCoords = mutableListOf<Float>()
        val indices = mutableListOf<Int>()

        File(filePath).forEachLine { line ->
            val tokens = line.split(" ")
            when (tokens[0]) {
                "v" -> {
                    vertices.add(tokens[1].toFloat())
                    vertices.add(tokens[2].toFloat())
                    vertices.add(tokens[3].toFloat())
                }
                "vn" -> {
                    normals.add(tokens[1].toFloat())
                    normals.add(tokens[2].toFloat())
                    normals.add(tokens[3].toFloat())
                }
                "vt" -> {
                    textureCoords.add(tokens[1].toFloat())
                    textureCoords.add(tokens[2].toFloat())
                }
                "f" -> {
                    tokens.drop(1).forEach {
                        val faceData = it.split("/")
                        indices.add(faceData[0].toInt() - 1)
                    }
                }
            }
        }

        return Model(
            vertices = vertices.toFloatArray(),
            normals = normals.toFloatArray(),
            textureCoords = textureCoords.toFloatArray(),
            indices = indices.toIntArray()
        )
    }
}
