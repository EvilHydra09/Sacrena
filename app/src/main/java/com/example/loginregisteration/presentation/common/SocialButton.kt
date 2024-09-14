package com.example.loginregisteration.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loginregisteration.ui.theme.AppTheme

@Composable
fun SocialButton(modifier: Modifier = Modifier,iconResId: Int, onButtonClick: () -> Unit? = {},) {
    Card(
        onClick = { onButtonClick.invoke() },
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .height(50.dp)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = Color.Unspecified // Ensures the icon uses its original color
            )
        }
    }

}

@Preview
@Composable
private fun SocialButtonPreview() {
    AppTheme {
        SocialButton(iconResId = com.example.loginregisteration.R.drawable.facebook_icon)
    }

}