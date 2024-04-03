package fortytwo.luxembourg

fun main() {
    println("Hello, World!")
    println("Select monitor:")
    println("0: Primary")
    println("1: Secondary")
    println("-1: windowed mode")
    val monitor = readln().toIntOrNull() ?: -1

    val virtualThread = Thread.startVirtualThread { BasicFrame(monitor = monitor) }
    virtualThread.join()
}
