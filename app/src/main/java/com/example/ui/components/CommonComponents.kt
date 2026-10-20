package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.model.ProductCategory
import com.example.ui.theme.GladBlue
import com.example.ui.theme.GladGold
import com.example.ui.theme.GladGreen
import com.example.ui.theme.GladPink
import com.example.ui.theme.SellerNavy
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

fun formatRupee(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return formatter.format(amount).replace("₹", "₹ ")
}

@Composable
fun GladRatingBadge(rating: Float, modifier: Modifier = Modifier) {
    Surface(
        color = if (rating >= 4.0f) GladGreen else Color(0xFFFF9800),
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = String.format(Locale.ENGLISH, "%.1f", rating),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(10.dp)
            )
        }
    }
}

@Composable
fun GladAssuredBadge(modifier: Modifier = Modifier) {
    Surface(
        color = Color(0xFFF1F3F6),
        shape = RoundedCornerShape(3.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFB0BEC5)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
        ) {
            Text(
                text = "GLAD",
                color = GladBlue,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Assured",
                color = GladPink,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = GladBlue,
                modifier = Modifier.size(9.dp)
            )
        }
    }
}

@Composable
fun PriceDisplay(
    price: Double,
    originalPrice: Double,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false
) {
    val discount = if (originalPrice > price) {
        (((originalPrice - price) / originalPrice) * 100).toInt()
    } else 0

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = formatRupee(price),
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = if (isLarge) 22.sp else 15.sp
        )
        if (originalPrice > price) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = formatRupee(originalPrice),
                color = TextSecondary,
                fontSize = if (isLarge) 14.sp else 11.sp,
                textDecoration = TextDecoration.LineThrough
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$discount% off",
                color = GladGreen,
                fontWeight = FontWeight.Bold,
                fontSize = if (isLarge) 15.sp else 12.sp
            )
        }
    }
}

@Composable
fun ProductVisualCard(
    imageType: String,
    title: String,
    modifier: Modifier = Modifier,
    badgeText: String? = null
) {
    val (icon, bgBrush) = when (imageType.uppercase()) {
        "PHONE", "MOBILES" -> Pair(
            Icons.Default.PhoneAndroid,
            Brush.linearGradient(listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD)))
        )
        "HEADPHONES" -> Pair(
            Icons.Outlined.Headphones,
            Brush.linearGradient(listOf(Color(0xFFF3E8FF), Color(0xFFDDD6FE)))
        )
        "EARBUDS" -> Pair(
            Icons.Outlined.Headphones,
            Brush.linearGradient(listOf(Color(0xFFECFDF5), Color(0xFFA7F3D0)))
        )
        "SHOES" -> Pair(
            Icons.Default.Checkroom,
            Brush.linearGradient(listOf(Color(0xFFFFF7ED), Color(0xFFFED7AA)))
        )
        "CLOTHING", "FASHION" -> Pair(
            Icons.Default.Checkroom,
            Brush.linearGradient(listOf(Color(0xFFFFF1F2), Color(0xFFFECDD3)))
        )
        "APPLIANCE", "APPLIANCES", "HOME_KITCHEN" -> Pair(
            Icons.Default.Kitchen,
            Brush.linearGradient(listOf(Color(0xFFF0FDF4), Color(0xFFBBF7D0)))
        )
        "BEAUTY" -> Pair(
            Icons.Default.Face,
            Brush.linearGradient(listOf(Color(0xFFFDF2F8), Color(0xFFFBCFE8)))
        )
        "SMARTWATCH" -> Pair(
            Icons.Default.Watch,
            Brush.linearGradient(listOf(Color(0xFFEFF6FF), Color(0xFFBFDBFE)))
        )
        "GROCERY" -> Pair(
            Icons.Default.LocalGroceryStore,
            Brush.linearGradient(listOf(Color(0xFFFEFCE8), Color(0xFFFEF08A)))
        )
        else -> Pair(
            Icons.Outlined.Inventory2,
            Brush.linearGradient(listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0)))
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = GladBlue,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        if (!badgeText.isNullOrBlank()) {
            Surface(
                color = GladPink,
                shape = RoundedCornerShape(bottomEnd = 8.dp),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = badgeText,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryIconItem(
    category: ProductCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon: ImageVector = when (category) {
        ProductCategory.ALL -> Icons.Default.Search
        ProductCategory.MOBILES -> Icons.Default.PhoneAndroid
        ProductCategory.ELECTRONICS -> Icons.Default.Laptop
        ProductCategory.FASHION -> Icons.Default.Checkroom
        ProductCategory.BEAUTY -> Icons.Default.Face
        ProductCategory.HOME_KITCHEN -> Icons.Default.Home
        ProductCategory.APPLIANCES -> Icons.Default.Kitchen
        ProductCategory.GROCERY -> Icons.Default.LocalGroceryStore
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isSelected) GladBlue else Color(0xFFF1F3F6))
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) GladPink else Color(0xFFE0E0E0),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = category.displayName,
                tint = if (isSelected) Color.White else GladBlue,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.displayName,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) GladBlue else TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun GladBottomNavigation(
    currentScreen: AppScreen,
    cartItemCount: Int,
    isSellerRegistered: Boolean,
    onNavigate: (AppScreen) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == AppScreen.HOME,
            onClick = { onNavigate(AppScreen.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Shop", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GladBlue,
                selectedTextColor = GladBlue,
                indicatorColor = GladBlue.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.SELLER_HUB,
            onClick = { onNavigate(AppScreen.SELLER_HUB) },
            icon = {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = if (isSellerRegistered) GladGreen else GladGold,
                            contentColor = Color.Black
                        ) {
                            Text(
                                text = if (isSellerRegistered) "HUB" else "SELL",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                ) {
                    Icon(Icons.Default.Storefront, contentDescription = "Supply Panel")
                }
            },
            label = { Text("Supply Panel", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GladPink,
                selectedTextColor = GladPink,
                indicatorColor = GladPink.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_seller_hub")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.CART,
            onClick = { onNavigate(AppScreen.CART) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(
                                containerColor = GladPink,
                                contentColor = Color.White
                            ) {
                                Text(cartItemCount.toString(), fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            },
            label = { Text("Cart", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GladBlue,
                selectedTextColor = GladBlue,
                indicatorColor = GladBlue.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_cart")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.WISHLIST,
            onClick = { onNavigate(AppScreen.WISHLIST) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Wishlist") },
            label = { Text("Wishlist", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GladPink,
                selectedTextColor = GladPink,
                indicatorColor = GladPink.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_wishlist")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.PROFILE,
            onClick = { onNavigate(AppScreen.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
            label = { Text("Account", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GladBlue,
                selectedTextColor = GladBlue,
                indicatorColor = GladBlue.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_profile")
        )
    }
}
