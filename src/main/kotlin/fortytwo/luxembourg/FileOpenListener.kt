package fortytwo.luxembourg

fun interface FileOpenListener {
    fun onFileOpen(
        vertices: FloatArray,
        indices: IntArray,
    )
}
