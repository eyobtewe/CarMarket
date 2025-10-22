package com.cars.cars_marketplace.presentation.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*

@Composable
fun AnimatedIcon(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null,
    enableMicroInteractions: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Super cool micro-interactions
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enableMicroInteractions) 0.9f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "press_scale"
    )
    
    val rotation by animateFloatAsState(
        targetValue = if (isPressed && enableMicroInteractions) 5f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "press_rotation"
    )
    
    val finalModifier = if (onClick != null) {
        modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .scale(scale)
            .rotate(rotation)
    } else {
        modifier
            .scale(scale)
            .rotate(rotation)
    }
    
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        modifier = finalModifier,
        tint = tint
    )
}

@Composable
fun LottieAnimatedIcon(
    animationRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    isPlaying: Boolean = false,
    speed: Float = 1f,
    onClick: (() -> Unit)? = null
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(animationRes)
    )
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        speed = speed,
        iterations = LottieConstants.IterateForever
    )
    
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "lottie_scale"
    )
    
    val finalModifier = if (onClick != null) {
        modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .scale(scale)
    } else {
        modifier.scale(scale)
    }
    
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = finalModifier
    )
}

@Composable
fun PulsingIcon(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.primary,
    isActive: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val pulseScale by animateFloatAsState(
        targetValue = if (isActive) 1.2f else 1f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = if (isActive) 0 else 200
        ),
        label = "pulse_scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isActive) 0.8f else 1f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = if (isActive) 0 else 100
        ),
        label = "pulse_alpha"
    )
    
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "press_scale"
    )
    
    val finalModifier = if (onClick != null) {
        modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .scale(pulseScale * pressScale)
    } else {
        modifier.scale(pulseScale * pressScale)
    }
    
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        modifier = finalModifier,
        tint = tint.copy(alpha = alpha)
    )
}
