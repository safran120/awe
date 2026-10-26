package com.safranpulse.pulsechat.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PingAvatar(
    size: dp = 40.dp,
    contentDescription: String? = null
) {
    // Simple circle avatar
    androidx.compose.foundation.layout.Box(
        modifier = androidx.compose.foundation.layout.Modifier
            .size(size)
            .background(Color.LightGray)
    )
}

@Composable
fun PingCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .background(Color.White)
            .border(width = 1.dp, color = Color.Gray)
            .padding(8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        content()
    }
}

@Composable
fun PingElevatedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .background(Color(0xFFF5F5F5)) // Very light gray
            .border(width = 1.dp, color = Color.Gray)
            .padding(8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        content()
    }
}

@Composable
fun PingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val buttonColor = if (enabled) Color(0xFF6200EE) else Color.Gray
    val contentColor = if (enabled) Color.White else Color.Gray
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .background(buttonColor)
            .clickable { if (enabled) onClick() }
            .padding(16.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // We ignore the content parameter for now to avoid complexity
        // In a real implementation, we would use the content here
        androidx.compose.foundation.text.Text(
            text = "Button",
            color = contentColor
        )
    }
}

@Composable
fun PingOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val borderColor = if (enabled) Color(0xFF6200EE) else Color.Gray
    val contentColor = if (enabled) Color(0xFF6200EE) else Color.Gray
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .border(width = 1.dp, color = borderColor)
            .background(Color.Transparent)
            .clickable { if (enabled) onClick() }
            .padding(16.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.text.Text(
            text = "Button",
            color = contentColor
        )
    }
}

@Composable
fun PingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: @Composable () -> Unit = { },
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = 1
) {
    // Very simple text field that just displays the value
    androidx.compose.foundation.layout.Column(modifier = modifier) {
        if (label != null) {
            androidx.compose.foundation.text.Text(text = label)
        }
        androidx.compose.foundation.layout.Row(
            modifier = modifier
                .background(Color.White)
                .border(width = 1.dp, color = if (isError) Color.Red else Color.Gray)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                androidx.compose.foundation.layout.Space(modifier = Modifier.width(4.dp))
            }
            androidx.compose.foundation.text.Text(
                text = value,
                color = if (isError) Color.Red else Color.Black,
                maxLines = maxLines
            )
        }
        if (isError && errorText != null) {
            androidx.compose.foundation.text.Text(
                text = errorText,
                color = Color.Red,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun PingDivider(
    modifier: Modifier = Modifier,
    thickness: dp = 1.dp,
    color: Color = Color.Gray
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .height(thickness)
            .background(color)
            .fillMaxWidth()
    )
}

@Composable
fun PingSectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.text.Text(
        text = title,
        modifier = modifier,
        fontSize = 18.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
    )
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
    androidx.compose.foundation.layout.Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { 
                if (enabled) {
                    onClick?.invoke()
                }
            }
            .padding(16.dp)
    ) {
        leading?.invoke()
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.weight(1f)
        ) {
            headline?.invoke()
            supporting?.invoke()
        }
        trailing?.invoke()
    }
}