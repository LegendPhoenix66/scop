package fortytwo.luxembourg

import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.BufferUtils
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.IntBuffer
import kotlin.math.cos
import kotlin.math.sin

class BasicFrame(monitor: Int = -1) : FileOpenListener, CallbackListener {
    private var display = 0L
    private var vertices: FloatArray = floatArrayOf()
    private var indices: IntArray = intArrayOf()
    private val drawMode = DrawMode.WHITE

    init {
        initializeGLFW()
        display = createDisplay("My Frame", true, monitor)
        GLFWCallbackHandler(display, this)
        glfwFocusWindow(display)
        drawSquareOrtho()
        loop()
    }

    private fun drawSquareScreen(scale: Float = 0.5f) {
        useOrthoProjection = false
        // square
        val squareVertices = // positions
            floatArrayOf(
                -1.0f, 1.0f, 0.0f, // top-left
                1.0f, 1.0f, 0.0f, // top-right
                1.0f, -1.0f, 0.0f, // bottom-right
                -1.0f, -1.0f, 0.0f, // bottom-left
            )
        val squareIndices = // indices
            intArrayOf(
                0, 1, 2, // first triangle
                2, 3, 0, // second triangle
            )
        // scale the square
        for (i in squareVertices.indices) {
            squareVertices[i] *= scale
        }
        vertices = squareVertices
        indices = squareIndices
    }

    private fun drawSquareOrtho(scale: Float = 1f) {
        useOrthoProjection = true
        val topRight = Vector3f(50f, 50f, 0f) // top-right
        val topLeft = Vector3f(-50f, 50f, 0f) // top-left
        val bottomLeft = Vector3f(-50f, -50f, 0f) // bottom-left
        val bottomRight = Vector3f(50f, -50f, 0f) // bottom-right

        // square
        val squareVertices = // positions
            floatArrayOf(
                topLeft.x, topLeft.y, topLeft.z, // top-left
                topRight.x, topRight.y, topRight.z, // top-right
                bottomRight.x, bottomRight.y, bottomRight.z, // bottom-right
                bottomLeft.x, bottomLeft.y, bottomLeft.z, // bottom-left
            )
        val squareIndices = // indices
            intArrayOf(
                0, 1, 2, // first triangle
                2, 3, 0, // second triangle
            )
        // scale the square
        for (i in squareVertices.indices) {
            squareVertices[i] *= scale
        }
        vertices = squareVertices
        indices = squareIndices
    }
    private fun drawObj() {
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
            DrawMode.FULL -> {
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
                glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0) // draw the square
            }
            DrawMode.POINTS -> {
                // red
                glColor3f(1.0f, 0.0f, 0.0f)
                glPolygonMode(GL_FRONT_AND_BACK, GL_POINT)
                glDrawElements(GL_POINTS, indices.size, GL_UNSIGNED_INT, 0) // draw the square
            }
            DrawMode.LINES -> {
                // blue
                glColor3f(0.0f, 0.0f, 1.0f)
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
                glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0) // draw the square
            }
            DrawMode.BLANK -> {
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
                glDrawElements(GL_TRIANGLES, 0, GL_UNSIGNED_INT, 0) // draw the square
            }
            DrawMode.WHITE -> {
                glColor3f(1.0f, 1.0f, 1.0f)
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
                glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0) // draw the square
            }
            DrawMode.RANDOM_COLOR -> {
                for (i in indices.indices step 3) {
                    // Get the coordinates of the triangle
                    val x = vertices[indices[i] * 3]
                    val y = vertices[indices[i] * 3 + 1]
                    val z = vertices[indices[i] * 3 + 2]

                    // Calculate the color based on the coordinates
                    val r = (x + 1) / 2 // normalize to 0-1 range
                    val g = (y + 1) / 2 // normalize to 0-1 range
                    val b = (z + 1) / 2 // normalize to 0-1 range

                    glColor3f(r, g, b)

                    glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_INT, i * 4L) // draw the triangle
                }
            }
        }

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

        // Set up the orthographic projection matrix
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(0.0, monitorWidth.toDouble(), monitorHeight.toDouble(), 0.0, -1.0, 1.0)
        glMatrixMode(GL_MODELVIEW)

        glfwSwapInterval(1)
        glfwShowWindow(id)
        return id
    }

    private var frames = 0
    private var lastTime = System.currentTimeMillis()
    private var useOrthoProjection = false
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

            if (useOrthoProjection) {
                drawInOrthoProjection()
            } else {
                drawInScreenSpace()
            }

            drawObj()

            glfwSwapBuffers(display) // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()

            // Increment the frames counter
            frames++

            // Check if a second has passed
            if (System.currentTimeMillis() > lastTime + 1000) {
                // Update the window title with the FPS count
                glfwSetWindowTitle(display, "FPS: $frames")

                // Reset the frames counter and the timer
                frames = 0
                lastTime = System.currentTimeMillis()
            }
        }
    }

    private fun drawInScreenSpace() {
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0)
        glMatrixMode(GL_MODELVIEW)
    }
    private fun drawInOrthoProjection() {
        val widthBuffer: IntBuffer = BufferUtils.createIntBuffer(1)
        val heightBuffer: IntBuffer = BufferUtils.createIntBuffer(1)

        glfwGetWindowSize(display, widthBuffer, heightBuffer)

        val monitorWidth = widthBuffer[0]
        val monitorHeight = heightBuffer[0]

        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(
            -monitorWidth.toDouble() / 2,
            monitorWidth.toDouble() / 2,
            -monitorHeight.toDouble() / 2,
            monitorHeight.toDouble() / 2,
            -100.0,
            100.0,
        )
        glMatrixMode(GL_MODELVIEW)
    }
    override fun onFileOpen(vertices: FloatArray, indices: IntArray) {
        this.vertices = vertices
        this.indices = indices
    }

    override fun onKey(key: Int, action: Int) {
        println("$key: $action")
        when (key) {
            GLFW_KEY_ESCAPE -> {
                if (action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(display, true)
                }
            }
            GLFW_KEY_O -> {
                if (action == GLFW_RELEASE) {
                    FileOpener().openFile(this)
                }
            }
        }
    }

    override fun onMouseButton(button: Int, action: Int) {
        TODO("Not yet implemented")
    }

    override fun onScroll(xoffset: Double, yoffset: Double) {
        scale(yoffset.toFloat())
    }

    override fun onCursorPos(xpos: Double, ypos: Double) {
        TODO("Not yet implemented")
    }
}
}
