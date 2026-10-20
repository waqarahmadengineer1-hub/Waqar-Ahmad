package com.example.data

import com.example.model.Address
import com.example.model.CartItem
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.ProductCategory

object SampleData {

    val sampleAddresses = listOf(
        Address(
            id = "addr_1",
            fullName = "Waqar Ahmad",
            phone = "9876543210",
            pincode = "560001",
            houseNo = "Flat 402, Royal Residency",
            street = "MG Road, Indiranagar",
            city = "Bengaluru",
            state = "Karnataka",
            type = "Home",
            isDefault = true
        ),
        Address(
            id = "addr_2",
            fullName = "Waqar Ahmad (Tech Office)",
            phone = "9876543210",
            pincode = "560103",
            houseNo = "Building 3B, Tech Park",
            street = "Outer Ring Road, Bellandur",
            city = "Bengaluru",
            state = "Karnataka",
            type = "Work",
            isDefault = false
        )
    )

    val initialProducts = listOf(
        Product(
            id = "prod_1",
            title = "Apple iPhone 15 (128 GB) - Blue",
            brand = "Apple",
            category = ProductCategory.MOBILES,
            price = 58999.0,
            originalPrice = 69900.0,
            rating = 4.6f,
            ratingsCount = 18450,
            description = "Experience Dynamic Island, 48MP Main camera with 2x Telephoto, and USB-C in an all-new durable color-infused glass and aluminum design.",
            features = listOf(
                "128 GB ROM | 15.49 cm (6.1 inch) Super Retina XDR Display",
                "48MP + 12MP | 12MP Front Camera",
                "A16 Bionic Chip, 6 Core Processor",
                "Dynamic Island bubbles up alerts and Live Activities"
            ),
            stockCount = 42,
            sellerName = "Glad Heart SuperComNet",
            badge = "Glad Assured",
            isGladAssured = true,
            imageType = "PHONE"
        ),
        Product(
            id = "prod_2",
            title = "Samsung Galaxy S24 5G (Onyx Black, 256 GB)",
            brand = "Samsung",
            category = ProductCategory.MOBILES,
            price = 64999.0,
            originalPrice = 79999.0,
            rating = 4.5f,
            ratingsCount = 8920,
            description = "Galaxy AI is here. Search like never before, get quick language translation on a call, convert voice into text then summarize it, and effortlessly edit photos.",
            features = listOf(
                "8 GB RAM | 256 GB ROM",
                "15.75 cm (6.2 inch) Full HD+ Dynamic AMOLED 2X Display",
                "50MP + 12MP + 10MP | 12MP Front Camera",
                "4000 mAh Battery with Super Fast Charging"
            ),
            stockCount = 18,
            sellerName = "RetailNet Solutions",
            badge = "Hot Deal",
            isGladAssured = true,
            imageType = "PHONE"
        ),
        Product(
            id = "prod_3",
            title = "Sony WH-1000XM5 Wireless Active Noise Cancelling Headphones",
            brand = "Sony",
            category = ProductCategory.ELECTRONICS,
            price = 26990.0,
            originalPrice = 34990.0,
            rating = 4.7f,
            ratingsCount = 5340,
            description = "Industry-leading noise cancellation with two processors and 8 microphones. Magnificent Sound, engineered to perfection with High-Res Audio.",
            features = listOf(
                "Up to 30-hour battery life with quick charge (3 min charge = 3 hours playback)",
                "Ultra-comfortable, lightweight design with soft fit leather",
                "Multipoint connection lets you pair two Bluetooth devices simultaneously",
                "Speak-to-chat and quick attention mode"
            ),
            stockCount = 30,
            sellerName = "AudioPrime India",
            badge = "Glad Assured",
            isGladAssured = true,
            imageType = "HEADPHONES"
        ),
        Product(
            id = "prod_4",
            title = "Nike Air Zoom Pegasus 40 Men Running Shoes",
            brand = "Nike",
            category = ProductCategory.FASHION,
            price = 8495.0,
            originalPrice = 11495.0,
            rating = 4.4f,
            ratingsCount = 3120,
            description = "A springy ride for any run, the Peg's familiar, just-for-you feel returns to help you accomplish your goals with engineered mesh upper.",
            features = listOf(
                "Nike React technology delivers a smooth, responsive ride",
                "Zoom Air unit in the forefoot and heel provides energized snap",
                "Engineered mesh for breathability and flexible support",
                "Waffle-inspired outsole gives exceptional traction"
            ),
            stockCount = 55,
            sellerName = "StrideSports Store",
            badge = "Bestseller",
            isGladAssured = true,
            imageType = "SHOES"
        ),
        Product(
            id = "prod_5",
            title = "boAt Airdopes 141 Bluetooth TWS Earbuds (Bold Black)",
            brand = "boAt",
            category = ProductCategory.ELECTRONICS,
            price = 1099.0,
            originalPrice = 4490.0,
            rating = 4.2f,
            ratingsCount = 98400,
            description = "Equipped with 8mm drivers, ENx Environmental Noise Cancellation technology for clear calls, and up to 42 hours total playback.",
            features = listOf(
                "42 Hours Playback time | ASAP Charge (5 mins charge = 75 mins play)",
                "ENx Environmental Noise Cancellation for crystal-clear calls",
                "IPX4 Water & Sweat Resistance",
                "BEAST Mode for super low latency gaming"
            ),
            stockCount = 120,
            sellerName = "Glad Heart Retail",
            badge = "Top Deal",
            isGladAssured = true,
            imageType = "EARBUDS"
        ),
        Product(
            id = "prod_6",
            title = "Men Pure Cotton Casual Slim Fit Spread Collar Shirt",
            brand = "Highlander",
            category = ProductCategory.FASHION,
            price = 699.0,
            originalPrice = 1799.0,
            rating = 4.3f,
            ratingsCount = 14200,
            description = "Crisp and breathable 100% combed cotton casual shirt for office wear and weekend get-togethers.",
            features = listOf(
                "Fabric: 100% Breathable Combed Cotton",
                "Pattern: Solid with clean stitch finishing",
                "Sleeve: Full Sleeve with buttoned cuffs",
                "Collar: Spread Collar"
            ),
            stockCount = 80,
            sellerName = "TrendyFabrics Hub",
            badge = "Glad Assured",
            isGladAssured = true,
            imageType = "CLOTHING"
        ),
        Product(
            id = "prod_7",
            title = "Prestige Iris 750W Mixer Grinder with 3 Stainless Steel Jars",
            brand = "Prestige",
            category = ProductCategory.HOME_KITCHEN,
            price = 2899.0,
            originalPrice = 6195.0,
            rating = 4.2f,
            ratingsCount = 28300,
            description = "Robust 750-watt heavy duty motor designed for dry & wet Indian grinding, with overload protection and anti-skid feet.",
            features = listOf(
                "750 Watts powerful copper winding motor",
                "3 Stainless steel jars + 1 Juicer jar with sieve",
                "Multi-purpose blade system for fine pastes and powders",
                "2 Years manufacturer warranty"
            ),
            stockCount = 35,
            sellerName = "KitchenWorld Direct",
            badge = "Glad Assured",
            isGladAssured = true,
            imageType = "APPLIANCE"
        ),
        Product(
            id = "prod_8",
            title = "Lakmé Absolute Skin Gloss Gel Face Crème (50g)",
            brand = "Lakmé",
            category = ProductCategory.BEAUTY,
            price = 475.0,
            originalPrice = 850.0,
            rating = 4.4f,
            ratingsCount = 6720,
            description = "Enriched with mineral-laden glacial water, this gel crème hydrates deeply to give skin an ultra-glossy, dewy radiant glow.",
            features = listOf(
                "Lightweight hydrating gel texture",
                "Infused with moisture droplets for 24-hr glow",
                "Dermatologically tested and non-comedogenic",
                "Ideal for all Indian skin types"
            ),
            stockCount = 65,
            sellerName = "BeautyGlow Hub",
            badge = "Bestseller",
            isGladAssured = true,
            imageType = "BEAUTY"
        ),
        Product(
            id = "prod_9",
            title = "Fastrack Limitless Glide Smartwatch 1.78\" AMOLED",
            brand = "Fastrack",
            category = ProductCategory.ELECTRONICS,
            price = 1799.0,
            originalPrice = 4995.0,
            rating = 4.1f,
            ratingsCount = 15800,
            description = "Ultra-bright AMOLED display, Bluetooth calling with single-sync BT 5.3, 100+ sports modes and continuous health suite.",
            features = listOf(
                "1.78 inch UltraVU AMOLED Display with Always-on option",
                "SingleSync BT Calling with crystal clear speaker & mic",
                "SpO2, Heart Rate, Sleep & Stress Monitor",
                "Up to 7 days battery standby"
            ),
            stockCount = 45,
            sellerName = "WatchHub India",
            badge = "Hot Deal",
            isGladAssured = true,
            imageType = "SMARTWATCH"
        ),
        Product(
            id = "prod_10",
            title = "Fortune Sunlite Refined Sunflower Oil Pouch (1 L)",
            brand = "Fortune",
            category = ProductCategory.GROCERY,
            price = 135.0,
            originalPrice = 175.0,
            rating = 4.6f,
            ratingsCount = 42000,
            description = "Light, healthy and enriched with Vitamin A & D to keep heart healthy and active.",
            features = listOf(
                "Rich in natural antioxidants & Vitamin E",
                "High smoke point, perfect for daily Indian cooking",
                "Zero cholesterol and trans fats",
                "Pure & hygienically packed"
            ),
            stockCount = 200,
            sellerName = "Glad Heart Grocery Supermart",
            badge = "Glad Assured",
            isGladAssured = true,
            imageType = "GROCERY"
        )
    )

    val sampleOrders = listOf(
        Order(
            orderId = "OD30918274192",
            items = listOf(
                CartItem(product = initialProducts[2], quantity = 1)
            ),
            totalAmount = 26990.0,
            address = sampleAddresses[0],
            paymentMethod = "UPI (Google Pay)",
            orderDate = "18 Oct 2026, 04:30 PM",
            status = OrderStatus.SHIPPED,
            trackingNumber = "GH-EXP-8930129",
            estimatedDelivery = "Tomorrow, by 8:00 PM"
        ),
        Order(
            orderId = "OD10827364120",
            items = listOf(
                CartItem(product = initialProducts[4], quantity = 1),
                CartItem(product = initialProducts[5], quantity = 2)
            ),
            totalAmount = 2497.0,
            address = sampleAddresses[0],
            paymentMethod = "Credit Card (HDFC)",
            orderDate = "14 Oct 2026, 11:15 AM",
            status = OrderStatus.DELIVERED,
            trackingNumber = "GH-EXP-1102934",
            estimatedDelivery = "Delivered on 16 Oct"
        )
    )
}
