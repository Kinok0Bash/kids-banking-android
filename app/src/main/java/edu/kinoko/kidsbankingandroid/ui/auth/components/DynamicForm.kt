package edu.kinoko.kidsbankingandroid.ui.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.data.dto.config.FieldConfig

@Composable
fun DynamicForm(
    fields: List<FieldConfig>,
    values: Map<String, String>,
    onValueChange: (String, String) -> Unit // key, newValue
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        fields.forEach { field ->
            FormInput(
                value = values[field.key] ?: "",
                onValueChange = { onValueChange(field.key, it) },
                placeholder = field.placeholder,
                isPassword = field.isPassword
            )
        }
    }
}

