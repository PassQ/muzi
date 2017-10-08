package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.PaymentHistoryAdapter;
import com.ocwvar.muzi.Beans.PayRecordBean;
import com.ocwvar.muzi.Network.Callbacks.OnGetPayRecordByMonthCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetPayRecordCallback;
import com.ocwvar.muzi.Network.Callbacks.OnPayHistoryClickCallbacks;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/6/13  17:23
 * Project: Muzi
 * 缴费历史 查询界面
 */
public class PaymentHistoryActivity extends AppCompatActivity implements View.OnClickListener, OnGetPayRecordByMonthCallback, DialogInterface.OnCancelListener, OnPayHistoryClickCallbacks, RadioGroup.OnCheckedChangeListener, OnGetPayRecordCallback {

    RecyclerView recyclerView;
    ListView submitList;
    PaymentHistoryAdapter historyAdapter;
    EditText getYear, getMonth;
    TextView beginChack, beginAll, submitTitle, submitMessage, submitYear, submitMonth;
    ProgressDialog progressDialog;

    AlertDialog dialog;
    RadioGroup radioGroup;
    View panelMonth, panelAll, panelSubmit;

    String nowYear, nowMonth;

    SubmitDatasThread submitDatasThread;
    ArrayAdapter<String> submitDatasAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        setTitle("缴费历史查询");
        panelMonth = findViewById(R.id.panel_payhistory_month);
        panelAll = findViewById(R.id.panel_payhistory_all);
        panelSubmit = findViewById(R.id.panel_payhistory_submit);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_payhistory);
        getYear = (EditText) findViewById(R.id.editText_payhistory_year);
        getMonth = (EditText) findViewById(R.id.editText_payhistory_month);
        beginChack = (TextView) findViewById(R.id.textView_payhistory_start);
        beginAll = (TextView) findViewById(R.id.textView_payhistory_getall);

        submitTitle = (TextView) findViewById(R.id.textView_payhistory_submit_title);
        submitMessage = (TextView) findViewById(R.id.textView_payhistory_submit_message);
        submitYear = (TextView) findViewById(R.id.textView_payhistory_submit_year);
        submitMonth = (TextView) findViewById(R.id.textView_payhistory_submit_month);

        submitList = (ListView) findViewById(R.id.listView_payhistory_submit_list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_payHistoryList);
        submitDatasAdapter = new ArrayAdapter<>(PaymentHistoryActivity.this, android.R.layout.simple_list_item_1);
        historyAdapter = new PaymentHistoryAdapter();
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("读取中 , 请稍候...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(this);
        beginChack.setOnClickListener(this);
        beginAll.setOnClickListener(this);
        submitList.setAdapter(submitDatasAdapter);
        historyAdapter.setOnPayHistoryClickCallbacks(this);
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PaymentHistoryActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_payhistory_start:
                if (isDataVaild()) {

                    //记录下日期给 统计数据 页面使用
                    nowMonth = getMonth.getText().toString();
                    nowYear = getYear.getText().toString();

                    NetworkHelper.getInstance().getPayrecordListByMonth(PaymentHistoryActivity.this, AppOptions.USERINFO.user.getRoom(), "1000", "1", getMonth.getText().toString(), getYear.getText().toString());
                    progressDialog.show();
                } else {
                    Toast.makeText(PaymentHistoryActivity.this, "请输入正确的数据", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textView_payhistory_getall:

                //清空记录的数据
                nowMonth = null;
                nowYear = null;

                NetworkHelper.getInstance().getPayrecordList(PaymentHistoryActivity.this, AppOptions.USERINFO.user.getRoom(), "1000", "1");
                progressDialog.show();
                break;
        }
    }


    /**
     * 获取订单列表回调接口
     *
     * @param loadedCount   本次读取的数量
     * @param totalCount    数据库一共拥有的数据
     * @param roomID        房间号ID
     * @param month         月份
     * @param year          年份
     * @param payRecordBeen 获取到的数据列表.  当无数据时为NULL
     */
    @Override
    public void onGotPayRecords(int loadedCount, int totalCount, String roomID, String month, String year, ArrayList<PayRecordBean> payRecordBeen) {
        progressDialog.dismiss();
        if (!historyAdapter.changeDataSet(payRecordBeen)) {
            Toast.makeText(PaymentHistoryActivity.this, "当前日期内没有数据", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 订单获取失败回调接口
     *
     * @param isException 是否为异常
     */
    @Override
    public void onGotPayRecordsByMonthFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(PaymentHistoryActivity.this, "获取数据失败 , 请检查网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PaymentHistoryActivity.this, "数据解析失败 , 或操作取消", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取订单列表回调接口 (获取所有数据)
     *
     * @param loadedCount   本次读取的数量
     * @param totalCount    数据库一共拥有的数据
     * @param roomID        房间号ID
     * @param payRecordBeen 获取到的数据列表.  当无数据时为NULL
     */
    @Override
    public void onGotPayRecords(int loadedCount, int totalCount, String roomID, ArrayList<PayRecordBean> payRecordBeen) {
        progressDialog.dismiss();
        if (!historyAdapter.changeDataSet(payRecordBeen)) {
            Toast.makeText(PaymentHistoryActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 订单获取失败回调接口 (获取所有数据)
     *
     * @param isException 是否为异常
     */
    @Override
    public void onGotPayRecordsFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(PaymentHistoryActivity.this, "获取数据失败 , 请检查网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PaymentHistoryActivity.this, "数据解析失败 , 或操作取消", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 当进度对话框被取消的时候
     *
     * @param dialog
     */
    @Override
    public void onCancel(DialogInterface dialog) {
        //取消读取月份列表
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetPayRecordByMonth);
        //取消读取总表数据
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetPayRecordList);
        //如果有正在统计的线程 , 则取消执行
        if (submitDatasThread != null && submitDatasThread.getStatus() == AsyncTask.Status.RUNNING) {
            submitDatasThread.cancel(true);
        }
    }

    /**
     * 检测数据的数据是否合法
     *
     * @return 是否合法
     */
    private boolean isDataVaild() {
        if (TextUtils.isEmpty(getMonth.getText().toString()) && TextUtils.isEmpty(getYear.getText().toString())) {
            return false;
        } else {
            int month = Integer.parseInt(getMonth.getText().toString());
            int year = Integer.parseInt(getYear.getText().toString());

            return month >= 1 && month <= 12 && year >= 2000;
        }


    }

    /**
     * 显示订单号对话框
     *
     * @param bean 订单数据Bean
     */
    private void showOrderNoDialog(PayRecordBean bean) {
        if (bean != null) {
            if (dialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentHistoryActivity.this);
                builder.setTitle("交易订单号");
                dialog = builder.create();
            }
            dialog.setMessage(bean.getOrderNo());
            dialog.show();
        }
    }

    @Override
    public void onPayHistoryClick(PayRecordBean bean, int position) {
        showOrderNoDialog(bean);
    }

    /**
     * 选择显示标签
     *
     * @param group
     * @param checkedId 被选中的标签资源ID
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton_payhistory_month:
                recyclerView.setVisibility(View.VISIBLE);
                panelMonth.setVisibility(View.VISIBLE);
                panelSubmit.setVisibility(View.GONE);
                panelAll.setVisibility(View.GONE);
                break;
            case R.id.radioButton_payhistory_all:
                recyclerView.setVisibility(View.VISIBLE);
                panelAll.setVisibility(View.VISIBLE);
                panelMonth.setVisibility(View.GONE);
                panelSubmit.setVisibility(View.GONE);
                break;
            case R.id.radioButton_payhistory_submit:
                panelSubmit.setVisibility(View.VISIBLE);
                panelMonth.setVisibility(View.GONE);
                panelAll.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                submitDatas();
                break;
        }
    }

    /**
     * 开始统计数据
     */
    private void submitDatas() {
        if (!TextUtils.isEmpty(nowYear) && !TextUtils.isEmpty(nowMonth)) {
            submitTitle.setText("当前日期的统计结果");
            submitYear.setText(nowYear + "年");
            submitMonth.setText(nowMonth + "月");
        } else {
            submitTitle.setText("前1000条数据的统计结果");
            submitYear.setText(" -- ");
            submitMonth.setText(" -- ");
        }

        submitDatasAdapter.clear();

        if (historyAdapter.getItemCount() == 0) {
            submitTitle.setText("当前没有读取数据 , 无法进行统计");
            submitYear.setText(" -- ");
            submitMonth.setText(" -- ");
            submitMessage.setText("");
            Toast.makeText(PaymentHistoryActivity.this, "当前没有读取数据 , 无法进行统计", Toast.LENGTH_SHORT).show();
        } else {
            submitDatasThread = new SubmitDatasThread();
            submitDatasThread.execute();
        }

    }

    /**
     * 统计数据线程
     */
    class SubmitDatasThread extends AsyncTask<Integer, Void, Boolean> {

        //统计缴费金额的数量
        float sumCount = 0f;
        //统计所有缴费项目
        ArrayList<String> types;
        //每个缴费项目的金额
        ArrayList<Float> eachCount;

        public SubmitDatasThread() {
            types = new ArrayList<>();
            eachCount = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            ArrayList<PayRecordBean> arrayList = historyAdapter.getPayHistoryBeen();

            //先统计  总消费金额  所有缴费项目
            for (int i = 0; i < arrayList.size(); i++) {
                PayRecordBean bean = arrayList.get(i);

                //消费总金额
                sumCount += Float.parseFloat(bean.getChargeSum());

                //消费所有类型
                if (!types.contains(bean.getName())) {
                    types.add(bean.getName());
                }

            }

            //统计每一项的总消费金额
            for (int i = 0; i < types.size(); i++) {
                float sumCount = 0f;
                String type = types.get(i);

                for (int k = 0; k < arrayList.size(); k++) {
                    PayRecordBean bean = arrayList.get(k);
                    if (bean.getName().equals(type)) {
                        sumCount += Float.parseFloat(bean.getChargeSum());
                    }
                }

                eachCount.add(sumCount);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            submitMessage.setText("总消费金额: " + sumCount + "元");
            //合并数据
            for (int i = 0; i < types.size(); i++) {
                submitDatasAdapter.add(types.get(i) + "   " + eachCount.get(i) + "元");
            }
            submitDatasAdapter.notifyDataSetChanged();
        }
    }

}
