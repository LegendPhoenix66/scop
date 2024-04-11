package fortytwo.luxembourg

import org.lwjgl.glfw.GLFW

class GLFWCallbackHandler(private val display: Long, private val callbackListener: CallbackListener) {
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
        GLFW.glfwSetKeyCallback(display) { _, key, _, action, _ ->
            callbackListener.onKey(key, action)
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
            callbackListener.onScroll(xoffset, yoffset)
        }
    }

    private fun setCursorPosCallback() {
        GLFW.glfwSetCursorPosCallback(display) { _, xpos, ypos ->
            println("Mouse: $xpos, $ypos")
        }
    }
}
