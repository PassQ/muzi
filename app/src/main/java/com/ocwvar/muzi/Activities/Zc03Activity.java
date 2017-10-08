package com.ocwvar.muzi.Activities;

import android.app.ProgressDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Beans.PositionDataBeans.ApartmentBean;
import com.ocwvar.muzi.Beans.PositionDataBeans.CommunityBean;
import com.ocwvar.muzi.Beans.PositionDataBeans.RoomBean;
import com.ocwvar.muzi.Beans.PositionDataBeans.UnitBean;
import com.ocwvar.muzi.Network.Callbacks.OnRegisterUser;
import com.ocwvar.muzi.Network.Callbacks.OnRequestDataListCallbacks;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.BaseActivity;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

public class Zc03Activity extends BaseActivity implements OnRequestDataListCallbacks, PopupMenu.OnMenuItemClickListener, OnRegisterUser {

    TextView commName, aparName, unitName, roomName;
    PopupMenu commMenu, aparMenu, unitMenu, roomMenu;
    View commList, aparList, unitList, roomList;
    String commTAG, aparTAG, unitTAG, roomTAG;

    ProgressDialog progressDialog;
    EditText getPassword, getPasswordSecond, getName, getLiver, getLiverTel;
    String phoneNumber;
    Button done;

    boolean needReloadApar = false, needReloadUnit = false, needReloadRoom = false;

    ArrayList<Object> communityBeen = null;
    ArrayList<Object> apartmentBeen = null;
    ArrayList<Object> unitBeen = null;
    ArrayList<Object> roomBeen = null;

    @Override
    protected boolean onPreSetup() {
        if (getIntent().getExtras() != null) {
            phoneNumber = getIntent().getExtras().getString("phoneNumber");
            return !TextUtils.isEmpty(phoneNumber);
        } else {
            return false;
        }
    }

    @Override
    protected int setActivityView() {
        return R.layout.activity_zc_03;
    }

