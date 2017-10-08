package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.ocwvar.muzi.Network.Callbacks.OnUploadUserHeadCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.CircleImageView;
import com.ocwvar.muzi.Utils.ImageHelper.OCImageLoader;
import com.ocwvar.muzi.Utils.PicturePickerUnity;
import com.ocwvar.muzi.Utils.SelectPicPopupWindow;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PortraitFixActivity extends AppCompatActivity implements View.OnClickListener, OnUploadUserHeadCallback, DialogInterface.OnCancelListener, AdapterView.OnItemSelectedListener {

    SelectPicPopupWindow menuWindow;
    ProgressDialog progressDialog;
    Spinner spinner;
    CircleImageView shower;
    Button button;
    ImageButton btn_sign;
    int angleFix = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_head);

        spinner = (Spinner) findViewById(R.id.spinner_angle);
        shower = (CircleImageView) findViewById(R.id.imageView_changeHead_shower);
        button = (Button) findViewById(R.id.button_changeHead_done);
        btn_sign= (ImageButton) findViewById(R.id.imageButton_sign);
        progressDialog = new ProgressDialog(PortraitFixActivity.this);
        menuWindow = new SelectPicPopupWindow(PortraitFixActivity.this, this);

        progressDialog.setMessage(getString(R.string.simple_working));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(this);

        spinner.setAdapter(new ArrayAdapter<>(PortraitFixActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.angleFix)));
        spinner.setOnItemSelectedListener(PortraitFixActivity.this);
        button.setOnClickListener(this);
        btn_sign.setOnClickListener(this);

        //同步用户头像数据
        if (AppOptions.USERINFO.getUserHead() instanceof Integer) {
            shower.setImageDrawable(getResources().getDrawable(AppOptions.USERINFO.userHeadRes));
        } else {
            Picasso
                    .with(PortraitFixActivity.this)
                    .load((String) AppOptions.USERINFO.getUserHead())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.ic_failed)
                    .placeholder(R.drawable.ic_loading)
                    .into(shower);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_changeHead_done:
                PicturePickerUnity.Builder builder = new PicturePickerUnity.Builder();
                builder
                        .needCompress(true)
                        .needCrop(true)
                        .setCropHeight(200)
                        .setCropWidth(200)
                        .setCompressValue(60)
                        .returnFile(true)
                        .startPickerNow(PortraitFixActivity.this,1,2);
                break;
            case R.id.imageButton_sign:
                Intent intent=new Intent();
                intent.setClass(this,SignActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            switch (requestCode) {
                case 1:
                    if (data.getAction().equals(PicturePickerUnity.ACTION_SUCCESS)){
                        final Bitmap bitmap = BitmapFactory.decodeFile(((File)data.getSerializableExtra(PicturePickerUnity.EXTRAS_FILE)).getPath());
                        shower.setImageBitmap(bitmap);
                        uploadImage(bitmap);
                    }else {
                        Toast.makeText(PortraitFixActivity.this, data.getStringExtra(PicturePickerUnity.EXTRAS_EXCEPTION), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    /**
     * 上传头像
     *
     * @param bitmap 头像的Bitmap对象
     */
    private void uploadImage(Bitmap bitmap) {
        progressDialog.show();
        //进行图像旋转修正
        bitmap = fixBitmapAngle(bitmap);
        OCImageLoader.loader().cacheImage("tempHead", bitmap);

        if (bitmap != null) {
            NetworkHelper.getInstance().uploadUserHeadImage(this, AppOptions.USERINFO.user.getUserID(), bitmap);
        } else {
            Toast.makeText(PortraitFixActivity.this, R.string.error_file, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 旋转修正图像
     *
     * @param bitmap 要处理的图像对象
     * @return 处理后的图像对象
     */
    private Bitmap fixBitmapAngle(Bitmap bitmap) {
        if (angleFix == 0) {
            return bitmap;
        } else {
            Matrix matrix = new Matrix();
            matrix.postRotate((float) angleFix);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
    }

    /**
     * 头像上传成功回调
     *
     * @param bitmap   头像的Bitmap对象
     * @param imageURL 头像的网址
     */
    @Override
    public void onUploaded(Bitmap bitmap, String imageURL) {
        progressDialog.dismiss();

        Toast.makeText(PortraitFixActivity.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
        AppOptions.USERINFO.user.setUserHeadImage(imageURL);
        shower.setImageBitmap(bitmap);
    }

    /**
     * 头像上传失败回调
     *
     * @param isException 是否为异常
     */
    @Override
    public void onUploadFailed(boolean isException) {
        progressDialog.dismiss();

        Toast.makeText(PortraitFixActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.UploadUserHead);
        Toast.makeText(PortraitFixActivity.this, R.string.simple_cancel_action, Toast.LENGTH_SHORT).show();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                angleFix = 0;
                break;
            case 1:
                angleFix = 90;
                break;
            case 2:
                angleFix = 180;
                break;
            case 3:
                angleFix = 270;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
