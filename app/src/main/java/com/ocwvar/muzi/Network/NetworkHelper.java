package com.ocwvar.muzi.Network;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ocwvar.muzi.Adapters.ReviewAdapter;
import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Beans.BulletinBean;
import com.ocwvar.muzi.Beans.BusinessImageBean;
import com.ocwvar.muzi.Beans.OrderBean;
import com.ocwvar.muzi.Beans.PayRecordBean;
import com.ocwvar.muzi.Beans.PaymentBean;
import com.ocwvar.muzi.Beans.RepireBean;
import com.ocwvar.muzi.Beans.RepireStatusBean;
import com.ocwvar.muzi.Beans.ReplyTabBean;
import com.ocwvar.muzi.Beans.UserBean;
import com.ocwvar.muzi.Network.Callbacks.OnArtcleLoadCallback;
import com.ocwvar.muzi.Network.Callbacks.OnCancalOrderCallback;
import com.ocwvar.muzi.Network.Callbacks.OnChangeRepireStatus;
import com.ocwvar.muzi.Network.Callbacks.OnCheckPaymentCallback;
import com.ocwvar.muzi.Network.Callbacks.OnCheckUser;
import com.ocwvar.muzi.Network.Callbacks.OnGetAskingListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetBusinessCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetBusinessImageCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetCircumCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetContribuListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetHelpingListCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetOrderCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetPayRecordByMonthCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetPayRecordCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetPaymentListCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetPropertyListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetQuestionListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetRepireDataCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetRepireStatusListCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetReplyImageCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetReplyListCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetReplyTabCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetScoreDeductCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetServiceListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetServicePropleListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetSignHistoryCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetSignScoreCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetSuggestListCallbacak;
import com.ocwvar.muzi.Network.Callbacks.OnGetSumBalanceCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetUserInfoCallback;
import com.ocwvar.muzi.Network.Callbacks.OnLoginCallback;
import com.ocwvar.muzi.Network.Callbacks.OnRegisterUser;
import com.ocwvar.muzi.Network.Callbacks.OnRequestDataListCallbacks;
import com.ocwvar.muzi.Network.Callbacks.OnUpLikeCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpOrderCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpReplyContentCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpReplyPhotoCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpReplyTabCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpScoreDeductCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpSignCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpdateInfoCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUpdateRequestHelpCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUploadPaymentCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUploadRepireScoreCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUploadRequestRepireCallback;
import com.ocwvar.muzi.Network.Callbacks.OnUploadUserHeadCallback;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.OCThreadExecutor;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network
 * Date: 2016/5/4  12:05
 * Project: Muzi
 * 所有网络请求帮助类
 */

public class NetworkHelper {
    public static final String BASE_URL = "http://anhe.gxmuzi.com/";
//    public static final String BASE_URL = "http://192.168.124.26/";

//    public static final String BASE_URL = "http://ahwyapp.com/";

    /*************************
     * 文章操作类接口
     ************************/
    //获取公共文章
    private static final String URL_request_readCommonInterface = BASE_URL + "AppAjax/ReadCommonListHandler.ashx";
    //专属文章接口
    private static final String URL_articleInterface = BASE_URL + "AppAjax/ReadListHandler.ashx";
    //用户提交文章接口  11  12  13  14  15
    private static final String URL_updateRequestHelpInterface = BASE_URL + "AppAjax/ArticleSubmitHandler.ashx";
    /*************************
     * 用户操作类接口
     ************************/
    //用户登录接口
    private static final String URL_loginInterface = BASE_URL + "AppAjax/AccountLoginHandler.ashx";
    //用户信息接口
    private static final String URL_userInfoInterface = BASE_URL + "AppAjax/GetUserInfoHandler.ashx";
    //用户信息更新接口
    private static final String URL_userUpdateInterface = BASE_URL + "AppAjax/AccountUpdateHandler.ashx";
    //检测用户是否存在接口
    private static final String URL_userIsExistInterface = BASE_URL + "AppAjax/CheckUserHandler.ashx";
    //用户创建接口
    private static final String URL_userRegisterInterface = BASE_URL + "AppAjax/AccountRegisterHandler.ashx";
    //用户上传头像接口
    private static final String URL_request_uploadHeadImageInterface = BASE_URL + "AppAjax/UploadImageFileHandler.ashx";
    /*************************
     * 小区数据操作类接口
     ************************/
    //获取小区接口
    private static final String URL_request_CommunitysInterface = BASE_URL + "AppAjax/CommunityInfoHandler.ashx";
    //获取楼号接口
    private static final String URL_request_ApartmentsInterface = BASE_URL + "AppAjax/ApartmentInfoHandler.ashx";
    //获取单元接口
    private static final String URL_request_UnitInterface = BASE_URL + "AppAjax/UnitInfoHandler.ashx";
    //获取房号接口
    private static final String URL_request_RoomInterface = BASE_URL + "AppAjax/RoomInfoHandler.ashx";
    /*************************
     * 缴费相关操作类接口
     ************************/
    //用户缴费类型接口
    private static final String URL_request_paymentListInterface = BASE_URL + "AppAjax/GetChargeTypeHandler.ashx";
    //用户缴费记录接口
    private static final String URL_request_payRecordListInterface = BASE_URL + "AppAjax/GetChargeRecordHandler.ashx";
    //用户缴费月记录接口
    private static final String URL_request_payRecordListByMonthInterface = BASE_URL + "AppAjax/GetMonthChargeRecordHandler.ashx";
    //用户项目费用余额接口
    private static final String URL_request_getSumBalanceInterface = BASE_URL + "AppAjax/GetSumBalanceHandler.ashx";
    //用户缴费信息登记接口
    private static final String URL_request_uploadPaymentInterface = BASE_URL + "AppAjax/PaymentHandler.ashx";
    //查看缴费订单是否存在
    private static final String URL_request_checkPaymentInterface = BASE_URL + "AppAjax/CompareOrderNoHandler.ashx";
    /*************************
     * 报修操作类接口
     ************************/
    //用户评价报修信息接口
    private static final String URL_request_uploadScoreInterface = BASE_URL + "AppAjax/SetEvaluateHandler.ashx";
    //获取报修类型接口
    private static final String URL_request_typeInterface = BASE_URL + "AppAjax/GetServiceTypeHandler.ashx";
    //用户提交报修信息接口
    private static final String URL_request_uploadInterface = BASE_URL + "AppAjax/SubmitServiceHandler.ashx";
    //用户获取报修信息接口
    private static final String URL_request_getStatusInterface = BASE_URL + "AppAjax/GetServiceInfoHandler.ashx";
    //用户提交报修照片
    private static final String URL_request_uploadRepirePhotosInterface = BASE_URL + "AppAjax/SubmitServiceAttrHandler.ashx";
    //维修人员的维修工单
    private static final String URL_request_getRepireListInterface = BASE_URL + "AppAjax/GetAdminServiceInfoHandler.ashx";
    //获取维修工单中的图片
    private static final String URL_request_getRepirePictureInterface = BASE_URL + "AppAjax/GetAdminServiceAttrHandler.ashx";
    //更改维修信息的状态
    private static final String URL_request_changeRepireStatusInterface = BASE_URL + "AppAjax/SubmitServiceStatusHandler.ashx";
    //签到接口
    private static final String URL_request_upSignInterface = BASE_URL + "AppAjax/AccountSignInHandler.ashx";
    //获取签到历史接口
    private static final String URL_request_getSignHistoryInterface = BASE_URL + "AppAjax/GetSignInHistoyHandler.ashx";
    //获取积分总值数据接口
    private static final String URL_request_getSignScoreInterface = BASE_URL + "AppAjax/GetAccountScoreHandler.ashx";
    //多类型获取积分接口
    private static final String URL_request_getMultitypeScoreInterface = BASE_URL + "AppAjax/MultitypeScoreGainHandler.ashx";
    //商家列表获取接口
    private static final String URL_request_getBusinessScoreInterface = BASE_URL +"AppAjax/Appointment/User/GetStoreListHandler.ashx";
    //周边列表获取接口
    private static final String URL_request_getCircumScoreInterface = BASE_URL + "AppAjax/Appointment/User/GetCircumListHandler.ashx";
    //我的预约列表获取接口
    private static final String URL_request_getOrderListScoreInterface = BASE_URL + "AppAjax/Appointment/User/GetUserAppointmentListHandler.ashx";
    //商家详情图片列表获取接口
    private static final String URL_request_getBusinessImageInterface = BASE_URL + "AppAjax/Appointment/User/GetStorePictureHandler.ashx";
    //商家预设评论列表获取接口
    private static final String URL_request_getReplyTabInterface = BASE_URL + "AppAjax/Appointment/User/GetPreinstallCommentHandler.ashx";
    //商家详细评论列表获取接口
    private static final String URL_request_getReplyListInterface = BASE_URL + "AppAjax/Appointment/User/GetCommentListHandler.ashx";
    //提交预约请求接口
    private static final String URL_request_upOrderInterface = BASE_URL + "AppAjax/Appointment/User/SubmitAppointmentInfoHandler.ashx";
    //提交预设回复接口
    private static final String URL_request_upReplyTab = BASE_URL + "AppAjax/Appointment/User/SubmitPreinstallCommentHandler.ashx";
    //提交详细评论接口
    private static final String URL_request_upReplyContent = BASE_URL + "AppAjax/Appointment/User/SubmitCommentHandler.ashx";
    //提交评价图片接口
    private static final String URL_request_upReplyPhoto = BASE_URL +"AppAjax/Appointment/User/SubmitCommenPictureHandler.ashx";
    //获取评论图片列表接口
    private static final String URL_request_getReplyImageInterface = BASE_URL +"AppAjax/Appointment/User/GetCommenPictureHandler.ashx";
    //取消预约接口
    private static final String URL_request_cancalOrder = BASE_URL + "AppAjax/Appointment/User/CancelAppointmentHandler.ashx";
    //获取消费积分数据请求接口
    private static final String URL_request_getScoreDeduct = BASE_URL + "AppAjax/ScoreConversionMoneyHandler.ashx";
    //消费积分请求接口
    private static final String URL_request_upScoreDeduct = BASE_URL + "AppAjax/ConsumptionScoreHandler.ashx";

    private static NetworkHelper networkHelper;
    private OCThreadExecutor threadExecutor;
    private OkHttpClient httpClient;
    private Handler handler;

    protected NetworkHelper() {
        handler = new Handler(Looper.getMainLooper());
        httpClient = new OkHttpClient();
        threadExecutor = new OCThreadExecutor(2, "networkTHS");
    }

    public static NetworkHelper getInstance() {
        if (networkHelper == null) {
            networkHelper = new NetworkHelper();
        }
        return networkHelper;
    }

    /**
     * UI 线程
     *
     * @param runnable 在UI线程运行的任务
     */
    private void runOnUIThread(@NonNull Runnable runnable) {
        boolean done = handler.post(runnable);
        while (!done) {
            handler = new Handler(Looper.getMainLooper());
            runOnUIThread(runnable);
        }
    }

    /**
     * 读取文章
     *
     * @param onArtcleLoadCallback 读取进度回调
     * @param needToCacheImage     是否进行缓存图片网址以供主界面滚动显示
     * @param args                 附带的参数
     */
    public void loadArtcles(@Nullable OnArtcleLoadCallback onArtcleLoadCallback, @NonNull boolean needToCacheImage, @NonNull String[] args) {
        threadExecutor.submit(new FutureTask<>(new GetArtclesThread(onArtcleLoadCallback, needToCacheImage, args)), GetArtclesThread.TAG + args[2]);
    }

    /**
     * 用户登录
     *
     * @param onLoginCallback 登录回调
     * @param username        用户名
     * @param password        用户密码
     */
    public void userLogin(@NonNull OnLoginCallback onLoginCallback, @NonNull String username, @NonNull String password) {
        threadExecutor.submit(new FutureTask<>(new LoginThread(onLoginCallback, username, password)), LoginThread.TAG);
    }

    /**
     * 更新用户信息
     *
     * @param onUpdateInfoCallback 更新状态回调
     * @param userBean             要更新的信息集合Bean
     */
    public void updateUser(@NonNull OnUpdateInfoCallback onUpdateInfoCallback, @NonNull UserBean userBean) {
        threadExecutor.submit(new FutureTask<>(new UpdateUserInfo(onUpdateInfoCallback, userBean)), UpdateUserInfo.TAG);
    }

    /**
     * 上传业主诉求数据
     *
     * @param onUpdateRequestHelpCallback 上传数据的状态回调
     * @param datas                       要上传的数据
     */
    public void updateRequestHelp(@NonNull OnUpdateRequestHelpCallback onUpdateRequestHelpCallback, @NonNull String[] datas) {
        threadExecutor.submit(new FutureTask<>(new UpdateRequestHelpData(onUpdateRequestHelpCallback, datas)), UpdateRequestHelpData.TAG);
    }

    /**
     * 请求 小区名称   楼号   单元号   房间号  的数据列表
     *
     * @param onRequestDataListCallbacks 请求结果回调
     * @param dataType                   请求的数据类型
     * @param arg                        请求的参数.  请求小区数据列表 此项可为空
     */
    public void requestDatasList(@NonNull OnRequestDataListCallbacks onRequestDataListCallbacks, @NonNull DataType dataType, @Nullable String arg) {
        threadExecutor.submit(new FutureTask<>(new RequestDataList(onRequestDataListCallbacks, dataType, arg)), RequestDataList.TAG + dataType);
    }

    /**
     * 上传报修数据
     *
     * @param onUploadRequestRepireCallback 上传数据的状态回调
     * @param datas                         要上传的数据
     * @param files                         要上传文件列表,传递Null表示没有要上传的文件
     */
    public void uploadRepireRequest(@NonNull OnUploadRequestRepireCallback onUploadRequestRepireCallback, @NonNull String[] datas, @Nullable String[] files) {
        threadExecutor.submit(new FutureTask<>(new UploadRepireRequest(onUploadRequestRepireCallback, datas, files)), UploadRepireRequest.TAG);
    }

    /**
     * 获取报修状态列表
     *
     * @param onGetRepireStatusListCallback 获取状态的回调
     * @param arg                           参数.  报修人的服务器ID
     */
    public void getRepireStatusList(@NonNull OnGetRepireStatusListCallback onGetRepireStatusListCallback, @NonNull String arg) {
        threadExecutor.submit(new FutureTask<>(new GetRepireStatus(arg, onGetRepireStatusListCallback)), GetRepireStatus.TAG);
    }

    /**
     * 提交报修评价
     *
     * @param onUploadRepireScoreCallback 获取状态的回调
     * @param datas                       传递的参数 0:报修项目的ID  1:评分  2:评价(选填)
     */
    public void uploadRepireScore(@NonNull OnUploadRepireScoreCallback onUploadRepireScoreCallback, @NonNull String[] datas) {
        threadExecutor.submit(new FutureTask<>(new UploadRepireScore(onUploadRepireScoreCallback, datas)), UploadRepireScore.TAG);
    }

