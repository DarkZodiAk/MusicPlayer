@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.musicplayer.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicplayer.presentation.home.components.PlayerBar
import com.example.musicplayer.presentation.playlists.PlaylistsScreenRoot
import com.example.musicplayer.presentation.songs.SongsScreenRoot
import kotlinx.coroutines.launch

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenPlayer: () -> Unit,
    onOpenSearch: () -> Unit,
    songsOnOpenPlayer: () -> Unit,
    playlistsOnPlaylistClick: (Long) -> Unit,
) {
    HomeScreen(
        state = viewModel.state,
        songsOnOpenPlayer = songsOnOpenPlayer,
        playlistsOnPlaylistClick = playlistsOnPlaylistClick,
        onAction = { action ->
            when(action) {
                HomeAction.OnPlayerBarClick -> onOpenPlayer()
                HomeAction.OnSearchButtonClick -> onOpenSearch()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    songsOnOpenPlayer: () -> Unit,
    playlistsOnPlaylistClick: (Long) -> Unit,
    onAction: (HomeAction) -> Unit
) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState(pageCount = { TabItem.entries.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Compose Player", fontSize = 20.sp)
                },
                actions = {
                    IconButton(
                        onClick = { onAction(HomeAction.OnSearchButtonClick) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .height(40.dp)
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                TabItem.entries.forEachIndexed { index, tabItem ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                            selectedTabIndex = index
                        },
                        text = {
                            Text(text = tabItem.title)
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                when (index) {
                    TabItem.SONGS.ordinal -> SongsScreenRoot(onOpenPlayer = songsOnOpenPlayer)
                    TabItem.PLAYLISTS.ordinal -> PlaylistsScreenRoot(onPlaylistClick = playlistsOnPlaylistClick)
                }
            }

            state.playingSong?.let {
                PlayerBar(
                    song = state.playingSong,
                    isPlaying = state.isPlaying,
                    currentProgress = state.currentProgress.toFloat(),
                    songDuration = state.playingSong.duration.toFloat(),
                    onClick = { onAction(HomeAction.OnPlayerBarClick) },
                    onPlayPauseClick = { onAction(HomeAction.OnPlayPauseClick) }
                )
            }
        }
    }
}

enum class TabItem(val title: String) {
    SONGS("Песни"), PLAYLISTS("Плейлисты")
}