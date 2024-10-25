package pl.rejfi.solvrorekruapp


fun <T> List<T>.plusUnique(elements: List<T>): List<T> {
    val uniqueElements = mutableListOf<T>()
    elements.forEach { element ->
        if (element !in this) {
            uniqueElements.add(element)
        }
    }
    return this + uniqueElements
}