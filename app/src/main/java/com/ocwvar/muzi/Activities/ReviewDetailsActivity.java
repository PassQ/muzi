package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Beans.ReplyTabBean;
import com.ocwvar.muzi.Network.Callbacks.OnGetReplyTabCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpReplyContentCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpReplyPhotoCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpReplyTabCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.BaseActivity;
import com.ocwvar.muzi.Utils.ImageHelper.PicassoImageLoader;
import com.ocwvar.muzi.Utils.ImageHelper.PicassoPauseOnScrollListener;
import com.ocwvar.muzi.Views.RatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/*
* 评论详情页面
* */
public class ReviewDetailsActivity extends BaseActivity implements View.OnClickListener,OnGetReplyTabCallback,OnUpReplyTabCallback , OnUpReplyContentCallback, RatingBar.OnRatingChangeListener,OnUpReplyPhotoCallback{
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private LinearLayout linearLayout4;
    private LinearLayout linearLayout5;
    private LinearLayout linearLayout6;
    private LinearLayout linearLayout7;
    private LinearLayout linearLayout8, goneLayout;
    private final static int COMPASS_LEVEL = 20;    //10~100  数值越低质量越差
    private final static String TEMP_PATH_1 = Environment.getExternalStorageDirectory().getPath() + "/ctmp1.jpg";//存放临时图片的目录
    private final static String TEMP_PATH_2 = Environment.getExternalStorageDirectory().getPath() + "/ctmp2.jpg";
    private final static String TEMP_PATH_3 = Environment.getExternalStorageDirectory().getPath() + "/ctmp3.jpg";
    private final static String TEMP_PATH_4 = Environment.getExternalStorageDirectory().getPath() + "/ctmp4.jpg";
    private final static String TEMP_PATH_5 = Environment.getExternalStorageDirectory().getPath() + "/ctmp5.jpg";
    private static ArrayList<String> temp_phths = new ArrayList<>();
    private EditText editText_content;
    private RatingBar ratingbar2,ratingBar;
    private static   FunctionConfig functionConfig;
    private int total, REQUEST_CODE_FLAG = 0;
    private final int REQUEST_CODE_GALLERY = 1001;
    private Toolbar toolbar;
    private ThemeConfig themeConfig;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8, text_title, text_score, text_getscore, text_photo;
    private Button upPhoto_button, apply_button;
    private static ArrayList <PhotoInfo> photoinfoList = new ArrayList<>();
    ArrayList<LinearLayout> linearLayouts=new ArrayList<>();
    ArrayList<TextView> textViews = new ArrayList<>();
    ArrayList<String> statuses = new ArrayList<>();
    ArrayList<ReplyTabBean> replyList = new ArrayList<>();
    Intent sourceIntent;
    BulletinBean sourceBean;
    public static String status1 = "0",status2 = "0",status3 = "0",status4 = "0",status5 = "0",status6 = "0",status7 = "0",status8 = "0", score= "3";



    @Override
    protected boolean onPreSetup() {
        return true;
    }

    @Override
    protected int setActivityView() {
        return R.layout.activity_review_details;
    }

    @Override
    protected int onSetToolBar() {
        return R.id.details_toolbar;
    }

    @Override
    protected void onSetupViews() {
        toolbar = getToolBar();
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button     显示返回箭头的按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sourceIntent = getIntent();
        sourceBean = (BulletinBean) sourceIntent.getSerializableExtra("bulletinBean");
        NetworkHelper.getInstance().getReplyTab(ReviewDetailsActivity.this,sourceBean.getBusinessID(),8);
        initView();
    }

