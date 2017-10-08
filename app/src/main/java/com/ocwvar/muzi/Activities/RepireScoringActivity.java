package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Beans.RepireStatusBean;
import com.ocwvar.muzi.Network.Callbacks.OnUploadRepireScoreCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/10  20:03
 * Project: Muzi
 * 报修评价
 */
public class RepireScoringActivity extends AppCompatActivity implements View.OnClickListener, OnUploadRepireScoreCallback, DialogInterface.OnCancelListener {

    TextView name, phone, startDate, endDate;
    ImageView best, normal, bad;
    View bestView, normalView, badView;
    Button done;

    EditText reason;

    RepireStatusBean statusBean;
    ProgressDialog progressDialog;

    int selectedTAG = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repire_scoring);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar8);
        setSupportActionBar(toolbar);
        setTitle("查询详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            //从Intent中获取 Bean 数据
            statusBean = getIntent().getExtras().getParcelable("RepireStatusBean");

            if (statusBean != null) {
                //如果数据有效,则进行View的各项加载
                progressDialog = new ProgressDialog(RepireScoringActivity.this);
                name = (TextView) findViewById(R.id.textView_scoring_name);
                phone = (TextView) findViewById(R.id.textView_scoring_phone);
                startDate = (TextView) findViewById(R.id.textView_scoring_start);
                endDate = (TextView) findViewById(R.id.textView_scoring_end);

                best = (ImageView) findViewById(R.id.imageView_scoring_best);
                normal = (ImageView) findViewById(R.id.imageView_scoring_normal);
                bad = (ImageView) findViewById(R.id.imageView_scoring_bad);

                reason = (EditText) findViewById(R.id.editText_badReason);

                bestView = findViewById(R.id.view_scoring_best);
                normalView = findViewById(R.id.view_scoring_normal);
                badView = findViewById(R.id.view_scoring_bad);

                done = (Button) findViewById(R.id.button_scoring_done);

                name.setText(statusBean.getServicemanName());
                phone.setText(statusBean.getServicemanTel());
                startDate.setText(statusBean.getSubmitTime());
                endDate.setText(statusBean.getFinishServiceTime());

                reason.setVisibility(View.GONE);
                progressDialog.setMessage("提交数据中......");
                progressDialog.setOnCancelListener(this);
                progressDialog.setCanceledOnTouchOutside(false);

                bestView.setOnClickListener(this);
                normalView.setOnClickListener(this);
                badView.setOnClickListener(this);
                done.setOnClickListener(this);
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_scoring_best:
                changeSelectedItem(0);
                break;
            case R.id.view_scoring_normal:
                changeSelectedItem(1);
                break;
            case R.id.view_scoring_bad:
                changeSelectedItem(2);
                break;
            case R.id.button_scoring_done:
                uploadScore();
                break;
        }
    }

    /**
     * 切换评价
     *
     * @param position 位置 0~2
     */
    private void changeSelectedItem(int position) {
        selectedTAG = position;

        best.setImageDrawable(getResources().getDrawable(R.drawable.ic_best_dark));
        normal.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal_dark));
        bad.setImageDrawable(getResources().getDrawable(R.drawable.ic_bad_dark));

        switch (position) {
            case 0:
                //Best 评价
                best.setImageDrawable(getResources().getDrawable(R.drawable.ic_best_light));
                reason.setVisibility(View.GONE);
                break;
            case 1:
                //Normal 评价
                normal.setImageDrawable(getResources().getDrawable(R.drawable.ic_normal_light));
                reason.setVisibility(View.GONE);
                break;
            case 2:
                //Bad 评价
                bad.setImageDrawable(getResources().getDrawable(R.drawable.ic_bad_light));
                reason.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * 提交评价
     */
    private void uploadScore() {
        if (selectedTAG <= -1) {
            Toast.makeText(RepireScoringActivity.this, "请选择一个评价", Toast.LENGTH_SHORT).show();
            return;
        }

        String reasonText = reason.getText().toString();
        String[] datas;

        if (selectedTAG == 2 && TextUtils.isEmpty(reasonText)) {
            //如果选择的是差评 , 但没有输入原因
            Toast.makeText(RepireScoringActivity.this, "请输入差评的原因", Toast.LENGTH_SHORT).show();
            return;
        } else if (selectedTAG == 2 && !TextUtils.isEmpty(reasonText)) {
            //提交差评
            datas = new String[]{
                    statusBean.getID(),
                    Integer.toString(selectedTAG),
                    reasonText
            };
        } else if (selectedTAG != 2) {
            //提交非差评的
            datas = new String[]{
                    statusBean.getID(),
                    Integer.toString(selectedTAG)
            };
        } else {
            return;
        }


        NetworkHelper.getInstance().uploadRepireScore(this, datas);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onUploadScoreCompleted() {
        progressDialog.dismiss();

        Toast.makeText(RepireScoringActivity.this, "评分提交成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onUploadScoreFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(RepireScoringActivity.this, "提交发生异常，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RepireScoringActivity.this, "提交失败,请重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.UploadRepireScore);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

}
