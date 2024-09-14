package com.example.loginregisteration.presentation.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.loginregisteration.R
import com.example.loginregisteration.ui.theme.AppTheme

import io.getstream.chat.android.models.User

@Composable
 fun ChannelImage(url: String, status: Boolean) {
    // We use coil for getting the images
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(durationMillis = 500)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.placeholder),
            fallback = painterResource(id = R.drawable.placeholder),
            placeholder = painterResource(id = R.drawable.placeholder),
        )
        Box(
            modifier = Modifier
                .padding(bottom = 5.dp)
                .size(10.dp)
                .align(Alignment.BottomStart)
                .clip(CircleShape)
                .background(
                    if (status) MaterialTheme.colorScheme.primary else Color.Transparent
                )
        )

    }

}

@Composable
private fun Error(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}
@Composable
private fun CustomChannelListItem(user: User, onChannelClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChannelClick(user.id) }
            .padding(all = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChannelImage(user.image, status = user.online)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = if (user.name != "") user.name else "Channel", fontWeight = FontWeight.Bold)
        }

    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        placeholder = { Text("Search users") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun UsersList(users: List<User>,onChannelClick: (String) -> Unit) {
    if (users.isEmpty()) {
        Error(message = "No users found")
        return
    }
    LazyColumn {
        items(users) { user ->
            CustomChannelListItem(user = user) {
                onChannelClick.invoke(it)
            }
        }
    }
}

@Preview
@Composable
private fun ChaneelListItemPreview() {
    AppTheme {
        ChannelImage(url = "", status = true)
    }

    
}


