package com.ocwvar.muzi.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Network.Callbacks.OnUpOrderCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/*
* 预约详情界面
* */

public class OrderActivity extends Activity implements View.OnClickListener,OnUpOrderCallback {

    private LinearLayout timeLayout,dateLayout;
    private TextView timeText,dateText;
    private EditText ordereEdit,telEdit;
    private Button button;
    private BulletinBean sourceBean;
    private Intent sourceIntent;
    private int hours,years,months,days;
    private int minute;
    private String yearsStr,monthsStr,daysStr,hoursStr,minuteStr;
    public static String status1 = "0",status2 = "0",status3 = "0",status4 = "0",status5 = "0",status6 = "0",status7 = "0",status8 = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        sourceIntent=this.getIntent();
        sourceBean = (BulletinBean) sourceIntent.getSerializableExtra("bulletinBean");
        initview();

        button.setOnClickListener(this);
        timeLayout.setOnClickListener(this);
        dateLayout.setOnClickListener(this);
        dateText.setText(years+"年"+(months+1)+"月"+days+"日");
    }
    private void initview() {
        timeLayout = (LinearLayout) findViewById(R.id.order_time);
        dateLayout = (LinearLayout) findViewById(R.id.order_date);
        timeText = (TextView) findViewById(R.id.order_time_tv);
        dateText = (TextView) findViewById(R.id.order_date_tv);
        ordereEdit= (EditText) findViewById(R.id.editText_order);
        telEdit= (EditText) findViewById(R.id.editText_tel);
        button= (Button) findViewById(R.id.order_btn);
        Calendar mycalendar= Calendar.getInstance(Locale.CHINA);
        Date mydate=new Date(); //获取当前日期Date对象
        mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期
        years=mycalendar.get(Calendar.YEAR);
        months=mycalendar.get(Calendar.MONTH);
        days=mycalendar.get(Calendar.DAY_OF_MONTH);
        hours=mycalendar.get(Calendar.HOUR_OF_DAY); //获取Calendar对象中的小时
        minute =mycalendar.get(Calendar.MINUTE);//获取Calendar对象中的分钟
        yearsStr = years+"";
        monthsStr = months<10? "0"+(months+1):""+(months+1);
        daysStr = days<10? "0"+days:""+days;




    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.order_btn:
                if(hoursStr==null){
                    Toast.makeText(this,"请选择预约时间",Toast.LENGTH_SHORT).show();
                } else  {
                    long todayTime=System.currentTimeMillis();//当前时间戳
                    long orderTime=getTimeStamp(yearsStr,monthsStr,daysStr,hoursStr,minuteStr);
                    if(todayTime>orderTime){
                        Toast.makeText(this,"请选择正确的预约时间",Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty((ordereEdit.getText()))){
                        Toast.makeText(this,"请输入您预约信息，如：我要预约理发服务",Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(telEdit.getText())){
                        Toast.makeText(this,"请输入您的联系号码",Toast.LENGTH_SHORT).show();
                    }else {
/*                        Intent intent = new Intent();
                        intent.setClass(OrderActivity.this, successActivity.class);
                        startActivity(intent);*/
                        NetworkHelper.getInstance().upOrder(OrderActivity.this,sourceBean.getBusinessID(),orderTime+"",todayTime+"",
                                                            telEdit.getText().toString(),ordereEdit.getText().toString(), AppOptions.USERINFO.user.getPhoneNumber());
                    }
                }

                break;
            case R.id.order_time:
                TimePickerDialog timePicker=new TimePickerDialog(OrderActivity.this,timeListener,hours,minute,true);
                timePicker.show();
                break;
            case R.id.order_date:
                DatePickerDialog datePicker=new DatePickerDialog(OrderActivity.this,dateSetListener,years,months,days);
                datePicker.show();
                break;
            default:
                break;
        }
    }
    private TimePickerDialog.OnTimeSetListener timeListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int myhours, int mymunite) {
            hours=myhours;
            minute=mymunite;
            updateTime();

        }
    };
    private DatePickerDialog.OnDateSetListener dateSetListener =new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker datePicker, int myyears, int mymonths, int mydays) {
            years=myyears;
            months=mymonths;
            days=mydays;
            dateText.setText(years+"年"+(months+1)+"月"+days+"日");
            yearsStr = years+"";
            monthsStr = months<10? "0"+(months+1):""+(months+1);
            daysStr = days<10? "0"+days:""+days;
        }
    };

    //更新时间
    private void updateTime()
    {
        //在TextView上显示时间
        hoursStr = hours<10?"0"+hours:""+hours;
        minuteStr = minute<10?"0"+minute:""+minute;
        timeText.setText(hoursStr + ":" + minuteStr);

    }

    /**
     * 时间转换为时间戳
     *
     */
    public static long getTimeStamp(String years, String months, String days, String hours, String minute) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        String  timeStr = years+"-"+months+"-"+days+" "+hours+":"+minute;

        try {
            date = simpleDateFormat.parse(timeStr);
            long timeStamp = date.getTime();
            return timeStamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }


    //预约成功回调
    @Override
    public void onUpOrderCompleted(String message, boolean isSuccessed) {
        if(isSuccessed){
            Intent intent = new Intent();
            intent.setClass(OrderActivity.this, successActivity.class);
            startActivity(intent);

        }else {
            Toast.makeText(OrderActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }

    //预约失败回调
    @Override
    public void onUpOrderFailed(boolean isException) {
        Toast.makeText(OrderActivity.this,"网络出错，请重试",Toast.LENGTH_LONG).show();

    }
}
