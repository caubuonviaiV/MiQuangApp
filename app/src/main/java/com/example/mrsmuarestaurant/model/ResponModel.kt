package com.example.mrsmuarestaurant.model

class ResponModel {
    var success = 0
    lateinit var message: String
    var user = User()

    var product: ArrayList<Product> = ArrayList()
    var slider: ArrayList<Slider> = ArrayList()
    var post: ArrayList<Post> = ArrayList()

    var districts: ArrayList<ModelAddress> = ArrayList()
    var wards: ArrayList<ModelAddress> = ArrayList()

    var transactions: ArrayList<Transaction> = ArrayList()
    var transaction = Transaction()

}