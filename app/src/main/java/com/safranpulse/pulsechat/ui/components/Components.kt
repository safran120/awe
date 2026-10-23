package com.safranpulse.pulsechat.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.safranpulse.pulsechat.ui.theme.*
import coil.compose.AsyncImage

@Composable
fun PingAvatar(
    model: Any?,
    size: Dp = 40.dp,
    contentDescription: String? = null
) {
    val painter = rememberAsyncImagePainter(model = model)
    Box(
        modifier = Modifier
            .size(size)
            .clipToBounds()
            .clip(RoundedCornerShape(size.value / 2))
    ) {
        AsyncImage(
            model = model,
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.graphics.ContentScale.Crop
        )
        if (model == null || painter.state is androidx.compose.ui.graphics.painter.PainterState.Loading) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PingCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    content: @Composable () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .background(
                color = colorScheme.surfaceContainerLow,
                shape = shape
            )
            .border(
                width = 1.dp,
                color = colorScheme.outlineVariant.copy(alpha = 0.3f),
                shape = shape
            ),
        contentAlignment = Alignment.TopStart
    ) {
        content()
    }
}

@Composable
fun PingElevatedCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    elevation: Dp = 1.dp,
    content: @Composable () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val ambientColor = if (colorScheme.isDark) Color(0xFF020617) else Color(0xFF000000)
    Surface(
        modifier = modifier,
        shape = shape,
        tonalElevation = elevation,
        color = colorScheme.surfaceContainerLow,
        shadowColor = ambientColor.copy(alpha = 0.2f)
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = colorScheme.outlineVariant.copy(alpha = 0.2f),
                    shape = shape
                ),
            contentAlignment = Alignment.TopStart
        ) {
            content()
        }
    }
}

@Composable
fun PingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .clickable(
                indication = if (enabled) rememberRipple(bounded = true) else null,
                enabled = enabled
            ),
        enabled = enabled,
        colors = colors,
        shape = MaterialTheme.shapes.large
    ) {
        content()
    }
}

@Composable
fun PingOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .clickable(
                indication = if (enabled) rememberRipple(bounded = true) else null,
                enabled = enabled
            ),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = colorScheme.surfaceContainerLow,
            contentColor = colorScheme.primary,
            disabledContainerColor = colorScheme.onSurfaceVariant.copy(alpha = 0.12f),
            disabledContentColor = colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
        ),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        content()
    }
}

@Composable
fun PingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String?,
    placeholder: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textInputType: androidx.compose.ui.text.input.TextInputType = androidx.compose.ui.text.input.TextInputType.Text,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    singleLine: Boolean = false,
    maxLines: Int = 1,
    keyboardOptions: androidx.compose.ui.text.input.KeyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default,
    isError: Boolean = false,
    errorText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val colorScheme = MaterialTheme.colorScheme
    val textFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = colorScheme.surfaceContainerLowest,
        focusedContainerColor = colorScheme.surfaceContainerLow,
        disabledContainerColor = colorScheme.onSurfaceVariant.copy(alpha = 0.12f),
        cursorColor = colorScheme.primary,
        focusedIndicatorColor = colorScheme.primary,
        unfocusedIndicatorColor = colorScheme.outlineVariant.copy(alpha = 0.4f),
        disabledIndicatorColor = colorScheme.outlineVariant.copy(alpha = 0.12f),
        textColor = colorScheme.onSurface,
        disabledTextColor = colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        labelColor = colorScheme.onSurfaceVariant,
        focusedLabelColor = colorScheme.primary,
        disabledLabelColor = colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        helperTextColor = colorScheme.onSurfaceVariant,
        focusedHelperTextColor = colorScheme.primary,
        disabledHelperTextColor = colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        errorContainerColor = colorScheme.errorContainer,
        focusedErrorContainerColor = colorScheme.errorContainer,
        disabledErrorContainerColor = colorScheme.errorContainer,
        errorTextColor = colorScheme.error,
        focusedErrorTextColor = colorScheme.error,
        disabledErrorTextColor = colorScheme.error,
        placeholderColor = colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        focusedPlaceholderColor = colorScheme.primary.copy(alpha = 0.5f),
        disabledPlaceholderColor = colorScheme.onSurfaceVariant.copy(alpha = 0.24f),
        prefixTextColor = colorScheme.onSurfaceVariant,
        suffixTextColor = colorScheme.onSurfaceVariant,
        leadingIconColor = colorScheme.onSurfaceVariant,
        trailingIconColor = colorScheme.onSurfaceVariant,
        focusedLeadingIconColor = colorScheme.primary,
        focusedTrailingIconColor = colorScheme.primary,
        disabledLeadingIconColor = colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        disabledTrailingIconColor = colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
    )

    var isFocused by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { label?.let { Text(text = it) } },
        placeholder = placeholder,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textInputType = textInputType,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        isError = isError,
        errorText = { errorText?.let { Text(text = it, style = MaterialTheme.typography.labelMedium) } },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = textFieldColors,
        shape = MaterialTheme.shapes.large,
        interactionSource = remember { MutableInteractionSource() },
        onFocusChange = { isFocused = it }
    )
}

@Composable
fun PingDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.divider.copy(alpha = 0.2f)
) {
    Divider(modifier = modifier, thickness = thickness, color = color)
}

@Composable
fun PingSectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PingListItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    leading: @Composable (() -> Unit)? = null,
    headline: @Composable (() -> Unit)? = null,
    supporting: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colorScheme = MaterialTheme.colorScheme

    ListItem(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.large,
        colors = ListItemDefaults.listItemColors(
            containerColor = colorScheme.surfaceContainerLow,
            focusedContainerColor = colorScheme.primaryContainer.copy(alpha = 0.1f),
            pressedContainerColor = colorScheme.primaryContainer.copy(alpha = 0.2f),
            selectedContainerColor = colorScheme.primaryContainer.copy(alpha = 0.15f),
            selectedFocusedContainerColor = colorScheme.primaryContainer.copy(alpha = 0.2f),
            selectedPressedContainerColor = colorScheme.primaryContainer.copy(alpha = 0.25f),
            disabledContainerColor = colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
        ),
        interactionSource = interactionSource,
        leadingContent = leading,
        headlineContent = headline,
        supportingContent = supporting,
        trailingContent = trailing
    )
}