package fortytwo.luxembourg

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

class Camera {
    private var projectionMatrix: Matrix4f? = null
    private var viewMatrix:Matrix4f? = null
    private var inverseProjection:Matrix4f? = null
    private var inverseView:Matrix4f? = null

    var position: Vector2f? = null
    private val projectionSize = Vector2f(32.0f * 40.0f, 32.0f * 21.0f)

    fun Camera(position: Vector2f?) {
        this.position = position
        this.projectionMatrix = Matrix4f()
        this.viewMatrix = Matrix4f()
        this.inverseProjection = Matrix4f()
        this.inverseView = Matrix4f()
        adjustProjection()
    }

    fun adjustProjection() {
        projectionMatrix!!.identity()
        projectionMatrix!!.ortho(0.0f, projectionSize.x, 0.0f, projectionSize.y, 0.0f, 100.0f)
        projectionMatrix!!.invert(inverseProjection)
    }

    fun getInverseProjection(): Matrix4f? {
        return this.inverseProjection
    }

    fun getInverseView(): Matrix4f? {
        return this.inverseView
    }

    fun getViewMatrix(): Matrix4f? {
        val cameraFront = Vector3f(0.0f, 0.0f, -1.0f)
        val cameraUp = Vector3f(0.0f, 1.0f, 0.0f)
        viewMatrix!!.identity()
        viewMatrix!!.lookAt(
            Vector3f(
                position!!.x, position!!.y, 20.0f
            ),
            cameraFront.add(position!!.x, position!!.y, 0.0f),
            cameraUp
        )
        viewMatrix!!.invert(inverseView)

        return this.viewMatrix 
    }

    fun getProjectionMatrix(): Matrix4f? {
        return this.projectionMatrix
    }

    fun getProjectionSize(): Vector2f {
        return projectionSize
    }
}