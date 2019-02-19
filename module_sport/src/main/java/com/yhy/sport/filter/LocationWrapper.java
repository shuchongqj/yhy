package com.yhy.sport.filter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;

/**
 * @Description:
 * @Created by zhaolei.yang 2018-07-07 10:38
 */
public class LocationWrapper implements Comparable<LocationWrapper>, Parcelable {

    private AMapLocation location;
    private long timeStamp;


    public AMapLocation getLocation() {
        return location;
    }

    public LocationWrapper setLocation(AMapLocation location) {
        this.location = location;
        return this;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public LocationWrapper setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    @Override
    public int compareTo(@NonNull LocationWrapper o) {
        return (int) (this.getTimeStamp() - o.getTimeStamp());
    }

    public LocationWrapper() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.location, flags);
        dest.writeLong(this.timeStamp);
    }

    protected LocationWrapper(Parcel in) {
        this.location = in.readParcelable(AMapLocation.class.getClassLoader());
        this.timeStamp = in.readLong();
    }

    public static final Creator<LocationWrapper> CREATOR = new Creator<LocationWrapper>() {
        @Override
        public LocationWrapper createFromParcel(Parcel source) {
            return new LocationWrapper(source);
        }

        @Override
        public LocationWrapper[] newArray(int size) {
            return new LocationWrapper[size];
        }
    };
}
