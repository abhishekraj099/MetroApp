package com.example.metro.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metro.R
import com.example.metro.ui.theme.DividerColor
import com.example.metro.ui.theme.IndigoBlue
import com.example.metro.ui.theme.LoraFont
import com.example.metro.ui.theme.OutfitFont
import com.example.metro.ui.theme.Parchment
import com.example.metro.ui.theme.SurfaceWhite
import com.example.metro.ui.theme.TextDark
import com.example.metro.ui.theme.TextLight
import com.example.metro.ui.theme.TextMedium
import com.example.metro.ui.theme.VermilionRed

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLoggedOut: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory(context)
    )
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Parchment)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header ──────────────────────────────────────────────────────
            SettingsHeader(onBack = onBack)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── User Profile Card ───────────────────────────────────────
                UserProfileCard(email = state.userEmail)

                // ── General Section ─────────────────────────────────────────
                SettingsSectionHeader("General")
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Outlined.Share,
                        title = "Share App",
                        subtitle = "Tell your friends about Patna Metro",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Check out Patna Metro – your Patna Metro companion app! 🚇\nhttps://play.google.com/store/apps/details?id=com.example.metro"
                                )
                            }
                            context.startActivity(Intent.createChooser(intent, "Share via"))
                        }
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Outlined.BugReport,
                        title = "Report a Bug",
                        subtitle = "Help us improve the app",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:namikazecompose@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Patna Metro Bug Report")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}\nAndroid: ${android.os.Build.VERSION.RELEASE}\n\nDescribe the bug:\n"
                                )
                            }
                            context.startActivity(intent)
                        }
                    )
                }

                // ── Feedback Section ────────────────────────────────────────
                SettingsSectionHeader("Feedback")
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Outlined.Feedback,
                        title = "Send Feedback",
                        subtitle = "Tell us what you think",
                        onClick = viewModel::openFeedbackDialog
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Filled.Star,
                        iconTint = Color(0xFFFFC107),
                        title = "Rate on Play Store",
                        subtitle = "If you enjoy Patna Metro, please rate us",
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.example.metro")
                            )
                            context.startActivity(intent)
                        }
                    )
                }

                // ── About Section ───────────────────────────────────────────
                SettingsSectionHeader("About")
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Outlined.Info,
                        title = "App Version",
                        subtitle = "1.0.0 (Build 1)",
                        onClick = {}
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Outlined.Description,
                        title = "Terms of Service",
                        subtitle = "Read our terms",
                        onClick = {}
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Outlined.PrivacyTip,
                        title = "Privacy Policy",
                        subtitle = "How we handle your data",
                        onClick = {}
                    )
                    SettingsDivider()
                    SettingsItem(
                        icon = Icons.Outlined.Code,
                        title = "Developer",
                        subtitle = "Built with ❤\uFE0F for Patna Metro riders",
                        onClick = {}
                    )
                }

                // ── Contact Section ─────────────────────────────────────────
                SettingsSectionHeader("Contact Us")
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Outlined.Mail,
                        title = "Email Support",
                        subtitle = "namikazecompose@gmail.com",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:namikazecompose@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Patna Metro App – Support Request")
                            }
                            context.startActivity(intent)
                        }
                    )
                }

                // ── Unofficial Disclaimer ───────────────────────────────────
                DisclaimerCard()

                // ── Logout Button ───────────────────────────────────────────
                LogoutButton(onClick = viewModel::showLogoutConfirmation)

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    // ── Feedback Dialog ─────────────────────────────────────────────────────
    if (state.showFeedbackDialog) {
        FeedbackDialog(
            message = state.feedbackMessage,
            rating = state.feedbackRating,
            isSending = state.isSendingFeedback,
            feedbackSent = state.feedbackSent,
            error = state.feedbackError,
            onMessageChange = viewModel::onFeedbackMessageChange,
            onRatingChange = viewModel::onFeedbackRatingChange,
            onSubmit = viewModel::submitFeedback,
            onDismiss = viewModel::dismissFeedbackDialog
        )
    }

    // ── Logout Confirmation Dialog ──────────────────────────────────────────
    if (state.showLogoutConfirm) {
        LogoutConfirmDialog(
            isLoading = state.isLoggingOut,
            onConfirm = { viewModel.logout(onLoggedOut) },
            onDismiss = viewModel::dismissLogoutConfirmation
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// COMPONENTS
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun SettingsHeader(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.newborder),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Parchment.copy(alpha = 0.85f))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextDark
                )
            }
            Spacer(Modifier.width(4.dp))
            Text(
                "Settings",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = LoraFont,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    fontSize = 22.sp
                )
            )
        }
    }
}