    /**
     * 获取用户信息
     *
     * @param onGetUserInfoCallback 获取状态的回调
     * @param phoneNumber           查找的依据参数 电话号码 (loginID)
     */
    public void getUserInfo(@NonNull OnGetUserInfoCallback onGetUserInfoCallback, @NonNull String phoneNumber) {
        threadExecutor.submit(new FutureTask<>(new GetUserInfo(onGetUserInfoCallback, phoneNumber)), GetUserInfo.TAG);
    }

    /**
     * 注册新用户
     *
     * @param onRegisterUser 获取状态的回调
     * @param datas          注册的信息  0:昵称  1:用户ID  2:密码  3:小区ID  4:栋数ID  5:单元ID  6:房号ID
     */
    public void registerUser(@NonNull OnRegisterUser onRegisterUser, @NonNull String[] datas) {
        threadExecutor.submit(new FutureTask<>(new RegisterNewUser(datas, onRegisterUser)), RegisterNewUser.TAG);
    }

    /**
     * 检查用户是否存在
     *
     * @param onCheckUser 获取状态的回调
     * @param checkID     要查询的用户ID   (loginID)
     */
    public void checkUser(@NonNull OnCheckUser onCheckUser, @NonNull String checkID) {
        threadExecutor.submit(new FutureTask<>(new CheckUserExist(checkID, onCheckUser)), CheckUserExist.TAG);
    }

    /**
     * 获取缴费项目
     *
     * @param onGetPaymentList 获取状态的回调
     * @param roomID           要查询的缴费房号
     */
    public void getPaymentItems(@NonNull OnGetPaymentListCallback onGetPaymentList, @NonNull String roomID) {
        threadExecutor.submit(new FutureTask<>(new GetPaymentList(onGetPaymentList, roomID)), GetPaymentList.TAG);
    }

    /**
     * 上传用户头像
     *
     * @param onUploadUserHeadCallback 获取状态的回调
     * @param userID                   用户的ID
     * @param imageBitmap              要上传的图像Bitmap
     */
    public void uploadUserHeadImage(@NonNull OnUploadUserHeadCallback onUploadUserHeadCallback, @NonNull String userID, @NonNull Bitmap imageBitmap) {
        threadExecutor.submit(new FutureTask<>(new UploadUserHead(onUploadUserHeadCallback, userID, imageBitmap)), UploadUserHead.TAG);
    }

    /**
     * 获取缴费列表数据
     *
     * @param onGetPayRecordCallback 获取状态的回调
     * @param roomID                 要查询的房号ID
     * @param pageSize               每页的数据量
     * @param pageIndex              页码
     */
    public void getPayrecordList(@NonNull OnGetPayRecordCallback onGetPayRecordCallback, @NonNull String roomID, @NonNull String pageSize, @NonNull String pageIndex) {
        threadExecutor.submit(new FutureTask<>(new GetPayRecordList(onGetPayRecordCallback, pageIndex, pageSize, roomID)), GetPayRecordList.TAG);
    }

    /**
     * 获取缴费列表数据
     *
     * @param onGetPayRecordByMonthCallback 获取状态的回调
     * @param roomID                        要查询的房号ID
     * @param pageSize                      每页的数据量
     * @param pageIndex                     页码
     * @param month                         月份
     * @param year                          年份
     */
    public void getPayrecordListByMonth(@NonNull OnGetPayRecordByMonthCallback onGetPayRecordByMonthCallback, @NonNull String roomID, @NonNull String pageSize, @NonNull String pageIndex, @NonNull String month, @NonNull String year) {
        threadExecutor.submit(new FutureTask<>(new GetPayRecordByMonth(onGetPayRecordByMonthCallback, roomID, month, year, pageSize, pageIndex)), GetPayRecordByMonth.TAG);
    }

    /**
     * 获取某一缴费项目的余额
     *
     * @param onGetSumBalanceCallback 获取状态的回调
     * @param roomID                  要查询的房号ID
     * @param chargeTypeID            要查询的项目ID
     */
    public void getSumBalance(@NonNull OnGetSumBalanceCallback onGetSumBalanceCallback, @NonNull String roomID, @NonNull String chargeTypeID) {
        threadExecutor.submit(new FutureTask<>(new GetSumBalance(onGetSumBalanceCallback, roomID, chargeTypeID)), GetSumBalance.TAG);
    }

    /**
     * 上传缴费记录
     *
     * @param onUploadPaymentCallback 上传状态回调
     * @param roomID                  房间号ID
     * @param chargeTypeID            缴费类型ID
     * @param chargeSum               缴费金额
     * @param orderNo                 订单号
     */
    public void uploadPayment(@NonNull OnUploadPaymentCallback onUploadPaymentCallback, @NonNull String roomID, @NonNull String chargeTypeID, @NonNull String chargeSum, @NonNull String orderNo, @NonNull String useScore,@NonNull String userTel) {
        threadExecutor.submit(new FutureTask<>(new UploadPayment(onUploadPaymentCallback, roomID, chargeTypeID, chargeSum, orderNo, useScore,userTel)), UploadPayment.TAG);
    }

    /**
     * 查询订单号是否存在
     *
     * @param onCheckPaymentCallback 查询的回调
     * @param orderNo                要查询的订单号
     */
    public void checkPayment(@NonNull OnCheckPaymentCallback onCheckPaymentCallback, @NonNull String orderNo) {
        threadExecutor.submit(new FutureTask<>(new CheckPaymentExist(onCheckPaymentCallback, orderNo)), CheckPaymentExist.TAG);
    }

    /**
     * 获取 我的爱心 列表数据
     *
     * @param onGetHelpingListCallback 获取数据的回调
     * @param pageSize                 一页读取的数据量
     * @param pageIndex                页码
     * @param proprietorID             用户ID
     */
    public void getHelpingList(@NonNull OnGetHelpingListCallback onGetHelpingListCallback, @NonNull String pageSize, @NonNull String pageIndex, @NonNull String proprietorID) {
        threadExecutor.submit(new FutureTask<>(new GetHelpingList(onGetHelpingListCallback, pageSize, pageIndex, proprietorID)), GetHelpingList.TAG);
    }

    /**
     * 获取 我的困难 列表数据
     *
     * @param onGetAskingListCallbacak 获取数据的回调
     * @param pageSize                 一页读取的数据量
     * @param pageIndex                页码
     * @param proprietorID             用户ID
     */
    public void getAskingList(@NonNull OnGetAskingListCallbacak onGetAskingListCallbacak, @NonNull String pageSize, @NonNull String pageIndex, @NonNull String proprietorID) {
        threadExecutor.submit(new FutureTask<>(new GetAskingList(onGetAskingListCallbacak, pageSize, pageIndex, proprietorID)), GetAskingList.TAG);
    }

    /**
     * 获取 公益展示 列表数据
     *
     * @param onGetContribuListCallbacak 获取数据的回调
     * @param pageSize                   一页读取的数据量
     * @param pageIndex                  页码
     */
    public void getContribuList(@NonNull OnGetContribuListCallbacak onGetContribuListCallbacak, @NonNull String pageSize, @NonNull String pageIndex) {
        threadExecutor.submit(new FutureTask<>(new GetContribuList(onGetContribuListCallbacak, pageSize, pageIndex)), GetContribuList.TAG);
    }

    /**
     * 获取 服务投诉 列表数据
     *
     * @param onGetQuestionListCallbacak 获取数据的回调
     * @param pageSize                   一页读取的数据量
     * @param pageIndex                  页码
     * @param communityID                小区ID
     */
    public void getQuestionList(@NonNull OnGetQuestionListCallbacak onGetQuestionListCallbacak, @NonNull String pageSize, @NonNull String pageIndex, @NonNull String communityID) {
        threadExecutor.submit(new FutureTask<>(new GetQuestionList(onGetQuestionListCallbacak, pageSize, pageIndex, communityID)), GetQuestionList.TAG);
    }

    /**
     * 获取 服务求助 列表数据
     *
     * @param onGetServiceListCallbacak 获取数据的回调
     * @param pageSize                  一页读取的数据量
     * @param pageIndex                 页码
     * @param communityID               小区ID
     */
    public void getServiceList(@NonNull OnGetServiceListCallbacak onGetServiceListCallbacak, @NonNull String pageSize, @NonNull String pageIndex, @NonNull String communityID) {
        threadExecutor.submit(new FutureTask<>(new GetServiceList(onGetServiceListCallbacak, pageSize, pageIndex, communityID)), GetServiceList.TAG);
    }

    /**
     * 获取 合理化建议 列表数据
     *
     * @param onGetSuggestListCallbacak 获取数据的回调
     * @param pageSize                  一页读取的数据量
     * @param pageIndex                 页码
     * @param communityID               小区ID
     */
    public void getSuggestList(@NonNull OnGetSuggestListCallbacak onGetSuggestListCallbacak, @NonNull String pageSize, @NonNull String pageIndex, @NonNull String communityID) {
        threadExecutor.submit(new FutureTask<>(new GetSuggestList(onGetSuggestListCallbacak, pageSize, pageIndex, communityID)), GetSuggestList.TAG);
    }

    /**
     * 获取 物业服务 列表数据
     *
     * @param onGetPropertyListCallbacak 获取数据的回调
     * @param pageSize                   一页读取的数据量
     * @param pageIndex                  页码
     * @param communityID                小区ID
     */
    public void getPropertyList(@NonNull OnGetPropertyListCallbacak onGetPropertyListCallbacak, @NonNull String pageSize, @NonNull String pageIndex, @NonNull String communityID) {
        threadExecutor.submit(new FutureTask<>(new GetPropertyList(onGetPropertyListCallbacak, pageSize, pageIndex, communityID)), GetPropertyList.TAG);
    }

    /**
     * 获取 便民服务 列表数据
     *
     * @param onGetServicePropleListCallbacak 获取数据的回调
     * @param pageSize                        一页读取的数据量
     * @param pageIndex                       页码
     */
    public void getServicePeopleList(@NonNull OnGetServicePropleListCallbacak onGetServicePropleListCallbacak, @NonNull String pageSize, @NonNull String pageIndex) {
        threadExecutor.submit(new FutureTask<>(new GetServicePeopleList(onGetServicePropleListCallbacak, pageSize, pageIndex)), GetServicePeopleList.TAG);
    }

    /**
     * 获取维修数据列表
     *
     * @param onGetRepireDataCallback 维修数据获取回调
     * @param status                  要获取的状态
     * @param tel                     维修人员的电话号码
     * @param pageSize                请求的页面数据量
     * @param pageIndex               请求的页面页码
     */
    public void getRepireList(@NonNull OnGetRepireDataCallback onGetRepireDataCallback, @NonNull String status, @NonNull String tel, int pageSize, int pageIndex) {
        threadExecutor.submit(new FutureTask<>(new GetRepireList(onGetRepireDataCallback, status, tel, Integer.toString(pageSize), Integer.toString(pageIndex))), GetRepireList.TAG + status);
    }

    /**
     * 更改当前维修工单的状态
     *
     * @param onChangeRepireStatus 更改状态回调
     * @param serviceID            工单服务ID
     * @param tel                  维修人员电话号码
     * @param status               要更改成的状态
     * @param files                上传图像数组
     */
    public void changeRepireStatus(@NonNull OnChangeRepireStatus onChangeRepireStatus, @NonNull String serviceID, @NonNull String tel, @NonNull String status, @Nullable String[] files) {
        threadExecutor.submit(new FutureTask<>(new ChangeRepireStatus(onChangeRepireStatus, tel, serviceID, status, files)), ChangeRepireStatus.TAG);
    }

    /**
     * 签到申请
     *
     * @param onUpSignCallback          获取状态的回调
     * @param userTel                   要签到的用户手机号
     * @param date                      签到日期
     */
    public void upSign (@NonNull OnUpSignCallback onUpSignCallback, @NonNull String userTel, @NonNull String date) {
        threadExecutor.submit(new FutureTask<>(new UpSign(onUpSignCallback, userTel, date)), UpSign.TAG);
    }

    /**
     * 获取签到历史
     *
     * @param onGetSignHistoryCallback        获取状态的回调
     * @param userTel                   要获取的用户手机号
     *
     */
    public void getSignHitory (@NonNull OnGetSignHistoryCallback onGetSignHistoryCallback, @NonNull String userTel) {
        threadExecutor.submit(new FutureTask<>(new GetSignHistory(onGetSignHistoryCallback, userTel)), GetSignHistory.TAG);
    }

    /**
     * 获取积分操作
     *
     * @param onGetSignScoreCallback       获取状态的回调
     * @param userTel                  要获取的用户手机号
     *
     */
    public void getSignScore (@NonNull OnGetSignScoreCallback onGetSignScoreCallback, @NonNull String userTel) {
        threadExecutor.submit(new FutureTask<>(new GetSignScore(onGetSignScoreCallback, userTel)), GetSignScore.TAG);
    }


    /**
     * 点赞申请
     *
     * @param onUpLikeCallback        获取状态的回调
     * @param userTel                   要签到的用户手机号
     * @param detail                    文章标题
     */
    public void upLike (@NonNull OnUpLikeCallback onUpLikeCallback, @NonNull String userTel, @NonNull String detail) {
        threadExecutor.submit(new FutureTask<>(new UpLike(onUpLikeCallback, userTel, detail)), UpLike.TAG);
    }

    /**
     * 获取商家列表数据
     *
     * @param onGetBusinessCallback     获取列表的回调
     * @param pageSize                  每页要加载的数据量
     * @param pageIndex                 需要加载的页码
     */
    public void getBusinessList (@NonNull OnGetBusinessCallback onGetBusinessCallback, @NonNull int pageSize, @NonNull int pageIndex) {
        threadExecutor.submit(new FutureTask<>(new GetBusinessList(onGetBusinessCallback, pageSize, pageIndex)), GetBusinessList.TAG);
    }


    /**
     * 获取周边列表数据
     *
     * @param onGetCircumCallback      获取列表的回调
     * @param pageSize                  每页要加载的数据量
     * @param pageIndex                 需要加载的页码
     */
    public void getCircumList (@NonNull OnGetCircumCallback onGetCircumCallback, @NonNull int pageSize, @NonNull int pageIndex) {
        threadExecutor.submit(new FutureTask<>(new GetCircumList(onGetCircumCallback, pageSize, pageIndex)), GetCircumList.TAG);
    }

    /**
     * 获取预约列表数据
     *
     * @param onGetOrderCallback      获取列表的回调
     * @param pageSize                  每页要加载的数据量
     * @param pageIndex                 需要加载的页码
     */
    public void getOrderList (@NonNull OnGetOrderCallback onGetOrderCallback, @NonNull int pageSize, @NonNull int pageIndex,String userTel) {
        threadExecutor.submit(new FutureTask<>(new GetOrderList(onGetOrderCallback, pageSize, pageIndex, userTel)), GetOrderList.TAG);
    }

    /**
     * 获取商家详情图片列表数据
     *
     * @param onGetBusinessImageCallback      获取列表的回调
     * @param businessID                      要加载图片的商家ID
     */
    public void getBusinessImage (@NonNull OnGetBusinessImageCallback onGetBusinessImageCallback, int businessID) {
        threadExecutor.submit(new FutureTask<>(new GetBusinessImage(onGetBusinessImageCallback,businessID)), GetBusinessImage.TAG);
    }


