package edu.kinoko.kidsbankingandroid.ui.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import edu.kinoko.kidsbankingandroid.data.dto.FieldConfig

@Composable
fun DynamicForm(
    fields: List<FieldConfig>,
    values: Map<String, String>,
    errors: Map<String, String?> = emptyMap(),
    touched: Set<String> = emptySet(),
    onValueChange: (String, String) -> Unit,
    onLastImeAction: () -> Unit = {},
) {
    val focusers = remember(fields) { fields.map { FocusRequester() } }

    Column {
        fields.forEachIndexed { i, field ->
            val last = i == fields.lastIndex
            FormInput(
                value = values[field.key] ?: "",
                onValueChange = { onValueChange(field.key, it) },
                label = field.placeholder,
                isDate = field.isDate,
                isPassword = field.isPassword,
                isChangeble = field.isChangeble,
                errorText = if (touched.contains(field.key)) {
                    errors[field.key]
                } else {
                    null
                },
                focusRequester = focusers[i],
                imeAction = if (last) {
                    ImeAction.Done
                } else {
                    ImeAction.Next
                },
                onImeAction = {
                    if (last) onLastImeAction() else focusers[i + 1].requestFocus()
                }
            )
        }
    }
}
