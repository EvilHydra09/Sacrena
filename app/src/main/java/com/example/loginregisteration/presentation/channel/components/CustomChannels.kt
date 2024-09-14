package com.example.loginregisteration.presentation.channel.components



import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.handlers.LoadMoreHandler
import io.getstream.chat.android.compose.state.channels.list.ChannelsState
import io.getstream.chat.android.compose.state.channels.list.ItemState
import io.getstream.chat.android.compose.ui.components.LoadingFooter


@Composable
public fun Channels(
    channelsState: ChannelsState,
    lazyListState: LazyListState,
    onLastItemReached: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    helperContent: @Composable BoxScope.() -> Unit = {},
    loadingMoreContent: @Composable () -> Unit = { DefaultChannelsLoadingMoreIndicator() },
    itemContent: @Composable (ItemState) -> Unit,
    divider: @Composable () -> Unit,
) {
    val (_, isLoadingMore, endOfChannels, channelItems) = channelsState

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("Stream_Channels"),
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = contentPadding,
        ) {
            item {
                DummyFirstChannelItem()
            }

            items(
                items = channelItems,
                key = { it.key },
            ) { item ->
                itemContent(item)

                divider()
            }

            if (isLoadingMore) {
                item {
                    loadingMoreContent()
                }
            }
        }

        if (!endOfChannels && channelItems.isNotEmpty()) {
            LoadMoreHandler(lazyListState) {
                onLastItemReached()
            }
        }

        helperContent()
    }
}

/**
 * The default loading more indicator.
 */
@Composable
internal fun DefaultChannelsLoadingMoreIndicator() {
    LoadingFooter(modifier = Modifier.fillMaxWidth())
}

/**
 * Represents an almost invisible dummy item to be added to the top of the list.
 *
 * If the list is scrolled to the top and a channel new item is added or moved
 * to the position above, then the list will automatically autoscroll to it.
 */
@Composable
private fun DummyFirstChannelItem() {
    Box(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
    )
}
