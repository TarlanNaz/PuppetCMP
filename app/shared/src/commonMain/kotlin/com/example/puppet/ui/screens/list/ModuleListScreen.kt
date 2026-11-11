package com.example.puppet.ui.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.puppet.resources.Res
import com.example.puppet.resources.nothing_found
import com.example.puppet.resources.search_hint
import com.example.puppet.ui.components.LoadError
import org.jetbrains.compose.resources.stringResource

private const val FOOTER_KEY = "__footer__"

@Composable
fun ModuleListScreen(
    state: ModuleListState,
    onIntent: (ModuleListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        OutlinedTextField(
            value = state.query,
            onValueChange = { onIntent(ModuleListIntent.QueryChanged(it)) },
            label = { Text(stringResource(Res.string.search_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        )
        when (state.status) {
            ListStatus.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            ListStatus.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadError(onRetry = { onIntent(ModuleListIntent.Retry) })
            }

            ListStatus.Content -> if (state.items.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(Res.string.nothing_found))
                }
            } else {
                ModuleList(state = state, onIntent = onIntent)
            }
        }
    }
}

@Composable
private fun ModuleList(
    state: ModuleListState,
    onIntent: (ModuleListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(state.items, key = { it.slug }, contentType = { "module" }) { module ->
            ModuleCard(
                module = module,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(ModuleListIntent.CardClicked(module.slug)) },
            )
        }
        if (state.append != AppendStatus.End) {
            item(key = FOOTER_KEY, contentType = "footer") {
                ListFooter(
                    failed = state.append == AppendStatus.Error,
                    loadedCount = state.items.size,
                    onIntent = onIntent,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

/**
 * Хвост списка. Когда до него долистали, он сам просит следующую страницу;
 * если догрузка упала — показывает ошибку и кнопку повтора.
 */
@Composable
private fun ListFooter(
    failed: Boolean,
    loadedCount: Int,
    onIntent: (ModuleListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
        if (failed) {
            LoadError(onRetry = { onIntent(ModuleListIntent.Retry) })
        } else {
            // Ключ — число загруженных карточек: если страница не заполнила экран
            // и хвост остался на виду, эффект сработает ещё раз и догрузит следующую.
            LaunchedEffect(loadedCount) { onIntent(ModuleListIntent.EndReached) }
            CircularProgressIndicator()
        }
    }
}
