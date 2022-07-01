package com.example.mrsmuarestaurant.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
class Address {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    var idTb = 0

    var id = 0
    var name = ""
    var phone = ""
    var address = ""

    var code = 0

//    var district_id = 0
//    var district = ""
//
//    var ward_id= 0
//    var ward = ""

    var isSelected = false
}