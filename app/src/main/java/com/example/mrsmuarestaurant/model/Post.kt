package com.example.mrsmuarestaurant.model

import java.io.Serializable

class Post : Serializable {
    var id = 0
    var name: String? = null
    var description: String? = null
    var content: String? = null
    var id_category = 0
    var thumb: String? = null
    var created_at: String? = null
    var updated_at: String? = null
}