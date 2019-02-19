package com.yhy.common.beans.album;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaItem implements Parcelable {
    public String mediaId;
    public String thumbnailPath;
    public String mediaPath;
    public long mediaCreateTime;
    public long size;
    public long duration;
    public int mediaType = 1;//1.图片 2.视频 3.音频（默认图片）
    public boolean isSelected = false;
    public boolean isNetImage = false;

    @Override
    public int describeContents() {
        return 0;
    }

    public MediaItem() {
        super();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mediaId);
        dest.writeString(thumbnailPath);
        dest.writeString(mediaPath);
        dest.writeLong(mediaCreateTime);
        dest.writeLong(size);
        dest.writeLong(duration);
        dest.writeInt(mediaType);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isNetImage ? 1 : 0));
    }

    public MediaItem(Parcel source) {
        mediaId = source.readString();
        thumbnailPath = source.readString();
        mediaPath = source.readString();
        mediaCreateTime = source.readLong();
        size = source.readLong();
        duration = source.readLong();
        mediaType = source.readInt();
        isSelected = source.readByte() != 0;
        isNetImage = source.readByte() != 0;
    }

    public static final Creator<MediaItem> CREATOR = new Creator<MediaItem>() {

        @Override
        public MediaItem[] newArray(int size) {
            return new MediaItem[size];
        }

        @Override
        public MediaItem createFromParcel(Parcel source) {
            return new MediaItem(source);
        }

    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isNetImage() {
        return isNetImage;
    }

    public void setNetImage(boolean netImage) {
        isNetImage = netImage;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public long getMediaCreateTime() {
        return mediaCreateTime;
    }

    public void setMediaCreateTime(long mediaCreateTime) {
        this.mediaCreateTime = mediaCreateTime;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }
}
