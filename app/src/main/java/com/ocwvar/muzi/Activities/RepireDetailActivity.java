package com.ocwvar.muzi.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.RepireDerailPicturesAdapter;
import com.ocwvar.muzi.Beans.RepireBean;
import com.ocwvar.muzi.Network.Callbacks.OnChangeRepireStatus;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Data: 2016/7/6 1:49
 * Project: Muzi
 * 维修详情界面
 */
public class RepireDetailActivity extends AppCompatActivity implements View.OnClickListener, OnChangeRepireStatus, View.OnLongClickListener {

    public final static int STATUS_UNACCPETED = 900;    //未受理
    public final static int STATUS_ACCPETED = 901;      //已受理
    public final static int STATUS_COMPLETED = 902;     //维修完成  待评价
    public final static int STATUS_READONLY = 903;      //只读

    public final static int ACTION_ACCEPT = 300;
    public final static int ACTION_COMPLETED = 301;
    public final static int ACTION_STAY = 302;

    private final static int PICTURE_MAX = 5;
    private final static int REQUEST_PERMISSION = 300;
    private final static int REQUEST_CAMERA = 301;
    private final static int COMPASS_LEVEL = 20;    //10~100  数值越低质量越差

    private final static String TEMP_PATH_1 = Environment.getExternalStorageDirectory().getPath() + "/ctmp1.jpg";
    private final static String TEMP_PATH_2 = Environment.getExternalStorageDirectory().getPath() + "/ctmp2.jpg";
    private final static String TEMP_PATH_3 = Environment.getExternalStorageDirectory().getPath() + "/ctmp3.jpg";
    private final static String TEMP_PATH_4 = Environment.getExternalStorageDirectory().getPath() + "/ctmp4.jpg";
    private final static String TEMP_PATH_5 = Environment.getExternalStorageDirectory().getPath() + "/ctmp5.jpg";
    int picCounts = 0;      //当前图片的数量

    RepireBean repireBean;
    RepireDerailPicturesAdapter adapter;
    TextView title, message, picCount;
    RecyclerView imageList;
    ImageButton addPic, delPic;
    Button done;
    View uploadPanel;

    ProgressDialog progressDialog;

