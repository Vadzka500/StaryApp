package com.example.navwithapinothing_2.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    trailingText: String? = null,
    textFieldState: TextFieldState,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isLeadingClick: () -> Unit = {},
    isTrailingClick: () -> Unit = {}
) {



    if (isPassword) {
        PasswordTextField(
            modifier = modifier,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            trailingText = trailingText,
            textFieldState = textFieldState,
            hint = hint,
            keyboardType = keyboardType,
            isLeadingClick = isLeadingClick,
            isTrailingClick = isTrailingClick
        )
    } else {
        TextTextField(
            modifier = modifier,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            trailingText = trailingText,
            textFieldState = textFieldState,
            hint = hint,
            keyboardType = keyboardType,
            isLeadingClick = isLeadingClick,
            isTrailingClick = isTrailingClick
        )
    }

}

@Composable
fun TextTextField(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    trailingText: String? = null,
    textFieldState: TextFieldState,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isLeadingClick: () -> Unit,
    isTrailingClick: () -> Unit
) {
    BasicTextField(
        state = textFieldState,
        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier,
        decorator = { innerText ->
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.clickable { isLeadingClick() })
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (textFieldState.text.isEmpty()) {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        innerText()

                    }

                    if (trailingIcon != null) {
                        Icon(
                            imageVector = trailingIcon, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.clickable { isTrailingClick() }
                        )
                    } else if (trailingText != null) {
                        Text(text = trailingText,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable { isTrailingClick() })
                    }

                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(modifier = Modifier.alpha(0.7f))
            }
        }
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    trailingText: String? = null,
    textFieldState: TextFieldState,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Password,
    isLeadingClick: () -> Unit,
    isTrailingClick: () -> Unit,
    isVisible: Boolean = false
) {
    BasicSecureTextField(
        state = textFieldState,
        textObfuscationMode =
        if (isVisible)
            TextObfuscationMode.Visible
        else
            TextObfuscationMode.Hidden,
        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier,
        decorator = { innerText ->
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.clickable { isLeadingClick() })

                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (textFieldState.text.isEmpty()) {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        innerText()

                    }

                    if (trailingIcon != null) {
                        Icon(
                            imageVector = trailingIcon, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.clickable { isTrailingClick() }
                        )
                    } else if (trailingText != null) {
                        Text(text = trailingText,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable { isTrailingClick() })
                    }

                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(modifier = Modifier.alpha(0.7f))
            }
        }
    )
}