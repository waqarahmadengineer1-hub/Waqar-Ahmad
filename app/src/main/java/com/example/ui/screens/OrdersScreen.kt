package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.components.ProductVisualCard
import com.example.ui.components.formatRupee
import com.example.ui.theme.GladBlue
import com.example.ui.theme.GladGreen
import com.example.ui.theme.GladPink
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GladHeartViewModel

@Composable
fun OrdersScreen(viewModel: GladHeartViewModel) {
    val orders by viewModel.orders.collectAsState()

    Scaffold(
        topBar = {
            Surface(color = GladBlue, tonalElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        modifier = Modifier.testTag("btn_orders_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "My Orders",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF1F3F6)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = GladBlue.copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No Orders Yet", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Browse our store to place your first order!", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        colors = ButtonDefaults.buttonColors(containerColor = GladBlue)
                    ) {
                        Text("Start Shopping")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF1F3F6))
            ) {
                items(orders) { order ->
                    OrderSummaryCard(
                        order = order,
                        onTrackOrder = { viewModel.viewOrderTracking(order) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
fun OrderSummaryCard(
    order: Order,
    onTrackOrder: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTrackOrder() }
            .testTag("order_item_${order.orderId}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Status row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (order.status == OrderStatus.DELIVERED) Icons.Default.CheckCircle else Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = if (order.status == OrderStatus.DELIVERED) GladGreen else GladBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${order.status.title} on ${order.orderDate}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (order.status == OrderStatus.DELIVERED) GladGreen else GladBlue
                    )
                }

                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // First item preview
            val firstItem = order.items.firstOrNull()
            if (firstItem != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductVisualCard(
                        imageType = firstItem.product.imageType,
                        title = firstItem.product.title,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = firstItem.product.title,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = TextPrimary
                        )
                        if (order.items.size > 1) {
                            Text(
                                text = "+ ${order.items.size - 1} more items",
                                fontSize = 11.sp,
                                color = GladBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Total: ${formatRupee(order.totalAmount)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderTrackingScreen(viewModel: GladHeartViewModel) {
    val order by viewModel.selectedOrderForTracking.collectAsState()

    Scaffold(
        topBar = {
            Surface(color = GladBlue, tonalElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.popBack() },
                        modifier = Modifier.testTag("btn_tracking_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Order Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        if (order == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Order details not available")
            }
        } else {
            val currOrder = order!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF1F3F6))
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with Order ID
                Surface(color = Color.White, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Order ID: ${currOrder.orderId}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GladBlue)
                        Text("Placed on: ${currOrder.orderDate}", fontSize = 11.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Tracking Number: ${currOrder.trackingNumber}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Flipkart-Style Vertical Stepper
                Surface(color = Color.White, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("TRACKING STATUS", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(14.dp))

                        val currentStepIndex = currOrder.status.stepIndex

                        TrackingStepRow(
                            stepName = "Order Confirmed",
                            desc = "Order placed and verified via payment gateway.",
                            isDone = currentStepIndex >= 0,
                            isLast = false
                        )
                        TrackingStepRow(
                            stepName = "Packed",
                            desc = "Seller has carefully packaged your products.",
                            isDone = currentStepIndex >= 1,
                            isLast = false
                        )
                        TrackingStepRow(
                            stepName = "Shipped via Glad Express",
                            desc = "Dispatched from Bengaluru fulfillment center.",
                            isDone = currentStepIndex >= 2,
                            isLast = false
                        )
                        TrackingStepRow(
                            stepName = "Out for Delivery",
                            desc = "Glad Express courier partner is on the way.",
                            isDone = currentStepIndex >= 3,
                            isLast = false
                        )
                        TrackingStepRow(
                            stepName = "Delivered",
                            desc = "Package delivered to your address.",
                            isDone = currentStepIndex >= 4,
                            isLast = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Delivery Address Card
                Surface(color = Color.White, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = GladBlue, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Delivery Address", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(currOrder.address.fullName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text(currOrder.address.formattedAddress, fontSize = 12.sp, color = TextSecondary)
                        Text("Phone: ${currOrder.address.phone}", fontSize = 12.sp, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Price & Payment
                Surface(color = Color.White, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("PAYMENT & INVOICE", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Amount Paid", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(formatRupee(currOrder.totalAmount), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GladGreen)
                        }
                        Text("Payment Mode: ${currOrder.paymentMethod}", fontSize = 12.sp, color = TextSecondary)

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { /* simulated invoice download */ },
                            colors = ButtonDefaults.buttonColors(containerColor = GladBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Download Tax Invoice")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TrackingStepRow(
    stepName: String,
    desc: String,
    isDone: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = CircleShape,
                color = if (isDone) GladGreen else Color(0xFFE2E8F0),
                modifier = Modifier.size(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isDone) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    } else {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF94A3B8)))
                    }
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(if (isDone) GladGreen else Color(0xFFE2E8F0))
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = stepName,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isDone) TextPrimary else TextSecondary
            )
            Text(
                text = desc,
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
