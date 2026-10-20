package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Address
import com.example.model.AppScreen
import com.example.ui.components.formatRupee
import com.example.ui.theme.GladBlue
import com.example.ui.theme.GladGold
import com.example.ui.theme.GladGreen
import com.example.ui.theme.GladPink
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GladHeartViewModel
import com.example.viewmodel.PaymentMethodType
import java.util.UUID

@Composable
fun CheckoutScreen(viewModel: GladHeartViewModel) {
    val addresses by viewModel.addresses.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()
    val finalPayableAmount by viewModel.finalPayableAmount.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()

    val paymentMethod by viewModel.paymentMethod.collectAsState()
    val selectedUpiApp by viewModel.selectedUpiApp.collectAsState()
    val customUpiId by viewModel.customUpiId.collectAsState()
    val cardNumber by viewModel.cardNumber.collectAsState()
    val cardExpiry by viewModel.cardExpiry.collectAsState()
    val cardCvv by viewModel.cardCvv.collectAsState()
    val cardHolder by viewModel.cardHolder.collectAsState()
    val selectedNetBank by viewModel.selectedNetBank.collectAsState()
    val codCaptcha by viewModel.codCaptcha.collectAsState()
    val codCaptchaInput by viewModel.codCaptchaInput.collectAsState()

    val isProcessingPayment by viewModel.isProcessingPayment.collectAsState()
    val showOtpDialog by viewModel.showOtpDialog.collectAsState()
    val otpInput by viewModel.otpInput.collectAsState()
    val showOrderSuccess by viewModel.showOrderSuccess.collectAsState()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsState()

    var showAddressModal by remember { mutableStateOf(false) }

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
                        modifier = Modifier.testTag("btn_checkout_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Payments & Checkout",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = formatRupee(finalPayableAmount),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Glad Heart Secure Pay",
                            color = GladGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { viewModel.processCheckout() },
                        enabled = !isProcessingPayment && selectedAddress != null &&
                            (paymentMethod != PaymentMethodType.CASH_ON_DELIVERY || codCaptchaInput == codCaptcha),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9F00)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(46.dp)
                            .testTag("btn_pay_securely")
                    ) {
                        if (isProcessingPayment) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (paymentMethod == PaymentMethodType.CASH_ON_DELIVERY) "Confirm Order" else "Pay Securely",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF1F3F6))
                .verticalScroll(rememberScrollState())
        ) {
            // STEP 1: Delivery Address Card
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = GladBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("1", color = GladBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DELIVERY ADDRESS",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }

                        TextButton(onClick = { showAddressModal = true }) {
                            Text("+ ADD NEW", color = GladBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (addresses.isEmpty()) {
                        Text("No saved address. Please add an address to continue.", color = GladPink, fontSize = 12.sp)
                    } else {
                        addresses.forEach { addr ->
                            val isSelected = selectedAddress?.id == addr.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) GladBlue else Color(0xFFE0E0E0),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        if (isSelected) GladBlue.copy(alpha = 0.04f) else Color.White,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.selectDeliveryAddress(addr) }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { viewModel.selectDeliveryAddress(addr) },
                                    colors = RadioButtonDefaults.colors(selectedColor = GladBlue)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(addr.fullName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = Color(0xFFECEFF1),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = addr.type.uppercase(),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextSecondary,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(addr.formattedAddress, fontSize = 12.sp, color = TextPrimary)
                                    Text("Phone: ${addr.phone}", fontSize = 12.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            // STEP 2: Order Items Summary Card
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = GladBlue.copy(alpha = 0.1f),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("2", color = GladBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ORDER SUMMARY (${cartItems.size} ITEMS)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    cartItems.take(2).forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${item.quantity}x ${item.product.title}",
                                fontSize = 12.sp,
                                color = TextPrimary,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = formatRupee(item.product.price * item.quantity),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                    if (cartItems.size > 2) {
                        Text(
                            text = "+ ${cartItems.size - 2} more items",
                            fontSize = 11.sp,
                            color = GladBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // STEP 3: Complete Payment Gateway Integration
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = GladBlue.copy(alpha = 0.1f),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("3", color = GladBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PAYMENT OPTIONS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Option A: UPI
                    PaymentOptionHeader(
                        icon = Icons.Default.Payments,
                        title = "UPI (Google Pay, PhonePe, Paytm, BHIM)",
                        subtitle = "Instant payment using any UPI app",
                        isSelected = paymentMethod == PaymentMethodType.UPI,
                        onClick = { viewModel.setPaymentMethod(PaymentMethodType.UPI) }
                    )

                    AnimatedVisibility(visible = paymentMethod == PaymentMethodType.UPI) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 36.dp, top = 8.dp, bottom = 8.dp)
                        ) {
                            val upiApps = listOf("Google Pay", "PhonePe", "Paytm", "BHIM", "Other UPI ID")
                            upiApps.forEach { app ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.setSelectedUpiApp(app) }
                                        .padding(vertical = 4.dp)
                                ) {
                                    RadioButton(
                                        selected = selectedUpiApp == app,
                                        onClick = { viewModel.setSelectedUpiApp(app) },
                                        colors = RadioButtonDefaults.colors(selectedColor = GladBlue)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(app, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                            }

                            if (selectedUpiApp == "Other UPI ID") {
                                OutlinedTextField(
                                    value = customUpiId,
                                    onValueChange = { viewModel.setCustomUpiId(it) },
                                    label = { Text("Enter UPI ID (e.g. mobile@upi)") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp)
                                )
                            }
                        }
                    }

                    Divider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 6.dp))

                    // Option B: Credit / Debit / ATM Card
                    PaymentOptionHeader(
                        icon = Icons.Default.CreditCard,
                        title = "Credit / Debit / ATM Card",
                        subtitle = "Visa, MasterCard, RuPay, Maestro",
                        isSelected = paymentMethod == PaymentMethodType.CREDIT_DEBIT_CARD,
                        onClick = { viewModel.setPaymentMethod(PaymentMethodType.CREDIT_DEBIT_CARD) }
                    )

                    AnimatedVisibility(visible = paymentMethod == PaymentMethodType.CREDIT_DEBIT_CARD) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 36.dp, top = 8.dp, bottom = 8.dp)
                        ) {
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = {
                                    val digits = it.filter { ch -> ch.isDigit() }.take(16)
                                    viewModel.setCardDetails(digits.chunked(4).joinToString(" "), cardExpiry, cardCvv, cardHolder)
                                },
                                label = { Text("Card Number") },
                                placeholder = { Text("XXXX XXXX XXXX 4242") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = cardExpiry,
                                    onValueChange = {
                                        viewModel.setCardDetails(cardNumber, it.take(5), cardCvv, cardHolder)
                                    },
                                    label = { Text("Valid Thru (MM/YY)") },
                                    placeholder = { Text("12/28") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = cardCvv,
                                    onValueChange = {
                                        viewModel.setCardDetails(cardNumber, cardExpiry, it.take(3), cardHolder)
                                    },
                                    label = { Text("CVV") },
                                    placeholder = { Text("123") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Divider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 6.dp))

                    // Option C: Net Banking
                    PaymentOptionHeader(
                        icon = Icons.Default.AccountBalance,
                        title = "Net Banking",
                        subtitle = "All major Indian banks supported",
                        isSelected = paymentMethod == PaymentMethodType.NET_BANKING,
                        onClick = { viewModel.setPaymentMethod(PaymentMethodType.NET_BANKING) }
                    )

                    AnimatedVisibility(visible = paymentMethod == PaymentMethodType.NET_BANKING) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 36.dp, top = 8.dp, bottom = 8.dp)
                        ) {
                            val banks = listOf("HDFC Bank", "State Bank of India", "ICICI Bank", "Axis Bank", "Kotak Mahindra")
                            banks.forEach { bank ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.setSelectedNetBank(bank) }
                                        .padding(vertical = 4.dp)
                                ) {
                                    RadioButton(
                                        selected = selectedNetBank == bank,
                                        onClick = { viewModel.setSelectedNetBank(bank) },
                                        colors = RadioButtonDefaults.colors(selectedColor = GladBlue)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(bank, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }

                    Divider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 6.dp))

                    // Option D: Cash on Delivery (COD) with Captcha
                    PaymentOptionHeader(
                        icon = Icons.Default.LocalAtm,
                        title = "Cash on Delivery",
                        subtitle = "Pay cash/UPI at your doorstep",
                        isSelected = paymentMethod == PaymentMethodType.CASH_ON_DELIVERY,
                        onClick = { viewModel.setPaymentMethod(PaymentMethodType.CASH_ON_DELIVERY) }
                    )

                    AnimatedVisibility(visible = paymentMethod == PaymentMethodType.CASH_ON_DELIVERY) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 36.dp, top = 8.dp, bottom = 8.dp)
                        ) {
                            Text(
                                text = "Enter characters shown below to confirm COD order:",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Captcha box
                                Surface(
                                    color = Color(0xFF263238),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = codCaptcha,
                                            color = GladGold,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 4.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        IconButton(
                                            onClick = { viewModel.refreshCaptcha() },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }

                                OutlinedTextField(
                                    value = codCaptchaInput,
                                    onValueChange = { viewModel.setCodCaptchaInput(it) },
                                    label = { Text("Code") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.width(110.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // --- Bank OTP Verification Simulation Dialog ---
    if (showOtpDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissOtpDialog() },
            icon = {
                Icon(Icons.Default.Security, contentDescription = null, tint = GladBlue, modifier = Modifier.size(36.dp))
            },
            title = {
                Text(
                    text = "Bank 3D-Secure Verification",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "A one-time OTP of 4 digits has been sent to your registered mobile ending in **210.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = { viewModel.setOtpInput(it) },
                        label = { Text("Enter OTP") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Simulated Demo OTP: 7412",
                        color = GladGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.submitOtpAndCompleteOrder() },
                    colors = ButtonDefaults.buttonColors(containerColor = GladBlue)
                ) {
                    Text("Verify & Pay")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissOtpDialog() }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    // --- Order Placed Celebration Dialog ---
    if (showOrderSuccess && lastPlacedOrder != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissOrderSuccess() },
            icon = {
                Surface(
                    shape = CircleShape,
                    color = GladGreen,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            },
            title = {
                Text(
                    text = "Order Placed Successfully!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Thank you for shopping with Glad Heart!",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Order ID: ${lastPlacedOrder?.orderId}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GladBlue
                    )
                    Text(
                        text = "Paid via: ${lastPlacedOrder?.paymentMethod}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Expected Delivery: In 2 Days",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GladGreen
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissOrderSuccess()
                        viewModel.viewOrderTracking(lastPlacedOrder!!)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GladBlue)
                ) {
                    Text("Track Order")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.dismissOrderSuccess()
                    viewModel.navigateTo(AppScreen.HOME)
                }) {
                    Text("Continue Shopping")
                }
            }
        )
    }

    // --- Add New Address Modal ---
    if (showAddressModal) {
        AddNewAddressDialog(
            onDismiss = { showAddressModal = false },
            onAdd = { newAddress ->
                viewModel.addAddress(newAddress)
                showAddressModal = false
            }
        )
    }
}

@Composable
private fun PaymentOptionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = GladBlue)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(icon, contentDescription = null, tint = if (isSelected) GladBlue else TextSecondary, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
            Text(subtitle, fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Composable
fun AddNewAddressDialog(
    onDismiss: () -> Unit,
    onAdd: (Address) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var houseNo by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Home") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Delivery Address", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name *") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.take(10) },
                    label = { Text("10-digit Mobile Number *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = pincode,
                    onValueChange = {
                        pincode = it.take(6)
                        if (it == "560001") {
                            city = "Bengaluru"
                            state = "Karnataka"
                        } else if (it == "110001") {
                            city = "New Delhi"
                            state = "Delhi"
                        }
                    },
                    label = { Text("Pincode *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = houseNo,
                    onValueChange = { houseNo = it },
                    label = { Text("Flat, House no., Building *") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Area, Colony, Street, Sector *") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City *") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = state,
                        onValueChange = { state = it },
                        label = { Text("State *") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Address Type: ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    listOf("Home", "Work").forEach { t ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { type = t }
                                .padding(horizontal = 6.dp)
                        ) {
                            RadioButton(selected = type == t, onClick = { type = t })
                            Text(t, fontSize = 12.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank() && pincode.isNotBlank()) {
                        onAdd(
                            Address(
                                id = "addr_" + UUID.randomUUID().toString().take(6),
                                fullName = name,
                                phone = phone,
                                pincode = pincode,
                                houseNo = houseNo.ifBlank { "Building 1" },
                                street = street.ifBlank { "Main Road" },
                                city = city.ifBlank { "Bengaluru" },
                                state = state.ifBlank { "Karnataka" },
                                type = type,
                                isDefault = true
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GladBlue)
            ) {
                Text("Save Address")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
