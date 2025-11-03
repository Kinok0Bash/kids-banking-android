package edu.kinoko.kidsbankingandroid.ui.childaccount.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.kinoko.kidsbankingandroid.ui.theme.AlfaColor
import edu.kinoko.kidsbankingandroid.ui.theme.Black

@Composable
fun ChildAccountMenuButton(
    onClick: () -> Unit = {}, // TODO убрать после отладки
    icon: ImageVector,
    text: String
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = AlfaColor,
            contentColor = Black
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        contentPadding = PaddingValues(0.dp, 12.dp),
    ) {
        val iconSize = 28.dp
        val sidePadding = 0.dp

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(iconSize),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentSize.provides(0.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier.size(iconSize)
                )
            }
            Spacer(Modifier.size(8.dp))
            Text(
                text = text,
                modifier = Modifier
                    .padding(horizontal = sidePadding),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}