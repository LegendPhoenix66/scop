package fortytwo.luxembourg

import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

class BasicFrame(monitor: Int = -1) {
    init {
        println("Hello, LGWJL ${Version.getVersion()}!")
        var errorCallback = GLFWErrorCallback.createPrint(System.err)
        glfwSetErrorCallback(errorCallback)
        check(glfwInit()) { "Unable to initialize GLFW" }

        val display = createDisplay("My Frame", true, monitor)
        glfwSetInputMode(display, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        glfwSetInputMode(display, GLFW_CURSOR, GLFW_CURSOR_HIDDEN)

        glfwMakeContextCurrent(display)
        GL.createCapabilities()

        glClearColor(0.7f, 0.0f, 0.7f, 0.0f)

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(display)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

            glfwSwapBuffers(display) // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()
        }
    }

    private fun createDisplay(
        title: String = "Display",
        resizable: Boolean = false,
        selectedMonitor: Int = -1,
    ): Long {
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE)
        glfwWindowHint(GLFW_RESIZABLE, if (resizable) GLFW_TRUE else GLFW_FALSE)

        // get monitor
        val monitors = glfwGetMonitors()
        val monitor =
            if (selectedMonitor >= (monitors?.capacity() ?: 0)) {
                System.err.println("Error: Monitor index out of bounds\nDefaulting to windowed mode")
                NULL
            } else if (selectedMonitor <= 0) {
                NULL
            } else {
                monitors?.get(selectedMonitor) ?: NULL
            }

        val monitorWidth =
            if (monitor == NULL) {
                1024
            } else {
                monitor.let {
                    val vidMode = glfwGetVideoMode(it)
                    vidMode?.width() ?: 1024
                }
            }
        val monitorHeight =
            if (monitor == NULL) {
                768
            } else {
                monitor.let {
                    val vidMode = glfwGetVideoMode(it)
                    vidMode?.height() ?: 768
                }
            }

        val id =
            glfwCreateWindow(
                monitorWidth,
                monitorHeight,
                title,
                monitor,
                NULL,
            ).also {
                check(it != NULL) { "Failed to create the GLFW window" }
            }

        glfwMakeContextCurrent(id)
        GL.createCapabilities()

        glfwSwapInterval(1)
        glfwShowWindow(id)
        return id
    }
}