    int status = STATUS_READONLY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            repireBean = savedInstanceState.getParcelable("RepireBean");
        } else if (getIntent().getExtras() != null) {
            repireBean = getIntent().getExtras().getParcelable("RepireBean");
        } else {
            Toast.makeText(RepireDetailActivity.this, "数据丢失,请重试", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (repireBean == null) {
            Toast.makeText(RepireDetailActivity.this, "数据损坏,请重试", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            setContentView(R.layout.activity_repire_detail);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setResult(ACTION_STAY);
            title = (TextView) findViewById(R.id.textView_repireDetail_title);
            message = (TextView) findViewById(R.id.textView_repireDetail_message);
            picCount = (TextView) findViewById(R.id.textView_repireDetail_picCount);

            imageList = (RecyclerView) findViewById(R.id.recycleView);

            addPic = (ImageButton) findViewById(R.id.imageButton_addPic);
            delPic = (ImageButton) findViewById(R.id.imageButton_delPic);

            done = (Button) findViewById(R.id.button_done);

            uploadPanel = findViewById(R.id.panel_repire_upload);

            progressDialog = new ProgressDialog(RepireDetailActivity.this);
            adapter = new RepireDerailPicturesAdapter(repireBean.getPicturesThu());
            progressDialog.setMessage("上传数据中 , 请等待...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            imageList.setAdapter(adapter);
            imageList.setHasFixedSize(true);
            imageList.setLayoutManager(new LinearLayoutManager(RepireDetailActivity.this, LinearLayoutManager.VERTICAL, false));
            title.setText(repireBean.getTitle());
            message.setText(repireBean.getContent());
            setTitle(repireBean.getTitle());

            done.setOnClickListener(this);
            addPic.setOnClickListener(this);
            delPic.setOnClickListener(this);
            delPic.setOnLongClickListener(this);

            //先清除旧的照片文件
            clearUpFiles();
            //确定当前的工单状态
            getCurrectStatus(repireBean.getCurrentState());

            if (status == STATUS_UNACCPETED) {
                //如果当前的是未受理状态   则应当是受理的界面
                done.setText("受理");
                uploadPanel.setVisibility(View.GONE);
            } else if (status == STATUS_ACCPETED) {
                //如果当前的是已受理状态   则应当是维修完成的界面
                done.setText("维修完成");
                uploadPanel.setVisibility(View.VISIBLE);
            } else {
                //如果当前的状态不明 , 或是待评价的状态  则应当是只读的界面
                done.setVisibility(View.GONE);
                uploadPanel.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 开始更改数据
     */
    private void changeStatus() {

        if (status == STATUS_ACCPETED) {
            //如果当前是已受理状态 , 那执行的应当是  维修完成  的动作
            if (picCounts > 0) {
                progressDialog.show();
                NetworkHelper.getInstance().changeRepireStatus(this, repireBean.getID(), AppOptions.USERINFO.user.getPhoneNumber(), "0", getPictures());
            } else {
                Toast.makeText(RepireDetailActivity.this, "完成维修需要拍照上传文件 , 最少需要一张 !", Toast.LENGTH_SHORT).show();
            }
        } else if (status == STATUS_UNACCPETED) {
            //如果当前是未受理状态 , 那执行的应当是  受理工单  的动作
            progressDialog.show();
            NetworkHelper.getInstance().changeRepireStatus(this, repireBean.getID(), AppOptions.USERINFO.user.getPhoneNumber(), "1", null);

        }


    }

    /**
     * 确定当前的受理状态
     *
     * @param currentState Bean的状态信息
     * @return 受理状态  true---未受理   false---已受理
     */
    private void getCurrectStatus(String currentState) {
        Log.d("工单处理", "工单状态:" + currentState);
        switch (currentState) {
            case "1":   //预指定
            case "2":   //公开
            case "6":   //强制指派
                status = STATUS_UNACCPETED;
                break;
            case "3":   //已受理
                status = STATUS_ACCPETED;
                break;
            case "4":   //待评价
            case "5":   //维修结束
                status = STATUS_COMPLETED;
                break;
            default:    //未定义状态是只读
                status = STATUS_READONLY;
        }
    }

    /**
     * 从摄像头获取图像
     *
     * @param picCounts 当前已经获取了多少张
     * @return 请求结果
     */
    private boolean takePictureFromCamera(int picCounts) {
        if (picCounts >= PICTURE_MAX) {
            Toast.makeText(RepireDetailActivity.this, "已达到最大图像数量", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (RepireDetailActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || RepireDetailActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                RepireDetailActivity.this.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                Toast.makeText(RepireDetailActivity.this, "请先给予应用权限", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (picCounts) {
            case 0:
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(TEMP_PATH_1)));
                break;
            case 1:
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(TEMP_PATH_2)));
                break;
            case 2:
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(TEMP_PATH_3)));
                break;
            case 3:
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(TEMP_PATH_4)));
                break;
            case 4:
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(TEMP_PATH_5)));
                break;
        }
        startActivityForResult(intent, REQUEST_CAMERA);
        return true;
    }

    /**
     * 压缩图像质量
     *
     * @return 执行结果
     */
    private boolean compassPictureFile() {
        File saveFile = null;
        switch (picCounts) {
            case 1:
                saveFile = new File(TEMP_PATH_1);
                break;
            case 2:
                saveFile = new File(TEMP_PATH_2);
                break;
            case 3:
                saveFile = new File(TEMP_PATH_3);
                break;
            case 4:
                saveFile = new File(TEMP_PATH_4);
                break;
            case 5:
                saveFile = new File(TEMP_PATH_5);
                break;
        }
        if (saveFile != null && saveFile.exists() && saveFile.length() > 0) {
            try {
                WeakReference<Bitmap> temp = new WeakReference<>(BitmapFactory.decodeFile(saveFile.getPath()));
                FileOutputStream outputStream = new FileOutputStream(saveFile.getPath(), false);
                temp.get().compress(Bitmap.CompressFormat.JPEG, COMPASS_LEVEL, outputStream);
                temp.get().recycle();
                System.gc();
                return true;
            } catch (Exception e) {
                Toast.makeText(RepireDetailActivity.this, "图像压缩失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(RepireDetailActivity.this, "图像获取失败", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 移除最新添加的图片
     *
     * @param picCounts 当前数量
     * @return 执行结果
     */
    private boolean removePicture(int picCounts) {
        if (picCounts == 0) {
            Toast.makeText(RepireDetailActivity.this, "当前没有图像", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            switch (picCounts) {
                case 1:
                    return new File(TEMP_PATH_1).delete();
                case 2:
                    return new File(TEMP_PATH_2).delete();
                case 3:
                    return new File(TEMP_PATH_3).delete();
                case 4:
                    return new File(TEMP_PATH_4).delete();
                case 5:
                    return new File(TEMP_PATH_5).delete();
                default:
                    Toast.makeText(RepireDetailActivity.this, "未定义错误", Toast.LENGTH_SHORT).show();
                    return false;
            }

        }
    }

    /**
     * 得到图片的路径数组
     *
     * @return 图片的路径数组
     */
    private String[] getPictures() {
        String[] files = new String[picCounts];
        switch (picCounts) {
            case 5:
                files[4] = TEMP_PATH_5;
            case 4:
                files[3] = TEMP_PATH_4;
            case 3:
                files[2] = TEMP_PATH_3;
            case 2:
                files[1] = TEMP_PATH_2;
            case 1:
                files[0] = TEMP_PATH_1;
                break;
            default:
                files = null;
        }
        return files;
    }

    /**
     * 清除残留的文件
     */
    private void clearUpFiles() {
        picCounts = 0;
        picCount.setText("0");
        final String TEMP_PATH_1 = Environment.getExternalStorageDirectory().getPath() + "/ctmp1.jpg";
        final String TEMP_PATH_2 = Environment.getExternalStorageDirectory().getPath() + "/ctmp2.jpg";
        final String TEMP_PATH_3 = Environment.getExternalStorageDirectory().getPath() + "/ctmp3.jpg";
        final String TEMP_PATH_4 = Environment.getExternalStorageDirectory().getPath() + "/ctmp4.jpg";
        final String TEMP_PATH_5 = Environment.getExternalStorageDirectory().getPath() + "/ctmp5.jpg";
        new File(TEMP_PATH_1).delete();
        new File(TEMP_PATH_2).delete();
        new File(TEMP_PATH_3).delete();
        new File(TEMP_PATH_4).delete();
        new File(TEMP_PATH_5).delete();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_done:
                changeStatus();
                break;
            case R.id.imageButton_addPic:
                takePictureFromCamera(picCounts);
                break;
            case R.id.imageButton_delPic:
                if (removePicture(picCounts)) {
                    Toast.makeText(RepireDetailActivity.this, "移除图片成功", Toast.LENGTH_SHORT).show();
                    picCounts -= 1;
                    picCount.setText(Integer.toString(picCounts));
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Toast.makeText(RepireDetailActivity.this, "已清除数据", Toast.LENGTH_SHORT).show();
        clearUpFiles();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                picCounts += 1;
                if (compassPictureFile()) {
                    Toast.makeText(RepireDetailActivity.this, "图像处理成功", Toast.LENGTH_SHORT).show();
                    picCount.setText(Integer.toString(picCounts));
                } else {
                    picCounts -= 1;
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("RepireBean", repireBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearUpFiles();
    }

    @Override
    public void onCompleted() {
        progressDialog.dismiss();
        clearUpFiles();

        if (status == STATUS_ACCPETED) {
            //刚才执行的是  维修完成  的动作
            Toast.makeText(RepireDetailActivity.this, "此工单维修完成", Toast.LENGTH_SHORT).show();
            setResult(ACTION_COMPLETED);
        } else if (status == STATUS_UNACCPETED) {
            //刚才执行的是  受理工单  的动作
            Toast.makeText(RepireDetailActivity.this, "受理工单成功", Toast.LENGTH_SHORT).show();
            setResult(ACTION_ACCEPT);
        }

        finish();
    }

    @Override
    public void onFailed() {
        progressDialog.dismiss();
        Toast.makeText(RepireDetailActivity.this, "数据上传失败 , 网络出错或拍照文件缺失 , 请清空数据后重试", Toast.LENGTH_SHORT).show();
    }

}
