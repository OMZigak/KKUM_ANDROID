package com.teamkkumul.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.teamkkumul.core.designsystem.theme.KkumulAndroidTheme
import com.teamkkumul.model.example.ReqresModel

@Composable
fun ReqresRow(user: ReqresModel) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = user.avatar,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp),
        )
        Column {
            Text(text = user.firstName + user.lastName)
            Text(text = user.email)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    KkumulAndroidTheme {
        ReqresRow(
            user = ReqresModel(
                1,
                "first",
                "last",
                "email",
                "avatar",
            ),
        )
    }
}
