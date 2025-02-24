package console

object ConsoleOutput {

    fun text(output: String) {
        println(output)
    }

    fun error(error: Exception) {
        error(error.message)
    }

    fun error(error: String?) {
        println("[ERROR] $error")
    }
}