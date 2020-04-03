package com.vincentzhangz.maverickcount.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService{
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAAKWj3SCI:APA91bFtY6acERyEHgiI8Xx2-NSoeHMvn4mkpBhqaBsPaxdTkabLxS8kp-S4DH5NLNMeZebZfsw8dpBKQjFEKNSRkXdBn72XNX9dQ7oJtr1BtbaWygYirMvdNFa9QP9oWsPx56vPA4AU"
    )

    @POST("fcm/send")
    fun sendNotification(@Body body:Sender):Call<Response>
}