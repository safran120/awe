package com.safranpulse.pulsechat.ui.screens.contacts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.safranpulse.pulsechat.navigation.Screen
import com.safranpulse.pulsechat.ui.components.*
import com.safranpulse.pulsechat.ui.theme.PulseChatTheme

@Composable
fun ContactsScreen(navController: NavHostController? = null) {
    val viewModel: ContactsViewModel = hiltViewModel()
    val callViewModel: com.safranpulse.pulsechat.ui.screens.call.CallViewModel = hiltViewModel()
    var showBlocked by remember { mutableStateOf(false) }

    PulseChatTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = "Contacts",
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(52.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = viewModel.query,
                        onValueChange = { viewModel.query = it },
                        placeholder = { Text("Search contacts") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.weight(1f)
                    )
                    PingButton(
                        onClick = { viewModel.search() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                if (viewModel.searchResults.isNotEmpty()) {
                    PingSectionTitle(
                        title = "Search results",
                        modifier = Modifier.padding(horizontal = 16.dp, top = 16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(viewModel.searchResults) { user ->
                            PingListItem(
                                modifier = Modifier.fillMaxWidth(),
                                leading = {
                                    PingAvatar(
                                        model = if (user.avatarUrl.isNotEmpty()) user.avatarUrl else null,
                                        size = 48.dp,
                                        contentDescription = "User avatar"
                                    )
                                },
                                headline = {
                                    Text(
                                        text = user.displayName.ifBlank { user.username },
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                supporting = {
                                    Text(
                                        text = user.email,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                trailing = {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        if (viewModel.contacts.any { it.uid == user.uid }) {
                                            PingOutlinedButton(
                                                onClick = { viewModel.removeContact(user.uid) },
                                                modifier = Modifier.height(36.dp)
                                            ) {
                                                Text(
                                                    text = "Remove",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                            PingOutlinedButton(
                                                onClick = { viewModel.blockUser(user.uid) },
                                                modifier = Modifier.height(36.dp)
                                            ) {
                                                Text(
                                                    text = "Block",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        } else {
                                            PingButton(
                                                onClick = { viewModel.addContact(user.uid) },
                                                modifier = Modifier.height(36.dp).width(72.dp),
                                                enabled = true
                                            ) {
                                                Text(
                                                    text = "Add",
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = MaterialTheme.colorScheme.onPrimary
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                            PingDivider(
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "My Contacts",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, top = 24.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(viewModel.contacts) { user ->
                        PingListItem(
                            modifier = Modifier.fillMaxWidth(),
                            leading = {
                                PingAvatar(
                                    model = if (user.avatarUrl.isNotEmpty()) user.avatarUrl else null,
                                    size = 48.dp,
                                    contentDescription = "Contact avatar"
                                )
                            },
                            headline = {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = user.displayName.ifBlank { user.username },
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = user.status,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (user.status == "Online") {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                        }
                                    )
                                }
                            },
                            trailing = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    TextButton(
                                        onClick = {
                                            callViewModel.startCall(user.uid) { id ->
                                                navController?.navigate(Screen.Call.createRoute(id))
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Call,
                                            contentDescription = "Call",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    TextButton(
                                        onClick = { viewModel.removeContact(user.uid) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove contact",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                    TextButton(
                                        onClick = { viewModel.blockUser(user.uid) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Block,
                                            contentDescription = "Block user",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        )
                        PingDivider(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                TextButton(
                    onClick = { showBlocked = !showBlocked },
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = if (showBlocked) "Hide Blocked Users" else "Show Blocked Users",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (showBlocked) {
                    PingSectionTitle(
                        title = "Blocked Users",
                        modifier = Modifier.padding(horizontal = 16.dp, top = 12.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(viewModel.blockedUsers) { user ->
                            PingListItem(
                                modifier = Modifier.fillMaxWidth(),
                                leading = {
                                    PingAvatar(
                                        model = if (user.avatarUrl.isNotEmpty()) user.avatarUrl else null,
                                        size = 48.dp,
                                        contentDescription = "Blocked user avatar"
                                    )
                                },
                                headline = {
                                    Text(
                                        text = user.displayName.ifBlank { user.username },
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                supporting = {
                                    Text(
                                        text = user.email,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                trailing = {
                                    PingOutlinedButton(
                                        onClick = { viewModel.unblockUser(user.uid) },
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        Text(
                                            text = "Unblock",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            )
                            PingDivider(
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
