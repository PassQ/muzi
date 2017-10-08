package com.ocwvar.muzi.Beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Beans
 * Date: 2016/5/4  17:49
 * Project: Muzi
 * 文章的Bean
 */

public class ArtclesBean implements Parcelable {

    public static final Creator<ArtclesBean> CREATOR = new Creator<ArtclesBean>() {
        @Override
        public ArtclesBean createFromParcel(Parcel in) {
            return new ArtclesBean(in);
        }

        @Override
        public ArtclesBean[] newArray(int size) {
            return new ArtclesBean[size];
        }
    };
    String title;
    String content;
    String addTime;
    String relativeThuPath;
    String relativePath;
    String preview;
    int totalArtclesCount;

    public ArtclesBean(String title, String content, String addTime, String relativeThuPath, String relativePath, String preview) {
        this.title = title;
        this.content = content;
        this.addTime = addTime;
        this.relativeThuPath = relativeThuPath;
        this.relativePath = relativePath;
        this.preview = preview;
    }

    public ArtclesBean() {
    }

    protected ArtclesBean(Parcel in) {
        title = in.readString();
        content = in.readString();
        addTime = in.readString();
        relativeThuPath = in.readString();
        relativePath = in.readString();
        totalArtclesCount = in.readInt();
        preview = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getRelativeThuPath() {
        return relativeThuPath;
    }

    public void setRelativeThuPath(String relativeThuPath) {
        this.relativeThuPath = relativeThuPath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public int getTotalArtclesCount() {
        return totalArtclesCount;
    }

    public void setTotalArtclesCount(int totalArtclesCount) {
        this.totalArtclesCount = totalArtclesCount;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtclesBean that = (ArtclesBean) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return addTime != null ? addTime.equals(that.addTime) : that.addTime == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(addTime);
        dest.writeString(relativeThuPath);
        dest.writeString(relativePath);
        dest.writeInt(totalArtclesCount);
        dest.writeString(preview);
    }


}
