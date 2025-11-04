package edu.kinoko.kidsbankingandroid.ui.moneysending.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton
import edu.kinoko.kidsbankingandroid.ui.moneysending.MoneySendingViewModel
import edu.kinoko.kidsbankingandroid.ui.moneysending.component.keyboard.SendingKeyboard
import kotlinx.coroutines.launch

@Composable
fun UserActionBlock(
    vm: MoneySendingViewModel,
    nav: NavHostController,
) {
    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        KidName(UserStore.userData.childName ?: "ОШИБКА")
        SendingKeyboard(
            onDigit = { d ->
                val exceeded = vm.inputAppend(d)
                if (exceeded) {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                } else {
                    haptics.performHapticFeedback(HapticFeedbackType.VirtualKey)
                }
            },
            onBackspace = {
                vm.inputBackspace()
                haptics.performHapticFeedback(HapticFeedbackType.VirtualKey)
            },
            onClear = {
                vm.inputClear()
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )
        CustomButton(
            "Перевести",
            onClick = {
                scope.launch {
                    val route = vm.sendTransaction()
                    Log.d("TRANSACTION ROUTE", route)
                    nav.navigate(route) {
                        popUpTo(nav.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}