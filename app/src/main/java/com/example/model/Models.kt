package com.example.model

enum class ProductCategory(val displayName: String, val iconName: String) {
    ALL("All", "Category"),
    MOBILES("Mobiles", "PhoneAndroid"),
    ELECTRONICS("Electronics", "Laptop"),
    FASHION("Fashion", "Checkroom"),
    BEAUTY("Beauty", "Face"),
    HOME_KITCHEN("Home", "Home"),
    APPLIANCES("Appliances", "Kitchen"),
    GROCERY("Grocery", "LocalGroceryStore")
}

data class Product(
    val id: String,
    val title: String,
    val brand: String,
    val category: ProductCategory,
    val price: Double,
    val originalPrice: Double,
    val rating: Float = 4.4f,
    val ratingsCount: Int = 1250,
    val description: String,
    val features: List<String> = emptyList(),
    val stockCount: Int = 25,
    val sellerName: String = "Glad Heart Retail",
    val badge: String? = "Glad Assured",
    val isGladAssured: Boolean = true,
    val imageType: String = "ELECTRONICS", // Used for rich custom visual presentation
    val isSellerProduct: Boolean = false
) {
    val discountPercent: Int
        get() = if (originalPrice > price) {
            (((originalPrice - price) / originalPrice) * 100).toInt()
        } else 0
}

data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val selectedSize: String? = null,
    val selectedColor: String? = null
)

data class Address(
    val id: String,
    val fullName: String,
    val phone: String,
    val pincode: String,
    val houseNo: String,
    val street: String,
    val city: String,
    val state: String,
    val type: String = "Home", // "Home", "Work"
    val isDefault: Boolean = false
) {
    val formattedAddress: String
        get() = "$houseNo, $street, $city, $state - $pincode"
}

enum class OrderStatus(val title: String, val stepIndex: Int) {
    ORDERED("Order Confirmed", 0),
    PACKED("Packed", 1),
    SHIPPED("Shipped", 2),
    OUT_FOR_DELIVERY("Out for Delivery", 3),
    DELIVERED("Delivered", 4)
}

data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val address: Address,
    val paymentMethod: String,
    val orderDate: String,
    val status: OrderStatus = OrderStatus.ORDERED,
    val trackingNumber: String,
    val estimatedDelivery: String
)

enum class SellerKycStep {
    STORE_DETAILS,
    PAN_AADHAAR_KYC,
    BANK_DETAILS,
    VERIFIED_ACTIVE
}

data class SellerProfile(
    val isRegistered: Boolean = false,
    val storeName: String = "",
    val ownerName: String = "",
    val email: String = "",
    val phone: String = "",
    val panNumber: String = "",
    val aadharNumber: String = "",
    val gstin: String = "",
    val bankAccountNumber: String = "",
    val bankIfsc: String = "",
    val bankName: String = "",
    val registrationDate: String = "",
    val totalEarnings: Double = 0.0,
    val totalOrdersCount: Int = 0,
    val isKycApproved: Boolean = false,
    val rating: Float = 4.8f
)

data class UserProfile(
    val name: String = "Waqar Ahmad",
    val email: String = "waqarahmadengineer1@gmail.com",
    val phone: String = "+91 98765 43210",
    val isPlusMember: Boolean = true,
    val gladCoins: Int = 450,
    val profileImageUri: String? = null
)

enum class AppScreen {
    HOME,
    PRODUCT_DETAIL,
    CART,
    CHECKOUT,
    ORDERS,
    ORDER_TRACKING,
    WISHLIST,
    PROFILE,
    SELLER_HUB
}
