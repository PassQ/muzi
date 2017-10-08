package com.ocwvar.muzi.Utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.netease.nis.bugrpt.CrashHandler;
import com.ocwvar.muzi.Activities.LoginActivityx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by 区成伟
 * Package: com.ocwvar.surfacetest.ExceptionHandler
 * Date: 2016/5/25  9:20
 * Project: SurfaceTest
 * 未处理异常接收器
 * <p>
 * 在重新启动Activity时会传递数据包 Bundle
 * 也可直接使用  AnHeApplication.handleIncomingBundle()  方法进行处理
 * <p>
 * 数据:
 * IsRecover   .布尔类型.                          区别这个数据是否为崩溃重启的数据 永远为  true
 * hasLogs     .布尔类型.                          是否成功生成了日志文件
 * Throwable  .Serializable序列化类型.       上次崩溃的异常对象
 * <p>
 * <p>
 * 参数:
 * SLEEPTIME_RESTART_ACTIVITY        重新启动应用程序指定Activity间隔时间.  毫秒.  1000ms = 1s
 * RESTART_ACTIVITY                         重新启动的Activity类
 * LOG_NAME_HEAD                             日志文件名开头
 * LOG_SAVE_FOLDER                          日志保存目录
 * SAVE_LOGS                                    是否生成日志
 */
public class AnHeApplication extends Application implements Thread.UncaughtExceptionHandler {

    public final static String THROWABLE_OBJECT = "Throwable";
    public final static String IS_RECOVERY = "IsRecover";
    public final static String HAS_LOGS = "hasLogs";
    private final static long SLEEPTIME_RESTART_ACTIVITY = 1000;
    private final static Class RESTART_ACTIVITY = LoginActivityx.class;
    private final static String LOG_NAME_HEAD = "OCLog";
    private final static String LOG_SAVE_FOLDER = "/log/";
    private final static boolean SAVE_LOGS = true;
    private final static boolean CATCH_EXCEPTION = false;
    private boolean logsCreated = false;

    /**
     * 处理上次崩溃重启传回Activity的Bundle数据
     *
     * @param bundle 传入的Bundle数据
     * @return True: 处理成功  False:不是上次崩溃时传入的数据
     */
    public static boolean handleIncomingBundle(@NonNull Bundle bundle, Context context) {
        if (bundle.get(IS_RECOVERY) != null) {
            if (bundle.getBoolean(HAS_LOGS)) {
                Toast.makeText(context, "程序已恢复 , 崩溃日志已生成", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "程序已恢复", Toast.LENGTH_SHORT).show();
            }
            Throwable throwable = (Throwable) bundle.getSerializable(THROWABLE_OBJECT);
            if (throwable != null) {
                Log.e("上次崩溃日志", "---------------------------------------------");
                for (int i = 0; i < throwable.getStackTrace().length; i++) {
                    System.out.println(throwable.getStackTrace()[i]);
                }
                Log.e("上次崩溃日志", "---------------------------------------------");
            } else {
                Log.e("上次崩溃日志", "日志丢失 或 记录失败 !");
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.init(getApplicationContext());
        AppOptions.getInstance().initAppOptions(getApplicationContext());
        if (CATCH_EXCEPTION) {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        logsCreated = createLogs(ex);
        restartActivity(RESTART_ACTIVITY, ex);
        System.exit(2);
    }

    /**
     * 创建日志文件
     *
     * @param throwable 异常
     * @return 执行结果
     */
    private boolean createLogs(Throwable throwable) {
        if (throwable == null || !SAVE_LOGS) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= 23 && !checkPermission()) {
            Log.e("异常处理--保存日志", "保存失败 , Android 6.0+ 系统. 内存卡读写权限没有获取");
            return false;
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //如果内存卡或内置储存已经挂载

            //创建储存目录
            String savePath = Environment.getExternalStorageDirectory().getPath() + LOG_SAVE_FOLDER;
            File file = new File(savePath);
            file.mkdirs();

            if (file.canWrite()) {
                //如果目录可以写入

                FileWriter fileWriter;
                PrintWriter printWriter;
                String exceptionTime = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString();

                file = new File(savePath + LOG_NAME_HEAD + "  " + exceptionTime + "  .log");
                try {
                    if (file.createNewFile()) {
                        fileWriter = new FileWriter(file, true);
                        printWriter = new PrintWriter(fileWriter);
                        printWriter.println("Date:" + exceptionTime + "\n");
                        printWriter.println("Exception Class Name: ");
                        printWriter.println(throwable.getStackTrace()[0].getClassName());
                        printWriter.println("");
                        printWriter.println("Exception Class Position: ");
                        printWriter.println("Line number: " + throwable.getStackTrace()[0].getLineNumber());
                        printWriter.println("");
                        printWriter.println("Exception Cause: ");
                        printWriter.println(throwable.getMessage());
                        printWriter.println("");
                        printWriter.println("-----------------------------------\nException Message: \n");
                        for (int i = 0; i < throwable.getStackTrace().length; i++) {
                            printWriter.println(throwable.getStackTrace()[i]);
                        }
                        printWriter.flush();
                        fileWriter.flush();
                        printWriter.close();
                        fileWriter.close();
                        Log.w("异常处理--保存日志", "日志保存成功");
                        return true;
                    } else {
                        Log.e("异常处理--保存日志", "保存失败 , 存在相同名称的日志文件");
                        return true;
                    }
                } catch (IOException e) {
                    Log.e("异常处理--保存日志", "保存失败 , 无法创建日志文件或写入流失败");
                    return false;
                }

            } else {
                //目录不可写入 , 操作失败
                Log.e("异常处理--保存日志", "保存失败 , 无法写入目录");
                file = null;
                return false;
            }

        } else {
            Log.e("异常处理--保存日志", "保存失败 , 储存未挂载");
            return false;
        }

    }

    /**
     * 检查内存卡读写权限 针对Android 6.0+
     *
     * @return 是否有权限
     */
    @TargetApi(23)
    private boolean checkPermission() {
        return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 重新启动应用程序
     *
     * @param activityClass 要启动的Activity
     */
    private void restartActivity(Class activityClass, Throwable throwable) {

        //创建用于启动的 Intent , 与对应的数据
        Intent intent = new Intent(getApplicationContext(), activityClass);
        intent.putExtra("IsRecover", true);
        intent.putExtra("hasLogs", logsCreated);
        intent.putExtra("Throwable", throwable);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent
                , PendingIntent.FLAG_ONE_SHOT
        );

        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + SLEEPTIME_RESTART_ACTIVITY, pendingIntent);
    }

}
