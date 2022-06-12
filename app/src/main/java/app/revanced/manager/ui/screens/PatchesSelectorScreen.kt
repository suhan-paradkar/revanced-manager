package app.revanced.manager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.revanced.manager.ui.data.PatcherSubscreenData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator

@OptIn(ExperimentalMaterialApi::class)
@Destination
@RootNavGraph
@Composable
fun PatchesSelectorScreen(
    patches: Array<String>,
    filter: Array<String>,
    resultNavigator: ResultBackNavigator<PatcherSubscreenData>
) {
    LazyColumn {
        items(count = patches.size) {
            var selected by rememberSaveable { mutableStateOf(true) }
            ListItem(
                modifier = Modifier.clickable {
                    selected = !selected
                },
                text = {
                    Text(patches[it])
                },
                trailing = {
                    Checkbox(checked = selected, onCheckedChange = { selected = !selected })
                }
            )
        }
    }
}