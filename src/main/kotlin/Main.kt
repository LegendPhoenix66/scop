package fortytwo.luxembourg

import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLEventListener
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLCanvas
import java.awt.Frame


class BasicFrame : GLEventListener {
    override fun display(arg0: GLAutoDrawable) {
        // method body
    }

    override fun dispose(arg0: GLAutoDrawable) {
        //method body
    }

    override fun init(arg0: GLAutoDrawable) {
        // method body
    }

    override fun reshape(arg0: GLAutoDrawable, arg1: Int, arg2: Int, arg3: Int, arg4: Int) {
        // method body
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            //getting the capabilities object of GL2 profile

            val profile = GLProfile.get(GLProfile.GL2)
            val capabilities = GLCapabilities(profile)


            // The canvas
            val glcanvas = GLCanvas(capabilities)
            val b = BasicFrame()
            glcanvas.addGLEventListener(b)
            glcanvas.setSize(400, 400)


            //creating frame
            val frame = Frame(" Basic Frame")


            //adding canvas to frame
            frame.add(glcanvas)
            frame.setSize(640, 480)
            frame.isVisible = true
        }
    }
}
