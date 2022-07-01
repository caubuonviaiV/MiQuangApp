package com.example.mrsmuarestaurant.model

class Checkout {
    lateinit var user_id: String
    lateinit var name: String
    lateinit var email: String
    lateinit var phone: String
    lateinit var address: String
    lateinit var content: String
    lateinit var payment_type: String
    lateinit var status : String
    lateinit var method : String

    lateinit var total_item :String
    lateinit var total_price :String


    var products = ArrayList<Cart>()

    class Cart {
        lateinit var id: String
        lateinit var total_item: String
        lateinit var total_price: String
    }
}