package com.murdeshwar.myrecipe.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.murdeshwar.myrecipe.Dimens.IndicatorSize
import com.murdeshwar.myrecipe.ui.theme.BlueGrayDark
import com.murdeshwar.myrecipe.ui.theme.BlueGrayLight

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pageSize: Int,
    selectedPage: Int,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = if (isSystemInDarkTheme()) BlueGrayDark else BlueGrayLight,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        repeat(times = pageSize) { page ->
            Box(
                modifier = Modifier
                    .size(IndicatorSize)
                    .clip(CircleShape)
                    .background(color = if (page == selectedPage) selectedColor else unselectedColor)
            )
            if (page < pageSize - 1) {
                Spacer(modifier = Modifier.width(4.dp)) // Add space between indicators
            }
        }

    }

}