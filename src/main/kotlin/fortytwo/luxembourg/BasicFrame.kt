package fortytwo.luxembourg

import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

class BasicFrame(monitor: Int = -1) {
    private var display = 0L

    init {
        initializeGLFW()
        display = createDisplay("My Frame", true, monitor)
        setCallbacks()
        loop()
    }

    private fun initializeGLFW() {
        println("Hello, LGWJL ${Version.getVersion()}!")
        val errorCallback = GLFWErrorCallback.createPrint(System.err)
        glfwSetErrorCallback(errorCallback)
        check(glfwInit()) { "Unable to initialize GLFW" }
    }

    private fun setCallbacks() {
        // glfwSetInputMode(display, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        glfwSetInputMode(display, GLFW_CURSOR, GLFW_CURSOR_HIDDEN)
        glfwMakeContextCurrent(display)

        setKeyCallback()
        setMouseButtonCallback()
        setScrollCallback()
        setCursorPosCallback()
    }

    private fun setKeyCallback() {
        glfwSetKeyCallback(display) { window, key, _, action, _ ->
            println("$key: $action")
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true)
            }
        }
    }

    private fun setMouseButtonCallback() {
        glfwSetMouseButtonCallback(display) { _, button, action, _ ->
            println("$button: $action")
        }
    }

    private fun setScrollCallback() {
        glfwSetScrollCallback(display) { _, xoffset, yoffset ->
            println("Scroll: $xoffset, $yoffset")
        }
    }

    private fun setCursorPosCallback() {
        glfwSetCursorPosCallback(display) { _, xpos, ypos ->
            println("Mouse: $xpos, $ypos")
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

    private fun loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
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
}
