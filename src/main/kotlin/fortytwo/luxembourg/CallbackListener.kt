package fortytwo.luxembourg

interface CallbackListener {
    fun onKey(key: Int, action: Int)
    fun onMouseButton(button: Int, action: Int)
    fun onScroll(xoffset: Double, yoffset: Double)
    fun onCursorPos(xpos: Double, ypos: Double)
}
