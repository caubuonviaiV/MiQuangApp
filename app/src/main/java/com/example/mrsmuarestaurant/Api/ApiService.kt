package com.example.mrsmuarestaurant.Api

import com.example.mrsmuarestaurant.model.Checkout
import com.example.mrsmuarestaurant.model.ResponModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //SigUp
    @FormUrlEncoded
    @POST("dang-ky")
    fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("phone") phone: String,
            @Field("password") password: String
    ): Call<ResponModel>

    //Login
    @FormUrlEncoded
    @POST("dang-nhap")
    fun login(
            @Field("email") email: String,
            @Field("password") password: String,
    ): Call<ResponModel>

    //Product
    @GET("product")
    fun getProduct(): Call<ResponModel>
    @GET("noodles-product")
    fun getNoodles(): Call<ResponModel>

    //Slider
    @GET("slider")
    fun getSlider(): Call<ResponModel>

    //Slider
    @GET("post")
    fun getPost(): Call<ResponModel>

    //Reservation
    @FormUrlEncoded
    @POST("reservation")
    fun reservation(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("content") content: String,
        @Field("people") people: String,
        @Field("time") time: String,
        @Field("date") date: String
    ): Call<ResponModel>

    //getDistrict
    @GET("district")
    fun getDistrict(): Call<ResponModel>
    //getWard
    @GET("ward/{district_id}")
    fun getWard(
            @Path("district_id") district_id: Int
    ): Call<ResponModel>

    //Checkout
    @POST("checkout")
    fun checkout(
        @Body data: Checkout
    ): Call<ResponModel>

    @GET("checkout/user/{id}")
    fun getHistory(
            @Path("id") id: Int
    ): Call<ResponModel>

    @POST("checkout/cancelled/{id}")
    fun cancelCheckout(
            @Path("id") id: Int
    ): Call<ResponModel>
}