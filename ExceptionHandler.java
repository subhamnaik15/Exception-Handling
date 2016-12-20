package com.example.lenovo.crashlog;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bismuth rath / subham Kumar Naik and satyam Kumar Naik .
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Activity Contex;
    private final String Next_Line = "\n";


    public ExceptionHandler(Activity Context) {

        this.Contex = Context;

    }

    @SuppressWarnings("deprecation")
    public void uncaughtException(Thread thread, Throwable exception) {


        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("Android Crash"+"\n\n");
        errorReport.append("************ APPLICATION NAME************\n\n");
        errorReport.append(Contex.getApplicationInfo().loadLabel(Contex.getPackageManager())+"("+Contex.getPackageName()+")"+"\n\n");
        errorReport.append("************ Crash log Time ************\n\n");
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        errorReport.append(ft.format(dNow)+Next_Line+Next_Line);
        errorReport.append("************  ERROR CAUSE In************\n\n");
        errorReport.append(Contex.getClass().getSimpleName()+"\n\n");
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        Log.e("********11", "------ " + Contex.getClass().getSimpleName() + Next_Line + Next_Line + stackTrace.toString());
        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(Next_Line);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(Next_Line);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(Next_Line);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(Next_Line);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(Next_Line);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(Next_Line);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(Next_Line);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(Next_Line);

        Log.e("********22", "---- " + errorReport);

        writeinsd(errorReport);

        MemoryInfo mi = new MemoryInfo();
        ActivityManager activityManager = (ActivityManager) Contex.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;

        //Percentage can be calculated for API 16+
        long percentAvail = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            percentAvail = mi.availMem / mi.totalMem;
        }

        Log.e("********3", "" + "----333avl meg :" + availableMegs + "  per%avil " + percentAvail);
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;

        Log.e("********444", "-----**4444Megs :" + megAvailable);
        
//        Intent intent = new Intent(Contex, ContactScreenActivity.class);
//        intent.putExtra("error", errorReport.toString());
//        Contex.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
        Contex.finish();
    }

    private void writeinsd(StringBuilder errorReport) {
        File root = android.os.Environment.getExternalStorageDirectory();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        String str= "Androidcrash"+date+".txt";
        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, str);

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(errorReport);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



