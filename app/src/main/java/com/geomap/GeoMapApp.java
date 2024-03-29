package com.geomap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.room.Room;

import com.geomap.faqModule.activities.FaqActivity;
import com.geomap.mapReportModule.activities.DashboardActivity;
import com.geomap.mapReportModule.activities.PdfViewActivity;
import com.geomap.mapReportModule.activities.SyncDataActivity;
import com.geomap.mapReportModule.activities.openCastModule.OpenCastDetailActivity;
import com.geomap.mapReportModule.activities.openCastModule.OpenCastDetailDraftActivity;
import com.geomap.mapReportModule.activities.openCastModule.OpenCastFormFirstStepActivity;
import com.geomap.mapReportModule.activities.openCastModule.OpenCastListActivity;
import com.geomap.mapReportModule.activities.openCastModule.OpenCastListDraftActivity;
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundDetailActivity;
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundDetailDraftActivity;
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormFirstStepActivity;
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormSecondStepActivity;
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundFormThirdStepActivity;
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundListActivity;
import com.geomap.mapReportModule.activities.underGroundModule.UnderGroundListDraftActivity;
import com.geomap.roomDataBase.GeoMapDatabase;
import com.geomap.userModule.activities.ContactUsActivity;
import com.geomap.userModule.activities.MenuListActivity;
import com.geomap.userModule.activities.SignInActivity;
import com.geomap.userModule.activities.UserProfileActivity;
import com.geomap.userModule.models.UserCommonDataModel;
import com.geomap.utils.CONSTANTS;
import com.geomap.utils.CryptLib;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import retrofit.mime.TypedFile;

public class GeoMapApp extends Application {
    static Context mContext;
    static GeoMapApp GeoMapApp;
    public static String fcmId = "";
    public static String checkLogin = "";
    public static TypedFile geologistSign = null, clientsGeologistSign = null;
    public static GeoMapDatabase DB;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        GeoMapApp = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
            return connected;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static String securityKey() {
        String key;
        String DeviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String AES = "OsEUHhecSs4gRGcy2vMQs1s/XajBrLGADR71cKMRNtA=";
        String RSA = "KlWxBHfKPGkkeTjkT7IEo32bZW8GlVCPq/nvVFuYfIY=";
        String TDES = "1dpra0SZhVPpiUQvikMvkDxEp7qLLJL9pe9G6Apg01g=";
        String SHA1 = "Ey8rBCHsqITEbh7KQKRmYObCGBXqFnvtL5GjMFQWHQo=";
        String MD5 = "/qc2rO3RB8Z/XA+CmHY0tCaJch9a5BdlQW1xb7db+bg=";

        Calendar calendar = Calendar.getInstance();
        TimeZone tm = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(new Date());
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateAsString = outputFmt.format(calendar.getTime());
        TimeZone.setDefault(tm);
        String finalKey = "";

        HashMap<String, String> hash_map = new HashMap<String, String>();
        hash_map.put("AES", AES);
        hash_map.put("RSA", RSA);
        hash_map.put("TDES", TDES);
        hash_map.put("SHA1", SHA1);
        hash_map.put("MD5", MD5);

        Random random = new Random();
        List<String> keys = new ArrayList<String>(hash_map.keySet());
        String randomKey = keys.get(random.nextInt(keys.size()));
        String value = hash_map.get(randomKey);
        key = DeviceId + "." + dateAsString + "." + randomKey + "." + value;

        try {
            finalKey = ProgramForAES(key);
            //            System.out.println(finalKey);
        } catch (Exception e) {
        }
        return finalKey;
    }