    /**
     * 获取商家预设评论列表数据
     *
     * @param onGetReplyTabCallback           获取列表的回调
     * @param businessID                      要加载图片的商家ID
     * @param tabNum                          需要显示的预设回复数
     */
    public void getReplyTab (@NonNull OnGetReplyTabCallback onGetReplyTabCallback, int businessID, int tabNum) {
        threadExecutor.submit(new FutureTask<>(new GetReplyTab(onGetReplyTabCallback,businessID, tabNum)), GetReplyTab.TAG);
    }

    /**
     * 获取商家详细评论列表数据
     *
     * @param onGetReplyListCallback          获取列表的回调
     * @param businessID                      要加载图片的商家ID
     * @param pageSize                         需要显示条目数量
     * @param pageIndex                        需要显示的页码
     */
    public void getReplyList (@NonNull OnGetReplyListCallback onGetReplyListCallback, int pageSize, int pageIndex,int businessID ) {
        threadExecutor.submit(new FutureTask<>(new GetReplyList(onGetReplyListCallback,pageSize, pageIndex,businessID)), GetReplyList.TAG);
    }


    /**
     * 提交预约请求
     *
     * @param onUpOrderCallback             获取信息的回调
     * @param storeID                       需要预约的商家ID
     * @param orderTime                     预约时间戳
     * @param sendTime                      发送信息时间戳
     * @param userTel                       用户填写的联系手机号码
     * @param orderMessage                  用户填写的预约信息
     * @param userID                        用户ID
     */
    public void upOrder (@NonNull OnUpOrderCallback onUpOrderCallback, int storeID,String  orderTime, String sendTime, String userTel,String orderMessage, String userID) {
        threadExecutor.submit(new FutureTask<>(new UpOrder(onUpOrderCallback,storeID, orderTime,sendTime,userTel,orderMessage,userID)), UpOrder.TAG);
    }


    /**
     * 提交预约请求
     *
     * @param onUpReplyTabCallback         获取信息的回调
     * @param storeID                       需要预约的商家ID

     */
    public void upReplyTab (@NonNull OnUpReplyTabCallback onUpReplyTabCallback, JSONArray jsonArray,int storeID) {
        threadExecutor.submit(new FutureTask<>(new UpReplyTab(onUpReplyTabCallback,jsonArray, storeID)), UpReplyTab.TAG);
    }


    /**
     * 提交预约请求
     *
     * @param onUpReplyContentCallback         获取信息的回调
     * @param storeID                       需要预约的商家ID
     * @param replyContent                  评论文字内容
     * @param replyScore                    评分
     * @param userID                        评论的用户ID
     * @param replyTime                     评论时间

     */
    public void upReplyContent (@NonNull OnUpReplyContentCallback onUpReplyContentCallback,int storeID , String replyScore, String replyContent, String userID, String replyTime, int orderID) {
        threadExecutor.submit(new FutureTask<>(new UpReplyContent(onUpReplyContentCallback,storeID, replyScore,replyContent,userID,replyTime,orderID)), UpReplyContent.TAG);
    }



    /**
     * 提交预约请求
     *
     * @param onUpReplyPhotoCallback         获取信息的回调
     * @param commentID                       需要预约的商家ID
     * @param files                           图片路径数组

     */
    public void upReplyPhoto (@NonNull OnUpReplyPhotoCallback onUpReplyPhotoCallback,int commentID , String[] files) {
        threadExecutor.submit(new FutureTask<>(new UpReplyPhoto(onUpReplyPhotoCallback,commentID, files)), UpReplyPhoto.TAG);
    }

    /**
     * 获取评论图片列表请求
     *
     * @param onGetReplyImageCallback         获取信息的回调
     * @param commentID                       需要预约的商家ID
     *

     */
    public void getReplyImage (@NonNull OnGetReplyImageCallback onGetReplyImageCallback, int commentID, int cuont) {
        threadExecutor.submit(new FutureTask<>(new GetReplyImage(onGetReplyImageCallback,commentID,cuont)), GetReplyImage.TAG);
    }

    /**
     * 取消预约请求
     *
     * @param onCancalOrderCallback         获取信息的回调
     * @param orderID                       需要取消预约的商家ID
     *

     */
    public void cancalOrder (@NonNull OnCancalOrderCallback onCancalOrderCallback, int orderID) {
        threadExecutor.submit(new FutureTask<>(new CancalOrder(onCancalOrderCallback,orderID)), CancalOrder.TAG);
    }

    /**
     * 获取积分抵消数据
     *
     * @param onGetScoreDeductCallback         获取信息的回调
     * @param userTel                       用户号码
     * @param money                         用户需要缴费的钱数
     *
     */
         public void getScoreDeduct (@NonNull OnGetScoreDeductCallback onGetScoreDeductCallback, String userTel, String money ) {
        threadExecutor.submit(new FutureTask<>(new GetScoreDeduct(onGetScoreDeductCallback,userTel,money)), GetScoreDeduct.TAG);
    }


    /**
     * 消费积分请求
     *
     * @param onUpScoreDeductCallback         获取信息的回调
     * @param userTel                       用户号码
     * @param count                         用户需要消费的积分
     * @param scoreType                     用户消费积分类别
     *
     */
    public void upScoreDeduct (@NonNull OnUpScoreDeductCallback onUpScoreDeductCallback, String userTel, String scoreType, String count ) {
        threadExecutor.submit(new FutureTask<>(new UpScoreDeduct(onUpScoreDeductCallback,userTel,scoreType,count)), UpScoreDeduct.TAG);
    }
    /**
     * 取消任务
     *
     * @param taskType 任务种类
     */
    public void cancelTask(TaskType taskType) {
        switch (taskType) {
            case ALL_TASK:
                threadExecutor.cancelAllTask();
                break;
            case Login_Task:
                threadExecutor.cancelTask(LoginThread.TAG);
                break;
            case GetArtcles_Task:
                threadExecutor.cancelTask(GetArtclesThread.TAG);
                break;
            case UpdateUserInfo_Task:
                threadExecutor.cancelTask(UpdateUserInfo.TAG);
                break;
            case UpdateRequestHelp_Task:
                threadExecutor.cancelTask(UpdateRequestHelpData.TAG);
                break;
            case RequestDatasList_List:
                threadExecutor.cancelTask(RequestDataList.TAG);
                break;
            case UploadRepireRequest:
                threadExecutor.cancelTask(UploadRepireRequest.TAG);
                break;
            case GetRepireStatus:
                threadExecutor.cancelTask(GetRepireStatus.TAG);
                break;
            case UploadRepireScore:
                threadExecutor.cancelTask(UploadRepireScore.TAG);
                break;
            case GetUserInfo:
                threadExecutor.cancelTask(GetUserInfo.TAG);
                break;
            case CheckUserExist:
                threadExecutor.cancelTask(CheckUserExist.TAG);
                break;
            case RegisterNewUser:
                threadExecutor.cancelTask(RegisterNewUser.TAG);
                break;
            case GetPaymentList:
                threadExecutor.cancelTask(GetPaymentList.TAG);
                break;
            case UploadUserHead:
                threadExecutor.cancelTask(UploadUserHead.TAG);
                break;
            case GetPayRecordList:
                threadExecutor.cancelTask(GetPayRecordList.TAG);
                break;
            case GetPayRecordByMonth:
                threadExecutor.cancelTask(GetPayRecordByMonth.TAG);
                break;
            case GetSumBalance:
                threadExecutor.cancelTask(GetSumBalance.TAG);
                break;
            case UploadPayment:
                threadExecutor.cancelTask(UploadPayment.TAG);
                break;
            case CheckPaymentExist:
                threadExecutor.cancelTask(CheckPaymentExist.TAG);
                break;
            case GetHelpingList:
                threadExecutor.cancelTask(GetHelpingList.TAG);
                break;
            case GetAskingList:
                threadExecutor.cancelTask(GetAskingList.TAG);
                break;
            case GetRepireDataList:
                threadExecutor.cancelTask(GetRepireList.TAG);
                break;
            case ChangeRepireStatus:
                threadExecutor.cancelTask(ChangeRepireStatus.TAG);
                break;
            case GetContribuList:
                threadExecutor.cancelTask(GetContribuList.TAG);
                break;
            case GetPropertyList:
                threadExecutor.cancelTask(GetPropertyList.TAG);
                break;
            case GetQuestionList:
                threadExecutor.cancelTask(GetQuestionList.TAG);
                break;
            case GetServicePeopleList:
                threadExecutor.cancelTask(GetServicePeopleList.TAG);
                break;
            case GetServiceList:
                threadExecutor.cancelTask(GetServiceList.TAG);
                break;
            case GetSuggestList:
                threadExecutor.cancelTask(GetSuggestList.TAG);
                break;

        }
    }

    /**
     * 任务的种类枚举,用于取消任务
     */
    public enum TaskType {

        ALL_TASK,
        Login_Task,
        GetArtcles_Task,
        UpdateUserInfo_Task,
        UpdateRequestHelp_Task,
        RequestDatasList_List,
        UploadRepireRequest,
        GetRepireStatus,
        UploadRepireScore,
        GetUserInfo,
        RegisterNewUser,
        CheckUserExist,
        GetPaymentList,
        UploadUserHead,
        GetPayRecordList,
        GetPayRecordByMonth,
        GetSumBalance,
        UploadPayment,
        CheckPaymentExist,
        GetHelpingList,
        GetAskingList,
        GetRepireDataList,
        ChangeRepireStatus,
        GetContribuList,
        GetSuggestList,
        GetServiceList,
        GetQuestionList,
        GetPropertyList,
        GetServicePeopleList

    }

    /**
     * 获取数据列表的种类
     */
    public enum DataType {

        LIST_community,
        LIST_apartment,
        LIST_unit,
        LIST_room,
        String_Array_RepireType

    }

    /**
     * 获取文章以及缓存首页滚动图片的任务  8  9  10
     */
    private class GetArtclesThread implements Callable<String> {
        public static final String TAG = "GetArtclesThread";

        private final OnArtcleLoadCallback onArtcleLoadCallback;
        private final boolean needToCacheImage;
        private ArrayList<ArtclesBean> artcles = null;

        private String[] args;

        GetArtclesThread(OnArtcleLoadCallback onArtcleLoadCallback, boolean needToCacheImage, String[] args) {
            this.onArtcleLoadCallback = onArtcleLoadCallback;
            this.needToCacheImage = needToCacheImage;
            this.args = args;
        }

        @Override
        public String call() throws Exception {
            //先检查OKHttp是否有效
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            //如果参数数量大于等于4，则执行请求
            if (args != null && args.length >= 4) {
                try {
                    artcles = requestData();
                } catch (Exception e) {
                    Log.d(TAG, "Exception:" + e);
                    failed(null, e);
                    return null;
                }
            } else {
                failed("无效的请求参数", null);
                return null;
            }

            completed();

            return null;
        }

        /**
         * 请求数据
         *
         * @return 文章的条目 JsonObject 列表
         * @throws Exception 期间产生的异常
         */
        private ArrayList<ArtclesBean> requestData() throws Exception {
            String arg1 = args[0]; //每页获取数据的数量
            String arg2 = args[1]; //获取第几页的数据
            String arg3 = args[2]; //文章所属标识  1(公司公告),2(小区公告), 4(爱心行动),5(流程与规定), 6(申请与租赁), 8(社区文化),9(安和文化园),10(传统文化),11(我的帮助),12(我的求助),13(服务投诉) ,13(服务求助) 7(合理化建议)
            String arg4 = args[3]; //小区标识 (为Null 为公共文章使用)

            RequestBody requestBody;
            Request request;

            if (arg4 != null) {
                //普通文章
                requestBody = new FormEncodingBuilder().add("PageSize", arg1).add("PageIndex", arg2).add("ParentID", arg3).add("communityID", arg4).build();
                request = new Request.Builder().url(URL_articleInterface).post(requestBody).build();
            } else {
                //公共文章
                requestBody = new FormEncodingBuilder().add("PageSize", arg1).add("PageIndex", arg2).add("ParentID", arg3).build();
                request = new Request.Builder().url(URL_request_readCommonInterface).post(requestBody).build();
            }

            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                Log.d(TAG, "RequestData Successful !");
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getArtclesJsonArray(result);
            } else {
                Log.d(TAG, "RequestData Failed !");
            }

            return null;
        }

        /**
         * 得到前五张滚动图片
         */
        private void getImage() {
            if (artcles != null && needToCacheImage) {
                //添加之前清空原本储存的对象
                AppOptions.CACHE.resetMainScrollingItems();

                for (int i = 0; i < artcles.size(); i++) {
                    if (i >= 5) {
                        break;
                    }
                    ArtclesBean artcle = artcles.get(i);
                    if (!TextUtils.isEmpty(artcle.getRelativePath())) {
                        //找到缩略图
                        AppOptions.CACHE.addMainScrollingItems(artcle);
                    } else {
                        Log.d(TAG, "Relative not found");
                    }
                }
            }
        }

        /**
         * 确定文章的类型
         *
         * @return 文章的类型枚举类
         */
        private ArtcleType getArtclesType() {
            switch (args[2]) {
                case "1":
                    return ArtcleType.公司公告;
                case "2":
                    return ArtcleType.小区公告;
                case "3":
                    return ArtcleType.未定义;
                case "4":
                    return ArtcleType.爱心行动;
                case "5":
                    return ArtcleType.流程与规定;
                case "6":
                    return ArtcleType.申请与租赁;
                case "7":
                    return ArtcleType.合理化建议;
                case "8":
                    return ArtcleType.社区文化;
                case "9":
                    return ArtcleType.安和文化园;
                case "10":
                    return ArtcleType.传统文化;
                case "11":
                    return ArtcleType.我的帮助;
                case "12":
                    return ArtcleType.我的求助;
                case "13":
                    return ArtcleType.服务投诉;
                case "14":
                    return ArtcleType.服务求助;
                default:
                    return ArtcleType.未定义;
            }
        }

        /**
         * 请求失败的时候
         *
         * @param e 抛出的错误异常
         */
        private void failed(String message, Exception e) {
            threadExecutor.removeTag(TAG + args[2]);

            if (onArtcleLoadCallback != null) {
                onArtcleLoadCallback.onArtcleLoadFailed(message, e);
            }
        }