    @Override
    protected void onViewClick(View clickedView) {
        switch (clickedView.getId()){
            case R.id.details_linear1:
                if (statuses.get(0).equals("0")|| statuses.get(0).equals("")) {
                    linearLayout1.setBackgroundResource(R.drawable.shape_cart_red);
                    tv1.setTextColor(this.getResources()
                            .getColor(R.color.red));
                    statuses.set(0,"1");
                }
                else  if (statuses.get(0).equals("1") ) {

                    linearLayout1.setBackgroundResource(R.drawable.shape_cart_gray);
                    tv1.setTextColor(this.getResources().getColor(R.color.gray));
                    statuses.set(0,"0");
                }
                break;


            case R.id.details_linear2:
                if (statuses.get(1).equals("0")||statuses.get(1).equals("0")) {
                    linearLayout2.setBackgroundResource(R.drawable.shape_cart_red);
                    tv2.setTextColor(this.getResources()
                            .getColor(R.color.orange));
                    statuses.set(1,"1");
                }

                else if (statuses.get(1).equals("1") ) {

                    linearLayout2.setBackgroundResource(R.drawable.shape_cart_gray);
                    tv2.setTextColor(this.getResources().getColor(R.color.gray));
                    statuses.set(1,"0");
                }
                break;

            case R.id.details_linear3:
                if (statuses.get(2).equals("0")|| statuses.get(2).equals("")) {
                    linearLayout3.setBackgroundResource(R.drawable.shape_cart_red);
                    tv3.setTextColor(this.getResources()
                            .getColor(R.color.orange));
                    statuses.set(2,"1");
                }
                else if (statuses.get(2).equals("1") ) {

                    linearLayout3.setBackgroundResource(R.drawable.shape_cart_gray);
                    tv3.setTextColor(this.getResources().getColor(R.color.gray));
                    statuses.set(2,"0");
                }
                break;

            case R.id.details_linear4:
                if (statuses.get(3).equals("0")|| statuses.get(3).equals("")) {
                    linearLayout4.setBackgroundResource(R.drawable.shape_cart_red);
                    tv4.setTextColor(this.getResources()
                            .getColor(R.color.orange));
                    statuses.set(3,"1");
                }
                else if (statuses.get(3).equals("1") ) {

                    linearLayout4.setBackgroundResource(R.drawable.shape_cart_gray);
                    tv4.setTextColor(this.getResources().getColor(R.color.gray));
                    statuses.set(3,"0");
                }
                break;
            case R.id.details_linear5:
                if (statuses.get(4).equals("0")|| statuses.get(4).equals("")) {
                    linearLayout5.setBackgroundResource(R.drawable.shape_cart_red);
                    tv5.setTextColor(this.getResources()
                            .getColor(R.color.orange));
                    statuses.set(4,"1");
                }
                else if (statuses.get(4).equals("1") ) {

                    linearLayout5.setBackgroundResource(R.drawable.shape_cart_gray);
                    tv5.setTextColor(this.getResources().getColor(R.color.gray));
                    statuses.set(4,"0");
                }
                break;
            case R.id.details_linear6:
                if (statuses.get(5).equals("0")|| statuses.get(5).equals("")) {
                    linearLayout6.setBackgroundResource(R.drawable.shape_cart_red);
                    tv6.setTextColor(this.getResources()
                            .getColor(R.color.orange));
                    statuses.set(5,"1");
                }
                else if (statuses.get(5).equals("1") ) {

                    linearLayout6.setBackgroundResource(R.drawable.shape_cart_gray);
                    tv6.setTextColor(this.getResources().getColor(R.color.gray));
                    statuses.set(5,"0");
                }
                break;
            case R.id.details_linear7:
                if (statuses.get(6).equals("0")|| statuses.get(6).equals("")) {
                    linearLayout7.setBackgroundResource(R.drawable.shape_cart_red);
                    tv7.setTextColor(this.getResources()
                            .getColor(R.color.orange));
                    statuses.set(6,"1");
                }
                else if (statuses.get(6).equals("1") ) {

                    linearLayout7.setBackgroundResource(R.drawable.shape_cart_gray);
                    tv7.setTextColor(this.getResources().getColor(R.color.gray));
                    statuses.set(6,"0");
                }
                break;
            case R.id.details_linear8:
                if (statuses.get(7).equals("0")|| statuses.get(7).equals("")) {
                    linearLayout8.setBackgroundResource(R.drawable.shape_cart_red);
                    tv8.setTextColor(this.getResources()
                            .getColor(R.color.orange));
                    statuses.set(7,"1");
                }
                else if (statuses.get(7).equals("1") ) {

                    linearLayout8.setBackgroundResource(R.drawable.shape_cart_gray);
                    tv8.setTextColor(this.getResources().getColor(R.color.gray));
                    statuses.set(7,"0");
                }
                break;
            case R.id.btn_photo:
                text_photo.setVisibility(View.INVISIBLE);
                photoinfoList.clear();
                REQUEST_CODE_FLAG = 0;
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

                break;
            case R.id.btn_apply:
                showProgressDialog("正在评论，请稍后",false,null);
                NetworkHelper.getInstance().upReplyTab(ReviewDetailsActivity.this,toJson(),sourceBean.getBusinessID());
                NetworkHelper.getInstance().upReplyContent(ReviewDetailsActivity.this,sourceBean.getBusinessID(),score,editText_content.getText().toString(), AppOptions.USERINFO.user.getPhoneNumber(), getTime(), sourceBean.getOrderID());

            default:
                break;

        }


    }

