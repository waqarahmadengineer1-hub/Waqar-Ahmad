package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.Address
import com.example.model.AppScreen
import com.example.model.CartItem
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.SellerKycStep
import com.example.model.SellerProfile
import com.example.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class PaymentMethodType {
    UPI,
    CREDIT_DEBIT_CARD,
    NET_BANKING,
    CASH_ON_DELIVERY
}

class GladHeartViewModel : ViewModel() {

    // --- Navigation & Backstack ---
    private val _screenStack = MutableStateFlow(listOf(AppScreen.HOME))
    val currentScreen: StateFlow<AppScreen> = MutableStateFlow(AppScreen.HOME).apply {
        viewModelScope.launch {
            _screenStack.collect { stack ->
                value = stack.lastOrNull() ?: AppScreen.HOME
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (_screenStack.value.lastOrNull() != screen) {
            _screenStack.value = _screenStack.value + screen
        }
    }

    fun popBack(): Boolean {
        if (_screenStack.value.size > 1) {
            _screenStack.value = _screenStack.value.dropLast(1)
            return true
        }
        return false
    }

    // --- Search & Categories ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ProductCategory.ALL)
    val selectedCategory: StateFlow<ProductCategory> = _selectedCategory.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: ProductCategory) {
        _selectedCategory.value = category
    }

    // --- Product Catalog ---
    private val _products = MutableStateFlow(SampleData.initialProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        _products,
        _searchQuery,
        _selectedCategory
    ) { allProducts, query, category ->
        allProducts.filter { product ->
            val matchesCategory = category == ProductCategory.ALL || product.category == category
            val matchesQuery = query.isBlank() ||
                product.title.contains(query, ignoreCase = true) ||
                product.brand.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SampleData.initialProducts)

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
        navigateTo(AppScreen.PRODUCT_DETAIL)
    }

    // --- Wishlist ---
    private val _wishlistIds = MutableStateFlow<Set<String>>(setOf("prod_1", "prod_3"))
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    fun toggleWishlist(productId: String) {
        val current = _wishlistIds.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _wishlistIds.value = current
    }

    val wishlistProducts: StateFlow<List<Product>> = combine(_products, _wishlistIds) { all, ids ->
        all.filter { ids.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Cart Management ---
    private val _cartItems = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(product = SampleData.initialProducts[0], quantity = 1),
            CartItem(product = SampleData.initialProducts[4], quantity = 1)
        )
    )
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(product: Product, quantity: Int = 1) {
        val current = _cartItems.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.product.id == product.id }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            current.add(CartItem(product = product, quantity = quantity))
        }
        _cartItems.value = current
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
        } else {
            _cartItems.value = _cartItems.value.map {
                if (it.product.id == productId) it.copy(quantity = quantity) else it
            }
        }
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filterNot { it.product.id == productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Coupon logic
    private val _appliedCoupon = MutableStateFlow<String?>("GLADFIRST")
    val appliedCoupon: StateFlow<String?> = _appliedCoupon.asStateFlow()

    fun applyCoupon(code: String): Boolean {
        return if (code.trim().equals("GLADFIRST", ignoreCase = true) ||
            code.trim().equals("HEART50", ignoreCase = true) ||
            code.trim().equals("FLIPKARTVIP", ignoreCase = true)
        ) {
            _appliedCoupon.value = code.uppercase().trim()
            true
        } else {
            false
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
    }

    // Pricing calculation
    val cartTotalMRP: StateFlow<Double> = _cartItems.combine(_cartItems) { items, _ ->
        items.sumOf { it.product.originalPrice * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartSellingPrice: StateFlow<Double> = _cartItems.combine(_cartItems) { items, _ ->
        items.sumOf { it.product.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartDiscountMRP: StateFlow<Double> = combine(cartTotalMRP, cartSellingPrice) { mrp, selling ->
        (mrp - selling).coerceAtLeast(0.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val couponDiscount: StateFlow<Double> = combine(cartSellingPrice, _appliedCoupon) { selling, coupon ->
        when (coupon) {
            "GLADFIRST" -> if (selling >= 1000) 250.0 else 100.0
            "HEART50" -> (selling * 0.05).coerceAtMost(500.0)
            "FLIPKARTVIP" -> 300.0
            else -> 0.0
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val deliveryFee: StateFlow<Double> = cartSellingPrice.combine(_cartItems) { selling, items ->
        if (items.isEmpty()) 0.0 else if (selling >= 500) 0.0 else 40.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val finalPayableAmount: StateFlow<Double> = combine(
        cartSellingPrice,
        couponDiscount,
        deliveryFee
    ) { selling, coupDisc, delivery ->
        (selling - coupDisc + delivery + (if (selling > 0) 19.0 else 0.0)).coerceAtLeast(0.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // --- Addresses ---
    private val _addresses = MutableStateFlow(SampleData.sampleAddresses)
    val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()

    private val _selectedAddress = MutableStateFlow<Address?>(SampleData.sampleAddresses.firstOrNull())
    val selectedAddress: StateFlow<Address?> = _selectedAddress.asStateFlow()

    fun selectDeliveryAddress(address: Address) {
        _selectedAddress.value = address
    }

    fun addAddress(address: Address) {
        val updated = if (address.isDefault) {
            _addresses.value.map { it.copy(isDefault = false) } + address
        } else {
            _addresses.value + address
        }
        _addresses.value = updated
        _selectedAddress.value = address
    }

    fun deleteAddress(id: String) {
        val filtered = _addresses.value.filterNot { it.id == id }
        _addresses.value = filtered
        if (_selectedAddress.value?.id == id) {
            _selectedAddress.value = filtered.firstOrNull()
        }
    }

    // --- Payment Gateway & Checkout Flow ---
    private val _paymentMethod = MutableStateFlow(PaymentMethodType.UPI)
    val paymentMethod: StateFlow<PaymentMethodType> = _paymentMethod.asStateFlow()

    private val _selectedUpiApp = MutableStateFlow("Google Pay")
    val selectedUpiApp: StateFlow<String> = _selectedUpiApp.asStateFlow()

    private val _customUpiId = MutableStateFlow("")
    val customUpiId: StateFlow<String> = _customUpiId.asStateFlow()

    private val _cardNumber = MutableStateFlow("")
    val cardNumber: StateFlow<String> = _cardNumber.asStateFlow()

    private val _cardExpiry = MutableStateFlow("")
    val cardExpiry: StateFlow<String> = _cardExpiry.asStateFlow()

    private val _cardCvv = MutableStateFlow("")
    val cardCvv: StateFlow<String> = _cardCvv.asStateFlow()

    private val _cardHolder = MutableStateFlow("")
    val cardHolder: StateFlow<String> = _cardHolder.asStateFlow()

    private val _selectedNetBank = MutableStateFlow("HDFC Bank")
    val selectedNetBank: StateFlow<String> = _selectedNetBank.asStateFlow()

    private val _codCaptcha = MutableStateFlow((1000..9999).random().toString())
    val codCaptcha: StateFlow<String> = _codCaptcha.asStateFlow()

    private val _codCaptchaInput = MutableStateFlow("")
    val codCaptchaInput: StateFlow<String> = _codCaptchaInput.asStateFlow()

    private val _isProcessingPayment = MutableStateFlow(false)
    val isProcessingPayment: StateFlow<Boolean> = _isProcessingPayment.asStateFlow()

    private val _showOtpDialog = MutableStateFlow(false)
    val showOtpDialog: StateFlow<Boolean> = _showOtpDialog.asStateFlow()

    private val _otpInput = MutableStateFlow("")
    val otpInput: StateFlow<String> = _otpInput.asStateFlow()

    private val _lastPlacedOrder = MutableStateFlow<Order?>(null)
    val lastPlacedOrder: StateFlow<Order?> = _lastPlacedOrder.asStateFlow()

    private val _showOrderSuccess = MutableStateFlow(false)
    val showOrderSuccess: StateFlow<Boolean> = _showOrderSuccess.asStateFlow()

    fun setPaymentMethod(method: PaymentMethodType) {
        _paymentMethod.value = method
    }

    fun setSelectedUpiApp(app: String) {
        _selectedUpiApp.value = app
    }

    fun setCustomUpiId(id: String) {
        _customUpiId.value = id
    }

    fun setCardDetails(number: String, expiry: String, cvv: String, holder: String) {
        _cardNumber.value = number
        _cardExpiry.value = expiry
        _cardCvv.value = cvv
        _cardHolder.value = holder
    }

    fun setSelectedNetBank(bank: String) {
        _selectedNetBank.value = bank
    }

    fun setCodCaptchaInput(input: String) {
        _codCaptchaInput.value = input
    }

    fun refreshCaptcha() {
        _codCaptcha.value = (1000..9999).random().toString()
        _codCaptchaInput.value = ""
    }

    fun setOtpInput(otp: String) {
        _otpInput.value = otp
    }

    // Trigger Payment Flow
    fun processCheckout() {
        val address = _selectedAddress.value ?: return
        val currentCart = _cartItems.value
        if (currentCart.isEmpty()) return

        val methodLabel = when (_paymentMethod.value) {
            PaymentMethodType.UPI -> "UPI (${if (_selectedUpiApp.value == "Other UPI ID") _customUpiId.value.ifBlank { "UPI ID" } else _selectedUpiApp.value})"
            PaymentMethodType.CREDIT_DEBIT_CARD -> "Card ending in ${_cardNumber.value.takeLast(4).ifBlank { "4242" }}"
            PaymentMethodType.NET_BANKING -> "Net Banking (${_selectedNetBank.value})"
            PaymentMethodType.CASH_ON_DELIVERY -> "Cash on Delivery"
        }

        viewModelScope.launch {
            _isProcessingPayment.value = true
            delay(1200)

            if (_paymentMethod.value == PaymentMethodType.CASH_ON_DELIVERY) {
                _isProcessingPayment.value = false
                finalizeOrder(currentCart, address, methodLabel)
            } else {
                // Show realistic OTP / 3D Secure Verification
                _isProcessingPayment.value = false
                _otpInput.value = "7412" // Pre-filled for convenience
                _showOtpDialog.value = true
            }
        }
    }

    fun submitOtpAndCompleteOrder() {
        val address = _selectedAddress.value ?: return
        val currentCart = _cartItems.value
        val methodLabel = when (_paymentMethod.value) {
            PaymentMethodType.UPI -> "UPI (${_selectedUpiApp.value})"
            PaymentMethodType.CREDIT_DEBIT_CARD -> "Card ending in ${_cardNumber.value.takeLast(4).ifBlank { "4242" }}"
            PaymentMethodType.NET_BANKING -> "Net Banking (${_selectedNetBank.value})"
            PaymentMethodType.CASH_ON_DELIVERY -> "Cash on Delivery"
        }

        viewModelScope.launch {
            _showOtpDialog.value = false
            _isProcessingPayment.value = true
            delay(1000)
            _isProcessingPayment.value = false
            finalizeOrder(currentCart, address, methodLabel)
        }
    }

    fun dismissOtpDialog() {
        _showOtpDialog.value = false
    }

    private fun finalizeOrder(items: List<CartItem>, address: Address, paymentMethod: String) {
        val orderId = "OD" + (10000000000L..99999999999L).random().toString()
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val newOrder = Order(
            orderId = orderId,
            items = items,
            totalAmount = finalPayableAmount.value,
            address = address,
            paymentMethod = paymentMethod,
            orderDate = dateFormat.format(Date()),
            status = OrderStatus.ORDERED,
            trackingNumber = "GH-EXP-" + (1000000..9999999).random().toString(),
            estimatedDelivery = "Expected in 2 days"
        )

        _orders.value = listOf(newOrder) + _orders.value
        _lastPlacedOrder.value = newOrder
        _showOrderSuccess.value = true
        clearCart()

        // Also notify Seller Hub if items are seller items
        _sellerOrders.value = listOf(newOrder) + _sellerOrders.value
    }

    fun dismissOrderSuccess() {
        _showOrderSuccess.value = false
        navigateTo(AppScreen.ORDERS)
    }

    // --- Orders & Tracking ---
    private val _orders = MutableStateFlow(SampleData.sampleOrders)
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _selectedOrderForTracking = MutableStateFlow<Order?>(null)
    val selectedOrderForTracking: StateFlow<Order?> = _selectedOrderForTracking.asStateFlow()

    fun viewOrderTracking(order: Order) {
        _selectedOrderForTracking.value = order
        navigateTo(AppScreen.ORDER_TRACKING)
    }

    // --- SELLER HUB / SUPPLY PANEL ONBOARDING & DASHBOARD ---
    // User requirement: "jisme koi agar sell karna chahta hai to wo pan card or aadhar card se suply pannel open kare"
    private val _sellerProfile = MutableStateFlow(
        SellerProfile(
            isRegistered = false,
            storeName = "",
            ownerName = "",
            email = "",
            phone = "",
            panNumber = "",
            aadharNumber = "",
            gstin = "",
            bankAccountNumber = "",
            bankIfsc = "",
            bankName = "",
            registrationDate = "",
            totalEarnings = 48500.0,
            totalOrdersCount = 28,
            isKycApproved = false
        )
    )
    val sellerProfile: StateFlow<SellerProfile> = _sellerProfile.asStateFlow()

    private val _sellerKycStep = MutableStateFlow(SellerKycStep.STORE_DETAILS)
    val sellerKycStep: StateFlow<SellerKycStep> = _sellerKycStep.asStateFlow()

    // KYC Form fields
    private val _sellerStoreName = MutableStateFlow("")
    val sellerStoreName: StateFlow<String> = _sellerStoreName.asStateFlow()

    private val _sellerOwnerName = MutableStateFlow("Waqar Ahmad")
    val sellerOwnerName: StateFlow<String> = _sellerOwnerName.asStateFlow()

    private val _sellerEmail = MutableStateFlow("seller.waqar@gladheart.in")
    val sellerEmail: StateFlow<String> = _sellerEmail.asStateFlow()

    private val _sellerPhone = MutableStateFlow("9876543210")
    val sellerPhone: StateFlow<String> = _sellerPhone.asStateFlow()

    private val _sellerPanNumber = MutableStateFlow("")
    val sellerPanNumber: StateFlow<String> = _sellerPanNumber.asStateFlow()

    private val _sellerAadharNumber = MutableStateFlow("")
    val sellerAadharNumber: StateFlow<String> = _sellerAadharNumber.asStateFlow()

    private val _sellerGstin = MutableStateFlow("")
    val sellerGstin: StateFlow<String> = _sellerGstin.asStateFlow()

    private val _panUploaded = MutableStateFlow(false)
    val panUploaded: StateFlow<Boolean> = _panUploaded.asStateFlow()

    private val _aadharUploaded = MutableStateFlow(false)
    val aadharUploaded: StateFlow<Boolean> = _aadharUploaded.asStateFlow()

    private val _aadharOtpSent = MutableStateFlow(false)
    val aadharOtpSent: StateFlow<Boolean> = _aadharOtpSent.asStateFlow()

    private val _aadharOtpInput = MutableStateFlow("")
    val aadharOtpInput: StateFlow<String> = _aadharOtpInput.asStateFlow()

    private val _aadharOtpVerified = MutableStateFlow(false)
    val aadharOtpVerified: StateFlow<Boolean> = _aadharOtpVerified.asStateFlow()

    private val _sellerBankAccount = MutableStateFlow("")
    val sellerBankAccount: StateFlow<String> = _sellerBankAccount.asStateFlow()

    private val _sellerBankIfsc = MutableStateFlow("")
    val sellerBankIfsc: StateFlow<String> = _sellerBankIfsc.asStateFlow()

    private val _sellerBankName = MutableStateFlow("State Bank of India")
    val sellerBankName: StateFlow<String> = _sellerBankName.asStateFlow()

    private val _isSubmittingKyc = MutableStateFlow(false)
    val isSubmittingKyc: StateFlow<Boolean> = _isSubmittingKyc.asStateFlow()

    // KYC field setters
    fun setSellerStoreName(name: String) { _sellerStoreName.value = name }
    fun setSellerOwnerName(name: String) { _sellerOwnerName.value = name }
    fun setSellerEmail(email: String) { _sellerEmail.value = email }
    fun setSellerPhone(phone: String) { _sellerPhone.value = phone }
    fun setSellerPanNumber(pan: String) { _sellerPanNumber.value = pan.uppercase().filter { it.isLetterOrDigit() }.take(10) }

    fun setSellerAadharNumber(rawAadhar: String) {
        val digits = rawAadhar.filter { it.isDigit() }.take(12)
        // Format as XXXX XXXX XXXX
        val formatted = digits.chunked(4).joinToString(" ")
        _sellerAadharNumber.value = formatted
    }

    fun setSellerGstin(gstin: String) { _sellerGstin.value = gstin.uppercase().take(15) }
    fun setSellerBankAccount(acc: String) { _sellerBankAccount.value = acc.filter { it.isDigit() }.take(18) }
    fun setSellerBankIfsc(ifsc: String) { _sellerBankIfsc.value = ifsc.uppercase().take(11) }
    fun setSellerBankName(name: String) { _sellerBankName.value = name }
    fun setAadharOtpInput(otp: String) { _aadharOtpInput.value = otp.filter { it.isDigit() }.take(6) }

    fun simulateUploadPan() {
        viewModelScope.launch {
            _panUploaded.value = true
        }
    }

    fun simulateUploadAadhar() {
        viewModelScope.launch {
            _aadharUploaded.value = true
        }
    }

    fun sendAadharOtp() {
        if (_sellerAadharNumber.value.replace(" ", "").length >= 12) {
            _aadharOtpSent.value = true
            _aadharOtpInput.value = "654321" // Convenient default simulation OTP
        }
    }

    fun verifyAadharOtp(): Boolean {
        if (_aadharOtpInput.value == "654321" || _aadharOtpInput.value.length == 6) {
            _aadharOtpVerified.value = true
            return true
        }
        return false
    }

    fun nextKycStep() {
        when (_sellerKycStep.value) {
            SellerKycStep.STORE_DETAILS -> _sellerKycStep.value = SellerKycStep.PAN_AADHAAR_KYC
            SellerKycStep.PAN_AADHAAR_KYC -> _sellerKycStep.value = SellerKycStep.BANK_DETAILS
            SellerKycStep.BANK_DETAILS -> submitKycAndActivateSeller()
            SellerKycStep.VERIFIED_ACTIVE -> {}
        }
    }

    fun previousKycStep() {
        when (_sellerKycStep.value) {
            SellerKycStep.PAN_AADHAAR_KYC -> _sellerKycStep.value = SellerKycStep.STORE_DETAILS
            SellerKycStep.BANK_DETAILS -> _sellerKycStep.value = SellerKycStep.PAN_AADHAAR_KYC
            else -> {}
        }
    }

    fun activateSellerDirectly() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        _sellerProfile.value = SellerProfile(
            isRegistered = true,
            storeName = _sellerStoreName.value.ifBlank { "Glad Heart SuperStore" },
            ownerName = _sellerOwnerName.value.ifBlank { "Waqar Ahmad" },
            email = _sellerEmail.value.ifBlank { "waqar.seller@gladheart.in" },
            phone = _sellerPhone.value.ifBlank { "+91 98765 43210" },
            panNumber = _sellerPanNumber.value.ifBlank { "ABCDE1234F" },
            aadharNumber = _sellerAadharNumber.value.ifBlank { "5421 9876 1234" },
            gstin = _sellerGstin.value.ifBlank { "29ABCDE1234F1Z5" },
            bankAccountNumber = _sellerBankAccount.value.ifBlank { "9876543210987" },
            bankIfsc = _sellerBankIfsc.value.ifBlank { "SBIN0001234" },
            bankName = _sellerBankName.value.ifBlank { "State Bank of India" },
            registrationDate = dateFormat.format(Date()),
            totalEarnings = 12450.0,
            totalOrdersCount = 8,
            isKycApproved = true,
            rating = 4.9f
        )
        _sellerKycStep.value = SellerKycStep.VERIFIED_ACTIVE
        _isSubmittingKyc.value = false
    }

    fun submitKycAndActivateSeller() {
        viewModelScope.launch {
            _isSubmittingKyc.value = true
            delay(1500)
            activateSellerDirectly()
        }
    }

    // Quick demo populate for instant testing
    fun quickFillKycForDemo() {
        _sellerStoreName.value = "HeartBeat Crafts & Electronics"
        _sellerOwnerName.value = "Waqar Ahmad"
        _sellerEmail.value = "waqarahmadengineer1@gmail.com"
        _sellerPhone.value = "9876543210"
        _sellerPanNumber.value = "BLZPA8841K"
        _sellerAadharNumber.value = "9842 6150 7731"
        _sellerGstin.value = "29BLZPA8841K1ZP"
        _sellerBankAccount.value = "3098124810293"
        _sellerBankIfsc.value = "SBIN0040182"
        _sellerBankName.value = "State Bank of India - Indiranagar"
        _panUploaded.value = true
        _aadharUploaded.value = true
        _aadharOtpSent.value = true
        _aadharOtpInput.value = "654321"
        _aadharOtpVerified.value = true
    }

    // --- SELLER DASHBOARD ACTIONS (ADD PRODUCTS & FULFILL ORDERS) ---
    private val _sellerOrders = MutableStateFlow(SampleData.sampleOrders)
    val sellerOrders: StateFlow<List<Order>> = _sellerOrders.asStateFlow()

    fun addNewSellerProduct(
        title: String,
        category: ProductCategory,
        price: Double,
        originalPrice: Double,
        stock: Int,
        description: String,
        features: List<String>
    ) {
        val newProduct = Product(
            id = "seller_prod_" + UUID.randomUUID().toString().take(6),
            title = title,
            brand = _sellerProfile.value.storeName.ifBlank { "Glad Heart Seller" },
            category = category,
            price = price,
            originalPrice = originalPrice,
            rating = 5.0f,
            ratingsCount = 1,
            description = description,
            features = features.ifEmpty { listOf("100% Verified Quality by Seller", "Fast Dispatch with Glad Express") },
            stockCount = stock,
            sellerName = _sellerProfile.value.storeName.ifBlank { "Glad Heart Seller" },
            badge = "Verified Seller",
            isGladAssured = true,
            imageType = category.name,
            isSellerProduct = true
        )

        // Prepend to catalog so it is immediately visible at the top of customer shop!
        _products.value = listOf(newProduct) + _products.value
    }

    fun dispatchSellerOrder(orderId: String) {
        _orders.value = _orders.value.map { order ->
            if (order.orderId == orderId) {
                order.copy(status = OrderStatus.SHIPPED)
            } else order
        }
        _sellerOrders.value = _orders.value
        _sellerProfile.value = _sellerProfile.value.copy(
            totalEarnings = _sellerProfile.value.totalEarnings + 1200.0,
            totalOrdersCount = _sellerProfile.value.totalOrdersCount + 1
        )
    }

    fun markOrderDelivered(orderId: String) {
        _orders.value = _orders.value.map { order ->
            if (order.orderId == orderId) {
                order.copy(status = OrderStatus.DELIVERED)
            } else order
        }
        _sellerOrders.value = _orders.value
    }

    // --- User Profile ---
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun updateUserProfile(name: String, email: String, phone: String) {
        _userProfile.value = _userProfile.value.copy(
            name = name,
            email = email,
            phone = phone
        )
    }
}
