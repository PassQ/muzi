package com.ocwvar.muzi.Fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ocwvar.muzi.Beans.PositionDataBeans.ApartmentBean;
import com.ocwvar.muzi.Beans.PositionDataBeans.CommunityBean;
import com.ocwvar.muzi.Beans.PositionDataBeans.RoomBean;
import com.ocwvar.muzi.Beans.PositionDataBeans.UnitBean;
import com.ocwvar.muzi.Beans.UserBean;
import com.ocwvar.muzi.Network.Callbacks.OnRequestDataListCallbacks;
import com.ocwvar.muzi.Network.Callbacks.OnUploadRequestRepireCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Fragments
 * Date: 2016/5/9  9:14
 * Project: Muzi
 * 保修页面的Fragment
 */
public class RepireFragment extends Fragment implements View.OnClickListener, DialogInterface.OnCancelListener, OnRequestDataListCallbacks, PopupMenu.OnMenuItemClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnUploadRequestRepireCallback {

    private final static int PICTURE_MAX = 5;
    private final static int REQUEST_PERMISSION = 300;
    private final static int REQUEST_CAMERA = 301;
    private final static int COMPASS_LEVEL = 20;    //10~100  数值越低质量越差
    private final static String TEMP_PATH_1 = Environment.getExternalStorageDirectory().getPath() + "/ctmp1.jpg";
    private final static String TEMP_PATH_2 = Environment.getExternalStorageDirectory().getPath() + "/ctmp2.jpg";
    private final static String TEMP_PATH_3 = Environment.getExternalStorageDirectory().getPath() + "/ctmp3.jpg";
    private final static String TEMP_PATH_4 = Environment.getExternalStorageDirectory().getPath() + "/ctmp4.jpg";
    private final static String TEMP_PATH_5 = Environment.getExternalStorageDirectory().getPath() + "/ctmp5.jpg";
    View commList, aparList, unitList, roomList, typeList;
    ImageButton setTimeButton, takePictures, removePic;
    Button done;
    TextView context, commName, aparName, unitName, roomName, typeName, picCount;
    EditText getContent;
    PopupMenu commMenu, aparMenu, unitMenu, roomMenu, typeMenu;
    ProgressDialog progressDialog;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    UserBean userBean;
    String commTAG, aparTAG, unitTAG, roomTAG, typeTAG;
    int thisYear, thisMonth, thisDay, thisHour, thisMin;
    int setYear = -1, setMonth = -1, setDay = -1, setHour = -1, setMin = -1;
    int picCounts = 0;
    boolean needReloadApar = false, needReloadUnit = false, needReloadRoom = false;
    private ArrayList<Object> communityBeen;
    private ArrayList<Object> apartmentBeen;
    private ArrayList<Object> unitBeen;
    private ArrayList<Object> roomBeen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        userBean = AppOptions.USERINFO.user;

        if (userBean == null) {
            Toast.makeText(getContext(), "用户数据读取失败 , 请重新进入页面重试", Toast.LENGTH_SHORT).show();
            return null;
        }

