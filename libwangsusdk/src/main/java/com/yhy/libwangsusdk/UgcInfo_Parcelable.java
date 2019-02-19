package com.yhy.libwangsusdk;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *
 * Created by Jiervs on 2018/5/30.
 */

public class UgcInfo_Parcelable implements Parcelable{

    public long userId;
    public String textContent;
    public List<String> picList;
    public UgcInfo_POIInfo poiInfo;
    public String videoUrl;
    public String videoPicUrl;
    public String shortVideoType;

    public UgcInfo_Parcelable(){}

    private UgcInfo_Parcelable(Parcel in) {
        userId = in.readLong();
        textContent = in.readString();
        picList = in.createStringArrayList();
        poiInfo = in.readParcelable(UgcInfo_POIInfo.class.getClassLoader());
        videoUrl = in.readString();
        videoPicUrl = in.readString();
        shortVideoType = in.readString();
    }

    public static final Creator<UgcInfo_Parcelable> CREATOR = new Creator<UgcInfo_Parcelable>() {
        @Override
        public UgcInfo_Parcelable createFromParcel(Parcel in) {
            return new UgcInfo_Parcelable(in);
        }

        @Override
        public UgcInfo_Parcelable[] newArray(int size) {
            return new UgcInfo_Parcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeString(textContent);
        dest.writeStringList(picList);
        dest.writeParcelable(poiInfo, flags);
        dest.writeString(videoUrl);
        dest.writeString(videoPicUrl);
        dest.writeString(shortVideoType);
    }


    public static class UgcInfo_POIInfo implements Parcelable{
        public String detail;
        public double longitude;
        public double latitude;

        public UgcInfo_POIInfo() {}

        public UgcInfo_POIInfo(Parcel in) {
            detail = in.readString();
            longitude = in.readDouble();
            latitude = in.readDouble();
        }

        public static final Creator<UgcInfo_POIInfo> CREATOR = new Creator<UgcInfo_POIInfo>() {
            @Override
            public UgcInfo_POIInfo createFromParcel(Parcel in) {
                return new UgcInfo_POIInfo(in);
            }

            @Override
            public UgcInfo_POIInfo[] newArray(int size) {
                return new UgcInfo_POIInfo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(detail);
            dest.writeDouble(longitude);
            dest.writeDouble(latitude);
        }
    }
}