        /**
         * 获取完成
         */
        private void completed() {
            threadExecutor.removeTag(TAG + args[2]);
            getImage();

            if (onArtcleLoadCallback != null) {

                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (args[3] != null && (artcles == null || artcles.size() == 0)) {
                            onArtcleLoadCallback.onArtcleLoadCompleted(
                                    0,
                                    0,
                                    getArtclesType(),
                                    Integer.parseInt(args[3]),
                                    null
                            );
                        } else if (args[3] != null) {
                            onArtcleLoadCallback.onArtcleLoadCompleted(
                                    artcles.size(),
                                    artcles.get(0).getTotalArtclesCount(),
                                    getArtclesType(),
                                    Integer.parseInt(args[3]),
                                    artcles
                            );
                        } else if (artcles == null) {
                            onArtcleLoadCallback.onArtcleLoadCompleted(
                                    0,
                                    0,
                                    getArtclesType(),
                                    -1,
                                    artcles
                            );
                        } else {
                            onArtcleLoadCallback.onArtcleLoadCompleted(
                                    artcles.size(),
                                    artcles.get(0).getTotalArtclesCount(),
                                    getArtclesType(),
                                    -1,
                                    artcles
                            );
                        }
                    }
                });

            }
        }

    }

    /**
     * 登录任务
     */
    private class LoginThread implements Callable<String> {
        public static final String TAG = "LoginThread";

        private OnLoginCallback onLoginCallback;
        private boolean isMainScrollingMessageLoaded = false;
        private String username;
        private String password;

        LoginThread(OnLoginCallback onLoginCallback, String username, String password) {
            this.onLoginCallback = onLoginCallback;
            this.username = username;
            this.password = password;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            String[] datas;
            UserBean userBean = null;

            Thread.sleep(3000);

            try {
                //获取登录的状态信息
                datas = login(username, password);
                if (datas != null && datas[0].toLowerCase().equals("success")) {

                    //如果登录成功，则请求用户数据信息
                    userBean = getUserInfo(username);

                    //然后获取主页滚动数据
                    if (!loadScrollingMessage()) {
                        throw new Exception();
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception: " + e);
                threadExecutor.removeTag(TAG);
                failed(null, true);
                return null;
            }

            threadExecutor.removeTag(TAG);

            if (userBean != null) {
                //情况1，成功获取到用户信息，表明登录肯定成功了
                completed(userBean);
            } else if (datas != null && datas[0].toLowerCase().equals("success")) {
                //情况2，登录成功，但获取用户数据失败
                failed("获取用户信息失败 , 请重试", false);
            } else if (datas != null && datas[0].toLowerCase().equals("failure")) {
                //情况3，登录失败
                failed("用户名或密码错误，请重试", false);
            } else if (datas == null) {
                //情况4，发生异常，登录数据并未获得
                failed(null, true);
            } else {
                //情况5，未知错误
                failed(null, true);
            }

            return null;
        }

        /**
         * 用户登录
         *
         * @param username 用户名
         * @param password 密码
         * @return 返回数据的数组，第一个元素：登录状态，第二个元素：返回的消息
         * @throws Exception 产生的异常
         */
        private String[] login(String username, String password) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder().add("loginID", username).add("loginPSW", password).build();
            Request request = new Request.Builder().post(requestBody).url(URL_loginInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "login successful");
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getStatusJsonData(result);
            } else {
                Log.d(TAG, "login failed");
                return null;
            }
        }

        /**
         * 获取用户信息
         *
         * @param username 用户名，作为查找的依据 (LoginID)
         * @return 用户信息Bean
         */
        UserBean getUserInfo(String username) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder().add("loginID", username).build();
            Request request = new Request.Builder().post(requestBody).url(URL_userInfoInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "request user_info successful");
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getUserJsonData(result);
            } else {
                Log.d(TAG, "request user_info failed");
                return null;
            }
        }

        /**
         * 读取滚动数据
         *
         * @return 是否已经下载完成
         */
        private boolean loadScrollingMessage() {
            //预读取文章图片，供主界面滚动显示
            final Thread thread = new Thread(new FutureTask<>(new GetArtclesThread(new OnArtcleLoadCallback() {
                @Override
                public void onArtcleLoadCompleted(int loadedArtclesCount, int totalArtclesCount, ArtcleType artcleType, int communityID, @Nullable ArrayList<ArtclesBean> arrayList) {
                    isMainScrollingMessageLoaded = true;
                }

                @Override
                public void onArtcleLoadFailed(String message, Exception e) {

                }
            }, true, new String[]{"5", "1", "8", null})));

            thread.start();

            //重试次数
            int retry = 60;

            //执行了拉取任务后开始等待下载完成
            while (!isMainScrollingMessageLoaded) {
                try {
                    //每隔1秒检测一次是否下载完成
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }

                retry -= 1;
                if (retry == 0) {
                    NetworkHelper.getInstance().cancelTask(NetworkHelper.TaskType.GetArtcles_Task);
                    return false;
                }
            }
            //重置状态
            isMainScrollingMessageLoaded = false;
            return true;
        }

        /**
         * 登录失败
         *
         * @param message 接口返回的失败消息
         */
        private void failed(final String message, final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onLoginCallback.onLoginFailed(message, isException);
                }
            });
        }

        /**
         * 登录成功
         */
        private void completed(final UserBean userBean) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onLoginCallback.onLoginCompleted(username, userBean);
                }
            });
        }

    }

    /**
     * 更新用户信息
     */
    private class UpdateUserInfo implements Callable<String> {
        public static final String TAG = "UpdateUserThread";

        private OnUpdateInfoCallback onUpdateInfoCallback;
        private UserBean updateBean;
        private UserBean updatedBean;

        UpdateUserInfo(OnUpdateInfoCallback onUpdateInfoCallback, UserBean updateBean) {
            this.onUpdateInfoCallback = onUpdateInfoCallback;
            this.updateBean = updateBean;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;
            boolean syncDataStatus = false;

            try {
                //开始更新用户信息
                result = update(updateBean);

                if (result) {
                    //如果更新成功，则再次请求用户数据，更新本地的信息
                    updatedBean = getUserInfo(updateBean.getLoginID());

                    //更新结果
                    if (updatedBean == null) {
                        syncDataStatus = false;
                    } else {
                        //同步本地信息

                        syncDataStatus = true;
                        AppOptions.USERINFO.setUser(updateBean);
                    }
                }
            } catch (Exception e) {
                failed(false);
                return null;
            }

            if (result && syncDataStatus) {
                //更新完成，本地数据同步完成
                completed(updatedBean);
            } else if (result) {
                //更新完成，本地同步数据失败
                failed(true);
            } else {
                failed(false);
            }

            return null;
        }

        /**
         * 更新用户信息
         *
         * @param updateBean 要更新的信息Bean
         * @return 更新状态结果
         * @throws Exception 更新时抛出的异常
         */
        private boolean update(UserBean updateBean) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("name", updateBean.getUserName())
                    .add("loginID", updateBean.getLoginID())
                    .add("loginPSW", updateBean.getPassword())
                    .add("communityID", updateBean.getCommunityID())
                    .add("apartment", updateBean.getApartment())
                    .add("unit", updateBean.getUnit())
                    .add("room", updateBean.getRoom())
                    .add("telephoneNum", updateBean.getPhoneNumber())
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_userUpdateInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "update request successful");
                final String result = response.body().string();
                response.body().close();
                String[] result2 = JsonDecoder.getStatusJsonData(result);
                if (result2 == null || result2[0].toLowerCase().equals("failure")) {
                    return false;
                } else {
                    return result2[0].toLowerCase().equals("success");
                }
            } else {
                return false;
            }

        }

        /**
         * 获取用户信息
         *
         * @param username 用户名，作为查找的依据 (LoginID)
         * @return 用户信息Bean
         */
        private UserBean getUserInfo(String username) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder().add("loginID", username).build();
            Request request = new Request.Builder().post(requestBody).url(URL_userInfoInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "request user_info successful");
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getUserJsonData(result);
            } else {
                Log.d(TAG, "request user_info failed");
                return null;
            }
        }

        /**
         * 失败回调
         *
         * @param syncFailed 是否同步失败
         */
        private void failed(final boolean syncFailed) {
            threadExecutor.removeTag(TAG);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpdateInfoCallback.failed(syncFailed);
                }
            });
        }

        /**
         * 成功回调
         *
         * @param userBean 更新后的Bean
         */
        private void completed(final UserBean userBean) {
            threadExecutor.removeTag(TAG);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpdateInfoCallback.completed(userBean);
                }
            });
        }

    }

    /**
     * 提交业主诉求
     */
    private class UpdateRequestHelpData implements Callable<String> {
        static final String TAG = "UpdateRequestHelpData";

        private OnUpdateRequestHelpCallback onUpdateRequestHelpCallback;
        private String[] datas;

        UpdateRequestHelpData(OnUpdateRequestHelpCallback onUpdateRequestHelpCallback, String[] datas) {
            this.onUpdateRequestHelpCallback = onUpdateRequestHelpCallback;
            this.datas = datas;
        }

        @Override
        public String call() throws Exception {

            //先检测要提交的数据是否有效
            if (isVaild(datas)) {
                boolean result;

                try {
                    //提交数据并获取结果
                    result = updateDatas(datas);
                } catch (Exception e) {
                    //提交过程发生异常
                    onFailed(true);
                    return null;
                }

                if (result) {
                    onCompleted();
                    return null;
                } else {
                    //数据提交失败
                    onFailed(false);
                    return null;
                }

            } else {
                //要提交的数据无效
                onFailed(false);
            }

            return null;
        }

        /**
         * 检测提交的数据是否有效
         *
         * @param datas 数据字符串数组
         * @return 是否有效
         */
        private boolean isVaild(String[] datas) {
            if (datas.length < 5) {
                return false;
            } else {
                for (int i = 0; i < datas.length; i++) {
                    if (TextUtils.isEmpty(datas[i])) {
                        return false;
                    }
                }
                return true;
            }
        }

        /**
         * 上传提交的诉求
         *
         * @param datas 要上传的数据
         * @return 上传结果
         * @throws Exception 中途抛出的异常
         */
        private boolean updateDatas(String[] datas) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("Title", datas[0])
                    .add("ParentID", datas[1])
                    .add("communityID", datas[2])
                    .add("proprietorID", datas[3])
                    .add("Content", datas[4])
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_updateRequestHelpInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] responseDatas = JsonDecoder.getStatusJsonData(result);
                return responseDatas != null && responseDatas[0].toLowerCase().equals("success");
            } else {
                return false;
            }
        }

        /**
         * 失败时的调用
         *
         * @param isException 是否为异常
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpdateRequestHelpCallback.onFailed(isException);
                }
            });
        }

        /**
         * 成功时的调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpdateRequestHelpCallback.onCompleted();
                }
            });
        }

    }

    /**
     * 提交报修请求
     */
    private class UploadRepireRequest implements Callable<String> {
        public static final String TAG = "UploadRepireRequest";

        private OnUploadRequestRepireCallback onUploadRequestRepireCallback;
        private String[] files;
        private String[] datas;

        UploadRepireRequest(OnUploadRequestRepireCallback onUploadRequestRepireCallback, String[] datas, String[] files) {
            this.onUploadRequestRepireCallback = onUploadRequestRepireCallback;
            this.datas = datas;
            this.files = files;
        }

        @Override
        public String call() throws Exception {

            //先检测要提交的数据是否有效
            if (isVaild(datas)) {
                String ID;

                try {
                    //提交数据并获取结果
                    ID = updateDatas(datas);
                } catch (Exception e) {
                    //提交过程发生异常
                    onFailed(true);
                    return null;
                }

                if (files == null && !TextUtils.isEmpty(ID)) {
                    //如果不需要上传图片 , 同时返回了ID , 则代表这次报修结束
                    onCompleted();
                } else if (files != null && !TextUtils.isEmpty(ID)) {
                    //如果需要上传照片 , 同时返回了ID , 则开始上传照片数据
                    updatePhotos(files, ID);
                    onCompleted();
                } else {
                    //其他情况就是上传数据失败
                    onFailed(false);
                }

            } else {
                //要提交的数据无效
                onFailed(false);
            }

            return null;
        }

        /**
         * 检测提交的数据是否有效
         *
         * @param datas 数据字符串数组
         * @return 是否有效
         */
        private boolean isVaild(String[] datas) {
            if (datas.length < 7) {
                return false;
            } else {
                for (int i = 0; i < datas.length; i++) {
                    if (TextUtils.isEmpty(datas[i])) {
                        return false;
                    }
                }
                return true;
            }
        }

        /**
         * 上传提交的诉求
         *
         * @param datas 要上传的数据
         * @return 上传得到的ID
         * @throws Exception 中途抛出的异常
         */
        private String updateDatas(String[] datas) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("location", datas[0])
                    .add("communityID", datas[1])
                    .add("serviceType", datas[2])
                    .add("describe", datas[3])
                    .add("reserveTime", datas[4])
                    .add("telephone", datas[5])
                    .add("proprietorID", datas[6])
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_uploadInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] responseDatas = JsonDecoder.getStatusIDJsonData(result);
                if (responseDatas != null && responseDatas[0].toLowerCase().equals("success")) {
                    return responseDatas[1];
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        /**
         * 循环上传图片附件
         *
         * @param files 图片的地址数组
         * @param ID    得到的ID
         * @return 是否所有图片都上传成功
         * @throws Exception 上传中抛出的异常
         */
        private boolean updatePhotos(String[] files, String ID) throws Exception {
            boolean result = false;
            for (String path : files) {
                File file = new File(path);
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
                        .addFormDataPart("serviceID", ID)
                        .addFormDataPart("status", "1")
                        .addFormDataPart("Filedata", "test.jpg", fileBody)
                        .build();
                Request request = new Request.Builder().post(requestBody).url(URL_request_uploadRepirePhotosInterface).build();
                Response response = httpClient.newCall(request).execute();
                result = response.isSuccessful();
            }
            return result;
        }

        /**
         * 失败时的调用
         *
         * @param isException 是否为异常
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUploadRequestRepireCallback.onUploadFailed(isException);
                }
            });
        }

        /**
         * 成功时的调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUploadRequestRepireCallback.onUploadCompleted();
                }
            });
        }

    }

    /**
     * 请求  小区名称   楼号   单元号   房间号  报修类型 数据List
     */
    private class RequestDataList implements Callable<String> {
        public static final String TAG = "RequestDataList";

        private OnRequestDataListCallbacks onRequestDataListCallbacks;
        private ArrayList<Object> datasList;
        private DataType dataType;
        private String arg;

        RequestDataList(OnRequestDataListCallbacks onRequestDataListCallbacks, DataType dataType, String arg) {
            this.onRequestDataListCallbacks = onRequestDataListCallbacks;
            this.dataType = dataType;
            this.arg = arg;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;
            try {
                result = requestList(dataType, arg);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result && datasList != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 请求数据列表
         *
         * @param dataType 请求的数据类型
         * @param arg      参数
         * @return 请求状态
         * @throws Exception 请求中抛出的异常
         */
        private boolean requestList(DataType dataType, String arg) throws Exception {
            datasList = new ArrayList<>();
            RequestBody requestBody;
            Request request;

            if (!TextUtils.isEmpty(arg)) {
                requestBody = new FormEncodingBuilder().add("condition", arg).build();
                switch (dataType) {
                    case LIST_apartment:
                        request = new Request.Builder().post(requestBody).url(URL_request_ApartmentsInterface).build();
                        break;
                    case LIST_unit:
                        request = new Request.Builder().post(requestBody).url(URL_request_UnitInterface).build();
                        break;
                    case LIST_room:
                        request = new Request.Builder().post(requestBody).url(URL_request_RoomInterface).build();
                        break;
                    default:
                        throw new Exception();
                }
            } else {
                switch (dataType) {
                    case LIST_community:
                        request = new Request.Builder().url(URL_request_CommunitysInterface).build();
                        break;
                    case String_Array_RepireType:
                        request = new Request.Builder().url(URL_request_typeInterface).build();
                        break;
                    default:
                        throw new Exception();
                }
            }

            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getPositionDatas(result, dataType, datasList);
            } else {
                datasList = null;
                return false;
            }

        }

        /**
         * 失败时调用
         *
         * @param isException 是否为异常
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG + dataType);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onRequestDataListCallbacks.onFailed(isException);
                }
            });
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG + dataType);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onRequestDataListCallbacks.onRequestCompleted(dataType, datasList);
                }
            });
        }

    }

    /**
     * 获取报修状态列表
     */
    private class GetRepireStatus implements Callable<String> {
        public static final String TAG = "GetRepireStatus";

        private String arg;
        private ArrayList<RepireStatusBean> repireStatusBeen;
        private OnGetRepireStatusListCallback onGetRepireStatusListCallback;

        GetRepireStatus(String arg, OnGetRepireStatusListCallback onGetRepireStatusListCallback) {
            this.arg = arg;
            this.onGetRepireStatusListCallback = onGetRepireStatusListCallback;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;
            try {
                result = getRepireList(arg);
                getPictureUrl(repireStatusBeen);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result && repireStatusBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 请求报修状态列表
         *
         * @param arg 传递的参数  报修人数据库ID
         * @return 执行状态
         * @throws Exception 执行过程中的异常
         */
        private boolean getRepireList(String arg) throws Exception {
            this.repireStatusBeen = new ArrayList<>();
            RequestBody requestBody = new FormEncodingBuilder().add("proprietorID", arg).build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_getStatusInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getRepireStatus(result, repireStatusBeen);
            } else {
                repireStatusBeen = null;
                return false;
            }
        }

        /**
         * 获取每一个id对应的图片地址
         *
         * @param beanArrayList 要获取的数组
         */
        private void getPictureUrl(ArrayList<RepireStatusBean> beanArrayList) throws Exception {
            if (beanArrayList != null && beanArrayList.size() > 0) {
                //循环遍历每个项目的图片id , 依次获取对应的图片地址
                for (int i = 0; i < beanArrayList.size(); i++) {
                    RepireStatusBean bean = beanArrayList.get(i);
                    String[] ids = bean.getPicturesID();

                    if (ids != null) {
                        //如果有图片id , 则开始请求数据
                        for (int k = 0; k < ids.length; k++) {
                            if (!TextUtils.isEmpty(ids[k])) {
                                Log.d(TAG, "正在获取ID:" + bean.getID() + "  图片ID:" + ids[k]);
                                String[] picUrl = requestPictureByIDs(ids[k]);
                                if (picUrl != null) {
                                    bean.getPicturesThu()[k] = picUrl[0];
                                    bean.getPictures()[k] = picUrl[1];
                                }
                            }
                        }
                    }
                }

            }
        }

        /**
         * 网络获取图片地址
         *
         * @param id 图片id
         * @return 图片地址数据  0:图片缩略图    1:图片大图
         */
        private String[] requestPictureByIDs(String id) throws Exception {
            RequestBody body = new FormEncodingBuilder()
                    .add("ID", id)
                    .build();
            Request request = new Request.Builder()
                    .post(body)
                    .url(URL_request_getRepirePictureInterface)
                    .build();
            try {
                Response response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    response.body().close();
                    return JsonDecoder.getPicturesUrl(result);
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetRepireStatusListCallback.onGotRepireStatusListCompleted(repireStatusBeen);
                }
            });
        }

        /**
         * 失败时调用
         *
         * @param isException 是否为异常
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetRepireStatusListCallback.onGotRepireStatusListFailed(isException);
                }
            });
        }

    }

    /**
     * 获取报修状态列表
     */
    private class UploadRepireScore implements Callable<String> {
        public static final String TAG = "UploadRepireScore";

        private String[] datas;
        private OnUploadRepireScoreCallback onUploadRepireScoreCallback;

        UploadRepireScore(OnUploadRepireScoreCallback onUploadRepireScoreCallback, String[] datas) {
            this.onUploadRepireScoreCallback = onUploadRepireScoreCallback;
            this.datas = datas;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;
            try {
                result = getRepireList(datas);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 上传数据
         *
         * @param datas 传递的参数 0:报修项目的ID  1:评分  2:评价(选填)
         * @return 执行状态
         * @throws Exception 执行过程中的异常
         */
        private boolean getRepireList(String[] datas) throws Exception {
            RequestBody requestBody;
            if (datas.length == 3) {
                requestBody = new FormEncodingBuilder().add("serviceID", datas[0]).add("evaluateResault", datas[1]).add("evaluateReson", datas[2]).build();
            } else {
                requestBody = new FormEncodingBuilder().add("serviceID", datas[0]).add("evaluateResault", datas[1]).build();
            }
            Request request = new Request.Builder().post(requestBody).url(URL_request_uploadScoreInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] result2 = JsonDecoder.getStatusJsonData(result);
                if (result2 == null || result2[0].toLowerCase().equals("failure")) {
                    return false;
                } else {
                    return result2[0].toLowerCase().equals("success");
                }
            } else {
                return false;
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUploadRepireScoreCallback.onUploadScoreCompleted();
                }
            });
        }

        /**
         * 失败时调用
         *
         * @param isException 是否为异常
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUploadRepireScoreCallback.onUploadScoreFailed(isException);
                }
            });
        }

    }

    /**
     * 获取用户信息
     */
    private class GetUserInfo implements Callable<String> {
        public static final String TAG = "getUserInfo";

        private String phoneNumber;
        private OnGetUserInfoCallback onGetUserInfo;

        GetUserInfo(OnGetUserInfoCallback onGetUserInfo, String phoneNumber) {
            this.phoneNumber = phoneNumber;
            this.onGetUserInfo = onGetUserInfo;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            UserBean userBean;

            try {
                userBean = getUserInfo(phoneNumber);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (userBean != null) {
                onCompleted(userBean);
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取用户信息
         *
         * @param username 用户名，作为查找的依据 (LoginID)
         * @return 用户信息Bean
         */
        UserBean getUserInfo(String username) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder().add("loginID", username).build();
            Request request = new Request.Builder().post(requestBody).url(URL_userInfoInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "request user_info successful");
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getUserJsonData(result);
            } else {
                Log.d(TAG, "request user_info failed");
                return null;
            }
        }

        /**
         * 成功时调用
         *
         * @param userBean 获取到的用户信息
         */
        private void onCompleted(final UserBean userBean) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetUserInfo.onCompleted(userBean);
                }
            });
        }

        /**
         * 失败时调用
         *
         * @param isException 是否为异常
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetUserInfo.onFailed(isException);
                }
            });
        }

    }

    /**
     * 注册新用户
     */
    private class RegisterNewUser implements Callable<String> {
        public static final String TAG = "RegisterNewUser";

        private String[] datas;  //0:昵称  1:用户ID  2:密码  3:小区ID  4:栋数ID  5:单元ID  6:房号ID  7:业主姓名  8:业主电话
        private OnRegisterUser onRegisterUser;

        RegisterNewUser(String[] datas, OnRegisterUser onRegisterUser) {
            this.datas = datas;
            this.onRegisterUser = onRegisterUser;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }
            boolean result;

            try {
                result = register(datas);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        boolean register(String[] datas) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("name", datas[0])
                    .add("loginID", datas[1])
                    .add("loginPSW", datas[2])
                    .add("communityID", datas[3])
                    .add("apartment", datas[4])
                    .add("unit", datas[5])
                    .add("room", datas[6])
                    .add("proprietorName", datas[7])
                    .add("proprietorTel", datas[8])
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_userRegisterInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] responseDatas = JsonDecoder.getStatusJsonData(result);
                return responseDatas != null && responseDatas[0].toLowerCase().equals("success");
            } else {
                return false;
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onRegisterUser.onCompleted();
                }
            });
        }

        /**
         * 失败时调用
         *
         * @param isException 是否为异常
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onRegisterUser.onFailed(isException);
                }
            });
        }

    }

    /**
     * 检测是否有新用户
     */
    private class CheckUserExist implements Callable<String> {
        public final static String TAG = "CheckUserExist";

        private String checkID;
        private OnCheckUser onCheckUser;

        CheckUserExist(String checkID, OnCheckUser onCheckUser) {
            this.checkID = checkID;
            this.onCheckUser = onCheckUser;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = check(checkID);
            } catch (Exception e) {
                onFailed();
                return null;
            }

            onCompleted(result);

            return null;
        }

        private boolean check(String checkID) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("loginID", checkID)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_userIsExistInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] responseDatas = JsonDecoder.getStatusJsonData(result);
                if (responseDatas != null && responseDatas[0].toLowerCase().equals("esist")) {
                    return true;
                } else if (responseDatas != null && responseDatas[0].toLowerCase().equals("non-esist")) {
                    return false;
                } else {
                    throw new Exception("error");
                }
            } else {
                throw new Exception("error");
            }
        }

        /**
         * 成功时调用
         *
         * @param isExist 用户是否存在
         */
        private void onCompleted(final boolean isExist) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onCheckUser.onCompleted(checkID, isExist);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onCheckUser.onFailed();
                }
            });
        }

    }

    /**
     * 获取缴费项目数据列表
     */
    private class GetPaymentList implements Callable<String> {
        public final static String TAG = "GetPaymentList";

        private OnGetPaymentListCallback onGetPaymentListCallback;
        private ArrayList<PaymentBean> paymentList;
        private String roomID;

        GetPaymentList(OnGetPaymentListCallback onGetPaymentListCallback, String roomID) {
            this.onGetPaymentListCallback = onGetPaymentListCallback;
            this.paymentList = new ArrayList<>();
            this.roomID = roomID;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = getPaymentList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取缴费项目列表
         *
         * @return 获取是否成功
         * @throws Exception 中途抛出的异常
         */
        private boolean getPaymentList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder().add("roomID", roomID).build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_paymentListInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getPaymentListDatas(result, paymentList);
            } else {
                return false;
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetPaymentListCallback.onGotList(paymentList);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetPaymentListCallback.onFailed(isException);
                }
            });
        }

    }

    /**
     * 上传用户头像
     */
    private class UploadUserHead implements Callable<String> {
        public final static String TAG = "UploadUserHead";
        private final String IMAGE_SAVEPATH = Environment.getExternalStorageDirectory().getPath() + "/Pictures";
        private OnUploadUserHeadCallback onUploadUserHeadCallback;
        private String url;
        private String userID;
        private Bitmap bitmap;

        UploadUserHead(OnUploadUserHeadCallback onUploadUserHeadCallback, String userID, Bitmap bitmap) {
            this.onUploadUserHeadCallback = onUploadUserHeadCallback;
            this.userID = userID;
            this.bitmap = bitmap;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = uploadImage(userID, bitmap);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result && !TextUtils.isEmpty(url)) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 上传用户头像
         *
         * @param bitmap 要上传的 Bitmap
         * @param userID 用户的ID
         * @return 执行结果
         * @throws Exception 运行时抛出的异常
         */
        private boolean uploadImage(String userID, Bitmap bitmap) throws Exception {
            Log.d(TAG, "USER ID:" + userID);
            File file = saveBitmap2File(bitmap);
            if (file != null) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

                RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
                        .addFormDataPart("proprietorID", userID)
                        .addFormDataPart("Filedata", "test.jpg", fileBody)
                        .build();
                Request request = new Request.Builder().post(requestBody).url(URL_request_uploadHeadImageInterface).build();

                Response response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    response.body().close();
                    String[] datas = JsonDecoder.getStatusJsonData(result);
                    if (datas != null && datas[0].toLowerCase().equals("success")) {
                        //处理头像地址
                        url = datas[1];
                        try {
                            url = url.replaceAll("Upload", "Upload/");
                        } catch (Exception e) {
                            //在处理字符的过程中产生异常
                            url = null;
                            return false;
                        }
                        url = BASE_URL + "" + url;
                        Log.d("用户头像地址", "" + url);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        /**
         * 将Bitmap对象保存成文件
         *
         * @param bitmap Bitmap对象
         * @return 文件对象 , 失败返回NULL
         */
        private
        @Nullable
        File saveBitmap2File(Bitmap bitmap) {
            File dir = new File(IMAGE_SAVEPATH);
            dir.mkdirs();
            dir = new File(IMAGE_SAVEPATH + "/UserHead.jpg");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(IMAGE_SAVEPATH + "/UserHead.jpg");
                boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                if (!result) {
                    return null;
                } else {
                    return dir;
                }
            } catch (FileNotFoundException e) {
                return null;
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUploadUserHeadCallback.onUploaded(bitmap, url);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUploadUserHeadCallback.onUploadFailed(isException);
                }
            });
        }

    }

    /**
     * 获取缴费信息列表
     */
    private class GetPayRecordList implements Callable<String> {
        public final static String TAG = "GetPayRecordList";
        ArrayList<PayRecordBean> payRecordBeen;
        String roomID;
        String pageSize, pageIndex;
        private OnGetPayRecordCallback onGetPayRecordCallback;

        GetPayRecordList(OnGetPayRecordCallback onGetPayRecordCallback, String pageIndex, String pageSize, String roomID) {
            this.onGetPayRecordCallback = onGetPayRecordCallback;
            this.payRecordBeen = new ArrayList<>();
            this.pageIndex = pageIndex;
            this.pageSize = pageSize;
            this.roomID = roomID;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = getRecordList(roomID, pageSize, pageIndex, payRecordBeen);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取缴费记录列表
         *
         * @param roomID    房间号ID
         * @param pageSize  每一页的数据量
         * @param pageIndex 页码
         * @param source    要导入数据的ArrayList
         * @return 执行结果
         */
        private boolean getRecordList(String roomID, String pageSize, String pageIndex, ArrayList<PayRecordBean> source) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("roomID", roomID)
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_payRecordListInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getPayRecordDatas(result, source);
            } else {
                return false;
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (payRecordBeen.size() == 0) {
                        onGetPayRecordCallback.onGotPayRecords(0, 0, roomID, null);
                    } else {
                        onGetPayRecordCallback.onGotPayRecords(payRecordBeen.size(), Integer.parseInt(payRecordBeen.get(0).getTotalCount()), roomID, payRecordBeen);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetPayRecordCallback.onGotPayRecordsFailed(isException);
                }
            });
        }

    }

    /**
     * 按月份 年份 获取缴费记录
     */
    private class GetPayRecordByMonth implements Callable<String> {
        public final static String TAG = "GetPayRecordByMonth";
        ArrayList<PayRecordBean> payRecordBeen;
        private OnGetPayRecordByMonthCallback onGetPayRecordByMonthCallback;
        private String roomID, month, year, pageSize, pageIndex;

        GetPayRecordByMonth(OnGetPayRecordByMonthCallback onGetPayRecordByMonthCallback, String roomID, String month, String year, String pageSize, String pageIndex) {
            this.onGetPayRecordByMonthCallback = onGetPayRecordByMonthCallback;
            this.payRecordBeen = new ArrayList<>();
            this.roomID = roomID;
            this.month = month;
            this.year = year;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = getRecordList(roomID, pageSize, pageIndex, month, year, payRecordBeen);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取缴费记录列表
         *
         * @param roomID    房间号ID
         * @param pageSize  每一页的数据量
         * @param pageIndex 页码
         * @param source    要导入数据的ArrayList
         * @return 执行结果
         */
        private boolean getRecordList(String roomID, String pageSize, String pageIndex, String month, String year, ArrayList<PayRecordBean> source) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("roomID", roomID)
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .add("month", month)
                    .add("year", year)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_payRecordListByMonthInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getPayRecordDatas(result, source);
            } else {
                return false;
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (payRecordBeen.size() == 0) {
                        onGetPayRecordByMonthCallback.onGotPayRecords(0, 0, roomID, month, year, null);
                    } else {
                        onGetPayRecordByMonthCallback.onGotPayRecords(payRecordBeen.size(), Integer.parseInt(payRecordBeen.get(0).getTotalCount()), roomID, month, year, payRecordBeen);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetPayRecordByMonthCallback.onGotPayRecordsByMonthFailed(isException);
                }
            });
        }

    }

    /**
     * 获取某项缴费项目当前余额
     */
    private class GetSumBalance implements Callable<String> {
        public final static String TAG = "GetSumBalance";

        private OnGetSumBalanceCallback onGetSumBalanceCallback;
        private String roomID;
        private String chargeTypeID;

        GetSumBalance(OnGetSumBalanceCallback onGetSumBalanceCallback, String roomID, String chargeTypeID) {
            this.onGetSumBalanceCallback = onGetSumBalanceCallback;
            this.roomID = roomID;
            this.chargeTypeID = chargeTypeID;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = getSumBalance(roomID, chargeTypeID);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        private boolean getSumBalance(String roomID, String chargeTypeID) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("roomID", roomID)
                    .add("chargeTypeID", chargeTypeID)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_getSumBalanceInterface).build();
            Response response = httpClient.newCall(request).execute();
            return response.isSuccessful();
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetSumBalanceCallback.onGotSumBalance();
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetSumBalanceCallback.onGotSumBalanceFailed(isException);
                }
            });
        }

    }

    /**
     * 上传订单号
     */
    private class UploadPayment implements Callable<String> {
        public final static String TAG = "UploadPayment";

        private OnUploadPaymentCallback onUploadPaymentCallback;
        private String roomID;
        private String chargeTypeID;
        private String chargeSum;
        private String orderNo;
        private String useScore;
        private String userTel;

        UploadPayment(OnUploadPaymentCallback onUploadPaymentCallback, String roomID, String chargeTypeID, String chargeSum, String orderNo, String useScore, String userTel) {
            this.onUploadPaymentCallback = onUploadPaymentCallback;
            this.roomID = roomID;
            this.chargeTypeID = chargeTypeID;
            this.chargeSum = chargeSum;
            this.orderNo = orderNo;
            this.useScore = useScore;
            this.userTel = userTel;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = upload(roomID, chargeTypeID, chargeSum, orderNo,useScore, userTel);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 上传缴费订单
         *
         * @param roomID       房间ID
         * @param chargeTypeID 缴费类型
         * @param chargeSum    缴费金额
         * @param orderNo      订单号
         * @return 执行成功或失败
         * @throws Exception 执行时抛出的异常
         */
        private boolean upload(String roomID, String chargeTypeID, String chargeSum, String orderNo,String useScore,String userTel) throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("roomID", roomID)
                    .add("chargeTypeID", chargeTypeID)
                    .add("chargeSum", chargeSum)
                    .add("orderNo", orderNo)
                    .add("score", useScore)
                    .add("userTel",userTel)
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_uploadPaymentInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] data = JsonDecoder.getStatusJsonData(result);
                if (data != null && data[0].toLowerCase().equals("sucess")) {
                    this.orderNo = data[1];
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            System.out.println("订单号  " + orderNo + "房间号  " + roomID + "缴费金额  " + chargeSum + "  缴费类型  " + chargeTypeID);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUploadPaymentCallback.onUploadCompleted(roomID, chargeTypeID, chargeSum, orderNo);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUploadPaymentCallback.onUploadFailed(isException, roomID, chargeTypeID, chargeSum, orderNo);
                }
            });
        }

    }

    /**
     * 查询订单号是否存在
     */
    private class CheckPaymentExist implements Callable<String> {
        public final static String TAG = "CheckPaymentExist";

        private boolean isExist;

        private OnCheckPaymentCallback onCheckPaymentCallback;
        private String orderNo;

        CheckPaymentExist(OnCheckPaymentCallback onCheckPaymentCallback, String orderNo) {
            this.onCheckPaymentCallback = onCheckPaymentCallback;
            this.orderNo = orderNo;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = check(orderNo);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 查询订单号是否存在
         *
         * @param orderNo 要查询的订单号
         * @return 查询是否成功
         * @throws Exception 查询时产生的异常
         */
        private boolean check(String orderNo) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("orderNo", orderNo)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_checkPaymentInterface).build();
            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] datas = JsonDecoder.getStatusJsonData(result);
                isExist = datas != null && datas[0].toLowerCase().equals("exsist");
                return true;
            } else {
                return false;
            }

        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onCheckPaymentCallback.onCheckedResult(isExist);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onCheckPaymentCallback.onCheckFailed(isException);
                }
            });
        }

    }

    /**
     * 获取  我献爱心  数据列表  11
     */
    private class GetHelpingList implements Callable<String> {
        public final static String TAG = "GetHelpingList";
        private final static String parentID = "11";

        ArrayList<ArtclesBean> artclesBeen;
        String pageSize, pageIndex, proprietorID;
        OnGetHelpingListCallback onGetHelpingListCallback;

        GetHelpingList(OnGetHelpingListCallback onGetHelpingListCallback, String pageSize, String pageIndex, String proprietorID) {
            this.onGetHelpingListCallback = onGetHelpingListCallback;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.proprietorID = proprietorID;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (artclesBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .add("ParentID", parentID)
                    .add("ProprietorID", proprietorID)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_readCommonInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                artclesBeen = JsonDecoder.getArtclesJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (artclesBeen.size() > 0) {
                        onGetHelpingListCallback.onGotHelpingList(pageSize, artclesBeen.get(0).getTotalArtclesCount(), pageIndex, artclesBeen);
                    } else {
                        onGetHelpingListCallback.onGotHelpingList(pageSize, 0, pageIndex, artclesBeen);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetHelpingListCallback.onGotHelpingFailed(isException);
                }
            });
        }

    }

    /**
     * 获取  我的困难  数据列表  12
     */
    private class GetAskingList implements Callable<String> {
        public final static String TAG = "GetAskingList";
        private final static String parentID = "12";

        ArrayList<ArtclesBean> artclesBeen;
        String pageSize, pageIndex, proprietorID;
        OnGetAskingListCallbacak onGetAskingListCallbacak;

        GetAskingList(OnGetAskingListCallbacak onGetAskingListCallbacak, String pageSize, String pageIndex, String proprietorID) {
            this.onGetAskingListCallbacak = onGetAskingListCallbacak;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.proprietorID = proprietorID;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (artclesBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .add("ParentID", parentID)
                    .add("ProprietorID", proprietorID)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_readCommonInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                artclesBeen = JsonDecoder.getArtclesJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (artclesBeen.size() > 0) {
                        onGetAskingListCallbacak.onGotAskingList(pageSize, artclesBeen.get(0).getTotalArtclesCount(), pageIndex, artclesBeen);
                    } else {
                        onGetAskingListCallbacak.onGotAskingList(pageSize, 0, pageIndex, artclesBeen);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetAskingListCallbacak.onGotAskingFailed(isException);
                }
            });
        }

    }

    /**
     * 获取  公益展示  数据列表  4
     */
    private class GetContribuList implements Callable<String> {
        public final static String TAG = "GetContribuList";
        private final static String parentID = "4";

        ArrayList<ArtclesBean> artclesBeen;
        String pageSize, pageIndex, communityID;
        OnGetContribuListCallbacak onGetContribuListCallbacak;

        GetContribuList(OnGetContribuListCallbacak onGetContribuListCallbacak, String pageSize, String pageIndex) {
            this.onGetContribuListCallbacak = onGetContribuListCallbacak;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (artclesBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .add("ParentID", parentID)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_readCommonInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                artclesBeen = JsonDecoder.getArtclesJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (artclesBeen.size() > 0) {
                        onGetContribuListCallbacak.onGotContribuList(pageSize, artclesBeen.get(0).getTotalArtclesCount(), pageIndex, artclesBeen);
                    } else {
                        onGetContribuListCallbacak.onGotContribuList(pageSize, 0, pageIndex, artclesBeen);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetContribuListCallbacak.onGotContribuFailed(isException);
                }
            });
        }

    }

    /**
     * 获取  服务投诉  数据列表  13
     */
    private class GetQuestionList implements Callable<String> {
        public final static String TAG = "GetQuestionList";
        private final static String parentID = "13";

        ArrayList<ArtclesBean> artclesBeen;
        String pageSize, pageIndex, communityID;
        OnGetQuestionListCallbacak onGetQuestionListCallbacak;

        GetQuestionList(OnGetQuestionListCallbacak onGetQuestionListCallbacak, String pageSize, String pageIndex, String communityID) {
            this.onGetQuestionListCallbacak = onGetQuestionListCallbacak;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.communityID = communityID;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (artclesBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .add("ParentID", parentID)
                    .add("communityID", communityID)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_articleInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                artclesBeen = JsonDecoder.getArtclesJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (artclesBeen.size() > 0) {
                        onGetQuestionListCallbacak.onGotQuestionList(pageSize, artclesBeen.get(0).getTotalArtclesCount(), pageIndex, artclesBeen);
                    } else {
                        onGetQuestionListCallbacak.onGotQuestionList(pageSize, 0, pageIndex, artclesBeen);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetQuestionListCallbacak.onGotQuestionFailed(isException);
                }
            });
        }

    }

    /**
     * 获取  服务求助  数据列表  14
     */
    private class GetServiceList implements Callable<String> {
        public final static String TAG = "GetServiceList";
        private final static String parentID = "14";

        ArrayList<ArtclesBean> artclesBeen;
        String pageSize, pageIndex, communityID;
        OnGetServiceListCallbacak onGetServiceListCallbacak;

        GetServiceList(OnGetServiceListCallbacak onGetServiceListCallbacak, String pageSize, String pageIndex, String communityID) {
            this.onGetServiceListCallbacak = onGetServiceListCallbacak;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.communityID = communityID;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (artclesBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .add("ParentID", parentID)
                    .add("communityID", communityID)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_articleInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                artclesBeen = JsonDecoder.getArtclesJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (artclesBeen.size() > 0) {
                        onGetServiceListCallbacak.onGotServiceList(pageSize, artclesBeen.get(0).getTotalArtclesCount(), pageIndex, artclesBeen);
                    } else {
                        onGetServiceListCallbacak.onGotServiceList(pageSize, 0, pageIndex, artclesBeen);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetServiceListCallbacak.onGotServiceFailed(isException);
                }
            });
        }

    }

    /**
     * 获取  合理化建议  数据列表  15
     */
    private class GetSuggestList implements Callable<String> {
        public final static String TAG = "GetSuggestList";
        private final static String parentID = "15";

        ArrayList<ArtclesBean> artclesBeen;
        String pageSize, pageIndex, communityID;
        OnGetSuggestListCallbacak onGetSuggestListCallbacak;

        GetSuggestList(OnGetSuggestListCallbacak onGetSuggestListCallbacak, String pageSize, String pageIndex, String communityID) {
            this.onGetSuggestListCallbacak = onGetSuggestListCallbacak;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.communityID = communityID;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (artclesBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .add("ParentID", parentID)
                    .add("communityID", communityID)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_articleInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                artclesBeen = JsonDecoder.getArtclesJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (artclesBeen.size() > 0) {
                        onGetSuggestListCallbacak.onGotSuggestList(pageSize, artclesBeen.get(0).getTotalArtclesCount(), pageIndex, artclesBeen);
                    } else {
                        onGetSuggestListCallbacak.onGotSuggestList(pageSize, 0, pageIndex, artclesBeen);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetSuggestListCallbacak.onGotSuggestFailed(isException);
                }
            });
        }

    }

    /**
     * 获取  物业服务  数据列表  16
     */
    private class GetPropertyList implements Callable<String> {
        public final static String TAG = "GetPropertyList";
        private final static String parentID = "16";

        ArrayList<ArtclesBean> artclesBeen;
        String pageSize, pageIndex, communityID;
        OnGetPropertyListCallbacak onGetPropertyListCallbacak;

        GetPropertyList(OnGetPropertyListCallbacak onGetPropertyListCallbacak, String pageSize, String pageIndex, String communityID) {
            this.onGetPropertyListCallbacak = onGetPropertyListCallbacak;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.communityID = communityID;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (artclesBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .add("ParentID", parentID)
                    .add("communityID", communityID)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_articleInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                artclesBeen = JsonDecoder.getArtclesJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (artclesBeen.size() > 0) {
                        onGetPropertyListCallbacak.onGotPropertyList(pageSize, artclesBeen.get(0).getTotalArtclesCount(), pageIndex, artclesBeen);
                    } else {
                        onGetPropertyListCallbacak.onGotPropertyList(pageSize, 0, pageIndex, artclesBeen);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetPropertyListCallbacak.onGotPropertyFailed(isException);
                }
            });
        }

    }

    /**
     * 获取  便民服务  数据列表  17
     */
    private class GetServicePeopleList implements Callable<String> {
        public final static String TAG = "GetServicePeopleList";
        private final static String parentID = "17";

        ArrayList<ArtclesBean> artclesBeen;
        String pageSize, pageIndex, communityID;
        OnGetServicePropleListCallbacak onGetServicePropleListCallbacak;

        GetServicePeopleList(OnGetServicePropleListCallbacak onGetServicePropleListCallbacak, String pageSize, String pageIndex) {
            this.onGetServicePropleListCallbacak = onGetServicePropleListCallbacak;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (artclesBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .add("ParentID", parentID)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_readCommonInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                
                response.body().close();
                artclesBeen = JsonDecoder.getArtclesJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (artclesBeen.size() > 0) {
                        onGetServicePropleListCallbacak.onGotServicePropleList(pageSize, artclesBeen.get(0).getTotalArtclesCount(), pageIndex, artclesBeen);
                    } else {
                        onGetServicePropleListCallbacak.onGotServicePropleList(pageSize, 0, pageIndex, artclesBeen);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetServicePropleListCallbacak.onGotServicePropleFailed(isException);
                }
            });
        }

    }

    /**
     * 获取   维修人员    数据列表
     */
    private class GetRepireList implements Callable<String> {
        public final static String TAG = "GetRepireList";

        private OnGetRepireDataCallback onGetRepireDataCallback;
        private String status;
        private String tel;
        private String pageSize;
        private String pageIndex;

        GetRepireList(OnGetRepireDataCallback onGetRepireDataCallback, String status, String tel, String pageSize, String pageIndex) {
            this.onGetRepireDataCallback = onGetRepireDataCallback;
            this.status = status;
            this.tel = tel;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            ArrayList<RepireBean> repireBeen;

            try {
                Log.d(TAG, "获取维修工单数据中...");
                repireBeen = getData(status, tel, pageSize, pageIndex);
                Log.d(TAG, "获取维修工单图像...");
                getPictureUrl(repireBeen);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            onCompleted(repireBeen);

            return null;
        }

        /**
         * 开始获取数据
         *
         * @param status    要获取的状态
         * @param tel       维修人员手机号码
         * @param pageSize  请求的页面数据量
         * @param pageIndex 请求的页面页码
         * @return 得到的数据列表, 没有数据或解析失败返回NULL
         * @throws Exception 获取时发生的异常
         */
        private
        @Nullable
        ArrayList<RepireBean> getData(String status, String tel, String pageSize, String pageIndex) throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("tel", tel)
                    .add("treated", status)
                    .add("PageSize", pageSize)
                    .add("PageIndex", pageIndex)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_getRepireListInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                return JsonDecoder.getRepireList(result);
            } else {
                return null;
            }
        }

        /**
         * 获取每一个id对应的图片地址
         *
         * @param repireBeen 要获取的数组
         */
        private void getPictureUrl(ArrayList<RepireBean> repireBeen) throws Exception {
            if (repireBeen != null && repireBeen.size() > 0) {
                //循环遍历每个项目的图片id , 依次获取对应的图片地址
                for (int i = 0; i < repireBeen.size(); i++) {
                    RepireBean bean = repireBeen.get(i);
                    String[] ids = bean.getPicturesID();

                    if (ids != null) {
                        //如果有图片id , 则开始请求数据
                        for (int k = 0; k < ids.length; k++) {
                            if (!TextUtils.isEmpty(ids[k])) {
                                Log.d(TAG, "正在获取工单ID:" + bean.getID() + "  图片ID:" + ids[k]);
                                String[] picUrl = requestPictureByIDs(ids[k]);
                                if (picUrl != null) {
                                    bean.getPicturesThu()[k] = picUrl[0];
                                    bean.getPictures()[k] = picUrl[1];
                                }
                            }
                        }
                    }
                }

            }
        }

        /**
         * 网络获取图片地址
         *
         * @param id 图片id
         * @return 图片地址数据  0:图片缩略图    1:图片大图
         */
        private String[] requestPictureByIDs(String id) throws Exception {
            RequestBody body = new FormEncodingBuilder()
                    .add("ID", id)
                    .build();
            Request request = new Request.Builder()
                    .post(body)
                    .url(URL_request_getRepirePictureInterface)
                    .build();
            try {
                Response response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    response.body().close();
                    return JsonDecoder.getPicturesUrl(result);
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }

        private void onFailed() {
            threadExecutor.removeTag(TAG + status);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetRepireDataCallback.onFailed();
                }
            });
        }

        private void onCompleted(final ArrayList<RepireBean> repireBeen) {
            threadExecutor.removeTag(TAG + status);
            if (status.equals("1")) {
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (repireBeen != null) {
                            onGetRepireDataCallback.onGotRepiredData(repireBeen, repireBeen.get(0).getTotalCount());
                        } else {
                            onGetRepireDataCallback.onGotRepiredData(null, 0);
                        }
                    }
                });
            } else {
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (repireBeen != null) {
                            onGetRepireDataCallback.onGotUnRepiredData(repireBeen, repireBeen.get(0).getTotalCount());
                        } else {
                            onGetRepireDataCallback.onGotUnRepiredData(null, 0);
                        }
                    }
                });
            }
        }

    }

    /**
     * 更改维修工单状态
     */
    private class ChangeRepireStatus implements Callable<String> {
        public final static String TAG = "ChangeRepireStatus";

        private String tel;
        private String serviceID;
        private String status;
        private String[] files;
        private OnChangeRepireStatus onChangeRepireStatus;

        ChangeRepireStatus(OnChangeRepireStatus onChangeRepireStatus, String tel, String serviceID, String status, @Nullable String[] files) {
            this.onChangeRepireStatus = onChangeRepireStatus;
            this.tel = tel;
            this.serviceID = serviceID;
            this.status = status;
            this.files = files;
        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            if (status.equals("0") && files == null && !checkPhotos(files)) {
                //如果是确认维修 , 但是没有图像上传 , 则这次操作作为失败处理
                onFailed();
            } else if (status.equals("1")) {
                //如果单纯是开始受理 , 则不需要上传图像
                try {
                    if (changeStatus(serviceID, tel, status)) {
                        onCompleted();
                    } else {
                        onFailed();
                    }
                } catch (Exception e) {
                    onFailed();
                }
            } else if (status.equals("0")) {
                //如果完成维修 , 则上传数据 , 然后上传图像
                try {
                    if (changeStatus(serviceID, tel, status)) {
                        updatePhotos(files, serviceID);
                        onCompleted();
                    } else {
                        onFailed();
                    }
                } catch (Exception e) {
                    onFailed();
                }

            } else {
                onFailed();
            }

            return null;
        }

        /**
         * 开始更改数据
         *
         * @param serviceID 服务ID
         * @param tel       电话号码
         * @param status    要更改成的状态
         * @return 执行是否成功
         * @throws Exception 执行中抛出的异常
         */
        private boolean changeStatus(String serviceID, String tel, String status) throws Exception {
            Log.d(TAG, "维修人员手机号:" + tel);
            RequestBody body = new FormEncodingBuilder()
                    .add("tel", tel)
                    .add("serviceID", serviceID)
                    .add("status", status)
                    .build();
            Request request = new Request.Builder().post(body).url(URL_request_changeRepireStatusInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] result2 = JsonDecoder.getStatusJsonData(result);
                if (result2 == null || result2[0].toLowerCase().equals("failure")) {
                    return false;
                } else {
                    return result2[0].toLowerCase().equals("success");
                }
            } else {
                return false;
            }
        }

        /**
         * 循环上传图片附件
         *
         * @param files 图片的地址数组
         * @param ID    得到的ID
         * @return 是否所有图片都上传成功
         * @throws Exception 上传中抛出的异常
         */
        private boolean updatePhotos(String[] files, String ID) throws Exception {
            boolean result = false;
            for (String path : files) {
                File file = new File(path);
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
                        .addFormDataPart("serviceID", ID)
                        .addFormDataPart("status", "0")
                        .addFormDataPart("Filedata", "test.jpg", fileBody)
                        .build();
                Request request = new Request.Builder().post(requestBody).url(URL_request_uploadRepirePhotosInterface).build();
                Response response = httpClient.newCall(request).execute();
                Log.d(TAG, "updatePhotos:   " + response.body().string());
                result = response.isSuccessful();
            }
            return result;
        }

        /**
         * 检查图像文件是否可用
         *
         * @param files 图像文件的路径数组
         * @return 是否可用
         */
        private boolean checkPhotos(String[] files) {
            for (String path : files) {
                File file = new File(path);
                if (!file.exists() || !file.canRead()) {
                    return false;
                }
            }
            return true;
        }

        private void onFailed() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onChangeRepireStatus.onFailed();
                }
            });
        }

        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onChangeRepireStatus.onCompleted();
                }
            });
        }

    }


    /**
     * 进行签到
     */
    private class UpSign implements Callable<String> {
        public final static String TAG = "UpSign";

        private OnUpSignCallback onUpSignCallback;
        private String userTel;
        private String date;
        private String massage;
        private String score;


        UpSign(OnUpSignCallback onUpSignCallback, String userTel, String date) {
            this.onUpSignCallback = onUpSignCallback;
            this.userTel = userTel;
            this.date = date;

        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = upSignApply(userTel, date);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 申请签到操作
         *
         * @param userTel      用户手机号
         * @param date         签到日期
         * @return 执行成功或失败
         * @throws Exception 执行时抛出的异常
         */
        private boolean upSignApply(String userTel, String date) throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("userTel", userTel)
                    .add("date", date)
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_upSignInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] data = JsonDecoder.upSignData(result);
                if (data != null) {
                    this.massage = data[0];
                    this.score=data[1];
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            System.out.println("userTel  " + userTel + "date  " + date + "massage  " + massage );
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpSignCallback.onUpSignCompleted(userTel, date, massage, score);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpSignCallback.onUpSignFailed(isException, userTel, date);
                }
            });
        }

    }

    /**
     * 获取签到历史
     */
    private class GetSignHistory implements Callable<String> {
        public final static String TAG = "GetSignHistory";

        private OnGetSignHistoryCallback onGetSignHistoryCallback;
        private String userTel;
        private List<String> dateList;
        private String massage;


        GetSignHistory(OnGetSignHistoryCallback onGetSignHistoryCallback, String userTel) {
            this.onGetSignHistoryCallback = onGetSignHistoryCallback;
            this.userTel = userTel;


        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = getHistoryApply(userTel);
            } catch (Exception e) {
                e.printStackTrace();
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 申请获取签到历史操作
         *
         * @param userTel      用户手机号
         *
         * @return 执行成功或失败
         * @throws Exception 执行时抛出的异常
         */
        private boolean getHistoryApply(String userTel) throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("userTel", userTel)
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_getSignHistoryInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                dateList = JsonDecoder.getSignHistoryData(result);
                return  true;
            } else {
                return false;
            }

        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            System.out.println("userTel  " + userTel + "date  " + dateList  );
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetSignHistoryCallback.onGetSignHistoryCompleted(userTel, dateList);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetSignHistoryCallback.onGetSignHistoryFailed(isException, userTel);
                }
            });
        }

    }

    /**
     * 获取积分
     */
    private class GetSignScore implements Callable<String> {
        public final static String TAG = "GetSignScore";

        private OnGetSignScoreCallback onGetSignScore;
        private String userTel;
        private String score;


        GetSignScore(OnGetSignScoreCallback onGetSignScore, String userTel) {
            this.onGetSignScore = onGetSignScore;
            this.userTel = userTel;


        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = getScore(userTel);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         *获取积分操作
         *
         * @param userTel      用户手机号
         *
         * @return 执行成功或失败
         * @throws Exception 执行时抛出的异常
         */
        private boolean getScore(String userTel) throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("userTel", userTel)
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_getSignScoreInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String data = JsonDecoder.getScoreData(result);
                if (data != null) {
                    this.score=data;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            System.out.println("userTel  " + userTel + "data  " + score );
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                   onGetSignScore.onGetSignScoreCompleted(userTel,score);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetSignScore.onGetSignScoreFailed(isException,userTel);
                }
            });
        }

    }

    /**
     * 进行点赞操作
     */
    private class UpLike implements Callable<String> {
        public final static String TAG = "UpLike";

        private OnUpLikeCallback onUpLikeCallback;
        private String userTel;
        private String scoreType= "文章点赞";
        private String massage;
        private String score;
        String detail;


        UpLike(OnUpLikeCallback onUpLikeCallback, String userTel,  String detail) {
            this.onUpLikeCallback=onUpLikeCallback;
            this.userTel = userTel;
            this.detail=detail;

        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = upLikeApply(userTel,scoreType,detail);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 申请点赞操作
         *
         * @param userTel      用户手机号
         * @param scoreType    申请类型
         * @param detail       申请详情（文章标题）
         * @return score       点赞所得积分
         * @return isSuccessed 操作是否成功
         * @throws Exception 执行时抛出的异常
         */
        private boolean upLikeApply(String userTel, String scoreType, String detail) throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("userTel", userTel)
                    .add("scoreType", scoreType)
                    .add("detail", detail)
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_getMultitypeScoreInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] data = JsonDecoder.upLikeData(result);
                if (data != null) {
                    this.massage = data[0];
                    this.score=data[1];
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            System.out.println("userTel  " + userTel +  "massage  " + massage + "score" + score);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpLikeCallback.onUpLikeCompleted(userTel, massage, score);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpLikeCallback.onUpLikeFailed(isException, userTel);
                }
            });
        }

    }

    /**
     * 获取  商家列表
     */
    private class GetBusinessList implements Callable<String> {
        public final static String TAG = "GetBusinessList";


        ArrayList<BulletinBean> bulletinBeen;
        int pageSize, pageIndex;
        OnGetBusinessCallback onGetBusinessCallback;

        GetBusinessList(OnGetBusinessCallback onGetBusinessCallback, int pageSize, int pageIndex) {
            this.onGetBusinessCallback = onGetBusinessCallback;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;

        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (bulletinBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("pageSize", pageSize+"")
                    .add("pageIndex", pageIndex+"")
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_getBusinessScoreInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                bulletinBeen = JsonDecoder.getBusinessJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (bulletinBeen.size() > 0) {
                        onGetBusinessCallback.onGetBusinessCompleted(pageSize, pageIndex, bulletinBeen, bulletinBeen.get(0).getTotalCount());
                    } else {
                        onGetBusinessCallback.onGetBusinessCompleted(pageSize, pageIndex, bulletinBeen, 0);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetBusinessCallback.onGetBusinessFailed(isException);
                }
            });
        }

    }



    /**
     * 获取  周边列表
     */
    private class GetCircumList implements Callable<String> {
        public final static String TAG = "GetCircumList";


        ArrayList<BulletinBean> bulletinBeen;
        int pageSize, pageIndex;
        OnGetCircumCallback onGetCircumCallback;

        GetCircumList(OnGetCircumCallback onGetCircumCallback, int pageSize, int pageIndex) {
            this.onGetCircumCallback = onGetCircumCallback;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;

        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (bulletinBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("pageSize", pageSize+"")
                    .add("pageIndex", pageIndex+"")
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_getCircumScoreInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                bulletinBeen = JsonDecoder.getCircumJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (bulletinBeen.size() > 0) {
                        onGetCircumCallback.onGetCircumCompleted(pageSize, pageIndex, bulletinBeen, bulletinBeen.get(0).getTotalCount());
                    } else {
                        onGetCircumCallback.onGetCircumCompleted(pageSize, pageIndex, bulletinBeen, 0);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetCircumCallback.onGetCircumFailed(isException);
                }
            });
        }

    }


    /**
     * 获取  预约历史列表
     */
    private class GetOrderList implements Callable<String> {
        public final static String TAG = "GetOrderList";


        ArrayList<BulletinBean> bulletinBeen;
        int pageSize, pageIndex;
        String userTel;
        OnGetOrderCallback onGetOrderCallback;

        GetOrderList(OnGetOrderCallback onGetOrderCallback, int pageSize, int pageIndex, String userTel) {
            this.onGetOrderCallback=onGetOrderCallback;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.userTel=userTel;

        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (bulletinBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("pageSize", pageSize+"")
                    .add("pageIndex", pageIndex+"")
                    .add("userTel", userTel)
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_getOrderListScoreInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                bulletinBeen = JsonDecoder.getOrderJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (bulletinBeen.size() > 0) {
                        onGetOrderCallback.onGetOrderCompleted(pageSize, pageIndex, bulletinBeen, bulletinBeen.get(0).getTotalCount());
                    } else {
                        onGetOrderCallback.onGetOrderCompleted(pageSize, pageIndex, bulletinBeen, 0);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetOrderCallback.onGetOrderFailed(isException);
                }
            });
        }

    }

    /**
     * 获取  商家详情图片列表
     */
    private class GetBusinessImage implements Callable<String> {
        public final static String TAG = "GetBusinessImage";


        ArrayList<BusinessImageBean> beanList;
        int businessID;
        OnGetBusinessImageCallback onGetBusinessImageCallback;

        GetBusinessImage(OnGetBusinessImageCallback onGetBusinessImageCallback, int businessID) {
            this.onGetBusinessImageCallback=onGetBusinessImageCallback;
            this.businessID = businessID;


        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (beanList != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("storeID", businessID+"")
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_getBusinessImageInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                beanList = JsonDecoder.getBusinessImageJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                        if(beanList.size()>0) {
                            onGetBusinessImageCallback.onGetBusinessImageCompleted(beanList, beanList.get(0).getTotal());
                        }else {
                            onGetBusinessImageCallback.onGetBusinessImageCompleted(beanList, 1);
                        }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetBusinessImageCallback.onGetBusinessImageFailed(isException);
                }
            });
        }

    }


    /**
     * 获取  商家预设评论列表
     */
    private class GetReplyTab implements Callable<String> {
        public final static String TAG = "GetReplyTab";


        ArrayList<ReplyTabBean> replyTabBeen;
        int businessID;
        int tabNum;
        OnGetReplyTabCallback onGetReplyTabCallback;

        GetReplyTab(OnGetReplyTabCallback onGetReplyTabCallback, int businessID, int tabNum) {
            this.onGetReplyTabCallback=onGetReplyTabCallback;
            this.businessID = businessID;
            this.tabNum = tabNum;


        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (replyTabBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("storeID", businessID+"")
                    .add("preinstallNum", tabNum+"")
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_getReplyTabInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                replyTabBeen = JsonDecoder.getReplyTabJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if(replyTabBeen.size()>0) {
                        onGetReplyTabCallback.onGetReplyTabCompleted(replyTabBeen, replyTabBeen.get(0).getTotal());
                    }else {
                        onGetReplyTabCallback.onGetReplyTabCompleted(replyTabBeen, 0);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetReplyTabCallback.onGetReplyTabFailed(isException);
                }
            });
        }

    }

    /**
     * 获取  详细评论列表
     */
    private class GetReplyList implements Callable<String> {
        public final static String TAG = "GetReplyList";

        ArrayList<BusinessImageBean> imageList;
        ArrayList<BulletinBean> bulletinBeen;
        int pageSize, pageIndex;
        int storeID;
        OnGetReplyListCallback onGetReplyListCallback;

        GetReplyList(OnGetReplyListCallback onGetReplyListCallback, int pageSize, int pageIndex, int storeID) {
            this.onGetReplyListCallback=onGetReplyListCallback;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.storeID=storeID;

        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (bulletinBeen != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("pageSize", pageSize+"")
                    .add("pageIndex", pageIndex+"")
                    .add("storeID", String.valueOf(storeID))
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_getReplyListInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                bulletinBeen = JsonDecoder.getReplyListJsonArray(result);

                for (int i = 0; i <bulletinBeen.size() ; i++) {
                    int commentID = bulletinBeen.get(i).getCommentID();
                    RequestBody requestBody1 = new FormEncodingBuilder()
                            .add("commentID", commentID+"")
                            .build();
                    Request request1 = new Request.Builder().post(requestBody1).url(URL_request_getReplyImageInterface).build();
                    Response response1 = httpClient.newCall(request1).execute();
                    if (response1.isSuccessful()) {
                        final String result1 = response1.body().string();
                        response1.body().close();
                        imageList = JsonDecoder.getReplyImageJsonArray(result1);
                        if(imageList!=null) {
                            if(imageList.size()>0) {
                                bulletinBeen.get(i).setImageList(imageList);
                            }
                        }

                    }
                }

            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (bulletinBeen.size() > 0) {
                        onGetReplyListCallback.onGetReplyListCompleted( bulletinBeen, bulletinBeen.get(0).getTotalCount());
                    } else {
                        onGetReplyListCallback.onGetReplyListCompleted( bulletinBeen, 0);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetReplyListCallback.onGetReplyListFailed(isException);
                }
            });
        }

    }


    /**
     * 提交预约请求
     */
    private class UpOrder implements Callable<String> {
        public final static String TAG = "UpOrder";

        private OnUpOrderCallback onUpOrderCallback;
        private String userTel;
        private int storeID ;
        private String orderTime ;
        private String sendTime ;
        private String orderMessage;
        private String userID;
        private String message;
        private boolean isSuccessed;


        UpOrder(OnUpOrderCallback onUpOrderCallback, int storeID,String  orderTime, String sendTime, String userTel,String orderMessage, String userID) {
            this.onUpOrderCallback = onUpOrderCallback;
            this.userTel = userTel;
            this.storeID = storeID;
            this.orderTime = orderTime;
            this.sendTime = sendTime;
            this.orderMessage = orderMessage;
            this.userID = userID;


        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = upOrderMessage();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         *上传操作
         *
         *
         *
         * @return 执行成功或失败
         * @throws Exception 执行时抛出的异常
         */
        private boolean upOrderMessage() throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("storeID", storeID+"")
                    .add("orderTime", orderTime)
                    .add("sendTime", sendTime)
                    .add("userTel", userTel)
                    .add("orderMessage", orderMessage)
                    .add("userID", userID)
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_upOrderInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                OrderBean orderBean = JsonDecoder.upOrderData(result);
                if (orderBean!= null) {
                        this.isSuccessed = orderBean.isSuccessed();
                        this.message=orderBean.getMessage();
                        return true;

                } else {
                    return false;
                }
            } else {
                return false;
            }

        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                   onUpOrderCallback.onUpOrderCompleted(message,isSuccessed);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpOrderCallback.onUpOrderFailed(isException);
                }
            });
        }

    }



    /**
     * 上传详细评论请求
     */
    private class UpReplyContent implements Callable<String> {
        public final static String TAG = "UpReplyContent";

        private OnUpReplyContentCallback onUpReplyContentCallback;
        private String score;
        private int storeID ;
        private String content;
        private String userID;
        private boolean isSuccessed;
        private String replyTime;
        private int commentID;
        private int orderID;


        UpReplyContent(OnUpReplyContentCallback onUpReplyContentCallback, int storeID, String score, String content, String userID, String replyTime, int orderID) {
            this.onUpReplyContentCallback = onUpReplyContentCallback;
            this.storeID = storeID;
            this.score = score;
            this.content= content;
            this.userID = userID;
            this.replyTime = replyTime;
            this.orderID = orderID;

        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = upOrderMessage();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         *上传操作
         *
         *
         *
         * @return 执行成功或失败
         * @throws Exception 执行时抛出的异常
         */
        private boolean upOrderMessage() throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("storeID", storeID+"")
                    .add("replyScore", score)
                    .add("replyContent", content)
                    .add("userID", userID)
                    .add("replyTime", replyTime)
                    .add("orderID", orderID+"")
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_upReplyContent).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                OrderBean orderBean = JsonDecoder.upReplyContentData(result);
                if (orderBean!= null) {
                    this.isSuccessed = orderBean.isSuccessed();
                    this.commentID=orderBean.getCommentID();
                    return true;

                } else {
                    return false;
                }
            } else {
                return false;
            }

        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpReplyContentCallback.onUpReplyContentCompleted(commentID,isSuccessed);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpReplyContentCallback.onUpReplyContentFailed(isException);
                }
            });
        }

    }

    /**
     * 上传预设评论请求
     */
    private class UpReplyTab implements Callable<String> {
        public final static String TAG = "UpReplyTab";

        private OnUpReplyTabCallback onUpReplyTabCallback;
        private JSONObject jsonObject;
        private JSONArray jsonArray;
        private int storeID ;
        private String message;
        private boolean isSuccessed;


        UpReplyTab(OnUpReplyTabCallback onUpReplyTabCallback, JSONArray jsonArray, int storeID) {
            this.onUpReplyTabCallback = onUpReplyTabCallback;
            this.storeID = storeID;
            this.jsonArray = jsonArray;




        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = upOrderMessage();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         *获取积分操作
         *
         *
         *
         * @return 执行成功或失败
         * @throws Exception 执行时抛出的异常
         */
        private boolean upOrderMessage() throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("storeID", storeID+"")
                    .add("Json", jsonArray+"")
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_upReplyTab).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                OrderBean orderBean = JsonDecoder.upReplyTabData(result);
                if (orderBean!= null) {
                    this.isSuccessed = orderBean.isSuccessed();
                    this.message=orderBean.getMessage();
                    return true;

                } else {
                    return false;
                }
            } else {
                return false;
            }

        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpReplyTabCallback.onUpReplyTabCompleted(message,isSuccessed);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpReplyTabCallback.onUpReplyTabFailed(isException);
                }
            });
        }

    }


    /**
     * 上传评论图片请求
     */
    private class UpReplyPhoto implements Callable<String> {
        public final static String TAG = "UpReplyPhoto";

        private OnUpReplyPhotoCallback onUpReplyPhotoCallback;

        private int commentID ;

        private String[] files;


        UpReplyPhoto(OnUpReplyPhotoCallback onUpReplyPhotoCallback, int commentID, String[] files) {
            this.onUpReplyPhotoCallback = onUpReplyPhotoCallback;
            this.commentID = commentID;
            this.files=files;



        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = updatePhotos(files,commentID);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 循环上传图片附件
         *
         * @param files        图片的地址数组
         * @param commentID    评论id
         * @return 是否所有图片都上传成功
         * @throws Exception 上传中抛出的异常
         */
        private boolean updatePhotos(String[] files, int commentID) throws Exception {
            boolean result = false;
            for (String path : files) {
                File file = new File(path);
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
                        .addFormDataPart("commentID", commentID+"")
                        .addFormDataPart("replyImage", "test.jpg", fileBody)
                        .build();
                Request request = new Request.Builder().post(requestBody).url(URL_request_upReplyPhoto).build();
                Response response = httpClient.newCall(request).execute();
                result = response.isSuccessful();
            }
            return result;
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpReplyPhotoCallback.onUpReplyPhotoCompleted("图片上传成功",true);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpReplyPhotoCallback.onUpReplyPhotoFailed(isException);
                }
            });
        }


    }



    /**
     * 获取  评论列表图片列表
     */
    private class GetReplyImage implements Callable<String> {
        public final static String TAG = "GetReplyImage";


        ArrayList<BusinessImageBean> beanList;
        int commentID;
        int count;
        OnGetReplyImageCallback onGetReplyImageCallback;


        GetReplyImage(OnGetReplyImageCallback onGetReplyImageCallback, int commentID, int cuont) {
            this.onGetReplyImageCallback=onGetReplyImageCallback;
            this.commentID = commentID;
            this.count = cuont;


        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }


            try {
                getList();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (beanList != null) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 获取数据列表
         *
         * @throws Exception 获取中产生的异常
         */
        private void getList() throws Exception {
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("commentID", commentID+"")
                    .build();
            Request request = new Request.Builder().post(requestBody).url(URL_request_getReplyImageInterface).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                beanList = JsonDecoder.getReplyImageJsonArray(result);
            }
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if(beanList.size()>0) {
                        onGetReplyImageCallback.onGetReplyImageCompleted(beanList,count);
                    }
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetReplyImageCallback.onGetReplyImageFailed(isException);
                }
            });
        }

    }


    /**
     * 取消预约请求
     */
    private class CancalOrder implements Callable<String> {
        public final static String TAG = "CancalOrder";

        private OnCancalOrderCallback  onCancalOrderCallback;
        private int orderID;
        private boolean isSuccessed = false;


        CancalOrder(OnCancalOrderCallback onCancalOrderCallback, int orderID) {
            this.onCancalOrderCallback = onCancalOrderCallback;
            this.orderID = orderID;


        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = upOrderMessage();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         *上传操作
         *
         *
         *
         * @return 执行成功或失败
         * @throws Exception 执行时抛出的异常
         */
        private boolean upOrderMessage() throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("ID", orderID+"")
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_cancalOrder).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                isSuccessed = JsonDecoder.cancalOrderData(result);


        }
            return isSuccessed;
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onCancalOrderCallback.onCancalOrderCompleted(orderID,isSuccessed);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onCancalOrderCallback.onCancalOrderFailed(isException);
                }
            });
        }

    }


    /**
     * 获取积分抵消数据
     */
    private class GetScoreDeduct implements Callable<String> {
        public final static String TAG = "GetScoreDeduct";

        private OnGetScoreDeductCallback onGetScoreDeductCallback;
        private String userTel;
        private String money;
        private String restMoney;
        private String useScore;


        GetScoreDeduct(OnGetScoreDeductCallback onGetScoreDeductCallback, String userTel, String money) {
            this.onGetScoreDeductCallback = onGetScoreDeductCallback;
            this.userTel = userTel;
            this.money = money;

        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = GetScoreDeduct(userTel, money);
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         * 申请签到操作
         *
         * @param userTel      用户手机号
         * @param money        用户本次缴费金额
         * @return 执行成功或失败
         * @throws Exception 执行时抛出的异常
         */
        private boolean GetScoreDeduct(String userTel, String money) throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("userTel", userTel)
                    .add("money", money)
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_getScoreDeduct).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                String[] data = JsonDecoder.getScoreDeductData(result);
                if (data != null) {
                    this.useScore = data[0];
                    this.restMoney=data[1];
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetScoreDeductCallback.onGetScoreDeductCompleted(useScore,restMoney);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onGetScoreDeductCallback.onGetScoreDeductFailed(isException);
                }
            });
        }

    }

    /**
     * 消耗积分请求
     */
    private class UpScoreDeduct implements Callable<String> {
        public final static String TAG = "UpScoreDeduct";

        private OnUpScoreDeductCallback onUpScoreDeductCallback;
        private String userTel;
        private String scoreType;
        private String count;
        private boolean isSuccessed = false;


        UpScoreDeduct(OnUpScoreDeductCallback onUpScoreDeductCallback, String userTel, String scoreType, String count) {
            this.onUpScoreDeductCallback = onUpScoreDeductCallback;
            this.userTel = userTel;
            this.scoreType = scoreType;
            this.count = count;


        }

        @Override
        public String call() throws Exception {
            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            boolean result;

            try {
                result = upOrderMessage();
            } catch (Exception e) {
                onFailed(true);
                return null;
            }

            if (result) {
                onCompleted();
            } else {
                onFailed(false);
            }

            return null;
        }

        /**
         *上传操作
         *
         *
         *
         * @return 执行成功或失败
         * @throws Exception 执行时抛出的异常
         */
        private boolean upOrderMessage() throws Exception {

            RequestBody requestBody = new FormEncodingBuilder()
                    .add("userTel", userTel)
                    .add("scoreType", scoreType)
                    .add("count", count)
                    .build();

            Request request = new Request.Builder().post(requestBody).url(URL_request_upScoreDeduct).build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String result = response.body().string();
                response.body().close();
                isSuccessed = JsonDecoder.cancalOrderData(result);


            }
            return isSuccessed;
        }

        /**
         * 成功时调用
         */
        private void onCompleted() {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpScoreDeductCallback.onUpScoreDeductCompleted(isSuccessed);
                }
            });
        }

        /**
         * 失败时调用
         */
        private void onFailed(final boolean isException) {
            threadExecutor.removeTag(TAG);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    onUpScoreDeductCallback.onUpScoreDeductFailed(isException);
                }
            });
        }

    }


}