@Composable
private fun UserProfileCard(email: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(IndigoBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = IndigoBlue,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                )
                Text(
                    text = email.ifBlank { "Not logged in" },
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMedium),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(
            fontFamily = OutfitFont,
            fontWeight = FontWeight.SemiBold,
            color = IndigoBlue,
            fontSize = 13.sp,
            letterSpacing = 1.sp
        ),
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconTint: Color = IndigoBlue,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = TextDark
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = TextLight),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = DividerColor.copy(alpha = 0.5f)
    )
}

@Composable
private fun DisclaimerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = VermilionRed.copy(alpha = 0.06f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                tint = VermilionRed,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Unofficial App",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = VermilionRed
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Patna Metro is an unofficial companion app for Patna Metro. " +
                            "This app is not affiliated with, endorsed by, or associated " +
                            "with Patna Metro Rail Corporation (PMRC) or the Government " +
                            "of Bihar in any way. All metro data is sourced from publicly " +
                            "available information and may not reflect real-time changes. " +
                            "Please verify critical information from official sources.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMedium,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = VermilionRed
        ),
        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
            brush = androidx.compose.ui.graphics.SolidColor(VermilionRed.copy(alpha = 0.5f))
        )
    ) {
        Icon(
            Icons.AutoMirrored.Filled.Logout,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "Log Out",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DIALOGS
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun FeedbackDialog(
    message: String,
    rating: Int,
    isSending: Boolean,
    feedbackSent: Boolean,
    error: String,
    onMessageChange: (String) -> Unit,
    onRatingChange: (Int) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = SurfaceWhite,
        title = {
            if (feedbackSent) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Thank You!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                }
            } else {
                Text(
                    "Send Feedback",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )
            }
        },
        text = {
            if (feedbackSent) {
                Text(
                    "Your feedback has been submitted successfully. We appreciate your input!",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextMedium)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "How would you rate your experience?",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMedium)
                    )

                    // Star rating
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..5) {
                            IconButton(onClick = { onRatingChange(i) }) {
                                Icon(
                                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                                    contentDescription = "Star $i",
                                    tint = if (i <= rating) Color(0xFFFFC107) else TextLight,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

                    // Feedback text
                    OutlinedTextField(
                        value = message,
                        onValueChange = onMessageChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = {
                            Text(
                                "Tell us what you think...",
                                color = TextLight
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IndigoBlue,
                            unfocusedBorderColor = DividerColor,
                            cursorColor = IndigoBlue,
                            focusedContainerColor = Parchment.copy(alpha = 0.5f)
                        ),
                        enabled = !isSending
                    )

                    // Error
                    AnimatedVisibility(
                        visible = error.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = VermilionRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                error,
                                style = MaterialTheme.typography.bodySmall.copy(color = VermilionRed)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (feedbackSent) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoBlue)
                ) {
                    Text("Done")
                }
            } else {
                Button(
                    onClick = onSubmit,
                    enabled = !isSending,
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoBlue)
                ) {
                    if (isSending) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Submit")
                    }
                }
            }
        },
        dismissButton = {
            if (!feedbackSent) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = TextMedium)
                }
            }
        }
    )
}

@Composable
private fun LogoutConfirmDialog(
    isLoading: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        shape = RoundedCornerShape(20.dp),
        containerColor = SurfaceWhite,
        icon = {
            Icon(
                Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                tint = VermilionRed,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                "Log Out?",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                "You will need to log in again to access the app. Your saved routes and preferences will remain on this device.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMedium),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = VermilionRed)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Log Out")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancel", color = TextMedium)
            }
        }
    )
}

