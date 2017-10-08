package com.ocwvar.muzi.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Beans.UserBean;
import com.ocwvar.muzi.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Project muzi
 * Created by 区成伟
 * On 2016/11/2 10:38
 * File Location com.ocwvar.muzi.Utils
 * App全局数据存储器
 */

@SuppressLint("StaticFieldLeak")
public class AppOptions {


    public static final String SMS_KEY = "127175c20ec2b";
    public static final String SMS_SECRET = "598589ef4547329822525e4a6065c586";
    public static int SDK_VERSION;
    public static Context ApplicationContext;
    private static AppOptions appOptions;

    public static AppOptions getInstance() {
        if (appOptions == null) {
            appOptions = new AppOptions();
        }
        return appOptions;
    }

    void initAppOptions(Context applicationContext) {

        SDK_VERSION = Build.VERSION.SDK_INT;

        ApplicationContext = applicationContext;

    }

    /**
     * 弱缓存储存对象
     */
    static public class WEAKCACHE {

        //注册部分短信倒计时临时储存
        static private WeakReference<Long> zcSMSCounting_Start = new WeakReference<>(null);
        static private WeakReference<Long> zcSMSCounting_End = new WeakReference<>(null);

        //忘记密码部分短信倒计时临时储存
        static private WeakReference<Long> forgetSMSCounting_Start = new WeakReference<>(null);
        static private WeakReference<Long> forgetSMSCounting_End = new WeakReference<>(null);

        public static long getZCSMSCounting() {
            return getZcSMSCounting_End() - getZcSMSCounting_Start();
        }

        public static long getForgetSMSCounting() {
            return getForgetSMSCounting_End() - getForgetSMSCounting_Start();
        }

        static long getZcSMSCounting_Start() {
            if (zcSMSCounting_Start.get() == null) {
                return 0L;
            } else {
                return zcSMSCounting_Start.get();
            }
        }

        public static void setZcSMSCounting_Start(long time) {
            zcSMSCounting_Start = new WeakReference<>(time);
        }

        public static long getZcSMSCounting_End() {
            if (zcSMSCounting_End.get() == null) {
                return 0L;
            } else {
                return zcSMSCounting_End.get();
            }
        }

        public static void setZcSMSCounting_End(long time) {
            zcSMSCounting_End = new WeakReference<>(time);
        }

        static long getForgetSMSCounting_Start() {
            if (forgetSMSCounting_Start.get() == null) {
                return 0L;
            } else {
                return forgetSMSCounting_Start.get();
            }
        }

        public static void setForgetSMSCounting_Start(long time) {
            forgetSMSCounting_Start = new WeakReference<>(time);
        }

        public static long getForgetSMSCounting_End() {
            if (forgetSMSCounting_End.get() == null) {
                return 0L;
            } else {
                return forgetSMSCounting_End.get();
            }
        }

        public static void setForgetSMSCounting_End(long time) {
            forgetSMSCounting_End = new WeakReference<>(time);
        }

        public static void resetZCCounting() {
            zcSMSCounting_Start.clear();
            zcSMSCounting_End.clear();

            zcSMSCounting_Start = null;
            zcSMSCounting_End = null;
        }

        public static void resetForgetCounting() {
            forgetSMSCounting_Start.clear();
            forgetSMSCounting_End.clear();

            forgetSMSCounting_Start = null;
            forgetSMSCounting_End = null;
        }

    }

    /**
     * 主要缓存存储对象
     */
    static public class CACHE {

        //主界面轮播对象列表
        static private List<ArtclesBean> mainScrollingItems = new ArrayList<>();

        /**
         * 获取主界面轮播对象列表
         *
         * @return 对象列表
         */
        public static
        @Nullable
        List<ArtclesBean> getMainScrollingItems() {

            return mainScrollingItems;
        }

        /**
         * 添加主界面轮播对象
         *
         * @param artclesBean 新的轮播对象
         */
        public static void addMainScrollingItems(@NonNull ArtclesBean artclesBean) {
            if (mainScrollingItems == null) {
                mainScrollingItems = new ArrayList<>();
            }

            mainScrollingItems.add(artclesBean);
        }

        /**
         * 清空主界面轮播对象储存容器
         */
        public static void resetMainScrollingItems() {
            if (mainScrollingItems != null) {
                mainScrollingItems.clear();
            }
        }


    }

    /**
     * 用户信息存储对象
     */
    static public class USERINFO {

        //用户对象
        static public UserBean user;

        //用户临时选择的小区ID
        static public String tempCommunityTAG = null;

        //用户临时选择的小区名称
        static public String tempCommunityName = null;

        //用户头像资源
        static public int userHeadRes = R.drawable.default_head;
        //用户积分
        static public String score;

        /**
         * 设置用户
         *
         * @param user 用户对象
         */
        public static void setUser(UserBean user) {
            if (user != null) {
                USERINFO.user = user;
            }
        }

        /**
         * 设置用户头像网址
         *
         * @param url 头像网址
         */
        public static void setUserHeadImage(String url) {
            if (USERINFO.user != null) {
                USERINFO.user.setUserHeadImage(url);
            }
        }

        /**
         * 获取用户头像资源
         *
         * @return 如果没有头像网址, 返回资源ID
         */
        public static Object getUserHead() {
            if (user != null && !TextUtils.isEmpty(user.getUserHeadImage())) {
                return user.getUserHeadImage();
            } else {
                return userHeadRes;
            }
        }

        /**
         * 获取用户所在小区TAG
         *
         * @return 优先返回用户自定义小区TAG
         */
        public static String getCommunityTAG() {
            if (TextUtils.isEmpty(tempCommunityTAG)) {
                return user.getCommunityID();
            } else {
                return tempCommunityTAG;
            }
        }

        /**
         * 获取用户所在小区名称
         *
         * @return 优先返回用户自定义小区TAG
         */
        public static String getCommunityName() {
            if (TextUtils.isEmpty(tempCommunityName)) {
                return user.getCommunityName();
            } else {
                return tempCommunityName;
            }
        }

        /**
         * 重置临时区域
         *
         * @return 返回False表示已经重置, 不需要重复进行操作
         */
        public static boolean resetTempLocation() {
            if (TextUtils.isEmpty(tempCommunityTAG) && TextUtils.isEmpty(tempCommunityName)) {
                return false;
            } else {
                tempCommunityTAG = null;
                tempCommunityName = null;
                return true;
            }
        }

    }

}
