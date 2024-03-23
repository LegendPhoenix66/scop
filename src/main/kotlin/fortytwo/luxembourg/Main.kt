package fortytwo.luxembourg.fortytwo.luxembourg

import fortytwo.luxembourg.BasicFrame

fun main() {
    println("Hello, World!")
    val virtualThread = Thread.startVirtualThread { BasicFrame() }
    virtualThread.join()
}
