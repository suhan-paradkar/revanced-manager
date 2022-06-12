package app.revanced.manager.ui.screens.mainsubscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.revanced.manager.Global
import app.revanced.manager.R
import app.revanced.manager.extensions.add
import app.revanced.manager.ui.data.PatcherSubscreenData
import app.revanced.manager.ui.models.PatcherViewModel
import app.revanced.manager.ui.models.utils.Resource
import app.revanced.manager.ui.screens.destinations.AppSelectorScreenDestination
import app.revanced.manager.ui.screens.destinations.PatchesSelectorScreenDestination
import app.revanced.patcher.data.base.Data
import app.revanced.patcher.patch.base.Patch
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@RootNavGraph
@Composable
fun PatcherSubscreen(
    navigator: NavController,
    resultRecipient: ResultRecipient<AppSelectorScreenDestination, PatcherSubscreenData>,
    vm: PatcherViewModel = viewModel()
) {
    var selectedAppPackage by rememberSaveable { mutableStateOf("") }
    val selectedPatches by rememberSaveable { mutableStateOf(arrayOf<String>()) }

    LaunchedEffect(Unit) {
        vm.fetchApps(Global.app.packageManager)

        val workdir = Global.app.filesDir.resolve("work").also { it.mkdirs() }
        vm.downloadPatches(workdir).invokeOnCompletion {
            val downloadState by vm.patchDownload
            if (downloadState is Resource.Success) {
                vm.loadPatches((downloadState as Resource.Success<File>).data).invokeOnCompletion {
                    val patchesState by vm.patches
                    if (patchesState is Resource.Success) {
                        for (key in (patchesState as Resource.Success<Map<String, Class<out Patch<Data>>>>).data.keys) {
                            selectedPatches.add(key)
                        }
                    }
                }
            }
        }
    }

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Value -> result.value.packageId?.let { selectedAppPackage = it }
            else -> {}
        }
    }
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = { null },
            icon = { Icon(imageVector = Icons.Default.Build, contentDescription = "sd") },
            text = { Text(text = "Patch") })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            val appsState by vm.applications
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                onClick = {
                    when (val state = appsState) {
                        is Resource.Success -> {
                            navigator.navigate(
                                AppSelectorScreenDestination(state.data, arrayOf("aboba")).route
                            )
                        }
                        else -> {}
                    }
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.card_application_header),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = when (val state = appsState) {
                            Resource.Loading -> "Fetching applications"
                            is Resource.Failure -> stringResource(
                                R.string.error_message,
                                state.what
                            )
                            is Resource.Success -> {
                                when (selectedAppPackage) {
                                    "" -> stringResource(R.string.card_application_body)
                                    else -> selectedAppPackage
                                }
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )
                }
            }
            val patchesState by vm.patches
            val downloadState by vm.patchDownload
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                onClick = {
                    when (patchesState) {
                        is Resource.Success -> {
                            navigator.navigate(
                                PatchesSelectorScreenDestination(
                                    selectedPatches,
                                    arrayOf("asd")
                                ).route
                            )
                        }
                        else -> {}
                    }
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.card_patches_header),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = when (val dlState = downloadState) {
                            Resource.Loading -> "Downloading patches"
                            is Resource.Failure -> stringResource(
                                R.string.error_message,
                                dlState.what
                            )
                            is Resource.Success -> {
                                when (val state = patchesState) {
                                    Resource.Loading -> "Loading patches"
                                    is Resource.Failure -> stringResource(
                                        R.string.error_message,
                                        state.what
                                    )
                                    is Resource.Success -> if (selectedPatches.isEmpty()) {
                                        stringResource(R.string.card_patches_body_patches)
                                    } else {
                                        val patch =
                                            "patch" + if (selectedPatches.size != 1) "es" else ""
                                        "${selectedPatches.size} $patch selected."
                                    }
                                }
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )
                }
            }

        }
    }

}