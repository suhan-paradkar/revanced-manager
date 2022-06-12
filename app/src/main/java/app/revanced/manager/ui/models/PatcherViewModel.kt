package app.revanced.manager.ui.models

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.revanced.manager.backend.api.ManagerAPI
import app.revanced.manager.backend.utils.PatchLoader
import app.revanced.manager.ui.models.utils.Resource
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.data.base.Data
import app.revanced.patcher.patch.base.Patch
import kotlinx.coroutines.launch
import java.io.File

class PatcherViewModel : ViewModel() {
    private val tag = "PatcherViewModel"

    val applications = mutableStateOf<Resource<Array<ApplicationInfo>>>(Resource.Loading)
    val patchDownload = mutableStateOf<Resource<File>>(Resource.Loading)
    val patches = mutableStateOf<Resource<Map<String, Class<out Patch<Data>>>>>(Resource.Loading)

    fun fetchApps(pm: PackageManager) {
        viewModelScope.launch {
            try {
                applications.value = Resource.Success(
                    pm.getInstalledApplications(PackageManager.GET_META_DATA).toTypedArray()
                )
            } catch (e: Exception) {
                Log.e(tag, "failed to fetch apps", e)
                applications.value = Resource.Failure("fetching apps")
            }
        }
    }

    fun downloadPatches(workdir: File) = viewModelScope.launch {
        try {
            // TODO: do something with returned asset
            val (_, out) = ManagerAPI.downloadPatches(workdir)
            patchDownload.value = Resource.Success(out)
        } catch (e: Exception) {
            Log.e(tag, "failed to download patches", e)
            patchDownload.value = Resource.Failure("downloading patches")
        }
    }

    fun loadPatches(file: File) = viewModelScope.launch {
        try {
            patches.value = Resource.Success(buildMap {
                PatchLoader.loadFromFile(file).forEach { p ->
                    val name =
                        (p.annotations.find { it is Name } as? Name)?.name ?: p.simpleName
                    put(name, p)
                }
            })
        } catch (e: Exception) {
            Log.e(tag, "failed to load patches", e)
            patches.value = Resource.Failure("loading patches")
        }
    }
}