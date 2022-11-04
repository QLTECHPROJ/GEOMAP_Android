package com.geomap.utils

import android.provider.Settings
import com.geomap.GeoMapApp.getContext
import com.geomap.GeoMapApp.securityKey
import com.geomap.faqModule.models.FaqListModel
import com.geomap.mapReportModule.models.*
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
    @POST("app_version")
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

    @POST("delete_user")
    @FormUrlEncoded
    fun postDeleteUser(@Field("id") id : String?) : Call<SuccessModel>

    @get:GET("faq")
    val faqLists : Call<FaqListModel>

    @get:GET("ur_or_listing")
    val getDashboardlisting : Call<DashboardModel>

    @get:GET("ur_or_listing_view_all")
    val getDashboardViewAlllisting : Call<DashboardViewAllModel>

    @get:GET("ur_detail")
    val getUnderGroundDetails : Call<UnderGroundDetailsModel>

    @get:GET("or_detail")
    val getOpenCastDetails : Call<OpenCastDetailsModel>

    @get:GET("attribute_data_number")
    val getAttributesList : Call<AttributesListModel>

    @get:GET("sample_collecteds")
    val getSampleCollectedsList : Call<CommonPopupListModel>

    @get:GET("weathering_data")
    val getWeatheringDataList : Call<CommonPopupListModel>

    @get:GET("rock_strength_data")
    val getRockStrengthDataList : Call<CommonPopupListModel>

    @get:GET("water_condition_data")
    val getWaterConditionDataList : Call<CommonPopupListModel>

    @get:GET("type_of_geological_structures")
    val getTypeOfGeologicalStructuresList : Call<CommonPopupListModel>

    @get:GET("types_of_fault")
    val getTypeOfFaultList : Call<CommonPopupListModel>

    @get:GET("geologist_data")
    val getGeologistData : Call<CommonPopupListModel>

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