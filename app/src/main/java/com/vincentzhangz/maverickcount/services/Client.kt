package com.vincentzhangz.maverickcount.services

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client{
    companion object{
        private var retofit: Retrofit? =null

        fun getClient(url:String): Retrofit? {
            if(retofit==null){
                retofit= Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()
            }
            Log.d("cekNull", retofit.toString())
            return retofit
        }
    }


}