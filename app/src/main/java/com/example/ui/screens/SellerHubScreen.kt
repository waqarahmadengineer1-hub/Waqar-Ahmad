package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppScreen
import com.example.model.OrderStatus
import com.example.model.ProductCategory
import com.example.model.SellerKycStep
import com.example.ui.components.GladRatingBadge
import com.example.ui.components.ProductVisualCard
import com.example.ui.components.formatRupee
import com.example.ui.theme.GladBlue
import com.example.ui.theme.GladGold
import com.example.ui.theme.GladGreen
import com.example.ui.theme.GladPink
import com.example.ui.theme.KycVerified
import com.example.ui.theme.SellerNavy
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GladHeartViewModel

@Composable
fun SellerHubScreen(viewModel: GladHeartViewModel) {
    val sellerProfile by viewModel.sellerProfile.collectAsState()
    val sellerKycStep by viewModel.sellerKycStep.collectAsState()

    if (!sellerProfile.isRegistered) {
        SellerKycOnboardingView(viewModel = viewModel)
    } else {
        SellerDashboardView(viewModel = viewModel)
    }
}

@Composable
fun SellerKycOnboardingView(viewModel: GladHeartViewModel) {
    val step by viewModel.sellerKycStep.collectAsState()

    val storeName by viewModel.sellerStoreName.collectAsState()
    val ownerName by viewModel.sellerOwnerName.collectAsState()
    val email by viewModel.sellerEmail.collectAsState()
    val phone by viewModel.sellerPhone.collectAsState()

    val panNumber by viewModel.sellerPanNumber.collectAsState()
    val aadharNumber by viewModel.sellerAadharNumber.collectAsState()
    val gstin by viewModel.sellerGstin.collectAsState()
    val panUploaded by viewModel.panUploaded.collectAsState()
    val aadharUploaded by viewModel.aadharUploaded.collectAsState()
    val aadharOtpSent by viewModel.aadharOtpSent.collectAsState()
    val aadharOtpInput by viewModel.aadharOtpInput.collectAsState()
    val aadharOtpVerified by viewModel.aadharOtpVerified.collectAsState()

    val bankAccount by viewModel.sellerBankAccount.collectAsState()
    val bankIfsc by viewModel.sellerBankIfsc.collectAsState()
    val bankName by viewModel.sellerBankName.collectAsState()
    val isSubmittingKyc by viewModel.isSubmittingKyc.collectAsState()

    // PAN regex: 5 uppercase letters, 4 digits, 1 uppercase letter
    val isPanValid = remember(panNumber) {
        panNumber.length == 10 && panNumber.matches(Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}"))
    }

    val isAadharValid = remember(aadharNumber) {
        aadharNumber.replace(" ", "").length == 12
    }

    Scaffold(
        topBar = {
            Surface(color = SellerNavy, tonalElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.navigateTo(AppScreen.HOME) },
                            modifier = Modifier.testTag("btn_seller_hub_back")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Column {
                            Text(
                                text = "Glad Heart Supply Panel",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Seller Registration & e-KYC",
                                color = GladGold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Quick Fill Demo Button
                    Button(
                        onClick = { viewModel.quickFillKycForDemo() },
                        colors = ButtonDefaults.buttonColors(containerColor = GladGold),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("btn_quick_demo_kyc")
                    ) {
                        Text("⚡ Demo Fill", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Banner
            Card(
                shape = RoundedCornerShape(0.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.banner_seller_hub),
                        contentDescription = "Seller Hub Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Stepper Indicator (1. Store -> 2. PAN & Aadhaar KYC -> 3. Bank Account)
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KycStepIndicator(
                        stepNum = 1,
                        title = "Store Info",
                        isActive = step == SellerKycStep.STORE_DETAILS,
                        isCompleted = step == SellerKycStep.PAN_AADHAAR_KYC || step == SellerKycStep.BANK_DETAILS
                    )
                    Divider(modifier = Modifier.width(32.dp), color = Color(0xFFCBD5E1))
                    KycStepIndicator(
                        stepNum = 2,
                        title = "PAN & Aadhaar",
                        isActive = step == SellerKycStep.PAN_AADHAAR_KYC,
                        isCompleted = step == SellerKycStep.BANK_DETAILS
                    )
                    Divider(modifier = Modifier.width(32.dp), color = Color(0xFFCBD5E1))
                    KycStepIndicator(
                        stepNum = 3,
                        title = "Bank Payout",
                        isActive = step == SellerKycStep.BANK_DETAILS,
                        isCompleted = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step Content
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    when (step) {
                        SellerKycStep.STORE_DETAILS -> {
                            Text(
                                text = "Step 1: Business & Store Details",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = SellerNavy
                            )
                            Text(
                                text = "Enter your commercial store and merchant contact information.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = storeName,
                                onValueChange = { viewModel.setSellerStoreName(it) },
                                label = { Text("Display Store Name *") },
                                placeholder = { Text("e.g. HeartBeat Crafts & Electronics") },
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_store_name")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = ownerName,
                                onValueChange = { viewModel.setSellerOwnerName(it) },
                                label = { Text("Proprietor / Owner Full Name *") },
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_owner_name")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = email,
                                onValueChange = { viewModel.setSellerEmail(it) },
                                label = { Text("Business Email *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = phone,
                                onValueChange = { viewModel.setSellerPhone(it.take(10)) },
                                label = { Text("Registered Mobile Number *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { viewModel.nextKycStep() },
                                colors = ButtonDefaults.buttonColors(containerColor = SellerNavy),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("btn_step1_next")
                            ) {
                                Text("Continue to PAN & Aadhaar KYC →", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }

                        SellerKycStep.PAN_AADHAAR_KYC -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = KycVerified, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Step 2: PAN & Aadhaar Verification",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = SellerNavy
                                    )
                                    Text(
                                        text = "Mandatory Indian Government e-KYC for Supply Panel activation.",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // --- PAN CARD SECTION ---
                            Surface(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("1. PAN Card Number *", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SellerNavy)
                                        if (isPanValid) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = KycVerified, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Valid PAN Format", color = KycVerified, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    OutlinedTextField(
                                        value = panNumber,
                                        onValueChange = { viewModel.setSellerPanNumber(it) },
                                        placeholder = { Text("e.g. ABCDE1234F") },
                                        singleLine = true,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_pan_number")
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Upload / Scan PAN
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Button(
                                            onClick = { viewModel.simulateUploadPan() },
                                            colors = ButtonDefaults.buttonColors(containerColor = if (panUploaded) KycVerified else GladBlue),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.height(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (panUploaded) Icons.Default.Check else Icons.Default.FileUpload,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(if (panUploaded) "PAN Document Uploaded" else "Upload PAN Image")
                                        }

                                        if (panUploaded) {
                                            Text("Verified ✓", color = KycVerified, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // --- AADHAAR CARD SECTION ---
                            Surface(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("2. Aadhaar Card Number *", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SellerNavy)
                                        if (aadharOtpVerified) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = KycVerified, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("e-KYC Verified", color = KycVerified, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    OutlinedTextField(
                                        value = aadharNumber,
                                        onValueChange = { viewModel.setSellerAadharNumber(it) },
                                        placeholder = { Text("XXXX XXXX XXXX") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_aadhar_number")
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (!aadharOtpSent && !aadharOtpVerified) {
                                        Button(
                                            onClick = { viewModel.sendAadharOtp() },
                                            enabled = isAadharValid,
                                            colors = ButtonDefaults.buttonColors(containerColor = GladBlue),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.height(36.dp)
                                        ) {
                                            Text("Send Aadhaar OTP for Instant e-KYC")
                                        }
                                    } else if (aadharOtpSent && !aadharOtpVerified) {
                                        Column {
                                            Text("OTP sent to mobile linked with Aadhaar (Demo OTP: 654321)", fontSize = 11.sp, color = GladGreen)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                OutlinedTextField(
                                                    value = aadharOtpInput,
                                                    onValueChange = { viewModel.setAadharOtpInput(it) },
                                                    label = { Text("6-digit OTP") },
                                                    singleLine = true,
                                                    shape = RoundedCornerShape(6.dp),
                                                    modifier = Modifier.width(130.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Button(
                                                    onClick = { viewModel.verifyAadharOtp() },
                                                    colors = ButtonDefaults.buttonColors(containerColor = KycVerified),
                                                    shape = RoundedCornerShape(6.dp),
                                                    modifier = Modifier.height(48.dp)
                                                ) {
                                                    Text("Verify OTP")
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Upload Aadhaar Card
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Button(
                                            onClick = { viewModel.simulateUploadAadhar() },
                                            colors = ButtonDefaults.buttonColors(containerColor = if (aadharUploaded) KycVerified else GladBlue),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.height(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (aadharUploaded) Icons.Default.Check else Icons.Default.FileUpload,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(if (aadharUploaded) "Aadhaar Document Uploaded" else "Upload Aadhaar Front/Back")
                                        }

                                        if (aadharUploaded) {
                                            Text("Uploaded ✓", color = KycVerified, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // GSTIN (Optional)
                            OutlinedTextField(
                                value = gstin,
                                onValueChange = { viewModel.setSellerGstin(it) },
                                label = { Text("GSTIN Number (Optional if turnover < ₹40L)") },
                                placeholder = { Text("e.g. 29ABCDE1234F1Z5") },
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.previousKycStep() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64748B)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                ) {
                                    Text("Back")
                                }

                                Button(
                                    onClick = { viewModel.nextKycStep() },
                                    colors = ButtonDefaults.buttonColors(containerColor = SellerNavy),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(2f)
                                        .height(48.dp)
                                        .testTag("btn_step2_next")
                                ) {
                                    Text("Continue to Bank Payout →", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }

                        SellerKycStep.BANK_DETAILS -> {
                            Text(
                                text = "Step 3: Bank Account for Payout Settlements",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = SellerNavy
                            )
                            Text(
                                text = "Your sale proceeds will be directly credited every 7 days.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = bankAccount,
                                onValueChange = { viewModel.setSellerBankAccount(it) },
                                label = { Text("Bank Account Number *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_bank_account")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = bankIfsc,
                                onValueChange = { viewModel.setSellerBankIfsc(it) },
                                label = { Text("Bank IFSC Code *") },
                                placeholder = { Text("e.g. SBIN0040182") },
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_bank_ifsc")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = bankName,
                                onValueChange = { viewModel.setSellerBankName(it) },
                                label = { Text("Bank Name & Branch *") },
                                placeholder = { Text("State Bank of India") },
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                color = Color(0xFFECFDF5),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = KycVerified, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "PAN & Aadhaar details verified. By submitting, you agree to Glad Heart Seller Agreement.",
                                        fontSize = 11.sp,
                                        color = Color(0xFF065F46)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.previousKycStep() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64748B)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                ) {
                                    Text("Back")
                                }

                                Button(
                                    onClick = { viewModel.submitKycAndActivateSeller() },
                                    enabled = !isSubmittingKyc,
                                    colors = ButtonDefaults.buttonColors(containerColor = KycVerified),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(2f)
                                        .height(48.dp)
                                        .testTag("btn_submit_kyc")
                                ) {
                                    if (isSubmittingKyc) {
                                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                                    } else {
                                        Text("Activate Supply Panel ✓", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun KycStepIndicator(
    stepNum: Int,
    title: String,
    isActive: Boolean,
    isCompleted: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = when {
                isCompleted -> KycVerified
                isActive -> SellerNavy
                else -> Color(0xFFE2E8F0)
            },
            modifier = Modifier.size(30.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                } else {
                    Text(
                        text = stepNum.toString(),
                        color = if (isActive) Color.White else Color(0xFF64748B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) SellerNavy else TextSecondary
        )
    }
}

// --- ACTIVE SELLER DASHBOARD (SUPPLY PANEL) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardView(viewModel: GladHeartViewModel) {
    val sellerProfile by viewModel.sellerProfile.collectAsState()
    val products by viewModel.products.collectAsState()
    val sellerOrders by viewModel.sellerOrders.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddProductDialog by remember { mutableStateOf(false) }

    val mySellerProducts = remember(products) {
        products.filter { it.isSellerProduct || it.sellerName == sellerProfile.storeName }
    }

    Scaffold(
        topBar = {
            Surface(color = SellerNavy, tonalElevation = 4.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = GladGold,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Storefront, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = sellerProfile.storeName,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = "KYC Verified", tint = KycVerified, modifier = Modifier.size(16.dp))
                                }
                                Text(
                                    text = "PAN & Aadhaar Verified Merchant",
                                    color = KycVerified,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Switch Back to Customer Mode
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = GladBlue,
                            modifier = Modifier
                                .clickable { viewModel.navigateTo(AppScreen.HOME) }
                                .testTag("btn_switch_to_customer")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Customer Mode", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            Button(
                onClick = { showAddProductDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = GladPink),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("btn_add_product_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add New Product", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF1F5F9))
        ) {
            // Stats Row (2x2)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SellerStatCard(
                        title = "Total Earnings",
                        value = formatRupee(sellerProfile.totalEarnings),
                        icon = Icons.Default.MonetizationOn,
                        iconColor = GladGreen,
                        modifier = Modifier.weight(1f)
                    )
                    SellerStatCard(
                        title = "Orders Received",
                        value = sellerProfile.totalOrdersCount.toString(),
                        icon = Icons.Default.LocalShipping,
                        iconColor = GladBlue,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SellerStatCard(
                        title = "Active Listings",
                        value = mySellerProducts.size.toString(),
                        icon = Icons.Default.Inventory,
                        iconColor = GladPink,
                        modifier = Modifier.weight(1f)
                    )
                    SellerStatCard(
                        title = "Merchant Rating",
                        value = "${sellerProfile.rating} ★",
                        icon = Icons.Default.Star,
                        iconColor = GladGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // KYC Compliance Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = KycVerified, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "PAN: ${sellerProfile.panNumber} • Aadhaar: ${sellerProfile.aadharNumber.takeLast(4).padStart(12, '*')}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Settlement Bank: ${sellerProfile.bankName}",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Surface(
                        color = Color(0xFFECFDF5),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "ACTIVE",
                            color = KycVerified,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Tab Bar: Listings vs Orders to Fulfill
            androidx.compose.material3.TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = SellerNavy
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("My Products (${mySellerProducts.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Fulfill Orders (${sellerOrders.size})", fontWeight = FontWeight.Bold) }
                )
            }

            // Tab Content
            if (selectedTab == 0) {
                // Products List
                if (mySellerProducts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Inventory, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No products listed yet", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Click 'Add New Product' to list items in Glad Heart shop!", color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        items(mySellerProducts) { prod ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ProductVisualCard(
                                        imageType = prod.imageType,
                                        title = prod.title,
                                        modifier = Modifier.size(56.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(prod.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Text(
                                            "${formatRupee(prod.price)} • Stock: ${prod.stockCount} units",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GladBlue
                                        )
                                        Text(
                                            "Category: ${prod.category.displayName}",
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    Surface(
                                        color = if (prod.stockCount > 0) Color(0xFFECFDF5) else Color(0xFFFEF2F2),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = if (prod.stockCount > 0) "IN STOCK" else "OUT OF STOCK",
                                            color = if (prod.stockCount > 0) KycVerified else GladPink,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Orders to fulfill
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    items(sellerOrders) { order ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(order.orderId, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GladBlue)
                                    Surface(
                                        color = when (order.status) {
                                            OrderStatus.DELIVERED -> Color(0xFFECFDF5)
                                            OrderStatus.SHIPPED -> Color(0xFFEFF6FF)
                                            else -> Color(0xFFFEF3C7)
                                        },
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = order.status.title.uppercase(),
                                            color = when (order.status) {
                                                OrderStatus.DELIVERED -> KycVerified
                                                OrderStatus.SHIPPED -> GladBlue
                                                else -> Color(0xFFD97706)
                                            },
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "Buyer: ${order.address.fullName} • ${order.address.city}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                order.items.forEach { item ->
                                    Text(
                                        text = "• ${item.quantity}x ${item.product.title}",
                                        fontSize = 12.sp,
                                        color = TextPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Amount: ${formatRupee(order.totalAmount)}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = TextPrimary
                                    )

                                    if (order.status == OrderStatus.ORDERED || order.status == OrderStatus.PACKED) {
                                        Button(
                                            onClick = { viewModel.dispatchSellerOrder(order.orderId) },
                                            colors = ButtonDefaults.buttonColors(containerColor = SellerNavy),
                                            shape = RoundedCornerShape(4.dp),
                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Text("Dispatch Item →", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else if (order.status == OrderStatus.SHIPPED) {
                                        Button(
                                            onClick = { viewModel.markOrderDelivered(order.orderId) },
                                            colors = ButtonDefaults.buttonColors(containerColor = KycVerified),
                                            shape = RoundedCornerShape(4.dp),
                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Text("Mark Delivered ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal to Add New Product
    if (showAddProductDialog) {
        AddNewProductModal(
            onDismiss = { showAddProductDialog = false },
            onAdd = { title, cat, price, original, stock, desc ->
                viewModel.addNewSellerProduct(
                    title = title,
                    category = cat,
                    price = price,
                    originalPrice = original,
                    stock = stock,
                    description = desc,
                    features = listOf("100% Quality Guaranteed by Seller", "Rapid Dispatch with Glad Express")
                )
                showAddProductDialog = false
            }
        )
    }
}

@Composable
fun SellerStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.12f),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(title, fontSize = 10.sp, color = TextSecondary)
                Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewProductModal(
    onDismiss: () -> Unit,
    onAdd: (String, ProductCategory, Double, Double, Int, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ProductCategory.ELECTRONICS) }
    var priceText by remember { mutableStateOf("") }
    var originalPriceText by remember { mutableStateOf("") }
    var stockText by remember { mutableStateOf("25") }
    var description by remember { mutableStateOf("") }

    var expandedCategoryDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("List Product on Glad Heart", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Product Title *") },
                    placeholder = { Text("e.g. Wireless Noise Canceling Headphones") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_prod_title")
                )

                // Category selector
                ExposedDropdownMenuBox(
                    expanded = expandedCategoryDropdown,
                    onExpandedChange = { expandedCategoryDropdown = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryDropdown) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategoryDropdown,
                        onDismissRequest = { expandedCategoryDropdown = false }
                    ) {
                        ProductCategory.values().filter { it != ProductCategory.ALL }.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.displayName) },
                                onClick = {
                                    selectedCategory = cat
                                    expandedCategoryDropdown = false
                                }
                            )
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Selling Price (₹) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_new_prod_price")
                    )
                    OutlinedTextField(
                        value = originalPriceText,
                        onValueChange = { originalPriceText = it },
                        label = { Text("Original MRP (₹) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = stockText,
                    onValueChange = { stockText = it },
                    label = { Text("Available Stock Units *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & Highlights *") },
                    maxLines = 3,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceText.toDoubleOrNull() ?: 999.0
                    val original = originalPriceText.toDoubleOrNull() ?: (price * 1.4)
                    val stock = stockText.toIntOrNull() ?: 20
                    if (title.isNotBlank()) {
                        onAdd(title, selectedCategory, price, original, stock, description.ifBlank { "Top quality product by seller." })
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GladPink),
                modifier = Modifier.testTag("btn_publish_product")
            ) {
                Text("Publish to Store")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
