package com.geomap.utils

import android.provider.Settings
import com.geomap.GeoMapApp.getContext
import com.geomap.GeoMapApp.securityKey
import com.geomap.faqModule.models.FaqListModel
import com.geomap.userModule.models.LoginModel
import com.geomap.userModule.models.VersionModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface RetrofitService {
    @POST("app-version")
    @FormUrlEncoded
    fun getAppVersions(
        @Field("custId") custId : String?, @Field("version") version : String?,
        @Field("deviceType") deviceType : String?, @Field("deviceToken") deviceToken : String?,
        @Field("timeZone") timeZone : String?, @Field("deviceId") deviceId : String?
    ) : Call<VersionModel>

    @POST("login")
    @FormUrlEncoded
    fun postLoginData(@Field("userName") userName : String?,
        @Field("password") password : String?,
        @Field("deviceToken") deviceToken : String?,
        @Field("deviceId") deviceId : String?,
        @Field("deviceType") deviceType : String?) : Call<LoginModel>

    @get:GET("cs-faq")
    val faqLists : Call<FaqListModel>


    companion object {
        fun getInstance() : RetrofitService {
            val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder().readTimeout(
                120,
                TimeUnit.SECONDS
            ).writeTimeout(120, TimeUnit.SECONDS).connectTimeout(
                120,
                TimeUnit.SECONDS
            ).addInterceptor(Interceptor { chain : Interceptor.Chain ->
                val request = chain.request().newBuilder().header(
                    "Oauth",
                    securityKey()
                ).header(
                    "Yaccess",
                    Settings.Secure.getString(
                        getContext().contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                ).method(
                    chain.request().method,
                    chain.request().body
                ).build()
                chain.proceed(request)
            }).addInterceptor(interceptor).build()/*Gson gson = new GsonBuilder()
                .setLenient()
                .create();*/
            val retrofit = Retrofit.Builder().baseUrl(AppUtils.New_BASE_URL).addConverterFactory(
                GsonConverterFactory.create()
            ).addConverterFactory(
                ScalarsConverterFactory.create()
            ).client(okHttpClient).build()
            return retrofit.create(RetrofitService::class.java)

        }
    }
}