    @Override
    protected int onSetToolBar() {
        return R.id.toolbar;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onSetupViews() {
        setTitle(R.string.register_step3_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getPassword = (EditText) findViewById(R.id.editText_zc3_getPassword);
        getPasswordSecond = (EditText) findViewById(R.id.editText_zc3_getPassword_Second);
        getName = (EditText) findViewById(R.id.editText_zc3_getName);
        getLiver = (EditText) findViewById(R.id.editText_zc3_liver);
        getLiverTel = (EditText) findViewById(R.id.editText_zc3_liverTel);
        commList = findViewById(R.id.view_zc3_commList);
        aparList = findViewById(R.id.view_zc3_aparList);
        unitList = findViewById(R.id.view_zc3_unitList);
        roomList = findViewById(R.id.view_zc3_roomList);
        commName = (TextView) findViewById(R.id.textView_zc3_commName);
        aparName = (TextView) findViewById(R.id.textView_zc3_aparName);
        unitName = (TextView) findViewById(R.id.textView_zc3_unitName);
        roomName = (TextView) findViewById(R.id.textView_zc3_roomName);
        commMenu = new PopupMenu(Zc03Activity.this, commList);
        aparMenu = new PopupMenu(Zc03Activity.this, aparList);
        unitMenu = new PopupMenu(Zc03Activity.this, unitList);
        roomMenu = new PopupMenu(Zc03Activity.this, roomList);
        done = (Button) findViewById(R.id.button_zc3_done);
        progressDialog = new ProgressDialog(Zc03Activity.this);
        progressDialog.setMessage(getString(R.string.simple_working));
        progressDialog.setCancelable(false);

        getPasswordSecond.setText("");
        done.setOnClickListener(this);
        commList.setOnClickListener(this);
        aparList.setOnClickListener(this);
        unitList.setOnClickListener(this);
        roomList.setOnClickListener(this);
        commMenu.setOnMenuItemClickListener(this);
        aparMenu.setOnMenuItemClickListener(this);
        unitMenu.setOnMenuItemClickListener(this);
        roomMenu.setOnMenuItemClickListener(this);

        requestDatas(NetworkHelper.DataType.LIST_community);
    }

    @Override
    protected void onViewClick(View clickedView) {

    }

    @Override
    protected boolean onViewLongClick(View holdedView) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_zc3_done:
                newUser();
                break;
            case R.id.view_zc3_commList:
                //如果小区列表为空,则进行加载
                if (commMenu.getMenu().size() <= 0) {
                    requestDatas(NetworkHelper.DataType.LIST_community);
                } else {
                    commMenu.show();
                }

                break;
            case R.id.view_zc3_aparList:

                if (TextUtils.isEmpty(commTAG)) {
                    //如果没有选择上一项
                    Toast.makeText(Zc03Activity.this, R.string.no_community, Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(commTAG) && aparMenu.getMenu().size() <= 0) {
                    //如果上一项已经选择了,但还是没有数据
                    Toast.makeText(Zc03Activity.this, R.string.noData, Toast.LENGTH_SHORT).show();
                } else {
                    //最后显示菜单
                    aparMenu.show();
                }

                break;
            case R.id.view_zc3_unitList:

                if (TextUtils.isEmpty(aparTAG)) {
                    //如果没有选择上一项
                    Toast.makeText(Zc03Activity.this, R.string.no_apar, Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(aparTAG) && unitMenu.getMenu().size() <= 0) {
                    //如果上一项已经选择了,但还是没有数据
                    Toast.makeText(Zc03Activity.this, R.string.noData, Toast.LENGTH_SHORT).show();
                } else {
                    //最后显示菜单
                    unitMenu.show();
                }

                break;
            case R.id.view_zc3_roomList:

                if (TextUtils.isEmpty(unitTAG)) {
                    Toast.makeText(Zc03Activity.this, R.string.no_unit, Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(unitTAG) && roomMenu.getMenu().size() <= 0) {
                    //如果上一项已经选择了,但还是没有数据
                    Toast.makeText(Zc03Activity.this, R.string.noData, Toast.LENGTH_SHORT).show();
                } else {
                    //最后显示菜单
                    roomMenu.show();
                }

                break;
        }
    }

    /**
     * 请求数据
     *
     * @param dataType 数据类型
     */
    private void requestDatas(NetworkHelper.DataType dataType) {

        progressDialog.show();

        switch (dataType) {
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
            case LIST_room:
                break;
        }
    }

    /**
     * 开始创建新用户
     */
    private void newUser() {
        if (checkInfo()) {
            String[] datas = new String[]{
                    getName.getText().toString(),
                    phoneNumber,
                    getPassword.getText().toString(),
                    commTAG,
                    aparTAG,
                    unitTAG,
                    roomTAG,
                    getLiver.getText().toString(),
                    getLiverTel.getText().toString()
            };
            progressDialog.show();
            NetworkHelper.getInstance().registerUser(this, datas);
        } else {
            Toast.makeText(Zc03Activity.this, R.string.error_input_empty_with_password, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查信息的完整度
     *
     * @return 检查结果
     */
    private boolean checkInfo() {
        String password = getPassword.getText().toString();
        String passwordSecond = getPasswordSecond.getText().toString();
        String name = getName.getText().toString();
        String liver = getLiver.getText().toString();
        String liverTel = getLiverTel.getText().toString();
        return password.equals(passwordSecond) &&
                !TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(commTAG) &&
                !TextUtils.isEmpty(aparTAG) &&
                !TextUtils.isEmpty(unitTAG) &&
                !TextUtils.isEmpty(roomTAG) &&
                !TextUtils.isEmpty(liver) &&
                !TextUtils.isEmpty(liverTel);
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

                break;
        }
        return true;
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

        Toast.makeText(Zc03Activity.this, R.string.dataUpdated, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted() {
        progressDialog.dismiss();
        Toast.makeText(Zc03Activity.this, R.string.register_successful, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailed(boolean isException) {
        progressDialog.dismiss();

        if (isException) {
            Toast.makeText(Zc03Activity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Zc03Activity.this, R.string.simple_failed, Toast.LENGTH_SHORT).show();
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

}

