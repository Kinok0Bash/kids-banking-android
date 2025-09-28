package edu.kinoko.kidsbankingandroid.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.data.enums.ModalType
import edu.kinoko.kidsbankingandroid.ui.components.Header
import edu.kinoko.kidsbankingandroid.ui.components.Modal
import edu.kinoko.kidsbankingandroid.ui.theme.ButtonRed
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isBusy by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Header("Здесь будет главная страница")
            }
            Spacer(Modifier.size(10.dp))
            Button(
                onClick = {
                    if (!isBusy) {
                        isBusy = true
                        scope.launch {
                            onLogout()
                            isBusy = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !isBusy
            ) {
                Text("Выйти")
            }
            Spacer(Modifier.size(50.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Modal(
                    text = "Тест модалки с состоянием: INFO",
                    modalType = ModalType.INFO
                )
                Modal(
                    text = "Тест модалки с состоянием: SUCCESSFUL",
                    modalType = ModalType.SUCCESSFUL
                )
                Modal(
                    text = "Тест модалки с состоянием: WARN",
                    modalType = ModalType.WARN
                )
                Modal(
                    text = "Тест модалки с состоянием: ERROR",
                    modalType = ModalType.ERROR
                )
                Spacer(Modifier.size(50.dp))
                Modal(
                    text = "Тест отображения длинного текста внутри модалки на случай если с сервера придет длинный ответ",
                    modalType = ModalType.INFO
                )
            }
        }
    }
}