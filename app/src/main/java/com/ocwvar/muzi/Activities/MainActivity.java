package com.ocwvar.muzi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ocwvar.muzi.Adapters.FuncGLAdapter;
import com.ocwvar.muzi.Adapters.InfoListAdapter;
import com.ocwvar.muzi.Adapters.MainScrollingVPAdapter;
import com.ocwvar.muzi.Adapters.SlidingMenuAdapter;
import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Beans.InfoListBean;
import com.ocwvar.muzi.Beans.UserType;
import com.ocwvar.muzi.Network.ArtcleType;
import com.ocwvar.muzi.Network.Callbacks.OnArtcleLoadCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetSignHistoryCallback;
import com.ocwvar.muzi.Network.Callbacks.OnGetSignScoreCallback;
import com.ocwvar.muzi.Network.Callbacks.OnRecycleViewClickCallbacks;
import com.ocwvar.muzi.Network.Callbacks.OnViewPagerClickCallback;
import com.ocwvar.muzi.Network.NetworkHelper;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;
import com.ocwvar.muzi.Utils.BaseActivity;
import com.ocwvar.muzi.Utils.CircleImageView;
import com.ocwvar.muzi.Utils.StaggeredGridDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Activities
 * Date: 2016/5/4  9:49
 * Project: Muzi
 * 主界面
 */

