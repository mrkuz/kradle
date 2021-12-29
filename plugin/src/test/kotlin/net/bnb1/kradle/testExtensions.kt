package net.bnb1.kradle

import java.util.concurrent.TimeUnit

fun String.execute(timeout: Int = 5): String {
    val process = ProcessBuilder()
        .command(*split(" ").toTypedArray())
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .start()
    process.waitFor(timeout.toLong(), TimeUnit.SECONDS)
    return process.inputStream.bufferedReader().use { it.readText() }
}
