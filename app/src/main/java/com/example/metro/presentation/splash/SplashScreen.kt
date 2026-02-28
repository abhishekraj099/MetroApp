package com.example.metro.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metro.R
import com.example.metro.ui.theme.IndigoBlue
import com.example.metro.ui.theme.LoraFont
import com.example.metro.ui.theme.OutfitFont
import com.example.metro.ui.theme.Parchment
import com.example.metro.ui.theme.ParchmentDark
import com.example.metro.ui.theme.TextDark
import com.example.metro.ui.theme.TextMedium
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // ── Animations ──────────────────────────────────────────────────────────
    val borderAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.3f) }
    val logoAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleOffset = remember { Animatable(30f) }
    val personScale = remember { Animatable(0f) }
    val personAlpha = remember { Animatable(0f) }
    val bubbleAlpha = remember { Animatable(0f) }
    val bubbleScale = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        // 1. Border fades in
        borderAlpha.animateTo(1f, tween(400, easing = FastOutSlowInEasing))

        // 2. Logo bounces in (alpha + scale in parallel)
        val logoJob = launch { logoAlpha.animateTo(1f, tween(300)) }
        val logoScaleJob = launch {
            logoScale.animateTo(
                1f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
        }
        logoJob.join()
        logoScaleJob.join()

        // 3. Title slides up (alpha + offset in parallel)
        val titleAlphaJob = launch { titleAlpha.animateTo(1f, tween(400)) }
        val titleOffsetJob = launch {
            titleOffset.animateTo(0f, tween(400, easing = FastOutSlowInEasing))
        }
        titleAlphaJob.join()
        titleOffsetJob.join()

        // 4. Person slides in (alpha + scale in parallel)
        val personAlphaJob = launch { personAlpha.animateTo(1f, tween(400)) }
        val personScaleJob = launch {
            personScale.animateTo(
                1f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
        }
        personAlphaJob.join()
        personScaleJob.join()

        // 5. Speech bubble pops in
        delay(200)
        val bubbleAlphaJob = launch { bubbleAlpha.animateTo(1f, tween(300)) }
        val bubbleScaleJob = launch {
            bubbleScale.animateTo(
                1f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
            )
        }
        bubbleAlphaJob.join()
        bubbleScaleJob.join()

        // Hold for a moment then navigate
        delay(1200)
        onSplashFinished()
    }

    // ── Background with subtle radial gradient ──────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFFDF8),
                        Parchment,
                        ParchmentDark.copy(alpha = 0.4f)
                    ),
                    radius = 1200f
                )
            )
    ) {
        // ── Border image at top ─────────────────────────────────────────────
        Image(
            painter = painterResource(R.drawable.newborder),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(borderAlpha.value),
            contentScale = ContentScale.FillWidth
        )

        // ── Center content ──────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ── App Logo ────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = IndigoBlue.copy(alpha = 0.15f),
                        spotColor = IndigoBlue.copy(alpha = 0.15f)
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.newlogo),
                    contentDescription = "Patna Metro Logo",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(Modifier.height(8.dp))

            // ── "Patna Metro" text below logo ─────────────────────────────────────
            Text(
                text = "Patna Metro",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = OutfitFont,
                    fontWeight = FontWeight.Medium,
                    color = TextMedium,
                    fontSize = 13.sp
                ),
                modifier = Modifier
                    .alpha(logoAlpha.value)
                    .scale(logoScale.value)
            )

            Spacer(Modifier.height(32.dp))

            // ── App name ────────────────────────────────────────────────────
            Text(
                text = "Patna Metro",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = LoraFont,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    fontSize = 44.sp
                ),
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offset(y = titleOffset.value.dp)
            )

            Spacer(Modifier.height(4.dp))

            // ── Subtitle ────────────────────────────────────────────────────
            Text(
                text = "Patna Metro Companion",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = OutfitFont,
                    fontWeight = FontWeight.Normal,
                    color = TextMedium,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offset(y = titleOffset.value.dp)
            )

            Spacer(Modifier.height(48.dp))

            // ── Person with speech bubble ───────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Speech bubble - positioned top-right of person
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-20).dp, y = 10.dp)
                        .scale(bubbleScale.value)
                        .alpha(bubbleAlpha.value)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color.Black.copy(alpha = 0.08f)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.92f))
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Pranam! 🙏",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = OutfitFont,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark,
                            fontSize = 18.sp
                        )
                    )
                }

                // Namaste person image
                Image(
                    painter = painterResource(R.drawable.namastepersono),
                    contentDescription = "Welcome Character",
                    modifier = Modifier
                        .height(220.dp)
                        .scale(personScale.value)
                        .alpha(personAlpha.value)
                        .align(Alignment.BottomCenter),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
