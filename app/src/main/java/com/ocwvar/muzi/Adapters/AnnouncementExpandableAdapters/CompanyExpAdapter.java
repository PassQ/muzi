package com.ocwvar.muzi.Adapters.AnnouncementExpandableAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ocwvar.muzi.Beans.ArtclesBean;
import com.ocwvar.muzi.Network.Callbacks.OnExpScrollAtBottomCallback;
import com.ocwvar.muzi.R;

import java.util.ArrayList;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Adapters.AnnouncementExpandableAdapters
 * Date: 2016/5/18  17:02
 * Project: Muzi
 * 公司公告的折叠列表Adapter
 */
public class CompanyExpAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "公司公告";

    private ArrayList<String> groupTitles;
    private ArrayList<ArrayList<ArtclesBean>> groupItems;
    private OnExpScrollAtBottomCallback onScrollAtBottomCallback;

    public CompanyExpAdapter() {
        groupItems = new ArrayList<>();
        groupTitles = new ArrayList<>();
    }

    public void addGroup(String groupTitle) {
        if (!groupTitles.contains(groupTitle)) {
            this.groupTitles.add(groupTitle);
            this.groupItems.add(new ArrayList<ArtclesBean>());
            notifyDataSetChanged();
        }
    }

    public void addItem(int groupIndex, ArtclesBean item) {
        groupItems.get(groupIndex).add(item);
        notifyDataSetChanged();
    }

    public int indexPositionOfGroup(String groupTitle) {
        return groupTitles.indexOf(groupTitle);
    }

    public ArtclesBean getItem(int groupIndex, int itemIndex) {
        return groupItems.get(groupIndex).get(itemIndex);
    }

    public void setOnScrollAtBottomCallback(OnExpScrollAtBottomCallback onScrollAtBottomCallback) {
        this.onScrollAtBottomCallback = onScrollAtBottomCallback;
    }

    @Override
    public int getGroupCount() {
        return groupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupItems.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupItems.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupTitleViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expt_title, parent, false);
            viewHolder = new GroupTitleViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupTitleViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(groupTitles.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final GroupItemViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expt_item, parent, false);
            viewHolder = new GroupItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupItemViewHolder) convertView.getTag();
        }

        ArtclesBean artclesBean = groupItems.get(groupPosition).get(childPosition);

        viewHolder.date.setText(artclesBean.getAddTime().split(" ")[0]);
        viewHolder.message.setText(artclesBean.getTitle());

        if (childPosition == groupItems.get(groupPosition).size() - 1 && onScrollAtBottomCallback != null) {
            onScrollAtBottomCallback.onScrollatBottom(TAG, groupPosition);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    class GroupTitleViewHolder {

        View itemView;
        TextView title;

        public GroupTitleViewHolder(View itemView) {
            this.itemView = itemView;
            title = (TextView) itemView.findViewById(R.id.textView_expt_title);
        }

    }

    class GroupItemViewHolder {

        View itemView;
        TextView message, date;

        public GroupItemViewHolder(View itemView) {
            this.itemView = itemView;
            message = (TextView) itemView.findViewById(R.id.textView_expt_preview);
            date = (TextView) itemView.findViewById(R.id.textView_expt_date);
        }

    }

}
