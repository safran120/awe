package com.safranpulse.pulsechat.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.safranpulse.pulsechat.ui.components.*
import com.safranpulse.pulsechat.ui.theme.PulseChatTheme
import com.safranpulse.pulsechat.ui.theme.SuccessGreen

@Composable
fun ProfileScreen(onLoggedOut: () -> Unit) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        viewModel.avatarUri = uri
    }

    PulseChatTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = "Profile",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    elevation = 0.dp
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        viewModel.avatarUrl.takeIf { it.isNotEmpty() }?.let {
                            AsyncImage(
                                model = viewModel.avatarUri ?: it,
                                contentDescription = "Profile avatar",
                                modifier = Modifier.size(100.dp)
                                    .clip(MaterialTheme.shapes.extraLarge)
                            )
                        } ?: Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                    shape = MaterialTheme.shapes.extraLarge
                                ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "U",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                        OutlinedButton(
                            onClick = { picker.launch("image/*") },
                            modifier = Modifier.padding(top = 12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                contentColor = MaterialTheme.colorScheme.primary,
                                borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                            ),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PingCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
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
                            placeholder = { Text("Add a bio…") },
                            maxLines = 3,
                            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        PingButton(
                            onClick = { viewModel.save() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable(
                                    indication = rememberRipple(bounded = true),
                                    enabled = !viewModel.isLoading
                                ),
                            enabled = !viewModel.isLoading
                        ) {
                            if (viewModel.isLoading) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = if (viewModel.saved) "Saved!" else "Save Changes",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        if (viewModel.saved) {
                            Text(
                                text = "Profile updated successfully",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SuccessGreen,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        viewModel.error?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PingSectionTitle(
                    title = "Account",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                PingCard(
                    modifier = Modifier.padding(horizontal = 16.dp).clip(MaterialTheme.shapes.large)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PingListItem(
                            modifier = Modifier.fillMaxWidth(),
                            leading = {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            headline = {
                                Text(
                                    text = "Log out",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            supporting = {
                                Text(
                                    text = "Sign out of your account",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            trailing = {
                                Icon(
                                    imageVector = Icons.Default.NavigateNext,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                )
                            },
                            onClick = { viewModel.logout(onLoggedOut) }
                        )
                        PingDivider(
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        PingListItem(
                            modifier = Modifier.fillMaxWidth(),
                            leading = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            headline = {
                                Text(
                                    text = "Privacy & Security",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            supporting = {
                                Text(
                                    text = "Manage blocked users and privacy settings",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            trailing = {
                                Icon(
                                    imageVector = Icons.Default.NavigateNext,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
