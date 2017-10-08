package com.ocwvar.muzi.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocwvar.muzi.Network.Callbacks.OnGetSignHistoryCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpSignCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 品着龙井剥金橘 on 2016/11/3.
 * 签到日历页面
 */

public class SignActivity extends Activity implements OnUpSignCallback,View.OnClickListener,OnGetSignHistoryCallback {

    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    private String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式
    private TextView popupwindow_calendar_month;
    private SignCalendar calendar;
    private ImageView btn_close;
    private Button btn_signIn;
    private Intent sourceIntent;
    private List<String> list = new ArrayList<String>(); //设置标记列表
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // Make us non-modal, so that others can receive touch events.
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        setContentView(R.layout.dialog_signin);
        sourceIntent = getIntent();
        popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
        btn_close = (ImageView) findViewById(R.id.imageView_close);
        btn_signIn = (Button) findViewById(R.id.btn_signIn);
        calendar = (SignCalendar) findViewById(R.id.popupwindow_calendar);
        popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
                + calendar.getCalendarMonth() + "月");
        if (null != date) {

            int years = Integer.parseInt(date.substring(0,
                    date.indexOf("-")));
            int month = Integer.parseInt(date.substring(
                    date.indexOf("-") + 1, date.lastIndexOf("-")));
            popupwindow_calendar_month.setText(years + "年" + month + "月");

            calendar.showCalendar(years, month);
            calendar.setCalendarDayBgColor(date,
                    R.drawable.calendar_date_focused);
        }

        getHistoryDate(sourceIntent.getStringArrayListExtra("dateList"));

        NetworkHelper.getInstance().getSignHitory(SignActivity.this,AppOptions.USERINFO.user.getPhoneNumber());


//        isToday();

        btn_signIn.setOnClickListener(this);
        btn_close.setOnClickListener(this);

//        btn_signIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Date today= calendar.getThisday();
//                calendar.addMark(today, 0);
//                HashMap<String, Integer> bg = new HashMap<String, Integer>();
////            calendar.setCalendarDayBgColor(today, R.drawable.bg_sign_today);
//
//                networkHelper.upSign(, AppOptions.USERINFO.user.getPhoneNumber(),today.getTime()+"");
//                btn_signIn.setText("今日已签，明日继续");
//                btn_signIn.setEnabled(false);
//            }
//        });
        //监听所选中的日期
//		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {
//
//			public void onCalendarClick(int row, int col, String dateFormat) {
//				int month = Integer.parseInt(dateFormat.substring(
//						dateFormat.indexOf("-") + 1,
//						dateFormat.lastIndexOf("-")));
//
//				if (calendar.getCalendarMonth() - month == 1//跨年跳转
//						|| calendar.getCalendarMonth() - month == -11) {
//					calendar.lastMonth();
//
//				} else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
//						|| month - calendar.getCalendarMonth() == -11) {
//					calendar.nextMonth();
//
//				} else {
//					list.add(dateFormat);
//					calendar.addMarks(list, 0);
//					calendar.removeAllBgColor();
//					calendar.setCalendarDayBgColor(dateFormat,
//							R.drawable.calendar_date_focused);
//					date = dateFormat;//最后返回给全局 date
//				}
//			}
//		});

        //监听当前月份
        calendar.setOnCalendarDateChangedListener(new SignCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month
                        .setText(year + "年" + month + "月");
            }
        });
    }



    /*
    * 判断今日是否已经签到
    * */
    private void isToday() {
        Date date=calendar.getThisday();

        String todayString=simpleDateFormat.format(date);
        for (String day:list) {
            if(day.equals(todayString)){
                btn_signIn.setText("今日已签，请明天继续哦");
                btn_signIn.setEnabled(false);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signIn:
                Date today= calendar.getThisday();
                HashMap<String, Integer> bg = new HashMap<String, Integer>();
//            calendar.setCalendarDayBgColor(today, R.drawable.bg_sign_today);

                NetworkHelper.getInstance().upSign(SignActivity.this, AppOptions.USERINFO.user.getPhoneNumber(),today.getTime()+"");

                break;
            case R.id.imageView_close:
                finish();
                break;
            default:
                break;
        }
    }
    //获取签到日期成功回调方法
    @Override
    public void onGetSignHistoryCompleted(String userTel, List<String> dateList) {
        getHistoryDate(dateList);
    }

    private void getHistoryDate(List<String> dateList) {
        long time;
        String strTime;
        if(dateList!=null) {
            for (String date : dateList) {
                time = Long.parseLong(date);
                strTime = simpleDateFormat.format(time);
                Log.d("123", strTime);
                list.add(strTime);

            }
            calendar.addMarks(list, 0);

        }
        isToday();
    }

    //获取签到日期失败回调方法
    @Override
    public void onGetSignHistoryFailed(boolean isException, String userTel) {

    }


    //签到成功回调方法
    @Override
    public void onUpSignCompleted(String userTel, String date, String massage, String score) {
        if(massage!=null){

            calendar.addMark(calendar.getThisday(), 0);
            if("0".equals(score)) {
                btn_signIn.setText(massage);
                btn_signIn.setEnabled(false);
            }else {
                btn_signIn.setText(massage+"！积分+"+score);
                btn_signIn.setEnabled(false);
            }
        }
    }

    //签到失败回调方法
    @Override
    public void onUpSignFailed(boolean isException, String userTel, String date) {
        btn_signIn.setText("网络连接失败");
        btn_signIn.setEnabled(false);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return false;
//    }
//
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            return true;
        }

        return false;
    }


}
