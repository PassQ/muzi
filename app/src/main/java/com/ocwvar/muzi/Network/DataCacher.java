package com.ocwvar.muzi.Network;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network
 * Date: 2016/5/4  17:34
 * Project: Muzi
 * 这个类用于存储各种数据
 * <p>
 * 头像数据
 * 首页滚动显示图片的网站
 * 用户信息
 * 位置信息的缓存
 * 临时用的计数器
 */

import android.content.Context;
import android.text.TextUtils;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Beans.UserBean;
import com.ocwvar.muzi.Beans.UserType;
import com.ocwvar.muzi.R;

import java.util.ArrayList;

public class DataCacher {

    public static final String SMS_KEY = "127175c20ec2b";
    public static final String SMS_SECRET = "598589ef4547329822525e4a6065c586";
    private static DataCacher dataCacher;
    final int MAX_CACHE_COUNT = 5; //最大滚动图片缓存数
    private ArrayList<ArtclesBean> cachedImagesArtcle = null;
    private ArrayList<Object> communityBeen;
    private ArrayList<Object> apartmentBeen;
    private ArrayList<Object> unitBeen;
    private ArrayList<Object> roomBeen;
    private long smsCount_Start = 0L;
    private long smsCount_End = 0L;
    private long smsCount_zc_Start = 0L;
    private long smsCount_zc_End = 0L;
    private UserBean userBean = null;
    private String userHeadImage = "";
    private String communityTAG = "";
    private String communityName = "";
    private int userHeadRes;
    private Context appContext;
    private boolean cacheUpdated = false;

    public DataCacher() {
        cachedImagesArtcle = new ArrayList<>();
        userHeadRes = R.drawable.default_head;
    }

    public static DataCacher getInstance() {
        if (dataCacher == null) {
            dataCacher = new DataCacher();
        }
        return dataCacher;
    }

    public long getSmsCount_Start() {
        return smsCount_Start;
    }

    public void setSmsCount_Start(long smsCount_Start) {
        this.smsCount_Start = smsCount_Start;
    }

    public long getSmsCount_End() {
        return smsCount_End;
    }

    public void setSmsCount_End(long smsCount_End) {
        this.smsCount_End = smsCount_End;
    }

    public long getCountTime() {
        return smsCount_End - smsCount_Start;
    }

    public void resetSmsCountingData() {
        this.smsCount_Start = 0L;
        this.smsCount_End = 0L;
    }

    public long getSmsCount_zc_Start() {
        return smsCount_zc_Start;
    }

    public void setSmsCount_zc_Start(long smsCount_zc_Start) {
        this.smsCount_zc_Start = smsCount_zc_Start;
    }

    public long getSmsCount_zc_End() {
        return smsCount_zc_End;
    }

    public void setSmsCount_zc_End(long smsCount_zc_End) {
        this.smsCount_zc_End = smsCount_zc_End;
    }

    public long getZCCountTime() {
        return smsCount_zc_End - smsCount_zc_Start;
    }

    public void resetZCSmsCountingData() {
        this.smsCount_zc_Start = 0L;
        this.smsCount_zc_End = 0L;
    }

    /**
     * 获取已经缓存好的图片地址
     *
     * @param index 读取条目
     * @return 地址String。如果没有，或超出位置返回NULL
     */
    public ArtclesBean getCachedImage(int index) {
        if (index >= 0 && index < cachedImagesArtcle.size()) {
            return cachedImagesArtcle.get(index);
        } else {
            return null;
        }
    }

    /**
     * 获取已缓存的数目
     *
     * @return 缓存的数目
     */
    public int getCountOfCachedList() {
        if (cachedImagesArtcle != null) {
            return cachedImagesArtcle.size();
        } else {
            cachedImagesArtcle = new ArrayList<>();
            return 0;
        }
    }

    /**
     * 获取缓存图片更新状态
     *
     * @return 更新状态
     */
    public boolean isCacheUpdated() {
        return cacheUpdated;
    }

