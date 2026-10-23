package com.safranpulse.pulsechat.ui.screens.auth

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.safranpulse.pulsechat.ui.components.*
import com.safranpulse.pulsechat.ui.theme.PulseChatTheme

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val viewModel: AuthViewModel = hiltViewModel()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        viewModel.avatarUri = uri
    }

    val isCardFocused by remember { mutableStateOf(false) }

    PulseChatTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = if (viewModel.isSignUp) "Join PulseChat" else "Welcome back",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Text(
                    text = if (viewModel.isSignUp) "Create your account to start messaging" else "Sign in to your account",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 24.dp, top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                PingCard(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .clickable(
                            indication = rememberRipple(bounded = false),
                            onClick = { isCardFocused = !isCardFocused }
                        ),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (viewModel.isSignUp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                PingAvatar(
                                    model = viewModel.avatarUri,
                                    size = 72.dp,
                                    contentDescription = "Profile avatar"
                                )
                                OutlinedButton(
                                    onClick = { picker.launch("image/*") },
                                    modifier = Modifier.padding(start = 12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                        contentColor = MaterialTheme.colorScheme.primary,
                                        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                    ),
                                    shape = MaterialTheme.shapes.large
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddAPhoto,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            PingTextField(
                                value = viewModel.displayName,
                                onValueChange = { viewModel.displayName = it },
                                label = "Display Name",
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            PingTextField(
                                value = viewModel.bio,
                                onValueChange = { viewModel.bio = it },
                                label = "Bio",
                                placeholder = { Text("Tell us about yourself") },
                                maxLines = 2,
                                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        PingTextField(
                            value = viewModel.email,
                            onValueChange = { viewModel.email = it },
                            label = "Email address",
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.TextInputType.Email
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        PingTextField(
                            value = viewModel.password,
                            onValueChange = { viewModel.password = it },
                            label = "Password",
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.TextInputType.Password
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (viewModel.isLoading) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            PingButton(
                                onClick = {
                                    if (viewModel.isSignUp) viewModel.signUp(onAuthSuccess)
                                    else viewModel.login(onAuthSuccess)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .clickable(
                                        indication = rememberRipple(bounded = true),
                                        enabled = !viewModel.isLoading
                                    ),
                                enabled = !viewModel.isLoading
                            ) {
                                Text(
                                    text = if (viewModel.isSignUp) "Create Account" else "Sign In",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        TextButton(
                            onClick = { viewModel.isSignUp = !viewModel.isSignUp },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = if (viewModel.isSignUp) "Already have an account? Sign in" else "Don't have an account? Sign up",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                if (viewModel.error != null) {
                    Text(
                        text = viewModel.error,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 24.dp, top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