        thisYear = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
        thisMonth = Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH) + 1;
        thisDay = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
        thisHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        thisMin = Calendar.getInstance().get(Calendar.MINUTE);

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_repire_page1, container, false);

        datePickerDialog = new DatePickerDialog(getContext(), this, thisYear, thisMonth - 1, thisDay);
        timePickerDialog = new TimePickerDialog(getContext(), this, thisHour, thisMin, true);
        progressDialog = new ProgressDialog(getContext());
        commList = view.findViewById(R.id.view_repire1_communityList);
        aparList = view.findViewById(R.id.view_repire1_apartmentList);
        unitList = view.findViewById(R.id.view_repire1_unitList);
        roomList = view.findViewById(R.id.view_repire1_roomList);
        typeList = view.findViewById(R.id.view_repire1_typeLists);
        setTimeButton = (ImageButton) view.findViewById(R.id.imageButton_repire1_setTime);
        removePic = (ImageButton) view.findViewById(R.id.imageButton_repire1_remove);
        takePictures = (ImageButton) view.findViewById(R.id.imageButton_repire1_camera);
        done = (Button) view.findViewById(R.id.button_repire1_done);
        context = (TextView) view.findViewById(R.id.textView_repire1_context);
        commName = (TextView) view.findViewById(R.id.textView_repire1_community_name);
        aparName = (TextView) view.findViewById(R.id.textView_repire1_apartment_name);
        unitName = (TextView) view.findViewById(R.id.textView_repire1_unit_name);
        roomName = (TextView) view.findViewById(R.id.textView_repire1_room_name);
        typeName = (TextView) view.findViewById(R.id.textView_repire1_type_name);
        picCount = (TextView) view.findViewById(R.id.textView_repire1_piccount);
        getContent = (EditText) view.findViewById(R.id.editText_repire1_content);
        commMenu = new PopupMenu(getContext(), commList);
        aparMenu = new PopupMenu(getContext(), aparList);
        unitMenu = new PopupMenu(getContext(), unitList);
        roomMenu = new PopupMenu(getContext(), roomList);
        typeMenu = new PopupMenu(getContext(), typeList);

        commMenu.setOnMenuItemClickListener(this);
        aparMenu.setOnMenuItemClickListener(this);
        unitMenu.setOnMenuItemClickListener(this);
        roomMenu.setOnMenuItemClickListener(this);
        typeMenu.setOnMenuItemClickListener(this);
        commList.setOnClickListener(this);
        aparList.setOnClickListener(this);
        unitList.setOnClickListener(this);
        roomList.setOnClickListener(this);
        typeList.setOnClickListener(this);
        setTimeButton.setOnClickListener(this);
        takePictures.setOnClickListener(this);
        removePic.setOnClickListener(this);
        done.setOnClickListener(this);
        progressDialog.setMessage("正在加载数据 , 请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(this);

        context.setText("报修人:" + userBean.getUserName() + "\n报修人手机号:" + userBean.getPhoneNumber());
        requestDatas(NetworkHelper.DataType.LIST_community);
        requestDatas(NetworkHelper.DataType.String_Array_RepireType);
        return view;
    }

    /**
     * 请求数据
     *
     * @param dataType 数据类型
     */
    private void requestDatas(NetworkHelper.DataType dataType) {

        progressDialog.show();

        switch (dataType) {
            case String_Array_RepireType:
                NetworkHelper.getInstance().requestDatasList(this, NetworkHelper.DataType.String_Array_RepireType, null);
                break;
            case LIST_community:
                if (communityBeen != null) {
                    addListDatas(NetworkHelper.DataType.LIST_community, communityBeen);
                } else {
                    NetworkHelper.getInstance().requestDatasList(this, NetworkHelper.DataType.LIST_community, null);
                }
                break;
            case LIST_apartment:
                if (apartmentBeen != null && !needReloadApar) {
                    addListDatas(NetworkHelper.DataType.LIST_apartment, apartmentBeen);
                } else {
                    NetworkHelper.getInstance().requestDatasList(this, NetworkHelper.DataType.LIST_apartment, commTAG);
                }
                break;
            case LIST_unit:
                if (unitBeen != null && !needReloadUnit) {
                    addListDatas(NetworkHelper.DataType.LIST_unit, unitBeen);
                } else {
                    NetworkHelper.getInstance().requestDatasList(this, NetworkHelper.DataType.LIST_unit, aparTAG);
                }
                break;
            case LIST_room:
                if (roomBeen != null && !needReloadRoom) {
                    addListDatas(NetworkHelper.DataType.LIST_room, roomBeen);
                } else {
                    NetworkHelper.getInstance().requestDatasList(this, NetworkHelper.DataType.LIST_room, unitTAG);
                }
                break;
        }
    }

    /**
     * 添加数据到下拉菜单中
     *
     * @param dataType 数据类型
     * @param datas    数据源
     */
    private void addListDatas(NetworkHelper.DataType dataType, ArrayList<Object> datas) {
        progressDialog.dismiss();

        switch (dataType) {
            case String_Array_RepireType:
                typeMenu.getMenu().clear();
                for (int i = 0; i < datas.size(); i++) {
                    String[] strings = (String[]) datas.get(i);
                    typeMenu.getMenu().add(5, Integer.parseInt(strings[0]), 5, strings[1]);
                }
                break;
            case LIST_community:
                commMenu.getMenu().clear();
                for (int i = 0; i < datas.size(); i++) {
                    commMenu.getMenu().add(1, i, 1, ((CommunityBean) datas.get(i)).getName());
                }
                break;
            case LIST_apartment:
                aparMenu.getMenu().clear();
                for (int i = 0; i < datas.size(); i++) {
                    aparMenu.getMenu().add(2, i, 2, ((ApartmentBean) datas.get(i)).getName() + " 栋");
                }
                needReloadApar = false;
                break;
            case LIST_unit:
                unitMenu.getMenu().clear();
                for (int i = 0; i < datas.size(); i++) {
                    unitMenu.getMenu().add(3, i, 3, ((UnitBean) datas.get(i)).getName() + " 单元");
                }
                needReloadUnit = false;
                break;
            case LIST_room:
                roomMenu.getMenu().clear();
                for (int i = 0; i < datas.size(); i++) {
                    roomMenu.getMenu().add(4, i, 4, ((RoomBean) datas.get(i)).getName() + " 号");
                }
                needReloadRoom = false;
                break;
        }

    }

    /**
     * 当数据发生变动的时候 , 需要重置相关的数据
     *
     * @param dataType 发生变动的数据类型
     */
    private void resetDatas(NetworkHelper.DataType dataType) {
        switch (dataType) {
            case LIST_community:
                aparTAG = null;
                aparName.setText("栋数");
                aparMenu.getMenu().clear();
            case LIST_apartment:
                unitTAG = null;
                unitName.setText("单元");
                unitMenu.getMenu().clear();
            case LIST_unit:
                roomTAG = null;
                roomName.setText("房号");
                roomMenu.getMenu().clear();
                context.setText("报修人:" + userBean.getUserName() + "\n报修人手机号:" + userBean.getPhoneNumber());
            case LIST_room:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_repire1_remove:
                if (removePicture(picCounts)) {
                    Toast.makeText(getActivity(), "移除图片成功", Toast.LENGTH_SHORT).show();
                    picCounts -= 1;
                    picCount.setText(Integer.toString(picCounts));
                }
                break;
            case R.id.imageButton_repire1_camera:
                takePictureFromCamera(picCounts);
                break;
            case R.id.view_repire1_typeLists:
                typeMenu.show();
                break;
            case R.id.view_repire1_communityList:

                //如果小区列表为空,则进行加载
                if (commMenu.getMenu().size() <= 0) {
                    requestDatas(NetworkHelper.DataType.LIST_community);
                } else {
                    commMenu.show();
                }

                break;
            case R.id.view_repire1_apartmentList:

                if (TextUtils.isEmpty(commTAG)) {
                    //如果没有选择上一项
                    Toast.makeText(getContext(), "请先选择小区", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(commTAG) && aparMenu.getMenu().size() <= 0) {
                    //如果上一项已经选择了,但还是没有数据
                    Toast.makeText(getContext(), "此区域没有相关数据,或操作被取消", Toast.LENGTH_SHORT).show();
                } else {
                    //最后显示菜单
                    aparMenu.show();
                }

                break;
            case R.id.view_repire1_unitList:

                if (TextUtils.isEmpty(aparTAG)) {
                    //如果没有选择上一项
                    Toast.makeText(getContext(), "请先选择楼号", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(aparTAG) && unitMenu.getMenu().size() <= 0) {
                    //如果上一项已经选择了,但还是没有数据
                    Toast.makeText(getContext(), "此区域没有相关数据,或操作被取消", Toast.LENGTH_SHORT).show();
                } else {
                    //最后显示菜单
                    unitMenu.show();
                }

                break;
            case R.id.view_repire1_roomList:

                if (TextUtils.isEmpty(unitTAG)) {
                    Toast.makeText(getContext(), "请先选择单元", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(unitTAG) && roomMenu.getMenu().size() <= 0) {
                    //如果上一项已经选择了,但还是没有数据
                    Toast.makeText(getContext(), "此区域没有相关数据,或操作被取消", Toast.LENGTH_SHORT).show();
                } else {
                    //最后显示菜单
                    roomMenu.show();
                }

                break;
            case R.id.imageButton_repire1_setTime:
                datePickerDialog.show();
                break;
            case R.id.button_repire1_done:
                updateRepireRequest();
                break;
        }
    }

    /**
     * 提交数据
     */
    private void updateRepireRequest() {

        String message = getContent.getText().toString();
        String positionString = commName.getText().toString() + " " + aparName.getText().toString() + " " + unitName.getText().toString() + " " + roomName.getText().toString();
        String dataString = setYear + "年 " + setMonth + "月 " + setDay + "日   " + setHour + "时 " + setMin + "分";
        if (
                !TextUtils.isEmpty(message)
                        && dateCheck(setYear, setMonth, setDay)
                        && timeCheck(setHour, setMin)
                        && positionCheck()
                        && userBean != null
                ) {
            //检查输入内容是否为空 , 同时再检测一次日期有效性 , 检测位置信息有效性

            String[] datas = new String[]{
                    positionString,   //位置信息
                    commTAG,        //小区ID
                    typeTAG,        //报修类型
                    message,        //报修内容信息
                    dataString,     //日期信息
                    userBean.getPhoneNumber(),      //报修人手机号
                    userBean.getUserID()        //报修人  数据库ID
            };

            progressDialog.setCancelable(false);
            progressDialog.show();
            NetworkHelper.getInstance().uploadRepireRequest(this, datas, getPictures());

            Log.d("提交数据检查", "成功  用户ID: " + userBean.getUserID());
        } else {
            Log.d("提交数据检查", "失败");
        }

    }

    /**
     * 检查设定的时间有效性
     *
     * @param userSetHout 用户设置的小时
     * @param userSetMin  用户设置的分钟
     * @return 有效性
     */
    private boolean timeCheck(int userSetHout, int userSetMin) {

        boolean isToday = (thisYear == setYear && thisMonth == setMonth && thisDay == setDay);

        if (isToday) {

            thisHour = Calendar.getInstance(Locale.CHINA).get(Calendar.HOUR_OF_DAY);
            thisMin = Calendar.getInstance(Locale.CHINA).get(Calendar.MINUTE);

            if (userSetHout < thisHour) {
                return false;
            } else if (userSetHout == thisHour && userSetMin < thisMin) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    /**
     * 检查设定的日期有效性
     *
     * @param userSetYear  用户设置的年份
     * @param userSetMonth 用户设置的月份
     * @param userSetDay   用户设置的日期
     * @return 有效性
     */
    private boolean dateCheck(int userSetYear, int userSetMonth, int userSetDay) {
        if (userSetYear < thisYear) {
            return false;
        } else if (userSetYear == thisYear && userSetMonth + 1 < thisMonth) {
            return false;
        } else if (userSetYear == thisYear && userSetMonth + 1 == thisMonth && userSetDay < thisDay) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测位置信息有效性
     *
     * @return 有效性
     */
    private boolean positionCheck() {
        return !TextUtils.isEmpty(commTAG) && !TextUtils.isEmpty(aparTAG) && !TextUtils.isEmpty(unitTAG) && !TextUtils.isEmpty(roomTAG) && !TextUtils.isEmpty(typeTAG);
    }

    /**
     * 从摄像头获取图像
     *
     * @param picCounts 当前已经获取了多少张
     * @return 请求结果
     */
    private boolean takePictureFromCamera(int picCounts) {
        if (picCounts >= PICTURE_MAX) {
            Toast.makeText(getActivity(), "已达到最大图像数量", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                Toast.makeText(getActivity(), "请先给予应用权限", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "图像压缩失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getActivity(), "图像获取失败", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "当前没有图像", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "未定义错误", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                picCounts += 1;
                if (compassPictureFile()) {
                    Toast.makeText(getActivity(), "图像处理成功", Toast.LENGTH_SHORT).show();
                    picCount.setText(String.valueOf(picCounts));
                } else {
                    picCounts -= 1;
                }
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.ALL_TASK);
    }

    @Override
    public void onRequestCompleted(NetworkHelper.DataType dataType, ArrayList<Object> datasList) {

        addListDatas(dataType, datasList);

        //除了维修类型数据之外的都进行缓存
        switch (dataType) {
            case LIST_community:
                communityBeen = new ArrayList<>();
                communityBeen.addAll(datasList);
                break;
            case LIST_apartment:
                apartmentBeen = new ArrayList<>();
                apartmentBeen.addAll(datasList);
                break;
            case LIST_unit:
                unitBeen = new ArrayList<>();
                unitBeen.addAll(datasList);
                break;
            case LIST_room:
                roomBeen = new ArrayList<>();
                roomBeen.addAll(datasList);
                break;
        }

        Toast.makeText(getContext(), "数据更新完毕", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(getContext(), "读取发生异常,请检查网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "无数据,请选择其他位置", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getGroupId()) {
            case 1:
                //小区列表
                CommunityBean bean = (CommunityBean) communityBeen.get(item.getItemId());
                if (!TextUtils.isEmpty(commTAG) && commTAG.equals(bean.getID())) {
                    //如果重复选择同一个项目 , 则不响应
                    break;
                } else {
                    commTAG = bean.getID();
                    commName.setText(bean.getName());
                    needReloadApar = true;
                    resetDatas(NetworkHelper.DataType.LIST_community);
                    requestDatas(NetworkHelper.DataType.LIST_apartment);
                }
                break;
            case 2:
                //楼号列表

                ApartmentBean aparBean = (ApartmentBean) apartmentBeen.get(item.getItemId());
                if (!TextUtils.isEmpty(aparTAG) && aparTAG.equals(aparBean.getId())) {
                    //如果重复选择同一个项目 , 则不响应
                    break;
                } else {
                    aparTAG = aparBean.getId();
                    aparName.setText(String.format("%s 栋", aparBean.getName()));
                    needReloadUnit = true;
                    resetDatas(NetworkHelper.DataType.LIST_apartment);
                    requestDatas(NetworkHelper.DataType.LIST_unit);
                }

                break;
            case 3:
                //单元列表
                UnitBean unitBean = (UnitBean) unitBeen.get(item.getItemId());
                if (!TextUtils.isEmpty(unitTAG) && unitTAG.equals(unitBean.getId())) {
                    //如果重复选择同一个项目 , 则不响应
                    break;
                } else {
                    unitTAG = unitBean.getId();
                    unitName.setText(String.format("%s 单元", unitBean.getName()));
                    needReloadRoom = true;
                    resetDatas(NetworkHelper.DataType.LIST_unit);
                    requestDatas(NetworkHelper.DataType.LIST_room);
                }
                break;
            case 4:
                //房号列表
                RoomBean roomBean = (RoomBean) roomBeen.get(item.getItemId());
                roomTAG = roomBean.getId();
                roomName.setText(String.format("%s 号", roomBean.getName()));

                context.setText("报修人:" + userBean.getUserName() + "\n报修人手机号:" + userBean.getPhoneNumber() + "\n所在小区:" + commName.getText().toString() + "\n栋数:" + aparName.getText().toString() + "\n单元号:" + unitName.getText().toString() + "\n房号:" + roomName.getText().toString());
                break;
            case 5:
                //维修类型
                typeName.setText(item.getTitle());
                typeTAG = Integer.toString(item.getItemId());
                break;
        }
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //检查用户设定的时间是否正确

        if (dateCheck(year, monthOfYear, dayOfMonth)) {
            setYear = year;
            setMonth = monthOfYear + 1;
            setDay = dayOfMonth;
            Log.d("设定的日期", "年: " + setYear + "  月:" + setMonth + "  日:" + setDay);
            timePickerDialog.show();
        } else {
            Toast.makeText(getContext(), "设定的日期必须大于等于今天", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //在设定时间之前再检查一次当前时间,然后再检查设置的合法性

        if (timeCheck(hourOfDay, minute)) {
            setHour = hourOfDay;
            setMin = minute;
            Log.d("设定的时间", "小时:" + hourOfDay + "  分钟:" + minute);
        } else {
            Toast.makeText(getContext(), "设置的时间必须大于等于现在", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onUploadCompleted() {
        progressDialog.dismiss();
        progressDialog.setCancelable(true);
        getContent.getText().clear();
        picCounts = 0;
        picCount.setText("0");
        new File(TEMP_PATH_1).delete();
        new File(TEMP_PATH_2).delete();
        new File(TEMP_PATH_3).delete();
        new File(TEMP_PATH_4).delete();
        new File(TEMP_PATH_5).delete();
        Toast.makeText(getContext(), "报修成功,请耐心等待维修人员的到来.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadFailed(boolean isException) {
        if (isException) {
            Toast.makeText(getContext(), "数据上传失败，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "数据发生异常,请重试", Toast.LENGTH_SHORT).show();
        }
    }

}
