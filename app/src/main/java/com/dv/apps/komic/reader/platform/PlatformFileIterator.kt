package com.dv.apps.komic.reader.platform

class PlatformFileIterator(
    private val paths: List<String>,
    private val platformFileManager: PlatformFileManager
): Iterator<PlatformFile> {
    private val stack = ArrayDeque(paths.mapNotNull { platformFileManager.get(it) })

    override fun hasNext() = stack.isNotEmpty()

    override fun next(): PlatformFile {
        val next = stack.removeFirst()

        if (next.type == PlatformFile.Type.Folder) {
            platformFileManager
                .listFiles(next)
                .asReversed()
                .forEach(stack::addFirst)
        }

        return next
    }

    class Factory(
        val platformFileManager: PlatformFileManager
    ) {
        fun build(paths: List<String>) = PlatformFileIterator(
            paths,
            platformFileManager,
        )
    }
}