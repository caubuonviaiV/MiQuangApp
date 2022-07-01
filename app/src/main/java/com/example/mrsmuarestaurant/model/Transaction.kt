package com.example.mrsmuarestaurant.model

class Transaction {
    var id = 0
    var name = ""
    var email = ""
    var phone = ""
    var address = ""
    var total_price = 0
    var total_item = ""

    var status = ""
    var created_at = ""
    val carts = ArrayList<DetailTransaction>()
}