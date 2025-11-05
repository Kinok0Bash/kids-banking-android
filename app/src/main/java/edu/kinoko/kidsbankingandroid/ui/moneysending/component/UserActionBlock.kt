package edu.kinoko.kidsbankingandroid.ui.moneysending.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton
import edu.kinoko.kidsbankingandroid.ui.moneysending.MoneySendingViewModel
import edu.kinoko.kidsbankingandroid.ui.moneysending.component.keyboard.SendingKeyboard

@Composable
fun UserActionBlock(
    vm: MoneySendingViewModel,
    nav: NavHostController,
) {
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
                nav.navigate("${AppRoutes.TRANSACTION_SPLASH}?amount=${vm.amount.value}") {
                    launchSingleTop = true
                }
            },
            enabled = vm.amount.collectAsState().value >= 10
        )
    }
}