package com.geomap.utils

import android.provider.Settings
import com.geomap.GeoMapApp.getContext
import com.geomap.GeoMapApp.securityKey
import com.geomap.faqModule.models.FaqListModel
import com.geomap.mapReportModule.models.*
import com.geomap.userModule.models.ContactUsModel
import com.geomap.userModule.models.UserCommonDataModel
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
        /*@Field("userId") userId : String?, */@Field("version") version : String?,
        @Field("deviceType") deviceType : String?, @Field("deviceToken") deviceToken : String?,
        @Field("deviceId") deviceId : String?
    ) : Call<VersionModel>

    @POST("login")
    @FormUrlEncoded
    fun postLoginData(@Field("userName") userName : String?,
        @Field("password") password : String?,
        @Field("deviceToken") deviceToken : String?,
        @Field("deviceId") deviceId : String?,
        @Field("deviceType") deviceType : String?) : Call<UserCommonDataModel>

    @POST("user_details")
    @FormUrlEncoded
    fun getUserDetails(@Field("userId") userId : String?) : Call<UserCommonDataModel>

    @POST("contact_insert")
    @FormUrlEncoded
    fun postContactUs(@Field("userId") userId : String?, @Field("name") name : String?,
        @Field("mobile") mobile : String?, @Field("email") email : String?,
        @Field("subject") subject : String?,
        @Field("message") message : String?) : Call<ContactUsModel>

    @POST("delete_user")
    @FormUrlEncoded
    fun postDeleteUser(@Field("userId") userId : String?) : Call<SuccessModel>

    @get:GET("faq_data")
    val faqLists : Call<FaqListModel>

    @POST("ur_or_listing")
    @FormUrlEncoded
    fun getDashboardlisting(@Field("userId") userId : String?) : Call<DashboardModel>

    @POST("ur_listing_view_all")
    @FormUrlEncoded
    fun getURViewAlllisting(@Field("userId") userId : String?) : Call<DashboardViewAllModel>

    @POST("or_listing_view_all")
    @FormUrlEncoded
    fun getORViewAlllisting(@Field("userId") userId : String?) : Call<DashboardViewAllModel>

    @POST("underground_insert")
    @FormUrlEncoded
    fun postUndergroundInsert(
        @Field("userId") userId : String?,
        @Field("name") name : String?,
        @Field("shift") shift : String?,
        @Field("mappedBy") mappedBy : String?,
        @Field("scale") scale : String?,
        @Field("location") location : String?,
        @Field("venieLoad") venieLoad : String?,
        @Field("xCordinate") xCordinate : String?,
        @Field("yCordinate") yCordinate : String?,
        @Field("zCordinate") zCordinate : String?,
        @Field("comment") comment : String?,
        @Field("mapSerialNo") mapSerialNo : String?,
        @Field("ugDate") ugDate : String?,
        @Field("leftImage") leftImage : String?,
        @Field("roofImage") roofImage : String?,
        @Field("rightImage") rightImage : String?,
        @Field("faceImage") faceImage : String?,
        @Field("attribute") attribute : String?
    ) : Call<SuccessModel>

    @POST("open_cast_insert")
    @FormUrlEncoded
    fun postOpenCastInsert(
        @Field("userId") userId : String?,
        @Field("minesSiteName") minesSiteName : String?,
        @Field("mappingSheetNo") mappingSheetNo : String?,
        @Field("pitName") pitName : String?,
        @Field("pitLoaction") pitLoaction : String?,
        @Field("shiftInchargeName") shiftInchargeName : String?,
        @Field("geologistName") geologistName : String?,
        @Field("faceLocation") faceLocation : String?,
        @Field("faceLength") faceLength : String?,
        @Field("faceArea") faceArea : String?,
        @Field("faceRockType") faceRockType : String?,
        @Field("benchRl") benchRl : String?,
        @Field("benchHeightWidth") benchHeightWidth : String?,
        @Field("benchAngle") benchAngle : String?,
        @Field("thicknessOfOre") thicknessOfOre : String?,
        @Field("thicknessOfOverburdan") thicknessOfOverburdan : String?,
        @Field("thicknessOfInterburden") thicknessOfInterburden : String?,
        @Field("observedGradeOfOre") observedGradeOfOre : String?,
        @Field("actualGradeOfOre") actualGradeOfOre : String?,
        @Field("sampleColledted") sampleColledted : String?,
        @Field("weathring") weathring : String?,
        @Field("rockStregth") rockStregth : String?,
        @Field("waterCondition") waterCondition : String?,
        @Field("typeOfGeologist") typeOfGeologist : String?,
        @Field("typeOfFaults") typeOfFaults : String?,
        @Field("notes") notes : String?,
        @Field("shift") shift : String?,
        @Field("ocDate") ocDate : String?,
        @Field("dipDirectionAndAngle") dipDirectionAndAngle : String?,
        @Field("image") image : String?,
        @Field("geologistSign") geologistSign : String?,
        @Field("clientsGeologistSign") clientsGeologistSign : String?) : Call<SuccessModel>

    @POST("ur_detail")
    @FormUrlEncoded
    fun getUnderGroundDetails(@Field("id") id : String?) : Call<UnderGroundDetailsModel>

    @POST("or_detail")
    @FormUrlEncoded
    fun getOpenCastDetails(@Field("id") id : String?) : Call<OpenCastDetailsModel>

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