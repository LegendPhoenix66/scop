package fortytwo.luxembourg

import org.lwjgl.opengl.GL33.*
import java.nio.file.Files
import java.nio.file.Paths

fun loadShaderSource(resourcePath: String): String {
    val url = ClassLoader.getSystemResource(resourcePath)
    if (url == null) {
        throw RuntimeException("Resource not found: $resourcePath")
    }

    val path = Paths.get(url.toURI()) // Convert the URL to a URI and then to a Path
    return String(Files.readAllBytes(path))
}


fun main() {
    val window = Window(800, 600, "3D Renderer")
    window.init()

    // Load the vertex and fragment shaders from files
    val vertexShaderSource = loadShaderSource("vertex_shader.glsl")
    val fragmentShaderSource = loadShaderSource("fragment_shader.glsl")

    // Create the shader program using the loaded shader source code
    val shaderProgram = ShaderProgram(vertexShaderSource, fragmentShaderSource)
    val objLoader = OBJLoader()
    val model = objLoader.loadOBJ("C:/Users/Phoenix/Documents/GitHub/scop/obj-files/teapot.obj")
    val mesh = Mesh(model)

    val camera = Camera()

    while (!window.shouldClose()) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f)  // Set a dark gray background color

        shaderProgram.use()
        val viewMatrix = camera.getViewMatrix()
        // Upload the view matrix to the shader here


        mesh.render()

        window.update()
    }

    // Cleanup
    mesh.cleanup()
    shaderProgram.cleanup()
    window.cleanup()
}
