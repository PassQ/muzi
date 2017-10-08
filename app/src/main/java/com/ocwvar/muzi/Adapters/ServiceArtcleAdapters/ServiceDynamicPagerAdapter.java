package com.ocwvar.muzi.Adapters.ServiceArtcleAdapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.ocwvar.muzi.Activities.BulletinActivty;
import com.ocwvar.muzi.Beans.UserType;
import com.ocwvar.muzi.Fragments.Bulletin.BulletinBusinessFragment;
import com.ocwvar.muzi.Fragments.Bulletin.BulletinCircumFragment;
import com.ocwvar.muzi.R;
import com.ocwvar.muzi.Utils.AppOptions;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters
 * Date: 2016/7/7  12:56
 * Project: Muzi
 * 服务动态ViewPager适配器
 */
public class ServiceDynamicPagerAdapter extends PagerAdapter implements View.OnClickListener {

    //物业服务 , 便民服务  两个显示列表
    RecyclerView propertyList, servicePeopleList;
    PropertyAdapter propertyAdapter;
    ServicePeopleAdapter servicePeopleAdapter;
    View bulletinView;


    private static int Tag =0;
    private BulletinBusinessFragment bulletinBusinessFragment;
    private BulletinCircumFragment bulletinCircumFragment;
    private View businessLayout;
    private View circumLayout;

    private TextView businessText;
    private TextView circumText;

    private FragmentManager fragmentManager;

    public static void setTag(int tag) {
        Tag = tag;
    }

    @Override
    public int getCount() {
        return 3;
    }
    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager=fragmentManager;
    }

    public void setPropertyAdapter(PropertyAdapter propertyAdapter) {
        this.propertyAdapter = propertyAdapter;
    }

    public void setServicePeopleAdapter(ServicePeopleAdapter servicePeopleAdapter) {
        this.servicePeopleAdapter = servicePeopleAdapter;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        switch (position) {
            case 0:
                if(AppOptions.USERINFO.user.getUserType() != UserType.GUEST){
                    if(bulletinView==null){
                        initBulletinView(container.getContext());
                    }
                    if(bulletinView!=null){
                        container.addView(bulletinView);
                        return  bulletinView;
                    }else {
                        return  null;
                    }
                }else {
                    return null;
                }


            case 1:
                if (servicePeopleList == null) {
                    initServicePropleListList(container.getContext());
                }
                if (servicePeopleList != null) {
                    container.addView(servicePeopleList);
                    return servicePeopleList;
                } else {
                    return null;
                }
            case 2:

                if (propertyList == null) {
                    initPropertyList(container.getContext());
                }
                if (propertyList != null) {
                    container.addView(propertyList);
                    return propertyList;
                } else {
                    return null;
                }

            default:
                return null;
        }
    }
    //对信息公告页面进行初始化
    private void initBulletinView(Context context) {
        LayoutInflater inflater=LayoutInflater.from(context);
        bulletinView=inflater.inflate(R.layout.activity_bulletin,null);
        businessLayout=bulletinView.findViewById(R.id.business_Layout);
        circumLayout=bulletinView.findViewById(R.id.circum_Layout);
        businessText= (TextView) bulletinView.findViewById(R.id.business_text);
        circumText= (TextView) bulletinView.findViewById(R.id.circum_text);
        businessLayout.setOnClickListener(this);
        circumLayout.setOnClickListener(this);
        //第一次进入选择第一个子页面

        setTabSelection(Tag);


    }
    //选择子页面操作
    private void setTabSelection(int i) {
        clearSelection();//清空选择状态操作
        FragmentTransaction transaction =fragmentManager.beginTransaction();//开启fragment事务
        hideFragment(transaction);//隐藏所有fragment
        switch (i){
            case 0:
                //当点击了选项卡时，将tab标题进行变色和修改大小操作
                businessText.setTextColor(Color.rgb(252,122,19));
                businessText.setTextSize(20);
                if(bulletinBusinessFragment==null){//进行页面初始化操作
                    bulletinBusinessFragment=new BulletinBusinessFragment();
                    transaction.add(R.id.content,bulletinBusinessFragment);
                }else {
                    transaction.show(bulletinBusinessFragment);
                }
                break;
            case 1:
                circumText.setTextColor(Color.rgb(252,122,19));
                circumText.setTextSize(20);
                if(bulletinCircumFragment==null){
                    bulletinCircumFragment=new BulletinCircumFragment();
                    transaction.add(R.id.content,bulletinCircumFragment);
                }else {
                    transaction.show(bulletinCircumFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    //清空子页面操作
    private void hideFragment(FragmentTransaction transaction) {
        if(bulletinBusinessFragment!=null){
            transaction.hide(bulletinBusinessFragment);
        }
        if(bulletinCircumFragment!=null){
            transaction.hide(bulletinCircumFragment);
        }
    }

    //取消子页面标题选择效果操作
    private void clearSelection() {
        businessText.setTextSize(16);
        businessText.setTextColor(Color.GRAY);
        circumText.setTextColor(Color.GRAY);
        circumText.setTextSize(16);

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        switch (position) {
            case 0:
                container.removeView(bulletinView);
                break;
            case 1:
                container.removeView(servicePeopleList);
                break;
            case 2:
                container.removeView(propertyList);

                break;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "周边预约";
            case 1:
                return "便民服务";
            case 2:
                return "物业服务";
            default:
                return "未定义";
        }
    }

    private void initPropertyList(Context context) {
        if (propertyAdapter != null) {
            propertyList = new RecyclerView(context);
            propertyList.setAdapter(propertyAdapter);
            propertyList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            propertyList.setHasFixedSize(true);
        }
    }

    private void initServicePropleListList(Context context) {
        if (servicePeopleAdapter != null) {
            servicePeopleList = new RecyclerView(context);
            servicePeopleList.setAdapter(servicePeopleAdapter);
            servicePeopleList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            servicePeopleList.setHasFixedSize(true);
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.business_Layout:
                setTabSelection(0);//点击了tab时的操作
                break;
            case R.id.circum_Layout:
                setTabSelection(1);
                break;
            default:
                break;
        }

    }



}
