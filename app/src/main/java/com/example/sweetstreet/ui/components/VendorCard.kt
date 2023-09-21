package com.example.sweetstreet.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.sweetstreet.SweetEvent
import com.example.sweetstreet.data.Vendor
import com.example.sweetstreet.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.round

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VendorCard(
    vendor: Vendor,
    uiState: MutableStateFlow<UiState>,
    onEvent: (SweetEvent) -> Unit
) {
    var expanded by remember { mutableStateOf (false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = ""
    )

    val tabState by remember { mutableIntStateOf(uiState.value.tabIndex) }

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    easing = LinearOutSlowInEasing
                )
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
        ) {
            AsyncImage(
                model = vendor.img,
                contentDescription = "vendor image",
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .size(
                        width = 100.dp,
                        height = 100.dp
                    ),
                contentScale = ContentScale.Crop
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier
                    .padding(top = 12.dp)
                ) {
                    Text(
                        text = vendor.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${vendor.address}, ${vendor.postcode}",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (vendor.isOpen) {
                        Text(
                            text = "Open",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    } else {
                        Text(
                            text = "Closed",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row() {
                        StarRating(vendor = vendor)
                    }
                }
                Column {
                    IconButton(onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            Icons.Default.ExpandMore,
                            contentDescription = "expand",
                            modifier = Modifier
                                .rotate(rotationState)
                        )
                    }
                }
            }
        }
        if (expanded) {
            Divider(modifier = Modifier.padding(start = 24.dp, end = 24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            val urlIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("tel:0151 6543766")
                            )
                            context.startActivity(urlIntent)
                        }
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = "phone",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "0151 6543766",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(letterSpacing = (-0.3).sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            val urlIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(vendor.instagram)
                            )
                            context.startActivity(urlIntent)
                        }
                    ) {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = "phone",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Instagram",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            onEvent(SweetEvent.SelectVendor(vendor))
                            onEvent(SweetEvent.SelectTab(tabState))
                        }
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "phone",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "View on map",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(24.dp))
                Box(
                    modifier = Modifier
                        .height(75.dp)
                ) {
                    Text(
                        text = "\"${vendor.description}\"",
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            FlowRow(modifier = Modifier.padding(start = 12.dp, end = 12.dp)) {
                vendor.items.forEach { item: String ->
                    FilterChip(
                        enabled = false,
                        selected = true,
                        label = { Text(text = item) },
                        onClick = {},
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StarRating(vendor: Vendor) {
    val starsDouble = vendor.starRating.toDouble()
    val starsDoubleRounded = round(starsDouble)
    var halfStar = true
    if (starsDouble.toString().endsWith(".0")) {
        halfStar = false
    }
    var starsInt = starsDoubleRounded.toInt()

    for (star in 0 until starsInt) {
        Icon(
            Icons.Default.StarRate,
            contentDescription = "star",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .size(20.dp)
                .offset(x = (-4).dp)
        )
    }
    if (halfStar) {
        if (starsInt < 5) {
            starsInt++
        }
        Icon(
            Icons.Default.StarHalf,
            contentDescription = "star",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .size(20.dp)
                .offset(x = (-4).dp)
        )
    }
    for (star in 0 until 5 - starsInt) {
        Icon(
            Icons.Default.StarRate,
            contentDescription = "star",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .alpha(0.4f)
                .size(20.dp)
                .offset(x = (-4).dp)
        )
    }
}