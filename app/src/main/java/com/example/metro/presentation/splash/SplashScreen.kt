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
    val borderOffset = remember { Animatable(-60f) }
    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleOffset = remember { Animatable(40f) }
    val personScale = remember { Animatable(0f) }
    val personAlpha = remember { Animatable(0f) }
    val personOffset = remember { Animatable(80f) }
    val bubbleAlpha = remember { Animatable(0f) }
    val bubbleScale = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        // 1. Logo bounces in first (center of screen grabs attention)
        val logoAlphaJob = launch { logoAlpha.animateTo(1f, tween(400)) }
        val logoScaleJob = launch {
            logoScale.animateTo(
                1f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
        }
        logoAlphaJob.join()
        logoScaleJob.join()

        delay(150)

        // 2. Border slides down from top
        val borderAlphaJob = launch { borderAlpha.animateTo(1f, tween(500, easing = FastOutSlowInEasing)) }
        val borderOffsetJob = launch { borderOffset.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }
        borderAlphaJob.join()
        borderOffsetJob.join()

        // 3. Title + subtitle slide up into view
        val titleAlphaJob = launch { titleAlpha.animateTo(1f, tween(400)) }
        val titleOffsetJob = launch {
            titleOffset.animateTo(0f, tween(400, easing = FastOutSlowInEasing))
        }
        titleAlphaJob.join()
        titleOffsetJob.join()

        delay(100)

        // 4. Person card slides up and scales in
        val personAlphaJob = launch { personAlpha.animateTo(1f, tween(500)) }
        val personScaleJob = launch {
            personScale.animateTo(
                1f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
        }
        val personOffsetJob = launch {
            personOffset.animateTo(0f, tween(500, easing = FastOutSlowInEasing))
        }
        personAlphaJob.join()
        personScaleJob.join()
        personOffsetJob.join()

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
        delay(1000)
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
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Border image at top ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = borderOffset.value.dp)
                    .alpha(borderAlpha.value * 0.45f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(R.drawable.newborder),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }

            // ── Logo overlapping border bottom ──────────────────────────────
            Box(
                modifier = Modifier
                    .offset(y = (-40).dp)
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
                    .background(Color.White.copy(alpha = 0.95f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.newlogo),
                    contentDescription = "Patna Metro Logo",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // ── "Patna Metro" small label below logo ────────────────────────
            Text(
                text = "Patna Metro",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = OutfitFont,
                    fontWeight = FontWeight.Medium,
                    color = TextMedium,
                    fontSize = 13.sp
                ),
                modifier = Modifier
                    .offset(y = (-28).dp)
                    .alpha(logoAlpha.value)
                    .scale(logoScale.value)
            )

            Spacer(Modifier.height(8.dp))

            // ── App name large ──────────────────────────────────────────────
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

            Spacer(Modifier.height(32.dp))

            // ── Person with speech bubble inside a card ─────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .offset(y = personOffset.value.dp)
                    .scale(personScale.value)
                    .alpha(personAlpha.value),
                contentAlignment = Alignment.TopCenter
            ) {
                // Person card container
                Box(
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .fillMaxWidth(0.75f)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = Color.Black.copy(alpha = 0.06f)
                        )
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFFDF5EC)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Image(
                        painter = painterResource(R.drawable.img),
                        contentDescription = "Welcome Character",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }

                // Speech bubble - positioned top-right, overlapping the card
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 0.dp, y = 0.dp)
                        .scale(bubbleScale.value)
                        .alpha(bubbleAlpha.value)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color.Black.copy(alpha = 0.08f)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.95f))
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
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
