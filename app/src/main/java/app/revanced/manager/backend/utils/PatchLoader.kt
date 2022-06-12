package app.revanced.manager.backend.utils

import app.revanced.manager.Global
import app.revanced.patcher.data.base.Data
import app.revanced.patcher.patch.base.Patch
import app.revanced.patcher.util.patch.implementation.DexPatchBundle
import dalvik.system.DexClassLoader
import org.jf.dexlib2.DexFileFactory
import java.io.File

object PatchLoader {
    fun loadFromFile(file: File): List<Class<out Patch<Data>>> {
        val path = file.absolutePath
        val classLoader = DexClassLoader(
            path,
            Global.app.codeCacheDir.absolutePath,
            null,
            PatchLoader.javaClass.classLoader
        )

        return DexPatchBundle(path, classLoader).loadPatches()
    }
}

private fun String.trimType() = substring(1, length - 1)