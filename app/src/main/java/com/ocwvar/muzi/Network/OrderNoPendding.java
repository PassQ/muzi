package com.ocwvar.muzi.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.muzi.bean.DaoMaster;
import com.muzi.bean.DaoSession;
import com.muzi.bean.SQLUserBean;
import com.muzi.bean.SQLUserBeanDao;
import com.ocwvar.muzi.Network.Callbacks.OnUploadPaymentCallback;
import com.ocwvar.muzi.Utils.AppOptions;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network
 * Date: 2016/5/26  21:55
 * Project: Muzi
 * 提交失败的订单号
 */
public class OrderNoPendding {

    private final static String DB_NAME = "udo.db";
    private final static String DB_KEY_TEL = "telephone";
    private final static String DB_KEY_ORDERNO = "orderNo";
    private final static String DB_KEY_ROOMID = "roomID";
    private final static String DB_KEY_PAYTYPE = "chargeTypeID";
    private final static String DB_KEY_PAYSUM = "chargeSum";

    private static OrderNoPendding orderNoPendding;
    private static Context context;
    private String roomID;
    private String telephone;
    private String chargeSum;
    private String chargeTypeID;
    private String orderNo;
    private String useScore = "0";

    public OrderNoPendding() {
        this.context = AppOptions.ApplicationContext;
    }

    public static OrderNoPendding getInstance() {
        if (orderNoPendding == null) {
            orderNoPendding = new OrderNoPendding();
        }
        return orderNoPendding;
    }

    /**
     * 获取订单号
     *
     * @return 订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 缓存失败的订单
     *
     * @param telephone    用户的电话号码
     * @param roomID       房间号
     * @param chargeTypeID 缴费类型
     * @param chargeSum    交费总额
     * @param orderNo      订单号
     */
    public boolean pendOrderNo(String telephone, String roomID, String chargeTypeID, String chargeSum, String orderNo,String useScore) {
        this.telephone = telephone;
        this.orderNo = orderNo;
        this.chargeSum = chargeSum;
        this.chargeTypeID = chargeTypeID;
        this.roomID = roomID;
        this.useScore = useScore;
        return orderNo2DB(telephone, roomID, chargeTypeID, chargeSum, orderNo,useScore);
    }

    /**
     * 发送缓存的订单号
     *
     * @param onUploadPaymentCallback 请求回调
     * @return 是否开始执行上传任务
     */
    public boolean sendData(OnUploadPaymentCallback onUploadPaymentCallback) {
        if (isHasConnection() && isHasData()) {
            NetworkHelper.getInstance().uploadPayment(onUploadPaymentCallback, roomID, chargeTypeID, chargeSum, orderNo, useScore, AppOptions.USERINFO.user.getPhoneNumber());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否有缓存的数据
     *
     * @return
     */
    public boolean isHasData() {
        return !TextUtils.isEmpty(orderNo) && !TextUtils.isEmpty(chargeSum) && !TextUtils.isEmpty(chargeTypeID) && !TextUtils.isEmpty(roomID);
    }

    /**
     * 清空数据
     */
    public void clearData() {
        clearOrderNo(telephone);
        this.telephone = "";
        this.orderNo = "";
        this.chargeSum = "";
        this.chargeTypeID = "";
        this.roomID = "";
        this.useScore = "0";
    }

    /**
     * 将订单写入数据库
     *
     * @param telephone    电话号 作为查询ID
     * @param roomID       房间号
     * @param chargeTypeID 缴费类型
     * @param chargeSum    交费总额
     * @param orderNo      订单号
     * @return 保存是否成功
     */
    private boolean orderNo2DB(String telephone, String roomID, String chargeTypeID, String chargeSum, String orderNo,String userScore) {

        if (!TextUtils.isEmpty(orderNo)) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            DaoSession session = daoMaster.newSession();
            SQLUserBeanDao userBeanDao = session.getSQLUserBeanDao();
            userBeanDao.deleteByKey(telephone);
            SQLUserBean userBean = new SQLUserBean();
            userBean.setId(telephone);
            userBean.setChargeSum(chargeSum);
            userBean.setChargeTypeID(chargeTypeID);
            userBean.setOrderNo(orderNo);
            userBean.setRoomID(roomID);
            userBean.setUseScore(userScore);
            userBeanDao.insert(userBean);
            devOpenHelper.close();
            return true;
        } else {
            return false;
        }

    }

    /**
     * 将之前保存到数据库的订单取出
     *
     * @param telephone 用作查询的电话号码
     * @return 执行结果
     */
    public boolean db2OrderNo(String telephone) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession session = daoMaster.newSession();
        SQLUserBeanDao userBeanDao = session.getSQLUserBeanDao();
        SQLUserBean userBean = userBeanDao.load(telephone);
        if (userBean != null) {
            this.chargeSum = userBean.getChargeSum();
            this.chargeTypeID = userBean.getChargeTypeID();
            this.roomID = userBean.getRoomID();
            this.orderNo = userBean.getOrderNo();
            this.telephone = userBean.getId();
            this.useScore = userBean.getUseScore();
            devOpenHelper.close();
            return true;
        } else {
            this.telephone = "";
            this.orderNo = "";
            this.chargeTypeID = "";
            this.chargeSum = "";
            this.roomID = "";
            this.useScore = "";
            devOpenHelper.close();
            return false;
        }
    }

    /**
     * 清空数据库内对应电话号码的订单数据
     *
     * @param telephone 用作查询的电话号码
     */
    private void clearOrderNo(String telephone) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession session = daoMaster.newSession();
        SQLUserBeanDao userBeanDao = session.getSQLUserBeanDao();
        userBeanDao.deleteByKey(telephone);
        devOpenHelper.close();
    }

    /**
     * 获取当前是否有网络连接
     *
     * @return 网络状态
     */
    private boolean isHasConnection() {
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectionManager.getAllNetworkInfo();
        if (networkInfos != null && networkInfos.length > 0) {
            for (int i = 0; i < networkInfos.length; i++) {
                if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public String getUseScore() {
        return useScore;
    }
}
