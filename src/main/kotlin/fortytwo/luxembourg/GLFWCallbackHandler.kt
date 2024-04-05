package fortytwo.luxembourg

import org.lwjgl.glfw.GLFW

class GLFWCallbackHandler(private val display: Long) {
    init {
        GLFW.glfwSetInputMode(display, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)
        GLFW.glfwSetInputMode(display, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN)
        GLFW.glfwMakeContextCurrent(display)

        setKeyCallback()
        setMouseButtonCallback()
        setScrollCallback()
        setCursorPosCallback()
    }

    private fun setKeyCallback() {
        GLFW.glfwSetKeyCallback(display) { window, key, _, action, _ ->
            println("$key: $action")
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true)
            } else if (key == GLFW.GLFW_KEY_O && action == GLFW.GLFW_RELEASE) {
                // open file-chooser
                FileOpener.openFile()
            }
        }
    }

    private fun setMouseButtonCallback() {
        GLFW.glfwSetMouseButtonCallback(display) { _, button, action, _ ->
            println("$button: $action")
        }
    }

    private fun setScrollCallback() {
        GLFW.glfwSetScrollCallback(display) { _, xoffset, yoffset ->
            println("Scroll: $xoffset, $yoffset")
        }
    }

    private fun setCursorPosCallback() {
        GLFW.glfwSetCursorPosCallback(display) { _, xpos, ypos ->
            println("Mouse: $xpos, $ypos")
        }
    }
}
