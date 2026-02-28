package com.example.metro.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.metro.R
import com.example.metro.data.local.SessionDataStore
import com.example.metro.ui.theme.DividerColor
import com.example.metro.ui.theme.IndigoBlue
import com.example.metro.ui.theme.LoraFont
import com.example.metro.ui.theme.OutfitFont
import com.example.metro.ui.theme.Parchment
import com.example.metro.ui.theme.ParchmentDark
import com.example.metro.ui.theme.SurfaceWhite
import com.example.metro.ui.theme.TextDark
import com.example.metro.ui.theme.TextLight
import com.example.metro.ui.theme.TextMedium
import com.example.metro.ui.theme.VermilionRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val sessionDataStore = remember { SessionDataStore(context) }
    val vm: LoginViewModel = viewModel(factory = LoginViewModel.Factory(sessionDataStore))
    val state by vm.uiState.collectAsState()

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
    val headerAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.3f) }
    val logoAlpha = remember { Animatable(0f) }
    val cardAlpha = remember { Animatable(0f) }
    val cardOffset = remember { Animatable(40f) }
    val personScale = remember { Animatable(0f) }
    val personAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        headerAlpha.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
        launch { logoAlpha.animateTo(1f, tween(300)) }
        logoScale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
        launch { cardAlpha.animateTo(1f, tween(400)) }
        cardOffset.animateTo(0f, tween(400, easing = FastOutSlowInEasing))
        launch { personAlpha.animateTo(1f, tween(400)) }
        personScale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
    }

    // ── Layout ─────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Parchment)
    ) {
        // ── Madhubani decorative pattern in background ──────────────────
        MadhubaniPattern(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.06f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Border header image ─────────────────────────────────────
            Image(
                painter = painterResource(R.drawable.newborder),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(headerAlpha.value * 0.45f),
                contentScale = ContentScale.FillWidth
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))

                // ── Logo ────────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .scale(logoScale.value)
                        .alpha(logoAlpha.value)
                        .clip(RoundedCornerShape(22.dp))
                        .background(SurfaceWhite),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.newlogo),
                        contentDescription = "Patna Metro Logo",
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.height(16.dp))

                // ── Title ───────────────────────────────────────────────
                Text(
                    "Welcome to Patna Metro",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = LoraFont,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        fontSize = 28.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(logoAlpha.value)
                        .scale(logoScale.value)
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Your Patna Metro Companion",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = OutfitFont,
                        color = TextMedium
                    ),
                    modifier = Modifier.alpha(logoAlpha.value)
                )

                Spacer(Modifier.height(28.dp))

                // ── Login Card ──────────────────────────────────────────
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(cardAlpha.value)
                        .offset(y = cardOffset.value.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Card header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(VermilionRed.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    tint = VermilionRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Login with Email",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextDark
                                )
                            )
                        }

                        Spacer(Modifier.height(6.dp))

                        Text(
                            "We'll send a 6-digit OTP to verify your email",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextLight,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(20.dp))

                        // ── Email Field ─────────────────────────────────
                        OutlinedTextField(
                            value = state.email,
                            onValueChange = vm::onEmailChange,
                            label = { Text("Email Address") },
                            placeholder = { Text("example@gmail.com", color = TextLight) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            enabled = !state.isLoading && !state.showOtpField,
                            isError = state.emailError.isNotEmpty(),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    tint = if (state.emailError.isNotEmpty()) VermilionRed else IndigoBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark,
                                disabledTextColor = TextMedium,
                                errorTextColor = TextDark,
                                focusedContainerColor = Parchment.copy(alpha = 0.4f),
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = ParchmentDark.copy(alpha = 0.3f),
                                errorContainerColor = VermilionRed.copy(alpha = 0.05f),
                                focusedBorderColor = IndigoBlue,
                                unfocusedBorderColor = DividerColor,
                                disabledBorderColor = DividerColor,
                                errorBorderColor = VermilionRed,
                                cursorColor = IndigoBlue,
                                focusedLabelColor = IndigoBlue,
                                unfocusedLabelColor = TextMedium,
                                disabledLabelColor = TextLight,
                                errorLabelColor = VermilionRed
                            ),
                            supportingText = {
                                if (state.emailError.isNotEmpty()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = VermilionRed,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(state.emailError, color = VermilionRed)
                                    }
                                }
                            }
                        )

                        Spacer(Modifier.height(12.dp))

                        // ── OTP Field ───────────────────────────────────
                        AnimatedVisibility(
                            visible = state.showOtpField,
                            enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                            exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
                        ) {
                            Column {
                                OutlinedTextField(
                                    value = state.otp,
                                    onValueChange = vm::onOtpChange,
                                    label = { Text("Enter OTP") },
                                    placeholder = { Text("6-digit code", color = TextLight) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    enabled = !state.isVerifying,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = IndigoBlue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = TextDark,
                                        unfocusedTextColor = TextDark,
                                        disabledTextColor = TextMedium,
                                        focusedContainerColor = Parchment.copy(alpha = 0.4f),
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = ParchmentDark.copy(alpha = 0.3f),
                                        focusedBorderColor = IndigoBlue,
                                        unfocusedBorderColor = DividerColor,
                                        disabledBorderColor = DividerColor,
                                        cursorColor = IndigoBlue,
                                        focusedLabelColor = IndigoBlue,
                                        unfocusedLabelColor = TextMedium,
                                        disabledLabelColor = TextLight
                                    ),
                                    supportingText = {
                                        Text(
                                            "Check your email for the 6-digit code",
                                            color = TextLight
                                        )
                                    }
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }

                        // ── Login / Verify Button ───────────────────────
                        Button(
                            onClick = {
                                if (!state.showOtpField) vm.sendOtp() else vm.verifyOtp()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            enabled = if (!state.showOtpField) {
                                state.email.isNotEmpty() && vm.isValidEmail(state.email) && !state.isLoading
                            } else {
                                state.otp.length == 6 && !state.isVerifying
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VermilionRed,
                                disabledContainerColor = VermilionRed.copy(alpha = 0.4f)
                            )
                        ) {
                            if (state.isLoading || state.isVerifying) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    if (!state.showOtpField) "SEND OTP" else "VERIFY & LOGIN",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // ── Change Email ────────────────────────────────
                        AnimatedVisibility(
                            visible = state.showOtpField,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            TextButton(
                                onClick = vm::onChangeEmailClicked,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    "Change Email Address",
                                    color = IndigoBlue,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Message Card ────────────────────────────────────────
                AnimatedVisibility(
                    visible = state.message.isNotEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (state.isLoggedIn) Color(0xFFE8F5E9)
                            else IndigoBlue.copy(alpha = 0.08f)
                        ),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (state.isLoggedIn) Icons.Default.CheckCircle
                                else Icons.Default.Email,
                                contentDescription = null,
                                tint = if (state.isLoggedIn) Color(0xFF2E7D32) else IndigoBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                state.message,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextDark
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ── Person illustration ─────────────────────────────────
                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = "Welcome",
                    modifier = Modifier
                        .height(160.dp)
                        .scale(personScale.value)
                        .alpha(personAlpha.value),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.height(12.dp))

                // ── Unofficial disclaimer ───────────────────────────────
                Text(
                    "This is an unofficial app. Not affiliated with PMRC.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextLight,
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(cardAlpha.value)
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Madhubani-style decorative pattern drawn with Canvas
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun MadhubaniPattern(modifier: Modifier = Modifier) {
    val red = VermilionRed
    val indigo = IndigoBlue
    val turmeric = Color(0xFFFFC107)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Decorative corner lotus petals (top-left)
        val petalStroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)

        // Top-left corner motif
        for (i in 0..4) {
            val angle = i * 36f
            val path = Path().apply {
                moveTo(0f, 0f)
                quadraticTo(
                    80f + angle, 40f + angle * 0.5f,
                    30f + angle * 1.5f, 100f + angle
                )
            }
            drawPath(path, red.copy(alpha = 0.5f), style = petalStroke)
        }

        // Top-right corner motif
        for (i in 0..4) {
            val angle = i * 36f
            val path = Path().apply {
                moveTo(w, 0f)
                quadraticTo(
                    w - 80f - angle, 40f + angle * 0.5f,
                    w - 30f - angle * 1.5f, 100f + angle
                )
            }
            drawPath(path, indigo.copy(alpha = 0.4f), style = petalStroke)
        }

        // Scattered paisley dots along sides
        val dotRadius = 4.dp.toPx()
        for (i in 0..12) {
            val y = h * (i / 12f)
            // Left side dots
            drawCircle(red.copy(alpha = 0.3f), dotRadius, Offset(12.dp.toPx(), y))
            drawCircle(indigo.copy(alpha = 0.2f), dotRadius * 0.6f, Offset(24.dp.toPx(), y + 20f))
            // Right side dots
            drawCircle(indigo.copy(alpha = 0.3f), dotRadius, Offset(w - 12.dp.toPx(), y + 10f))
            drawCircle(turmeric.copy(alpha = 0.2f), dotRadius * 0.6f, Offset(w - 24.dp.toPx(), y + 30f))
        }

        // Bottom decorative wavy line
        val wavePath = Path().apply {
            moveTo(0f, h - 60f)
            for (x in 0..w.toInt() step 40) {
                val yOff = if ((x / 40) % 2 == 0) -12f else 12f
                quadraticTo(x.toFloat() + 20f, h - 60f + yOff, x.toFloat() + 40f, h - 60f)
            }
        }
        drawPath(wavePath, red.copy(alpha = 0.15f), style = Stroke(width = 1.5.dp.toPx()))

        // Second wavy line
        val wavePath2 = Path().apply {
            moveTo(0f, h - 40f)
            for (x in 0..w.toInt() step 50) {
                val yOff = if ((x / 50) % 2 == 0) -8f else 8f
                quadraticTo(x.toFloat() + 25f, h - 40f + yOff, x.toFloat() + 50f, h - 40f)
            }
        }
        drawPath(wavePath2, indigo.copy(alpha = 0.12f), style = Stroke(width = 1.dp.toPx()))

        // Center diamond motifs scattered
        val diamondSize = 8.dp.toPx()
        val diamondPositions = listOf(
            Offset(w * 0.15f, h * 0.3f),
            Offset(w * 0.85f, h * 0.25f),
            Offset(w * 0.1f, h * 0.7f),
            Offset(w * 0.9f, h * 0.65f),
            Offset(w * 0.2f, h * 0.5f),
            Offset(w * 0.8f, h * 0.45f),
        )
        diamondPositions.forEachIndexed { idx, pos ->
            val color = if (idx % 3 == 0) red else if (idx % 3 == 1) indigo else turmeric
            val dPath = Path().apply {
                moveTo(pos.x, pos.y - diamondSize)
                lineTo(pos.x + diamondSize, pos.y)
                lineTo(pos.x, pos.y + diamondSize)
                lineTo(pos.x - diamondSize, pos.y)
                close()
            }
            drawPath(dPath, color.copy(alpha = 0.25f), style = Stroke(width = 1.5.dp.toPx()))
        }
    }
}
