package com.example.puppet.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.puppet.ui.components.LoadError
import com.example.puppet.ui.components.ModuleAvatar
import com.example.puppet.ui.model.ModuleDetailUi
import com.example.puppet.resources.Res
import com.example.puppet.resources.detail_license
import com.example.puppet.resources.detail_owner
import com.example.puppet.resources.detail_updated
import com.example.puppet.resources.detail_version
import com.example.puppet.resources.module_downloads
import com.example.puppet.resources.module_not_found
import com.example.puppet.resources.no_summary
import com.example.puppet.resources.section_description
import com.example.puppet.resources.section_neighbours
import com.example.puppet.resources.section_tags
import org.jetbrains.compose.resources.stringResource

@Composable
fun ModuleDetailScreen(
    state: ModuleDetailState,
    onIntent: (ModuleDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        ModuleDetailState.Loading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        ModuleDetailState.NotFound -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(Res.string.module_not_found))
        }

        ModuleDetailState.Error -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadError(onRetry = { onIntent(ModuleDetailIntent.Retry) })
        }

        is ModuleDetailState.Loaded -> ModuleDetailContent(
            module = state.module,
            onIntent = onIntent,
            modifier = modifier,
        )
    }
}

@Composable
private fun ModuleDetailContent(
    module: ModuleDetailUi,
    onIntent: (ModuleDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ModuleAvatar(initial = module.initial, size = 96.dp)
        Text(module.name, style = MaterialTheme.typography.headlineMedium)
        Text(
            stringResource(Res.string.detail_owner, module.owner),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(stringResource(Res.string.detail_version, module.version))
        Text(stringResource(Res.string.module_downloads, module.downloads))
        Text(stringResource(Res.string.detail_license, module.license))
        Text(stringResource(Res.string.detail_updated, module.updatedAt))

        Section(stringResource(Res.string.section_description))
        Text(if (module.summary.isEmpty()) stringResource(Res.string.no_summary) else module.summary)

        Section(stringResource(Res.string.section_tags))
        Text(module.tags.joinToString(", "))

        Section(stringResource(Res.string.section_neighbours))
        NeighbourRow(
            neighbours = module.neighbours,
            onClick = { slug -> onIntent(ModuleDetailIntent.NeighbourClicked(slug)) },
        )
    }
}

@Composable
private fun Section(title: String) {
    Column(Modifier.padding(top = 12.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        HorizontalDivider(Modifier.padding(top = 4.dp))
    }
}
