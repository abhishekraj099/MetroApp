package com.example.metro.presentation.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.metro.data.local.SessionDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {

    // ── Colors ─────────────────────────────────────────────────────────────
    val darkBackground   = Color(0xFF0A0A0A)
    val cardBackground   = Color(0xFF1A1A1A)
    val accentColor      = Color(0xFF00D9FF)
    val textWhite        = Color.White
    val textGray         = Color(0xFF888888)
    val inputBackground  = Color(0xFF1E1E1E)
    val borderColor      = Color(0xFF333333)
    val highlightColor   = accentColor.copy(alpha = 0.15f)

    // ── ViewModel ──────────────────────────────────────────────────────────
    val context = LocalContext.current
    val sessionDataStore = remember { SessionDataStore(context) }
    val vm: LoginViewModel = viewModel(factory = LoginViewModel.Factory(sessionDataStore))
    val state by vm.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    // Navigate on login
    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            delay(400)
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // ── Entrance animations ────────────────────────────────────────────────
    val logoScale    = remember { Animatable(0.5f) }
    val contentAlpha = remember { Animatable(0f) }
    val titleOffset  = remember { Animatable(-50f) }

    LaunchedEffect(Unit) {
        logoScale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
        titleOffset.animateTo(0f, tween(500, easing = FastOutSlowInEasing))
        contentAlpha.animateTo(1f, tween(600))
    }

    // ── Google button press animation ──────────────────────────────────────
    var isGooglePressed by remember { mutableStateOf(false) }
    val googleScale  by animateFloatAsState(if (isGooglePressed) 0.95f else 1f,
        spring(Spring.DampingRatioMediumBouncy), label = "googleScale")
    val googleBorder by animateColorAsState(if (isGooglePressed) accentColor else borderColor,
        tween(150), label = "googleBorder")
    val googleBg     by animateColorAsState(if (isGooglePressed) highlightColor else Color.Transparent,
        tween(150), label = "googleBg")

    // ── Email field focus ──────────────────────────────────────────────────
    var isEmailFocused by remember { mutableStateOf(false) }
    val emailBorder  by animateColorAsState(if (isEmailFocused) accentColor else borderColor,
        tween(200), label = "emailBorder")
    val emailBg      by animateColorAsState(if (isEmailFocused) highlightColor else inputBackground,
        tween(200), label = "emailBg")
    val emailElevation by animateDpAsState(if (isEmailFocused) 8.dp else 0.dp,
        tween(200), label = "emailElevation")

    // ── OTP field focus ────────────────────────────────────────────────────
    var isOtpFocused by remember { mutableStateOf(false) }
    val otpBg        by animateColorAsState(if (isOtpFocused) highlightColor else inputBackground,
        tween(200), label = "otpBg")

    // ── Login button press ─────────────────────────────────────────────────
    var isLoginPressed by remember { mutableStateOf(false) }
    val loginScale   by animateFloatAsState(if (isLoginPressed) 0.95f else 1f,
        spring(Spring.DampingRatioMediumBouncy), label = "loginScale")

    // ── Layout ─────────────────────────────────────────────────────────────
    Box(
        Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // Title
            Text(
                "Welcome Back",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = textWhite,
                    fontSize = 36.sp,
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier
                    .scale(logoScale.value)
                    .offset(y = titleOffset.value.dp)
                    .alpha(contentAlpha.value)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Dominate competitive group discussions and win big.",
                style = MaterialTheme.typography.bodyMedium.copy(color = textGray),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(contentAlpha.value)
            )

            Spacer(Modifier.height(32.dp))

            // ── Google Button ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .scale(googleScale)
                    .clip(RoundedCornerShape(28.dp))
                    .background(googleBg)
                    .border(1.dp, googleBorder, RoundedCornerShape(28.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        scope.launch {
                            isGooglePressed = true
                            delay(100)
                            isGooglePressed = false
                            Toast.makeText(context, "Coming Soon!", Toast.LENGTH_SHORT).show()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null,
                        tint = textWhite, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Text("Continue with Google", color = textWhite, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── OR EMAIL Divider ───────────────────────────────────────────
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(Modifier.weight(1f), color = borderColor)
                Text("  OR EMAIL  ", color = textGray, style = MaterialTheme.typography.bodySmall)
                HorizontalDivider(Modifier.weight(1f), color = borderColor)
            }

            Spacer(Modifier.height(24.dp))

            // ── Email Field ────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(emailElevation, RoundedCornerShape(12.dp),
                        ambientColor = accentColor.copy(alpha = 0.3f),
                        spotColor = accentColor.copy(alpha = 0.3f))
            ) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = vm::onEmailChange,
                    label = { Text("Email Address", color = if (isEmailFocused) accentColor else textGray) },
                    placeholder = { Text("example@gmail.com", color = textGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isEmailFocused = it.isFocused },
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isLoading && !state.showOtpField,
                    isError = state.emailError.isNotEmpty(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textWhite, unfocusedTextColor = textWhite,
                        disabledTextColor = textWhite, errorTextColor = textWhite,
                        focusedContainerColor = emailBg, unfocusedContainerColor = inputBackground,
                        disabledContainerColor = inputBackground, errorContainerColor = inputBackground,
                        focusedBorderColor = accentColor, unfocusedBorderColor = borderColor,
                        disabledBorderColor = borderColor, errorBorderColor = Color.Red,
                        cursorColor = accentColor, errorCursorColor = accentColor,
                        focusedLabelColor = accentColor, unfocusedLabelColor = textGray,
                        disabledLabelColor = textGray, errorLabelColor = Color.Red,
                        focusedPlaceholderColor = textGray, unfocusedPlaceholderColor = textGray
                    ),
                    supportingText = {
                        if (state.emailError.isNotEmpty()) {
                            Text(state.emailError, color = Color.Red)
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── OTP Field ──────────────────────────────────────────────────
            AnimatedVisibility(
                visible = state.showOtpField,
                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit  = fadeOut(tween(200)) + shrinkVertically(tween(200))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .shadow(if (isOtpFocused) 8.dp else 0.dp, RoundedCornerShape(12.dp),
                            ambientColor = accentColor.copy(alpha = 0.3f),
                            spotColor = accentColor.copy(alpha = 0.3f))
                ) {
                    OutlinedTextField(
                        value = state.otp,
                        onValueChange = vm::onOtpChange,
                        label = { Text("Enter OTP", color = if (isOtpFocused) accentColor else textGray) },
                        placeholder = { Text("6-digit code", color = textGray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isOtpFocused = it.isFocused },
                        shape = RoundedCornerShape(12.dp),
                        enabled = !state.isVerifying,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textWhite, unfocusedTextColor = textWhite,
                            disabledTextColor = textWhite,
                            focusedContainerColor = otpBg, unfocusedContainerColor = inputBackground,
                            disabledContainerColor = inputBackground,
                            focusedBorderColor = accentColor, unfocusedBorderColor = borderColor,
                            disabledBorderColor = borderColor,
                            cursorColor = accentColor,
                            focusedLabelColor = accentColor, unfocusedLabelColor = textGray,
                            disabledLabelColor = textGray,
                            focusedPlaceholderColor = textGray, unfocusedPlaceholderColor = textGray
                        ),
                        supportingText = {
                            Text("Check your email for the 6-digit code", color = textGray)
                        }
                    )
                }
            }

            // ── Login / Verify Button ──────────────────────────────────────
            Button(
                onClick = {
                    scope.launch {
                        isLoginPressed = true; delay(100); isLoginPressed = false
                    }
                    if (!state.showOtpField) vm.sendOtp() else vm.verifyOtp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .scale(loginScale)
                    .shadow(8.dp, RoundedCornerShape(28.dp),
                        ambientColor = accentColor.copy(alpha = 0.4f),
                        spotColor = accentColor.copy(alpha = 0.4f)),
                enabled = if (!state.showOtpField) {
                    state.email.isNotEmpty() && vm.isValidEmail(state.email) && !state.isLoading
                } else {
                    state.otp.length == 6 && !state.isVerifying
                },
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    disabledContainerColor = accentColor.copy(alpha = 0.4f)
                )
            ) {
                if (state.isLoading || state.isVerifying) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        if (!state.showOtpField) "LOGIN" else "VERIFY & LOGIN",
                        color = Color.Black, fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null, tint = Color.Black
                    )
                }
            }

            // ── Change Email ───────────────────────────────────────────────
            AnimatedVisibility(
                visible = state.showOtpField,
                enter = fadeIn() + expandVertically(),
                exit  = fadeOut() + shrinkVertically()
            ) {
                TextButton(
                    onClick = vm::onChangeEmailClicked,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Change Email Address", color = accentColor)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Sign Up Link ───────────────────────────────────────────────
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("New here? ", color = textGray)
                Text(
                    "Sign Up", color = accentColor, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate("signup") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            // ── Message Card ───────────────────────────────────────────────
            if (state.message.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(cardBackground)) {
                    Text(
                        state.message,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textWhite
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