    public static String ProgramForAES(String baseString) {
        String cipher = "";
        try {
            String key = "5785abf057d4eea9e59151f75a6fadb724768053df2acdfabb68f2b946b972c6";
            CryptLib cryptLib = new CryptLib();
            cipher = cryptLib.encryptPlainTextWithRandomIV(baseString, key);
            //            println("cipherText" + cipher);
            String decryptedString = cryptLib.decryptCipherTextWithRandomIV(cipher, key);
            //            println("decryptedString" + decryptedString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipher;
    }


    public static void hideProgressBar(ProgressBar progressBar, FrameLayout progressBarHolder, Activity activity) {
        try {
            progressBarHolder.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressBar(ProgressBar progressBar, FrameLayout progressBarHolder, Activity activity) {
        try {
            progressBarHolder.setVisibility(View.VISIBLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(String message, Activity context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static void allDisable(Button btnConfirm) {
        btnConfirm.setEnabled(false);
        btnConfirm.setBackgroundResource(R.drawable.disable_button);
    }

    public static void callDelete403(Activity act, String msg) {
        deleteCall(act);
        showToast(msg, act);
        callSignActivity("", act);
    }

    public static void deleteCall(Context context) {
        checkLogin = "";
        geologistSign = null;
        clientsGeologistSign = null;
        SharedPreferences preferences = context.getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(CONSTANTS.userId);
        edit.remove(CONSTANTS.deviceToken);
        edit.remove(CONSTANTS.deviceId);
        edit.remove(CONSTANTS.deviceType);
        edit.remove(CONSTANTS.name);
        edit.remove(CONSTANTS.email);
        edit.remove(CONSTANTS.mobile);
        edit.remove(CONSTANTS.dob);
        edit.remove(CONSTANTS.profileImage);
        edit.remove(CONSTANTS.supportTitle);
        edit.remove(CONSTANTS.supportText);
        edit.remove(CONSTANTS.supportEmail);
        edit.apply();

        SharedPreferences preferencess = context.getSharedPreferences(CONSTANTS.FCMToken, Context.MODE_PRIVATE);
        SharedPreferences.Editor edited = preferencess.edit();
        edited.remove(CONSTANTS.Token);
        edited.clear();
        edited.apply();
        deleteCache(context);
    }

    public static void saveLoginData(UserCommonDataModel.ResponseData responseData, Context ctx, String flag, Activity act) {
        Gson gson = new Gson();
        SharedPreferences shared = ctx.getSharedPreferences(CONSTANTS.PREFE_ACCESS_USERDATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(CONSTANTS.userId, responseData.getUserId());
        editor.putString(CONSTANTS.deviceToken, responseData.getDeviceToken());
        editor.putString(CONSTANTS.deviceId, responseData.getDeviceId());
        editor.putString(CONSTANTS.deviceType, responseData.getDeviceType());
        editor.putString(CONSTANTS.name, responseData.getName());
        editor.putString(CONSTANTS.email, responseData.getEmail());
        editor.putString(CONSTANTS.mobile, responseData.getMobile());
        editor.putString(CONSTANTS.dob, responseData.getDob());
        editor.putString(CONSTANTS.profileImage, responseData.getProfileImage());
        editor.apply();
        SharedPreferences shared1 = ctx.getSharedPreferences(CONSTANTS.PREFE_ACCESS_ArrayData, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = shared1.edit();
        editor1.putString(CONSTANTS.attributeData, gson.toJson(responseData.getAttributeData()));
        editor1.putString(CONSTANTS.sampleCollected, gson.toJson(responseData.getSampleCollected()));
        editor1.putString(CONSTANTS.rockStrength, gson.toJson(responseData.getRockStrength()));
        editor1.putString(CONSTANTS.waterCondition, gson.toJson(responseData.getWaterCondition()));
        editor1.putString(CONSTANTS.weatheringData, gson.toJson(responseData.getWeatheringData()));
        editor1.putString(CONSTANTS.typeOfGeologicalStructures, gson.toJson(responseData.getTypeOfGeologicalStructures()));
        editor1.putString(CONSTANTS.typeOfFaults, gson.toJson(responseData.getTypeOfFaults()));
        editor1.apply();
        DataBaseFunctions.Companion.callLocalDBGetAndInsertFunction(responseData, ctx);
        if (flag.equalsIgnoreCase("1")) {
            callDashboardActivity(act, "0");
        }
    }

    public static GeoMapDatabase getDataBase(Context ctx) {
        DB = Room.databaseBuilder(ctx, GeoMapDatabase.class, "GeoMap_database").build();
        return DB;
    }

    public static void callSignActivity(String mobile, Activity act) {
        Intent i = new Intent(act, SignInActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra(CONSTANTS.mobile, mobile);
        act.startActivity(i);
        act.finish();
    }

    public static void callDashboardActivity(Activity act, String finish) {
        Intent i = new Intent(act, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callMenuListActivity(Activity act, String finish) {
        Intent i = new Intent(act, MenuListActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callViewPdfActivity(Activity act, String finish, String reportType, String id) {
        Intent i = new Intent(act, PdfViewActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("id", id);
        i.putExtra("reportType", reportType);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callUserProfileActivity(Activity act, String finish) {
        Intent i = new Intent(act, UserProfileActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callFaqActivity(Activity act, String finish) {
        Intent i = new Intent(act, FaqActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callUnderGroundListActivity(Activity act, String finish) {
        Intent i = new Intent(act, UnderGroundListActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callOpenCastListActivity(Activity act, String finish) {
        Intent i = new Intent(act, OpenCastListActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callUnderGroundListDraftActivity(Activity act, String finish) {
        Intent i = new Intent(act, UnderGroundListDraftActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callOpenCastListDraftActivity(Activity act, String finish) {
        Intent i = new Intent(act, OpenCastListDraftActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callUnderGroundDetailActivity(Activity act, String finish, String id) {
        Intent i = new Intent(act, UnderGroundDetailActivity.class);
        i.putExtra("id", id);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callOpenCastDetailActivity(Activity act, String finish, String id) {
        Intent i = new Intent(act, OpenCastDetailActivity.class);
        i.putExtra("id", id);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callUnderGroundDetailDraftActivity(Activity act, String finish, String reportData) {
        Intent i = new Intent(act, UnderGroundDetailDraftActivity.class);
        i.putExtra("report", reportData);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callOpenCastDetailDraftActivity(Activity act, String finish, String reportData) {
        Intent i = new Intent(act, OpenCastDetailDraftActivity.class);
        i.putExtra("report", reportData);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callContactUsActivity(Activity act, String finish) {
        Intent i = new Intent(act, ContactUsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callSyncDataActivity(Activity act, String finish) {
        Intent i = new Intent(act, SyncDataActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callUnderGroundFormFirstStepActivity(Activity act, String finish) {
        Intent i = new Intent(act, UnderGroundFormFirstStepActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callOpenCastFormFirstStepActivity(Activity act, String finish) {
        Intent i = new Intent(act, OpenCastFormFirstStepActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callUnderGroundFormSecondStepActivity(Activity act, String finish, String gsons) {
        Intent i = new Intent(act, UnderGroundFormSecondStepActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("attributeData", gsons);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }

    public static void callUnderGroundFormThirdStepActivity(Activity act, String finish) {
        Intent i = new Intent(act, UnderGroundFormThirdStepActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        act.startActivity(i);
        if (finish.equalsIgnoreCase("0")) {
            act.finish();
        }
    }
}