@SuppressWarnings("deprecation")
public class MainActivity
        extends BaseActivity
        implements
        OnRecycleViewClickCallbacks.OnFuncGLClickCallback,
        OnRecycleViewClickCallbacks.OnInfoListClickCallback,
        OnRecycleViewClickCallbacks.OnSlidingMenuClickCallback, View.OnClickListener, OnArtcleLoadCallback, OnViewPagerClickCallback ,OnGetSignScoreCallback,
        OnGetSignHistoryCallback{

    //默认读取公告数  默认:1
    static final int loadAnnouncementCount = 1;

    RecyclerView funcGL;                //网格菜单
    RecyclerView infoList;               //列表菜单
    RecyclerView slidingMenu;        //侧滑菜单
    ViewPager viewPager;                //滚动显示的VP
    DrawerLayout drawer;                //侧滑菜单
    FuncGLAdapter funcAdapter;
    InfoListAdapter infoAdapter;
    SlidingMenuAdapter slidingAdapter;
    MainScrollingVPAdapter vpAdapter;
    TextView scoreText,showText;                 //积分

    LoopScrollingVP loopScrollingThread;        //循环滚动线程

    CircleImageView userHead;     //用户头像

    ImageView ad2, ad3, ad4, ad5, ad6,ad7;

    long passTime = -999L;  //双点BACK注销

    private ArrayList <String> dateList = new ArrayList<>();

    @Override
    protected boolean onPreSetup() {
        return true;
    }

    @Override
    protected int setActivityView() {
        return R.layout.activity_mainpage;
    }

    @Override
    protected int onSetToolBar() {
        return R.id.toolbar;
    }

    @Override
    protected void onSetupViews() {
        //创建对象实例
        scoreText = (TextView) findViewById(R.id.main_score);
        showText = (TextView) findViewById(R.id.text_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        userHead = (CircleImageView) findViewById(R.id.slide_head);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        funcGL = (RecyclerView) findViewById(R.id.func_GList);
        infoList = (RecyclerView) findViewById(R.id.info_List);
        slidingMenu = (RecyclerView) findViewById(R.id.slide_list);
        viewPager = (ViewPager) findViewById(R.id.scrollVP);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        vpAdapter = new MainScrollingVPAdapter();
        funcAdapter = new FuncGLAdapter(getResources());
        slidingAdapter = new SlidingMenuAdapter(getResources());
        infoAdapter = new InfoListAdapter();


        //对象的设置
        funcGL.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        infoList.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        slidingMenu.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        funcGL.addItemDecoration(new StaggeredGridDecoration());
        slidingMenu.setAdapter(slidingAdapter);
        funcGL.setAdapter(funcAdapter);
        infoList.setAdapter(infoAdapter);
        slidingMenu.setHasFixedSize(true);
        funcGL.setHasFixedSize(true);
        infoList.setHasFixedSize(true);
        vpAdapter.setOnViewPagerClickCallback(this);
        viewPager.setAdapter(vpAdapter);
        viewPager.setOnClickListener(this);
        funcAdapter.setOnFuncGLClickCallback(this);
        infoAdapter.setOnInfoListClickCallback(this);
        slidingAdapter.setOnSlidingMenuClickCallback(this);
        userHead.setOnClickListener(this);

        NetworkHelper.getInstance().getSignScore(MainActivity.this,AppOptions.USERINFO.user.getPhoneNumber());
        NetworkHelper.getInstance().getSignHitory(MainActivity.this,AppOptions.USERINFO.user.getPhoneNumber());

        if (AppOptions.USERINFO.user.getUserType() == UserType.GUEST) {
            infoList.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            showText.setVisibility(View.GONE);
            scoreText.setVisibility(View.GONE);

        }

        toggle.syncState();

        ad2 = (ImageView) findViewById(R.id.imageView_ad2);
        ad2.setOnClickListener(this);
        ad3 = (ImageView) findViewById(R.id.imageView_ad3);
        ad3.setOnClickListener(this);
        ad4 = (ImageView) findViewById(R.id.imageView_ad4);
        ad4.setOnClickListener(this);
        ad5 = (ImageView) findViewById(R.id.imageView_ad5);
        ad5.setOnClickListener(this);
        ad6 = (ImageView) findViewById(R.id.imageView_ad6);
        ad6.setOnClickListener(this);
        ad7 = (ImageView) findViewById(R.id.imageView_ad7);
        ad7.setOnClickListener(this);
    }

    /**
     * 下方图像块被点击事件以及头像点击事件
     *
     * @param clickedView 被点击的控件
     */
    @Override
    protected void onViewClick(View clickedView) {
        switch (clickedView.getId()) {
            case R.id.slide_head:
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                    startActivity(new Intent(MainActivity.this, PortraitFixActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, R.string.error_permission_guest, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.scrollVP:
                if (vpAdapter.getCount() > 0 && viewPager.getCurrentItem() >= 0) {
                    Intent intent = new Intent(MainActivity.this, ArtcleActivity.class);
                    intent.putExtra("ArtclesBean", vpAdapter.getItem(viewPager.getCurrentItem()));
                    startActivity(intent);
                }
                break;
            case R.id.imageView_ad4:
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                    startActivity(new Intent(MainActivity.this, GuideActivity.class));
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.error_permission_guest, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageView_ad2:
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                ad2.setClickable(false);
                Intent intent=new Intent();
                intent.putStringArrayListExtra("dateList",dateList);
                intent.setClass(this,SignActivity.class);
                startActivityForResult(intent,1);
                }else {
                    Toast.makeText(MainActivity.this, R.string.error_permission_guest, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageView_ad3:
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                    Intent intent3 = new Intent();
                    intent3.putExtra("TAG",0);
                    intent3.setClass(MainActivity.this, ServiceDynamicActivity.class);
                    startActivity(intent3);
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.error_permission_guest, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageView_ad5:
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                    startActivity(new Intent(MainActivity.this, PayActivity.class));
                } else {

                }
                break;
            case R.id.imageView_ad6:
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                    Intent intent3 = new Intent();
                    intent3.putExtra("TAG",1);
                    intent3.setClass(MainActivity.this, ServiceDynamicActivity.class);
                    startActivity(intent3);
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.error_permission_guest, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageView_ad7:
                startActivity(new Intent(MainActivity.this, HelpingActivity.class));
                break;
        }
    }

    @Override
    protected boolean onViewLongClick(View holdedView) {
        return false;
    }

    /**
     * Activity被暂停的时候 轮播也暂停滚动
     */
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        if (loopScrollingThread != null) {
            loopScrollingThread.cancel();
            loopScrollingThread = null;
        }
    }

    /**
     * 从暂停中恢复过来的时候  恢复轮播滚动和重新加载用户头像
     */
    @Override
    protected void onResume() {
        super.onResume();
        ad2.setClickable(true);
        JPushInterface.onResume(this);

        //如果当前推送接收器是停止状态 , 则开始接收推送信息
        if (JPushInterface.isPushStopped(getApplicationContext()) && AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
            JPushInterface.resumePush(getApplicationContext());
        }

        //更新滚动显示器
        vpAdapter.updateDatas();

        //读取公告第一条
        if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
            NetworkHelper.getInstance().loadArtcles(this, false, new String[]{Integer.toString(loadAnnouncementCount), "1", "1", "1"});
        }

        //当Activity从Pause中恢复过来的时候，如果VP数据大于等于2，同时滚动播放线程已暂停，则启动滚动播放线程
        if (loopScrollingThread == null && vpAdapter.getCount() >= 2) {
            loopScrollingThread = new LoopScrollingVP();
            loopScrollingThread.start();
        }

        //同步用户头像数据
        if (AppOptions.USERINFO.getUserHead() instanceof Integer) {
            //默认图像
            userHead.setImageDrawable(getResources().getDrawable(AppOptions.USERINFO.userHeadRes));
        } else if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
            //如果是正式用户 , 则下载头像图像
            Picasso
                    .with(MainActivity.this)
                    .load((String) AppOptions.USERINFO.getUserHead())
                    .placeholder(AppOptions.USERINFO.userHeadRes)
                    .error(AppOptions.USERINFO.userHeadRes)
                    .config(Bitmap.Config.RGB_565)
                    .into(userHead);
        } else {
            //否则使用默认头像
            userHead.setImageDrawable(getResources().getDrawable(AppOptions.USERINFO.userHeadRes));
        }
        NetworkHelper.getInstance().getSignScore(MainActivity.this,AppOptions.USERINFO.user.getPhoneNumber());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                    return false;
                } else {
                    if (passTime == -999L || System.currentTimeMillis() - passTime > 1500) {
                        passTime = System.currentTimeMillis();
                        Toast.makeText(MainActivity.this, R.string.double_click_logout, Toast.LENGTH_LONG).show();
                    } else {
                        finish();
                    }
                    return false;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击左上角打开侧滑菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
        }
        return true;
    }

    /**
     * 宫格列表点击事件
     *
     * @param position 点击位置
     */
    @Override
    public void onFuncGLClick(int position) {
        switch (position) {
            case 0:
                //公告通知
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                    startActivity(new Intent(MainActivity.this, CommunityAnnouncementActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, R.string.error_permission_guest, Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                //居家文化
                startActivity(new Intent(MainActivity.this, CommunityCultureActivity.class));
                break;
            case 2:
                //办事指南
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                    startActivity(new Intent(MainActivity.this, GuideActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, R.string.error_permission_guest, Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                //爱心互助
                startActivity(new Intent(MainActivity.this, HelpingActivity.class));
                break;
            case 4:
                //一键缴费
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                    startActivity(new Intent(MainActivity.this, PayActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, R.string.error_permission_guest, Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                //物业保修
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                    startActivity(new Intent(MainActivity.this, RepireActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, R.string.error_permission_guest, Toast.LENGTH_SHORT).show();
                }
                break;
            case 6:
                //和谐之声
                if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
                    startActivity(new Intent(MainActivity.this, HXSoundActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, R.string.error_permission_guest, Toast.LENGTH_SHORT).show();
                }
                break;
            case 7:
                //服务动态
                startActivity(new Intent(MainActivity.this, ServiceDynamicActivity.class));
                break;
        }
    }

    /**
     * 垂直列表点击事件 (公告 论坛热门 条目)
     *
     * @param position 点击位置
     */
    @Override
    public void onInfoListClick(int position) {
        if (AppOptions.USERINFO.user.getUserType() != UserType.GUEST) {
            //游客不允许点击查看

            InfoListBean bean = infoAdapter.getSelectItem(position);

            //如果得到的文章Bean为空 , 则表示这个条目有网址链接
            if (bean.getBean() == null && !TextUtils.isEmpty(bean.getUrl())) {
                //有链接URL , 则跳转到浏览器
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(bean.getUrl()));
                startActivity(intent);
            } else if (bean.getBean() != null) {
                //有文章Bean , 则跳到文章解析显示页面
                Intent intent = new Intent(MainActivity.this, ArtcleActivity.class);
                intent.putExtra("ArtclesBean", bean.getBean());
                startActivity(intent);
            }
        }
    }

    /**
     * 侧滑列表点击事件
     *
     * @param position 点击位置
     */
    @Override
    public void onSlidingMenuClick(int position) {

        switch (position) {
            case 0:
                if (AppOptions.USERINFO.user.getUserType() == UserType.GUEST) {
                    //游客的第一个选项是切换小区浏览
                    startActivity(new Intent(MainActivity.this, SwitchCommunityActivity.class));
                } else {
                    //正常用户的第一个选择是签到页面
                    showProgressDialog("正在处理",false,null);
                    Intent intent=new Intent();
                    intent.putStringArrayListExtra("dateList",dateList);
                    intent.setClass(this,SignActivity.class);
                    startActivityForResult(intent,1);

                }
                break;
            case 1:
                if (AppOptions.USERINFO.user.getUserType() == UserType.GUEST) {
                    //游客的第二个选项是设置页面
                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                } else {
                    //正常用户的第二个选择是切换小区浏览
                    startActivity(new Intent(MainActivity.this, SwitchCommunityActivity.class));
                }
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, ShowUserInfoActivity.class));
                break;
            case 3:
                Intent intent = new Intent(MainActivity.this, UpdatePasswordActivity.class);
                intent.putExtra("isReset", false);
                startActivity(intent);
                break;
            case 4:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case 5:
                startActivity(new Intent(MainActivity.this, RepireTaskListActivity.class));
                break;
        }
    }

    /**
     * 读取公告第一条
     *
     * @param loadedArtclesCount 当前读取的数量
     * @param totalArtclesCount  总共的数量
     * @param artcleType         文章的所属类型
     * @param communityID        所属的社区ID
     * @param arrayList          文章列表 当没有文章时，为NULL
     */
    @Override
    public void onArtcleLoadCompleted(int loadedArtclesCount, int totalArtclesCount, ArtcleType artcleType, int communityID, @Nullable ArrayList<ArtclesBean> arrayList) {
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                ArtclesBean bean = arrayList.get(i);
                infoAdapter.putData(new InfoListBean(Color.argb(255, 239, 91, 37), "最新公告", bean));
            }
        }
    }

    /**
     * 读取公告失败
     *
     * @param message 失败返回的信息
     * @param e       失败抛出的异常
     */
    @Override
    public void onArtcleLoadFailed(String message, Exception e) {
        Toast.makeText(MainActivity.this, "加载公告失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * ViewPager点击事件
     *
     * @param artclesBean 返回的文章Bean
     */
    @Override
    public void onClickViewPager(ArtclesBean artclesBean) {
        if (artclesBean != null) {
            Intent intent = new Intent(MainActivity.this, ArtcleActivity.class);
            intent.putExtra("ArtclesBean", artclesBean);
            startActivity(intent);
        }
    }
    //获取积分成功回调方法
    @Override
    public void onGetSignScoreCompleted(String userTel, String score) {
        if(score==null||"".equals(score)){
            scoreText.setText("获取失败");
        }else {

            AppOptions.USERINFO.score=score;
            scoreText.setText(score);
        }

    }
    //获取积分失败回调方法
    @Override
    public void onGetSignScoreFailed(boolean isException, String userTel) {
            scoreText.setText("获取失败");
    }

    //获取签到历史成功
    @Override
    public void onGetSignHistoryCompleted(String userTel, List<String> dateList) {

        if(dateList!=null){
            this.dateList.clear();
            this.dateList.addAll(dateList);
        }

    }

    //获取签到历史失败
    @Override
    public void onGetSignHistoryFailed(boolean isException, String userTel) {

    }

    /**
     * 循环更新VP线程
     */
    class LoopScrollingVP extends Thread {

        int totalCount, nowPosition;
        boolean keepRunning;

        @Override
        public void run() {
            super.run();
            while (keepRunning && viewPager != null && vpAdapter.getCount() > 1) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                totalCount = vpAdapter.getCount();
                nowPosition = viewPager.getCurrentItem();
                if (nowPosition + 1 >= totalCount) {
                    nowPosition = 0;
                } else {
                    nowPosition += 1;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(nowPosition, true);
                    }
                });

            }
        }

        public void cancel() {
            keepRunning = false;
        }

        @Override
        public synchronized void start() {
            super.start();
            System.out.println("Scroll started");
            keepRunning = true;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        NetworkHelper.getInstance().getSignScore(MainActivity.this,AppOptions.USERINFO.user.getPhoneNumber());
        NetworkHelper.getInstance().getSignHitory(MainActivity.this,AppOptions.USERINFO.user.getPhoneNumber());
        dismissProgressDialog();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}

