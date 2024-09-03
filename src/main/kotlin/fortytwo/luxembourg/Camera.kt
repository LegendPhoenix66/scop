package fortytwo.luxembourg

import org.joml.Matrix4f
import org.joml.Vector3f

class Camera {
    private var position = Vector3f(0.0f, 0.0f, 3.0f)
    private var rotation = Vector3f(0.0f, 0.0f, 0.0f)

    fun getViewMatrix(): Matrix4f {
        return Matrix4f().identity()
            .rotateX(Math.toRadians(rotation.x.toDouble()).toFloat())
            .rotateY(Math.toRadians(rotation.y.toDouble()).toFloat())
            .translate(-position.x, -position.y, -position.z)
    }

    fun move(dx: Float, dy: Float, dz: Float) {
        position.add(dx, dy, dz)
    }

    fun rotate(dx: Float, dy: Float, dz: Float) {
        rotation.add(dx, dy, dz)
    }
}
