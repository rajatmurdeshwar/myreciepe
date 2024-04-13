package com.example.myrecipes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.myrecipes.ui.theme.MyRecipesTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailTopAppBar(titleName:String,onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = titleName)
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.menu_back))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Preview
@Composable
private fun RecipeDetailTopAppBarPreview() {
    MyRecipesTheme {
        Surface {
            RecipeDetailTopAppBar("",{ })
        }
    }
}