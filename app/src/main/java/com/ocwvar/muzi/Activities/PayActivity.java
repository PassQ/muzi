package com.ocwvar.muzi.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ocwvar.muzi.Adapters.PaymentAdapter;
import com.ocwvar.muzi.Beans.PaymentBean;
import com.ocwvar.muzi.Network.Callbacks.OnCheckPaymentCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetPaymentListCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetScoreDeductCallback;
import com.ocwvar.muzi.Network.Callbacks.OnPaymentChangedCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpScoreDeductCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUploadPaymentCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.Network.OrderNoPendding;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.PayUtils.PayResult;
import com.ocwvar.muzi.Utils.PayUtils.PayUtils;
import com.ocwvar.muzi.Utils.PayUtils.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/12  16:51
 * Project: Muzi
 * 缴费界面
 */

public class PayActivity extends AppCompatActivity
        implements OnPaymentChangedCallback, OnGetPaymentListCallback,
        View.OnClickListener,OnGetScoreDeductCallback,OnUpScoreDeductCallback {

    //View类
    TextView limit, restMoney,scoreText;
    Button pay;
    //缴费列表相关
    RecyclerView paymentRecycleView;
    LinearLayout scoreLayout;
    String scoreMoney;
    String useScore= "0";
    PaymentAdapter adapter;
    CheckBox scoreCheckBox;
    //加载中 对话框
    ProgressDialog progressDialog;
    //支付模块
    private PaymentFunction paymentFunction;
    //当前选择的缴费项目 和 要缴费的金额
    private PaymentBean paymentBean;
    private float payCount = -1f;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar13);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("物业缴费");
        }

        //初始化支付模块
        paymentFunction = new PaymentFunction();

        initViews();
        initViewsOptions();
        loadPaymentList();
        initFailedOrder();

    }

    /**
     * 加载所有的View
     */
    private void initViews() {
        progressDialog = new ProgressDialog(PayActivity.this);
        scoreLayout = (LinearLayout) findViewById(R.id.layout_pay_score);
        paymentRecycleView = (RecyclerView) findViewById(R.id.recycle_pay_paymentList);
        pay = (Button) findViewById(R.id.button_pay_startPay);
        scoreText = (TextView) findViewById(R.id.text_pay_score);
        limit = (TextView) findViewById(R.id.textView_pay_limit);
        restMoney = (TextView) findViewById(R.id.textView_pay_money);
        scoreCheckBox = (CheckBox) findViewById(R.id.checkbox_pay);
    }

    /**
     * 进行所有View的属性设置
     */
    private void initViewsOptions() {
        pay.setOnClickListener(this);
        adapter = new PaymentAdapter(this);
        progressDialog.setMessage("正在处理......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        paymentRecycleView.setAdapter(adapter);
        paymentRecycleView.setLayoutManager(new LinearLayoutManager(PayActivity.this, LinearLayoutManager.VERTICAL, false));
        paymentRecycleView.setHasFixedSize(false);
    }

    /**
     * 读取缴费项目列表
     */
    private void loadPaymentList() {
        progressDialog.show();
        NetworkHelper.getInstance().getPaymentItems(this, AppOptions.USERINFO.user.getRoom());
    }

    /**
     * 读取旧的失败订单
     */
    private void initFailedOrder() {
        if (OrderNoPendding.getInstance().isHasData()) {
            //如果有旧的失败订单 , 则限制新的订单请求

            Toast.makeText(PayActivity.this, "您有订单需要同步", Toast.LENGTH_SHORT).show();
            paymentFunction.canPay = false;
        } else {
            paymentFunction.canPay = true;
        }
    }

    /**
     * 当  缴费项目  和  金额  发生变动的回调
     *
     * @param paymentBean 缴费项目
     * @param position    在 RecyclerView 中的位置
     * @param payCount    要支付的金额
     */
    @Override
    public void onPaymentChanged(PaymentBean paymentBean, int position, float payCount) {
        //获取当前选择项的余额
        restMoney.setText(String.format("%s 元", paymentBean.getSumsBalance()));

        //获取对象 , 供提交缴费的时候调用
        this.paymentBean = paymentBean;
        this.payCount = payCount;

    }

    /**
     * 当获取到缴费项目列表的回调
     *
     * @param paymentList 缴费项目列表
     */
    @Override
    public void onGotList(ArrayList<PaymentBean> paymentList) {
        progressDialog.dismiss();

        if (paymentList.size() <= 0) {
            Toast.makeText(PayActivity.this, "暂无缴费项目", Toast.LENGTH_SHORT).show();
        } else {
            //将获取到的列表传入 Adapter 中,并进行展示
            adapter.setPaymentList(paymentList);

            //刷新当前的选择项目的金额
            if (this.paymentBean != null) {
                //从列表中  搜寻  与刷新前有相同  名称  单价  支付ID  房号 的缴费项目
                this.paymentBean = paymentList.get(paymentList.indexOf(this.paymentBean));
                if (this.paymentBean != null) {
                    restMoney.setText(String.format("%s 元", this.paymentBean.getSumsBalance()));
                } else {
                    restMoney.setText("--");
                }
            }
        }

    }

    /**
     * 但获取缴费项目失败时的回调
     *
     * @param isException 是否为异常
     */
    @Override
    public void onFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(PayActivity.this, "加载出现异常,请检查网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PayActivity.this, "加载出现异常,请重试", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击事件的回调
     *
     * @param v 触发点击事件的View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_pay_startPay:
                if (paymentBean != null && payCount > 0f) {
                    //如果当前有支付项目 , 同时支付金额大于 0

                    if (paymentFunction.canPay) {
                        //如果当前是可以支付状态 , 则开始提交订单前的准备工作
                        progressDialog.show();
                        NetworkHelper.getInstance().getScoreDeduct(PayActivity.this, AppOptions.USERINFO.user.getPhoneNumber(),payCount+"");

                    } else {
                        //如果当前是不可支付状态 , 则说明有旧订单未同步
                        Toast.makeText(PayActivity.this, "您尚有未同步的订单, 请先同步再支付新的订单", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //支付金额或项目不符合要求
                    Toast.makeText(PayActivity.this, "请选择有效支付项目和有效金额", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_paypage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_pay_syne:
                if (OrderNoPendding.getInstance().isHasData()) {
                    //如果当前有失败订单的数据 , 则开始提交

                    paymentFunction.uploadFailedOrder();
                } else {
                    Toast.makeText(PayActivity.this, "您目前没有需要同步的订单", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_pay_history:
                startActivity(new Intent(PayActivity.this, PaymentHistoryActivity.class));
                break;
        }
        return true;
    }


    //获取抵消积分数据成功回调
    @Override
    public void onGetScoreDeductCompleted(String useScore, String restMoney) {
        progressDialog.dismiss();
        if("0".equals(useScore)||useScore == null) {

        }else {
            scoreLayout.setVisibility(View.VISIBLE);
            scoreText.setText("使用" + useScore + "积分抵消费用");
            scoreMoney = restMoney;
            this.useScore = useScore;
            Intent intent = new Intent();
            intent.putExtra("useScore",useScore);//所需要抵消的积分
            intent.putExtra("restMoney",restMoney);//积分抵消后金额
            intent.putExtra("payCount",payCount);//消费总金额
            intent.putExtra("TAG",1);//根据TAG判断是否显示checkBox

            intent.setClass(PayActivity.this,PaymentDialog.class);
            startActivityForResult(intent,1);
        }


    }


    //获取抵消积分数据失败回调
    @Override
    public void onGetScoreDeductFailed(boolean isException) {
        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("payCount",payCount);
        intent.putExtra("TAG",0);

        intent.setClass(PayActivity.this,PaymentDialog.class);
        startActivityForResult(intent,1);

    }


    //消耗积分成功回调
    @Override
    public void onUpScoreDeductCompleted(boolean isSuccessed) {

    }

    //消耗积分失败回调
    @Override
    public void onUpScoreDeductFailed(boolean isException) {

    }

    /**
     * 支付模块
     */
    class PaymentFunction implements OnUploadPaymentCallback, OnCheckPaymentCallback {

        private static final int SDK_PAY_FLAG = 1;
        private boolean canPay = true;            //当前是否可支付
        private String orderNo;                       //生成的订单号
        private String orderInfo;                     //生成的订单信息
        private String PARTNER = PayUtils.getPARTNER();
        private String RSA_PRIVATE = PayUtils.getRsaPrivate();
        private String SELLER = PayUtils.getSELLER();
        @SuppressLint("HandlerLeak")
        private Handler mHandler = new Handler() {

            @SuppressWarnings("unused")
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        PayResult payResult = new PayResult((String) msg.obj);
                        /**
                         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                         * docType=1) 建议商户依赖异步通知
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            //上传订单
                            NetworkHelper.getInstance().uploadPayment(PaymentFunction.this, paymentBean.getRoomID(), paymentBean.getID(), Float.toString(payCount), orderNo, useScore, AppOptions.USERINFO.user.getPhoneNumber());
                        } else {
                            // 判断resultStatus 为非"9000"则代表可能支付失败
                            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(PayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                progressDialog.dismiss();
                                Toast.makeText(PayActivity.this, "支付失败 , 如果没有安装支付宝请先安装", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }
            }

            ;
        };

        /**
         * create the order info. 创建订单信息
         */
        private String getOrderInfo(String subject, String body, String price) {

            // 签约合作者身份ID
            String orderInfo = "partner=" + "\"" + PARTNER + "\"";

            // 签约卖家支付宝账号
            orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

            // 商户网站唯一订单号
            orderNo = getOutTradeNo();
            orderInfo += "&out_trade_no=" + "\"" + orderNo + "\"";

            // 商品名称
            orderInfo += "&subject=" + "\"" + subject + "\"";

            // 商品详情
            orderInfo += "&body=" + "\"" + body + "\"";

            // 商品金额
            orderInfo += "&total_fee=" + "\"" + price + "\"";

            // 服务器异步通知页面路径
            orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

            // 服务接口名称， 固定值
            orderInfo += "&service=\"mobile.securitypay.pay\"";

            // 支付类型， 固定值
            orderInfo += "&payment_type=\"1\"";

            // 参数编码， 固定值
            orderInfo += "&_input_charset=\"utf-8\"";

            // 设置未付款交易的超时时间
            // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
            // 取值范围：1m～15d。
            // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
            // 该参数数值不接受小数点，如1.5h，可转换为90m。
            orderInfo += "&it_b_pay=\"30m\"";

            // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
            // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

            // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
            orderInfo += "&return_url=\"m.alipay.com\"";

            // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
            // orderInfo += "&paymethod=\"expressGateway\"";

            return orderInfo;
        }

        /**
         * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
         */
        private String getOutTradeNo() {
            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
            Date date = new Date();
            String key = format.format(date);

            Random r = new Random(System.currentTimeMillis());
            key = key + r.nextInt();
            key = key.substring(0, 15);
            return key;
        }

        /**
         * sign the order info. 对订单信息进行签名
         *
         * @param content 待签名订单信息
         */
        private String sign(String content) {
            return SignUtils.sign(content, RSA_PRIVATE);
        }

        /**
         * get the sign type we use. 获取签名方式
         */
        private String getSignType() {
            return "sign_type=\"RSA\"";
        }

        /**
         * 支付新订单前进行检查
         *
         * @param paymentBean 要支付项目的 Bean
         * @param payCount    要支付的总金额
         */
        private void prePayAction(PaymentBean paymentBean, float payCount) {

            progressDialog.show();

            if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
                new AlertDialog.Builder(PayActivity.this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                finish();
                            }
                        }).show();
                return;
            }

            orderInfo = getOrderInfo(paymentBean.getName(), paymentBean.getName(), payCount+"");

            /**
             * 先检查是否有相同的订单号
             */
            NetworkHelper.getInstance().checkPayment(PaymentFunction.this, orderNo);

        }

        /**
         * 开始新的订单支付
         */
        private void startPaymentAction() {

            /**
             * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
             */
            String sign = sign(orderInfo);
            try {
                /**
                 * 仅需对sign 做URL编码
                 */
                sign = URLEncoder.encode(sign, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            /**
             * 完整的符合支付宝参数规范的订单信息
             */
            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {

                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(PayActivity.this);

                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo, true);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }

        /**
         * 上传旧的失败订单
         */
        private void uploadFailedOrder() {
            NetworkHelper.getInstance().checkPayment(PaymentFunction.this, OrderNoPendding.getInstance().getOrderNo());
        }

        /**
         * 订单上传成功
         *
         * @param roomID
         * @param chargeTypeID
         * @param payCount
         * @param orderNo      缴费成功的订单号
         */
        @Override
        public void onUploadCompleted(String roomID, String chargeTypeID, String payCount, String orderNo) {

            AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
            builder.setTitle("缴费成功");

            //如果之前的支付状态为否 , 则说明这次提交成功的是之前失败的订单
            if (!canPay) {
                //将旧的订单进行清除
                OrderNoPendding.getInstance().clearData();
                builder.setTitle("订单同步成功");

            }
            builder.setMessage("您的订单号是:" + orderNo);
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NetworkHelper.getInstance().getPaymentItems(PayActivity.this, AppOptions.USERINFO.user.getRoom());
                }
            });

            builder.show();
            canPay = true;
        }

        /**
         * 订单上传失败
         *
         * @param isException  是否为异常
         * @param roomID       房间号
         * @param chargeTypeID 支付类型
         * @param payCount     订单金额
         * @param orderNo      订单号
         */
        @Override
        public void onUploadFailed(boolean isException, String roomID, String chargeTypeID, String payCount, String orderNo) {
            progressDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
            if (isException) {
                builder.setTitle("缴费异常");
                builder.setMessage("缴费成功 , 但订单号因为网络原因上传失败 .\n\n 请在网络通畅时点击右上角的同步订单按钮进行订单同步 . \n\n您的订单号是:" + orderNo);
                OrderNoPendding.getInstance().pendOrderNo(AppOptions.USERINFO.user.getPhoneNumber(), roomID, chargeTypeID, payCount, orderNo,useScore);

            }

            canPay = false;
            builder.show();
        }

        /**
         * 检查订单结果
         *
         * @param isExist 订单号是否存在
         */
        @Override
        public void onCheckedResult(boolean isExist) {
            //如果订单号已存在
            if (isExist) {
                //如果当前是不可支付状态 , 即这次检查的是上次失败的订单
                if (!canPay) {
                    //那么我们就清除旧的失败订单号
                    OrderNoPendding.getInstance().clearData();
                    Toast.makeText(PayActivity.this, "您的订单已在别处同步 , 您现在可以进行新的订单支付", Toast.LENGTH_SHORT).show();
                } else {
                    //如果当前是可支付状态 , 即这次检查的是新订单
                    Toast.makeText(PayActivity.this, "订单相同 , 请勿重复支付", Toast.LENGTH_SHORT).show();
                }
            } else {
                //订单号不存在
                if (!canPay) {
                    //旧订单上传
                    OrderNoPendding.getInstance().sendData(PaymentFunction.this);
                } else {
                    //新订单上传
                    startPaymentAction();
                }
            }
        }


        /**
         * 检查订单失败
         *
         * @param isException 是否为异常
         */
        @Override
        public void onCheckFailed(boolean isException) {
            progressDialog.dismiss();
            Toast.makeText(PayActivity.this, "网络出现异常 , 请重试", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==2) {
            useScore = data.getStringExtra("useScore");
            paymentFunction.prePayAction(paymentBean, Float.parseFloat(data.getStringExtra("restMoney")));
        }else if(resultCode ==1){

            if( data.getFloatExtra("payCount",-1f)==-1f){
                Toast.makeText(PayActivity.this,"网络出错，请重试",Toast.LENGTH_SHORT).show();
            }else {
                useScore = "0";
                paymentFunction.prePayAction(paymentBean, data.getFloatExtra("payCount",-1f));
            }

        }else if(resultCode == 0){
            Toast.makeText(PayActivity.this,"网络出错，请重试",Toast.LENGTH_SHORT).show();
        }
    }
}
