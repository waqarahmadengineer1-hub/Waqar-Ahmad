package com.example

import com.example.model.Address
import com.example.model.ProductCategory
import com.example.viewmodel.GladHeartViewModel
import com.example.viewmodel.PaymentMethodType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

    private lateinit var viewModel: GladHeartViewModel

    @Before
    fun setUp() {
        viewModel = GladHeartViewModel()
    }

    @Test
    fun testInitialCatalogLoaded() {
        val products = viewModel.products.value
        assertTrue("Products catalog should not be empty", products.isNotEmpty())
        assertTrue("Should contain initial phones", products.any { it.category == ProductCategory.MOBILES })
    }

    @Test
    fun testCartOperationsAndPricing() {
        val initialCart = viewModel.cartItems.value
        val initialCount = initialCart.sumOf { it.quantity }

        val firstProduct = viewModel.products.value.first()
        viewModel.addToCart(firstProduct, 1)

        val updatedCart = viewModel.cartItems.value
        assertEquals(initialCount + 1, updatedCart.sumOf { it.quantity })

        // Test coupon application
        val couponApplied = viewModel.applyCoupon("GLADFIRST")
        assertTrue("Valid coupon GLADFIRST should be applied", couponApplied)
        assertEquals("GLADFIRST", viewModel.appliedCoupon.value)
    }

    @Test
    fun testPanCardValidationLogic() {
        // Valid PAN: 5 uppercase letters, 4 digits, 1 uppercase letter
        val validPan = "ABCDE1234F"
        val invalidPanShort = "ABC12"
        val invalidPanFormat = "12345ABCDE"

        val panRegex = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        assertTrue(validPan.matches(panRegex))
        assertFalse(invalidPanShort.matches(panRegex))
        assertFalse(invalidPanFormat.matches(panRegex))
    }

    @Test
    fun testAadhaarCardFormatting() {
        viewModel.setSellerAadharNumber("987654321098")
        assertEquals("9876 5432 1098", viewModel.sellerAadharNumber.value)
    }

    @Test
    fun testSellerKycAndProductPublishing() = runTest {
        viewModel.quickFillKycForDemo()
        viewModel.activateSellerDirectly()

        // Verify seller is registered
        val seller = viewModel.sellerProfile.value
        assertTrue("Seller should be registered", seller.isRegistered)
        assertTrue("Seller KYC should be approved", seller.isKycApproved)
        assertEquals("BLZPA8841K", seller.panNumber)

        // Add a new product via Seller Supply Panel
        viewModel.addNewSellerProduct(
            title = "Handcrafted Leather Wallet",
            category = ProductCategory.FASHION,
            price = 799.0,
            originalPrice = 1499.0,
            stock = 50,
            description = "Genuine premium leather wallet with RFID blocking.",
            features = listOf("100% Genuine Leather", "6 Card Slots")
        )

        // Check if product is prepended into the store catalog
        val topProduct = viewModel.products.value.first()
        assertEquals("Handcrafted Leather Wallet", topProduct.title)
        assertTrue(topProduct.isSellerProduct)
    }

    @Test
    fun testPaymentMethodSelection() {
        viewModel.setPaymentMethod(PaymentMethodType.UPI)
        assertEquals(PaymentMethodType.UPI, viewModel.paymentMethod.value)

        viewModel.setPaymentMethod(PaymentMethodType.CASH_ON_DELIVERY)
        assertEquals(PaymentMethodType.CASH_ON_DELIVERY, viewModel.paymentMethod.value)
    }
}
