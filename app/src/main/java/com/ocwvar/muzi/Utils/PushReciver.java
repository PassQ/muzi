package com.ocwvar.muzi.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ocwvar.muzi.Activities.ArtcleActivity;
import com.ocwvar.muzi.Activities.MessageDialog;
import com.ocwvar.muzi.Network.JsonDecoder;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Utils
 * Date: 2016/5/16  13:15
 * Project: Muzi
 * 推送接收器
 */
public class PushReciver extends BroadcastReceiver {
    private static final String TAG = "推送接收器";
    private static PushReciver pushReciver;
    Context context;

    public PushReciver() {
    }

    public static PushReciver getInstance() {
        if (pushReciver == null) {
            pushReciver = new PushReciver();
        }
        return pushReciver;
    }

    /**
     * 初始化推送接收器
     *
     * @param context      全局Context
     * @param communityTAG 小区TAG
     * @param tel          注册用户电话号码
     */
    public void init(Context context, String communityTAG, String tel) {
        Log.w(TAG, "开始设置极光TAG和别名");
        Set<String> sets = new LinkedHashSet<>();
        sets.add(communityTAG);
        JPushInterface.init(context);
        JPushInterface.setDebugMode(true);
        JPushInterface.setAliasAndTags(context, tel, sets, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.w(TAG, "gotResult: " + i);
                Log.w(TAG, "gotResult: " + s);
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    Log.w(TAG, "gotResult: " + iterator.next());
                }
            }
        });
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();

        Log.w(TAG, "TITLE:" + bundle.getString(JPushInterface.EXTRA_TITLE));
        Log.w(TAG, "MESSAGE:" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        Log.w(TAG, "EXTRA:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
        Log.w(TAG, "EXTRA:" + bundle.getString(JPushInterface.EXTRA_ALERT));

        if (action.equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {

            Log.w(TAG, "类型:自定义消息");

        } else if (action.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {

            Log.w(TAG, "类型:推送消息");

        } else if (action.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            Log.w(TAG, "类型:用户点击打开了通知");
            String[] datas = JsonDecoder.getPushMessageData(bundle.getString(JPushInterface.EXTRA_EXTRA));
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            if (datas != null && context != null) {
                //解析成功 , 则代表当前点击的条目是文章推送消息
                Intent readArtcle = new Intent(context, ArtcleActivity.class);
                readArtcle.putExtra("ArtclesID", datas[0]);
                readArtcle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(readArtcle);
            }else if(content != null && context !=null){
                Intent message = new Intent(context, MessageDialog.class);
                message.putExtra("message",content);
                message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(message);
            }

        } else if (action.equals(JPushInterface.ACTION_CONNECTION_CHANGE)) {

            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "类型:网络状态改变\n" + intent.getAction() + " 连接状态变化为: " + connected);

        }
    }

}
