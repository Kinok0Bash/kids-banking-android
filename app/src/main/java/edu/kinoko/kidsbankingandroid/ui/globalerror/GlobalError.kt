package edu.kinoko.kidsbankingandroid.ui.globalerror

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.data.store.GlobalErrorStore
import edu.kinoko.kidsbankingandroid.ui.components.Header
import edu.kinoko.kidsbankingandroid.R
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton

@Composable
fun GlobalError(
    retry: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Header(GlobalErrorStore.globalError.header)
                Image(
                    painter = painterResource(R.drawable.some_wrong_logo),
                    contentDescription = "None"
                )
                Text(GlobalErrorStore.globalError.message)
            }
            CustomButton(
                text = "Попробовать еще раз",
                onClick = retry
            )
        }
    }
}