    @Override
    protected boolean onViewLongClick(View holdedView) {
        return false;
    }

    private void initView() {
        linearLayout1= (LinearLayout) findViewById(R.id.details_linear1);
        linearLayout2= (LinearLayout) findViewById(R.id.details_linear2);
        linearLayout3= (LinearLayout) findViewById(R.id.details_linear3);
        linearLayout4= (LinearLayout) findViewById(R.id.details_linear4);
        linearLayout5= (LinearLayout) findViewById(R.id.details_linear5);
        linearLayout6= (LinearLayout) findViewById(R.id.details_linear6);
        linearLayout7= (LinearLayout) findViewById(R.id.details_linear7);
        linearLayout8= (LinearLayout) findViewById(R.id.details_linear8);
        goneLayout = (LinearLayout) findViewById(R.id.gone_layout1);

        text_photo = (TextView) findViewById(R.id.text_photomassage);
        editText_content = (EditText) findViewById(R.id.review_content);
        ratingbar2 = (RatingBar) findViewById(R.id.ratingBar2);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingChangeListener(this);
        ratingbar2.setStar(Float.parseFloat(sourceBean.getScore()));
        text_title = (TextView) findViewById(R.id.review_title);
        text_title.setText(sourceBean.getTitle());
        text_score = (TextView) findViewById(R.id.review_score);
        text_score.setText(sourceBean.getScore());
        text_getscore = (TextView) findViewById(R.id.review_getscore);

        linearLayouts.add(linearLayout1);
        linearLayouts.add(linearLayout2);
        linearLayouts.add(linearLayout3);
        linearLayouts.add(linearLayout4);
        linearLayouts.add(linearLayout5);
        linearLayouts.add(linearLayout6);
        linearLayouts.add(linearLayout7);
        linearLayouts.add(linearLayout8);

        temp_phths.add(TEMP_PATH_1);
        temp_phths.add(TEMP_PATH_2);
        temp_phths.add(TEMP_PATH_3);
        temp_phths.add(TEMP_PATH_4);
        temp_phths.add(TEMP_PATH_5);

        upPhoto_button= (Button) findViewById(R.id.btn_photo);
        upPhoto_button.setOnClickListener(this);
        apply_button = (Button) findViewById(R.id.btn_apply);
        apply_button.setOnClickListener(this);

        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);
        linearLayout4.setOnClickListener(this);
        linearLayout5.setOnClickListener(this);
        linearLayout6.setOnClickListener(this);
        linearLayout7.setOnClickListener(this);
        linearLayout8.setOnClickListener(this);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tv6 = (TextView) findViewById(R.id.tv6);
        tv7 = (TextView) findViewById(R.id.tv7);
        tv8 = (TextView) findViewById(R.id.tv8);
        textViews.add(tv1);
        textViews.add(tv2);
        textViews.add(tv3);
        textViews.add(tv4);
        textViews.add(tv5);
        textViews.add(tv6);
        textViews.add(tv7);
        textViews.add(tv8);
        statuses.add(status1);
        statuses.add(status2);
        statuses.add(status3);
        statuses.add(status4);
        statuses.add(status5);
        statuses.add(status6);
        statuses.add(status7);
        statuses.add(status8);
        setGalleryFineal();




    }

    private void setReplyTab(ArrayList<LinearLayout> linearLayouts, ArrayList<TextView> textViews, ArrayList<String> statuses, ArrayList<ReplyTabBean> replyList, int total) {
        for (int i = 0; i <total ; i++) {
            if(i>3){
                goneLayout.setVisibility(View.VISIBLE);
            }
            linearLayouts.get(i).setVisibility(View.VISIBLE);
            textViews.get(i).setText(replyList.get(i).getContent());
            if (statuses.get(i).equals("1") ) {
                linearLayouts.get(i).setBackgroundResource(R.drawable.shape_cart_red);
                textViews.get(i).setTextColor(this.getResources()
                        .getColor(R.color.red));
            } else if (statuses.get(i).equals("0")|| statuses.get(i).equals("")) {
                linearLayouts.get(i).setBackgroundResource(R.drawable.shape_cart_gray);
                textViews.get(i).setTextColor(this.getResources().getColor(R.color.gray));
            }
        }
    }


    //初始化GalleryFineal控件操作
    private void setGalleryFineal() {
        themeConfig = ThemeConfig.DARK;
        ImageLoader imageLoader = new PicassoImageLoader();
        PauseOnScrollListener pauseOnScrollListener = new PicassoPauseOnScrollListener(false, true);
        functionConfig = new FunctionConfig.Builder()
                                        .setMutiSelectMaxSize(5)
                                        .setEnablePreview(true)
                                        .setEnableCamera(true)
                                        .setSelected(photoinfoList)
                                        .build();
        CoreConfig coreConfig = new CoreConfig.Builder(ReviewDetailsActivity.this, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }



    private String getTime() {
        long time=System.currentTimeMillis();
        String timeStr = time+"";
        return timeStr;
    }

    private JSONArray toJson() {
        JSONObject json=new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < total ; i++) {
            JSONObject temp=new JSONObject();
            Log.e("statuses: ", statuses.get(i) );
            if("1".equals(statuses.get(i))){
                try {
                    temp.put("preinsta",textViews.get(i).getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(temp);

            }
        }
//        try {
//            json.put("preinstaArray",jsonArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return json;
        return jsonArray;
    }

    //选择图片回调
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                if(resultList.size()>0) {
                    REQUEST_CODE_FLAG = 1;
                    photoinfoList.addAll(resultList);
                    text_photo.setText("已选择" + photoinfoList.size() + "张图片");
                    text_photo.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ReviewDetailsActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };



    //获取预设回复成功回调
    @Override
    public void onGetReplyTabCompleted(ArrayList<ReplyTabBean> list, int total) {
        this.replyList = list;
        this.total = total;
        setReplyTab(linearLayouts,textViews,statuses,replyList,this.total);

    }
    //获取预设回复失败回调
    @Override
    public void onGetReplyTabFailed(boolean isException) {

    }
    //上传预设评论成功回调
    @Override
    public void onUpReplyTabCompleted(String massage, boolean isSuccessed) {

    }
    //上传预设评论失败回调
    @Override
    public void onUpReplyTabFailed(boolean isException) {

    }

    //上传详细评论成功回调
    @Override
    public void onUpReplyContentCompleted(int commentID, boolean isSuccessed) {
        if(REQUEST_CODE_FLAG==1){


            compassPictureFile();
            NetworkHelper.getInstance().upReplyPhoto(this,commentID,getPhoto());
        }else {
            dismissProgressDialog();
            Intent intent =new Intent();
            intent.setClass(ReviewDetailsActivity.this,ReplySuccessActivity.class);
            startActivityForResult(intent,0);
        }

    }


    //获取上传图片集合
    private String[] getPhoto() {
        String [] files= new String[photoinfoList.size()];
        for (int i = 0; i < photoinfoList.size() ; i++) {
            files[i]=temp_phths.get(i);
        }
        return files;
    }

    //上传详细评论失败回调
    @Override
    public void onUpReplyContentFailed(boolean isException) {
        Toast.makeText(this, "评论失败，请检查网络", Toast.LENGTH_SHORT).show();
    }


    //修改了评分控件的监听回调
    @Override
    public void onRatingChange(float ratingCount) {
            score = ratingCount+"";
            text_getscore.setText(score);
    }



    /**
     * 压缩图像质量
     *
     * @return 执行结果
     */
    private boolean compassPictureFile() {


        if (photoinfoList != null && photoinfoList.size()>0) {
            try {
                for (int i = 0; i <photoinfoList.size() ; i++) {
                    WeakReference<Bitmap> temp = new WeakReference<>(BitmapFactory.decodeFile(photoinfoList.get(i).getPhotoPath()));
                    FileOutputStream outputStream = new FileOutputStream(temp_phths.get(i), false);
                    temp.get().compress(Bitmap.CompressFormat.JPEG, COMPASS_LEVEL, outputStream);
                    temp.get().recycle();
                    System.gc();
                }

                return true;
            } catch (Exception e) {
                Toast.makeText(this, "图像压缩失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "图像获取失败", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onUpReplyPhotoCompleted(String massage, boolean isSuccessed) {
        Toast.makeText(this,massage,Toast.LENGTH_SHORT).show();
        for (int i = 0; i < photoinfoList.size()-1 ; i++) {
            new File(temp_phths.get(i)).delete();

        }
        dismissProgressDialog();
        Intent intent =new Intent();
        intent.setClass(ReviewDetailsActivity.this,ReplySuccessActivity.class);
        startActivityForResult(intent,0);
    }



    @Override
    public void onUpReplyPhotoFailed(boolean isException) {
        Toast.makeText(this, "图片上传失败", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch ( requestCode ){
            case 0:
                finish();
                break;
            default:
                break;
        }
    }
}
