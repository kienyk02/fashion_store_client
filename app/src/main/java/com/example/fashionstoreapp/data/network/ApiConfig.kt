package com.example.fashionstoreapp.data.network

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiConfig {

    private const val IP = "192.168.2.108"

    //    192.168.165.221
//    private const val BASE_URL = "http://$IP:8080/"

    private const val BASE_URL = "https://fahsionstoreserver-production.up.railway.app/"
    private const val BASE_URL_ADDRESS = "https://dev-online-gateway.ghn.vn/shiip/public-api/"
    private const val TOKEN = "f6a51432-8f83-11ef-8e53-0a00184fe694"
    private const val SHOP_ID = "195098"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitWithoutJwt: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAICFServer: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.2.108:5001/")
//        .client(clientSGAI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAddress: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_ADDRESS)
        .client(createOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun createOkHttpClient(): OkHttpClient {

        val httpClient = OkHttpClient.Builder()

        // Thêm interceptor để thêm JWT vào header của mỗi yêu cầu
        httpClient.addInterceptor { chain ->

            val original = chain.request()
            val requestBuilder = original.newBuilder()

            // Thêm JWT vào header nếu có
            JwtManager.CURRENT_JWT.let { jwtToken ->
                Log.d("AA", jwtToken)
                requestBuilder.header("Authorization", "Bearer $jwtToken")
            }
            requestBuilder.header("Token", TOKEN)
            requestBuilder.header("ShopId", SHOP_ID)

            val request = requestBuilder.build()
            chain.proceed(request)

        }

        return httpClient.build()
    }

}