    /**
     * 添加缓存的条目
     *
     * @param bean 添加的文章Bean
     * @return 执行结果
     */
    public boolean add(ArtclesBean bean) {
        if (bean != null && !TextUtils.isEmpty(bean.getRelativePath()) && cachedImagesArtcle.indexOf(bean) < 0) {

            //控制数量在限制之内，如果超出则移除最后一个Item，将新的Item添加到首位
            if (cachedImagesArtcle.size() == MAX_CACHE_COUNT) {
                cachedImagesArtcle.remove(MAX_CACHE_COUNT - 1);
                cachedImagesArtcle.add(0, bean);
            } else {
                cachedImagesArtcle.add(bean);
            }

            //更新状态
            this.cacheUpdated = true;

            System.out.println("已缓存:" + bean.getRelativePath());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取整个缓存的列表
     *
     * @return 缓存列表
     */
    public ArrayList<ArtclesBean> getCachedImagesURL() {
        this.cacheUpdated = false;
        return cachedImagesArtcle;
    }

    /**
     * 获取用户信息Bean
     *
     * @return 用户Bean (拷贝)，不存在时返回NULL
     */
    public UserBean getUserBean() {
        if (userBean == null) {
            return null;
        } else {
            return new UserBean(userBean);
        }
    }

    /**
     * 存储用户信息Bean
     *
     * @param userBean 要储存的用户信息Bean
     */
    public void setUserBean(UserBean userBean) {
        if (userBean != null) {
            this.userBean = userBean;
            if (userBean.getUserType() == UserType.GUEST) {
                this.communityTAG = "2";
                this.communityName = "银山小区";
            }
        }
    }

    /**
     * 得到用户头像数据
     *
     * @param onlyDefault 只返回默认头像资源
     * @return 如果有用户头像网址，则返回网址(String 类型)；否则返回资源ID (int 类型)
     */
    public Object getUserHead(boolean onlyDefault) {
        if (onlyDefault || TextUtils.isEmpty(this.userBean.getUserHeadImage())) {
            return userHeadRes;
        } else {
            return userBean.getUserHeadImage();
        }
    }

    /**
     * 设置用户头像网址
     *
     * @param url 头像网址
     */
    public void setUserHeadImage(String url) {
        if (userBean != null) {
            userBean.setUserHeadImage(url);
        }
    }

    /**
     * 获取全局AppContext
     *
     * @return ApplicationContext
     */
    public Context getAppContext() {
        return appContext;
    }

    /**
     * 设置全局AppContext
     *
     * @param appContext
     */
    public void setAppContext(Context appContext) {
        this.appContext = appContext.getApplicationContext();
    }

    /**
     * 获取小区缓存列表
     *
     * @return 已缓存的列表. 没有缓存的时候返回 NULL
     */
    public ArrayList<Object> getCommunityBeen() {
        return communityBeen;
    }

    /**
     * 缓存小区列表
     *
     * @param communityBeen 要缓存的小区列表数据
     */
    public void setCommunityBeen(ArrayList<Object> communityBeen) {
        if (this.communityBeen == null) {
            this.communityBeen = new ArrayList<>();
        }
        this.communityBeen.clear();
        this.communityBeen.addAll(communityBeen);
    }

    /**
     * 获取当前活动小区的TAG , 若没有设置临时小区TAG , 则为用户注册所在的小区
     *
     * @return 小区TAG
     */
    public String getUserCommunityTAG() {
        if (TextUtils.isEmpty(this.communityTAG)) {
            return this.userBean.getCommunityID();
        } else {
            return this.communityTAG;
        }
    }

    /**
     * 获取当前活动小区名称 , 若没有设置临时小区名称 , 则为用户注册所在的小区
     *
     * @return 小区名称
     */
    public String getUserCommunityName() {
        if (TextUtils.isEmpty(this.communityName)) {
            return this.userBean.getCommunityName();
        } else {
            return this.communityName;
        }
    }

    /**
     * 设置当前活动小区TAG
     *
     * @param communityTAG 活动小区TAG
     */
    public void setCommunityTAG(String communityTAG) {
        this.communityTAG = communityTAG;
    }

    /**
     * 设置当前活动小区名称
     *
     * @param communityName 活动小区名称
     */
    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    /**
     * 恢复用户原有的小区位置
     */
    public boolean resetTempCommunityStatus() {
        //如果是游客则不允许更改位置
        if (this.userBean.getUserType() == UserType.GUEST) {
            return false;
        } else {
            this.communityTAG = null;
            this.communityName = null;
            return true;
        }
    }

    /**
     * 获取缓存好的楼号数据
     *
     * @return 楼号数据列表
     */
    public ArrayList<Object> getApartmentBeen() {
        return apartmentBeen;
    }


    /**
     * 缓存楼号列表
     *
     * @param apartmentBeen 要缓存的楼号列表
     */
    public void setApartmentBeen(ArrayList<Object> apartmentBeen) {
        if (this.apartmentBeen == null) {
            this.apartmentBeen = new ArrayList<>();
        }
        this.apartmentBeen.clear();
        this.apartmentBeen.addAll(apartmentBeen);
    }

    /**
     * 获取缓存好的单元数据
     *
     * @return 单元数据列表
     */
    public ArrayList<Object> getUnitBeen() {
        return unitBeen;
    }

    /**
     * 缓存单元列表
     *
     * @param unitBeen 要缓存的单元列表
     */
    public void setUnitBeen(ArrayList<Object> unitBeen) {
        if (this.unitBeen == null) {
            this.unitBeen = new ArrayList<>();
        }
        this.unitBeen.clear();
        this.unitBeen.addAll(unitBeen);
    }

    /**
     * 获取缓存好的楼号数据
     *
     * @return 楼号数据列表
     */
    public ArrayList<Object> getRoomBeen() {
        return roomBeen;
    }

    /**
     * 缓存房号列表
     *
     * @param roomBeen 要缓存的房号列表
     */
    public void setRoomBeen(ArrayList<Object> roomBeen) {
        if (this.roomBeen == null) {
            this.roomBeen = new ArrayList<>();
        }
        this.roomBeen.clear();
        this.roomBeen.addAll(roomBeen);
    }
}
