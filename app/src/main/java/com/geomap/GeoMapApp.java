package com.geomap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class GeoMapApp extends Application {
    static Context mContext;
    static GeoMapApp GeoMapApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.e("onCreate", "Called");
        GeoMapApp = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            boolean flag = false;
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //For 3G check
            boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
            //For WiFi Check
            boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
            flag = !(!is3g && !isWifi);
            return flag;
        } catch (NullPointerException e) {
            return false;
        }
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            Toast toast = new Toast(context);
            View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
            TextView tvMessage = view.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160);
            toast.setView(view);
            toast.show();
        } else {
            /*final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_above_version_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            TextView tvMessage = dialog.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            dialog.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            });
            dialog.show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                dialog.hide();
            }, 2 * 600);

            dialog.setCancelable(true);*/
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            //            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160);
            toast.show();
        }
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

    public static String getKey(Context context) {
        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(context);
        String key = appSignatureHashHelper.getAppSignatures().get(0);
        SharedPreferences shared = context.getSharedPreferences(CONSTANTS.PREF_KEY_Splash, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(CONSTANTS.PREF_KEY_SplashKey, appSignatureHashHelper.getAppSignatures().get(0));
        editor.apply();
        return key;
    }

}
