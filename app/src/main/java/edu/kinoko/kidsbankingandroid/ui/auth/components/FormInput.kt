package edu.kinoko.kidsbankingandroid.ui.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.ui.auth.utils.DateVisualTransformation

@Composable
fun FormInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isDate: Boolean = false,
    isPassword: Boolean = false,
    isChangeble: Boolean = isPassword,
    errorText: String? = null,
    focusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: () -> Unit = {},
) {
    var isVisible by remember { mutableStateOf(!isPassword) }

    OutlinedTextField(
        label = { Text(label) },
        placeholder = { if (isDate) Text("dd.MM.yyyy") },
        value = value,
        onValueChange = { new ->
            val next = if (isDate) new.filter(Char::isDigit).take(8) else new
            onValueChange(next)
        },
        modifier = modifier
            .fillMaxWidth()
            .let { m -> focusRequester?.let { m.focusRequester(it) } ?: m },
        shape = RoundedCornerShape(16.dp),
        visualTransformation = when {
            isDate -> DateVisualTransformation()
            isPassword && !isVisible -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        },
        keyboardOptions = when {
            isDate -> KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeAction
            )

            isPassword -> KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            )

            else -> KeyboardOptions(
                imeAction = imeAction
            )
        },
        keyboardActions = KeyboardActions(
            onNext = { onImeAction() },
            onDone = { onImeAction() }
        ),
        singleLine = true,
        isError = errorText != null,
        supportingText = {
            if (errorText != null) {
                Text(errorText)
            }
        },
        trailingIcon = {
            if (isChangeble) {
                Icon(
                    imageVector = if (isVisible) {
                        Icons.Default.VisibilityOff
                    } else {
                        Icons.Default.Visibility
                    },
                    contentDescription = if (isVisible) {
                        "Скрыть пароль"
                    } else {
                        "Показать пароль"
                    },
                    modifier = Modifier.clickable { isVisible = !isVisible }
                )
            }
        }
    )
}
