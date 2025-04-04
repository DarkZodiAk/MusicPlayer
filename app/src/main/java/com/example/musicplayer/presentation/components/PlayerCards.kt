package com.example.musicplayer.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
@Stable
fun PlayerCard(
    title: String,
    subtitle: String,
    imageUri: String,
    titleColor: Color = Color.Unspecified,
    onClick: () -> Unit,
    action: @Composable (BoxScope.() -> Unit)? = null,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(60.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 8.dp)

    ) {
        Row(modifier = Modifier.weight(8f).fillMaxHeight()) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = titleColor
                )
                if(subtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        action?.let {
            Box(modifier = Modifier.weight(1f)) {
                action()
            }
        }
    }
}

@Composable
@Stable
fun SongCard(
    title: String,
    artistName: String,
    albumArtUri: String,
    isPlaying: Boolean,
    onClick: () -> Unit,
    action: @Composable (BoxScope.() -> Unit)? = null,
    modifier: Modifier
) {
    PlayerCard(
        title = title,
        subtitle = artistName,
        imageUri = albumArtUri,
        titleColor = if(isPlaying) MaterialTheme.colorScheme.primary else Color.Unspecified,
        onClick = onClick,
        action = action,
        modifier = modifier
    )
}

@Composable
@Stable
fun PlaylistCard(
    name: String,
    songsCount: String,
    imageUri: String,
    onClick: () -> Unit,
    action: @Composable (BoxScope.() -> Unit)? = null,
    modifier: Modifier
) {
    PlayerCard(
        title = name,
        subtitle = songsCount,
        imageUri = imageUri,
        onClick = onClick,
        action = action,
        modifier = modifier
    )
}

@Composable
@Stable
fun FolderCard(
    name: String,
    imageUri: String,
    onClick: () -> Unit,
    action: @Composable (BoxScope.() -> Unit)? = null,
    modifier: Modifier
) {

    PlayerCard(
        title = name,
        subtitle = "",
        imageUri = imageUri,
        onClick = onClick,
        action = action,
        modifier = modifier
    )
}