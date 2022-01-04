package net.bnb1.kradle

class Tracer {

    private var active = true
    private var level = 0
    private val _entries = mutableListOf<Entry>()
    val entries
        get() = _entries.toList()

    fun trace(message: String) {
        if (active) {
            _entries.add(Entry(level, message))
        }
    }

    fun deactivate() {
        active = false
    }

    fun branch(action: Tracer.() -> Unit) {
        level++
        action.invoke(this)
        level--
    }

    data class Entry(val level: Int, val message: String)
}
