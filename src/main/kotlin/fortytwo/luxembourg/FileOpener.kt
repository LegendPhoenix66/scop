package fortytwo.luxembourg

import java.awt.EventQueue
import java.io.File
import javax.swing.JFileChooser

class FileOpener {
    fun openFile(fileOpenListener: FileOpenListener) {
        EventQueue.invokeLater {
            val chooser = JFileChooser()
            chooser.currentDirectory = File(".")
            val result = chooser.showOpenDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                val file = chooser.selectedFile
                println("Selected file: ${file.absolutePath}")

                // convert file to array of vertices and indices
                val objVertices = mutableListOf<Float>()
                val objIndices = mutableListOf<Int>()
                file.forEachLine {
                    val parts = it.split(" ")
                    if (parts[0] == "v") {
                        objVertices.add(parts[1].toFloat())
                        objVertices.add(parts[2].toFloat())
                        objVertices.add(parts[3].toFloat())
                    } else if (parts[0] == "f") {
                        objIndices.add(parts[1].toInt() - 1)
                        objIndices.add(parts[2].toInt() - 1)
                        objIndices.add(parts[3].toInt() - 1)
                    }
                }
                fileOpenListener.onFileOpen(objVertices.toFloatArray(), objIndices.toIntArray())
            }
        }
    }
}
