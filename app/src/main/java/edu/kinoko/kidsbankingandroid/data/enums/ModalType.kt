package edu.kinoko.kidsbankingandroid.data.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import edu.kinoko.kidsbankingandroid.ui.theme.ErrorBackground
import edu.kinoko.kidsbankingandroid.ui.theme.ErrorIcon
import edu.kinoko.kidsbankingandroid.ui.theme.InfoBackground
import edu.kinoko.kidsbankingandroid.ui.theme.InfoIcon
import edu.kinoko.kidsbankingandroid.ui.theme.SuccessBackground
import edu.kinoko.kidsbankingandroid.ui.theme.SuccessIcon
import edu.kinoko.kidsbankingandroid.ui.theme.WarnBackground
import edu.kinoko.kidsbankingandroid.ui.theme.WarnIcon

enum class ModalType(
    val icon: ImageVector,
    val iconColor: Color,
    val backgroundColor: Color,
) {

    INFO(
        icon = Icons.Default.Info,
        iconColor = InfoIcon,
        backgroundColor = InfoBackground,
    ),

    SUCCESSFUL(
        icon = Icons.Default.CheckCircle,
        iconColor = SuccessIcon,
        backgroundColor = SuccessBackground,
    ),

    ERROR(
        icon = Icons.Default.Cancel,
        iconColor = ErrorIcon,
        backgroundColor = ErrorBackground,
    ),

    WARN(
        icon = Icons.Default.Info,
        iconColor = WarnIcon,
        backgroundColor = WarnBackground,
    ),
}