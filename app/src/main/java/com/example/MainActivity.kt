package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppScreen
import com.example.ui.components.GladBottomNavigation
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SellerHubScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GladHeartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: GladHeartViewModel = viewModel()
                GladHeartApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GladHeartApp(viewModel: GladHeartViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val sellerProfile by viewModel.sellerProfile.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()

    // Handle Android system back button
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        val popped = viewModel.popBack()
        if (!popped) {
            viewModel.navigateTo(AppScreen.HOME)
        }
    }

    val showBottomBar = currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.SELLER_HUB,
        AppScreen.CART,
        AppScreen.WISHLIST,
        AppScreen.PROFILE
    )

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            if (showBottomBar) {
                GladBottomNavigation(
                    currentScreen = currentScreen,
                    cartItemCount = cartItems.sumOf { it.quantity },
                    isSellerRegistered = sellerProfile.isRegistered,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                AppScreen.PRODUCT_DETAIL -> {
                    selectedProduct?.let { product ->
                        ProductDetailScreen(product = product, viewModel = viewModel)
                    } ?: HomeScreen(viewModel = viewModel)
                }
                AppScreen.CART -> CartScreen(viewModel = viewModel)
                AppScreen.CHECKOUT -> CheckoutScreen(viewModel = viewModel)
                AppScreen.ORDERS -> OrdersScreen(viewModel = viewModel)
                AppScreen.ORDER_TRACKING -> OrderTrackingScreen(viewModel = viewModel)
                AppScreen.WISHLIST -> WishlistScreen(viewModel = viewModel)
                AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
                AppScreen.SELLER_HUB -> SellerHubScreen(viewModel = viewModel)
            }
        }
    }
}
