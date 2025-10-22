package com.cars.cars_marketplace.presentation.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    carId: String?,
    onBack: () -> Unit,
    viewModel: CarDetailViewModel = hiltViewModel()
) {
    // load car when screen appears
    LaunchedEffect(carId) {
        carId?.let { viewModel.load(it) }
    }

    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Car Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val shareText = "Check out this car: ${(uiState as? CarDetailUiState.Success)?.car?.let { "${it.make} ${it.model} (${it.year}) for $${formatPriceRaw(it.price)}" } ?: "Car"}"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share car"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        when (uiState) {
            is CarDetailUiState.Loading -> {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = "Loading...", style = MaterialTheme.typography.bodyLarge)
                }
            }
            is CarDetailUiState.Error -> {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = (uiState as CarDetailUiState.Error).message, style = MaterialTheme.typography.bodyLarge)
                }
            }
            is CarDetailUiState.Success -> {
                val car = (uiState as CarDetailUiState.Success).car
                val scrollState = rememberScrollState()
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(innerPadding)
                ) {
                    // Hero Image Section
                    HeroImageSection(
                        car = car,
                        isFavorite = isFavorite,
                        onFavoriteClick = { carId?.let { viewModel.toggleFavorite(it) } }
                    )
                    
                    // Main Content
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Title and Price
                        CarTitleSection(car = car)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Specifications Cards
                        SpecificationsSection(car = car)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Features Section
                        if (car.features.isNotEmpty()) {
                            FeaturesSection(features = car.features)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        // Description
                        if (!car.description.isNullOrBlank()) {
                            DescriptionSection(description = car.description)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        // Contact Section
                        ContactSection(car = car, context = context)
                    }
                }
            }
        }
    }
}

// Hero Image Section with carousel
@Composable
private fun HeroImageSection(
    car: com.cars.cars_marketplace.domain.model.Car,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    val images = if (car.images.isEmpty()) listOf("") else car.images
    val listState = rememberLazyListState()
    var currentPage by remember { mutableStateOf(0) }
    
    val favScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.12f else 1f,
        animationSpec = tween(durationMillis = 180)
    )
    val favTint by animateColorAsState(
        targetValue = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
        animationSpec = tween(180)
    )
    
    val coroutineScope = rememberCoroutineScope()
    var showBurst by remember { mutableStateOf(false) }
    
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.collectLatest { idx ->
            currentPage = idx
        }
    }
    
    val imageHeight = 280.dp
    
    Column {
        // Image Carousel
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth().height(imageHeight)
        ) {
            itemsIndexed(images) { index, img ->
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(imageHeight)
                        .padding(end = 8.dp)
                ) {
                    if (img.isBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surface)
                        )
                    } else {
                        SubcomposeAsyncImage(
                            model = img,
                            contentDescription = "car image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(MaterialTheme.colorScheme.surface)
                                )
                                ShimmerOverlay()
                            },
                            error = {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(MaterialTheme.colorScheme.surface)
                                )
                            }
                        )
                        
                        // Star Button Overlay (consistent with app theme)
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Star",
                            tint = favTint,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .size(32.dp)
                                .scale(favScale)
                                .clickable {
                                    onFavoriteClick()
                                    showBurst = true
                                    coroutineScope.launch {
                                        delay(520)
                                        showBurst = false
                                    }
                                }
                        )
                        
                        // Burst Animation
                        if (showBurst && isFavorite) {
                            val burstScale by animateFloatAsState(
                                targetValue = if (showBurst) 1.6f else 0f,
                                animationSpec = tween(420)
                            )
                            val burstAlpha by animateFloatAsState(
                                targetValue = if (showBurst) 0.95f else 0f,
                                animationSpec = tween(420)
                            )
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = burstAlpha),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(end = 16.dp, top = 12.dp)
                                    .size(64.dp)
                                    .scale(burstScale)
                            )
                        }
                    }
                }
            }
        }
        
        // Page Indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in images.indices) {
                val active = i == currentPage
                val dotSize by animateDpAsState(
                    targetValue = if (active) 10.dp else 6.dp,
                    animationSpec = tween(200)
                )
                val dotColor by animateColorAsState(
                    targetValue = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    animationSpec = tween(200)
                )
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }
        }
    }
}

// Car Title and Price Section
@Composable
private fun CarTitleSection(car: com.cars.cars_marketplace.domain.model.Car) {
    Column {
        Text(
            text = "${car.make} ${car.model}",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${car.year}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "•",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = formatPrice(car.price),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        // Status Badge
        if (!car.status.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                color = when (car.status.lowercase()) {
                    "available" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    "sold" -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = when (car.status.lowercase()) {
                            "available" -> MaterialTheme.colorScheme.primary
                            "sold" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = car.status.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelMedium,
                        color = when (car.status.lowercase()) {
                            "available" -> MaterialTheme.colorScheme.primary
                            "sold" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}

// Specifications Cards Section
@Composable
private fun SpecificationsSection(car: com.cars.cars_marketplace.domain.model.Car) {
    Text(
        text = "Specifications",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    
    val specifications = listOfNotNull(
        car.mileage?.let { "Mileage" to "${String.format(Locale.US, "%,d", it)} miles" },
        car.transmission?.let { "Transmission" to it.replaceFirstChar { char -> char.uppercase() } },
        car.fuelType?.let { "Fuel Type" to it.replaceFirstChar { char -> char.uppercase() } },
        car.bodyType?.let { "Body Type" to it.replaceFirstChar { char -> char.uppercase() } },
        car.color?.let { "Color" to it.replaceFirstChar { char -> char.uppercase() } }
    )
    
    if (specifications.isNotEmpty()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(specifications.size) { index ->
                val (label, value) = specifications[index]
                SpecificationCard(label = label, value = value)
            }
        }
    }
}

// Individual Specification Card
@Composable
private fun SpecificationCard(label: String, value: String) {
    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Features Section
@Composable
private fun FeaturesSection(features: List<String>) {
    Text(
        text = "Features",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(features.size) { index ->
            val feature = features[index]
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = feature,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

// Description Section
@Composable
private fun DescriptionSection(description: String) {
    Text(
        text = "Description",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(16.dp)
        )
    }
}

// Contact Section
@Composable
private fun ContactSection(
    car: com.cars.cars_marketplace.domain.model.Car,
    context: android.content.Context
) {
    Text(
        text = "Contact Seller",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = android.net.Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_SUBJECT, "Inquiry about ${car.make} ${car.model}")
                    putExtra(Intent.EXTRA_TEXT, "Hi, I'm interested in your ${car.year} ${car.make} ${car.model}. Is it still available?")
                }
                try {
                    context.startActivity(emailIntent)
                } catch (e: Exception) {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Inquiry about ${car.year} ${car.make} ${car.model}")
                    }
                    context.startActivity(Intent.createChooser(intent, "Contact seller"))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Contact Seller",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
        
        if (!car.location.isNullOrBlank()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = car.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ShimmerOverlay() {
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface.copy(alpha = 0.08f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.14f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.08f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
    )
}

private fun formatPrice(price: Double): String {
    return "$${String.format(Locale.US, "%,.0f", price)}"
}

private fun formatPriceRaw(price: Double): String = String.format(Locale.US, "%,.0f", price)
