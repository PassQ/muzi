package com.ocwvar.muzi.Network.Callbacks;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Beans.RepireBean;
import com.ocwvar.muzi.Beans.RepireStatusBean;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Network.Callbacks
 * Date: 2016/5/6  10:08
 * Project: Muzi
 * 列表的点击事件接口
 */

public interface OnRecycleViewClickCallbacks {

    interface OnFuncGLClickCallback {
        void onFuncGLClick(int position);
    }

    interface OnInfoListClickCallback {
        void onInfoListClick(int position);
    }

    interface OnSlidingMenuClickCallback {
        void onSlidingMenuClick(int position);
    }

    interface OnRepireStatusListCallback {
        void onRepireStatusMenuClick(RepireStatusBean repireStatusBean);
    }

    interface OnCommunityCultureListCallback {
        void onCommunityCultureListClick(String type, int position, ArtclesBean artclesBean);
    }

    interface OnScrollAtBottomCallback {
        void onScrollAtBottomCallback(String type);
    }

    interface OnGuideListClickCallback {
        void onGuideListClick(int position, ArtclesBean artclesBean);
    }

    interface OnHelpingListClickCallback {
        void onHelpingClick(ArtclesBean artclesBean, int position);
    }

    interface OnContributionListClickCallback {
        void onContributionClick(ArtclesBean artclesBean, int position);
    }

    interface OnQuestionListClickCallback {
        void onQuestionListClick(ArtclesBean artclesBean, int position);
    }

    interface OnServiceListClickCallback {
        void onServiceListClick(ArtclesBean artclesBean, int position);
    }

    interface OnSuggestListClickCallback {
        void onSuggestListClick(ArtclesBean artclesBean, int position);
    }

    interface OnPropertyListClickCallback {
        void onPropertyListClick(ArtclesBean artclesBean, int position);
    }

    interface OnServicePeopleListClickCallback {
        void onServicePeopleListClick(ArtclesBean artclesBean, int position);
    }

    interface OnAskingListClickCallback {
        void onAskingClick(ArtclesBean artclesBean, int position);
    }

    interface OnClickRepireListCallback {
        void onClickList(RepireBean repireBean, int position);
    }

}
