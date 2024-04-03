package fortytwo.luxembourg

import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.NULL

class BasicFrame(monitor: Int = -1) {
    private var display = 0L
    val drawMode = DrawMode.RANDOM_COLOR

    init {
        initializeGLFW()
        display = createDisplay("My Frame", true, monitor)
        setCallbacks()
        loop()
    }

    fun drawSquare() {
        // square
        val vertices = // positions
            floatArrayOf(
                -0.5f, 0.5f, 0.0f, // top-left
                0.5f, 0.5f, 0.0f, // top-right
                0.5f, -0.5f, 0.0f, // bottom-right
                -0.5f, -0.5f, 0.0f, // bottom-left
            )
        val indices = // indices
            intArrayOf(
                0, 1, 2, // first triangle
                2, 3, 0, // second triangle
            )

        val vao = glGenVertexArrays() // vertex array object
        glBindVertexArray(vao) // bind vertex array object

        val vbo = glGenBuffers() // vertex buffer object
        glBindBuffer(GL_ARRAY_BUFFER, vbo) // bind vertex buffer object
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW) // copy vertices to buffer

        val ebo = glGenBuffers() // element buffer object
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo) // bind element buffer object
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW) // copy indices to buffer

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0) // position attribute
        glEnableVertexAttribArray(0) // enable the attribute

        when (drawMode) {
            DrawMode.FULL -> glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
            DrawMode.POINTS -> {
                // red
                glColor3f(1.0f, 0.0f, 0.0f)
                glPolygonMode(GL_FRONT_AND_BACK, GL_POINT)
            }
            DrawMode.LINES -> {
                //blue
                glColor3f(0.0f, 0.0f, 1.0f)
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
            }
            DrawMode.BLANK -> glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
            DrawMode.WHITE -> {
                glColor3f(1.0f, 1.0f, 1.0f)
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
            }
            DrawMode.RANDOM_COLOR -> {
                val r1 = Math.random().toFloat()
                val g1 = Math.random().toFloat()
                val b1 = Math.random().toFloat()
                glColor3f(r1, g1, b1)
                glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_INT, 0) // draw the first triangle

                val r2 = Math.random().toFloat()
                val g2 = Math.random().toFloat()
                val b2 = Math.random().toFloat()
                glColor3f(r2, g2, b2)
                glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_INT, 3 * 4) // draw the second triangle
            }
        }

        //glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0) // draw the square

        glBindBuffer(GL_ARRAY_BUFFER, 0) // unbind the buffer
        glBindVertexArray(0) // unbind the vertex array object

        glDeleteVertexArrays(vao) // delete the vertex array object
        glDeleteBuffers(vbo) // delete the vertex buffer object
        glDeleteBuffers(ebo) // delete the element buffer object
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

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(display)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

            drawSquare()

            glfwSwapBuffers(display) // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()
        }
    }
}
