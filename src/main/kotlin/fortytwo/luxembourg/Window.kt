package fortytwo.luxembourg

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL

class Window(val width: Int, val height: Int, val title: String) {
    private var windowHandle: Long = 0

    fun init() {
        // Initialize GLFW (window and context management library)
        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        // Configure GLFW
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, 0, 0)
        if (windowHandle == 0L) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle)
        // Enable v-sync
        glfwSwapInterval(1)

        // Make the window visible
        glfwShowWindow(windowHandle)

        // Initialize OpenGL
        GL.createCapabilities()
    }

    fun shouldClose() = glfwWindowShouldClose(windowHandle)

    fun update() {
        // Swap the color buffers
        glfwSwapBuffers(windowHandle)
        // Poll for window events
        glfwPollEvents()
    }

    fun cleanup() {
        glfwDestroyWindow(windowHandle)
        glfwTerminate()
    }
}
