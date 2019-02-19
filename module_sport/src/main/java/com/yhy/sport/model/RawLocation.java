package com.yhy.sport.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * @Description:运动原始数据
 * @Created by zhaolei.yang 2018-07-09 15:44
 */
@RealmClass
public class RawLocation implements RealmModel, Parcelable {

    /**
     * 经度 （直接从高德地图获取）
     */
    private double longitude;

    /**
     * 维度（直接从高德地图获取）
     */
    private double latitude;

    /**
     * 方向角度（直接从高德地图获取）
     */
    private double course;

    /**
     * 海拔（直接从高德地图获取）
     */
    private double altitude;

    /**
     * 精度 （直接从高德地图获取）
     */
    private double accuracy;

    /**
     * 时间戳（区分情况从高德地图获取）
     */
    private int timestamp;

    /**
     * 速度，这个值需要自己计算（每段的平均速度）
     */
    private double speed;

    /**
     * 步频，从传感器获取
     */
    private double cadence;

    /**
     * 上楼
     */
    private int floorsAscended;

    /**
     * 下楼
     */
    private int floorsDescended;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getCadence() {
        return cadence;
    }

    public void setCadence(double cadence) {
        this.cadence = cadence;
    }

    public int getFloorsAscended() {
        return floorsAscended;
    }

    public void setFloorsAscended(int floorsAscended) {
        this.floorsAscended = floorsAscended;
    }

    public int getFloorsDescended() {
        return floorsDescended;
    }

    public void setFloorsDescended(int floorsDescended) {
        this.floorsDescended = floorsDescended;
    }

    @Override
    public String toString() {
        return "RawLocation{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", course=" + course +
                ", altitude=" + altitude +
                ", accuracy=" + accuracy +
                ", timestamp=" + timestamp +
                ", speed=" + speed +
                ", cadence=" + cadence +
                ", floorsAscended=" + floorsAscended +
                ", floorsDescended=" + floorsDescended +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.course);
        dest.writeDouble(this.altitude);
        dest.writeDouble(this.accuracy);
        dest.writeInt(this.timestamp);
        dest.writeDouble(this.speed);
        dest.writeDouble(this.cadence);
        dest.writeInt(this.floorsAscended);
        dest.writeInt(this.floorsDescended);
    }

    public RawLocation() {
    }

    protected RawLocation(Parcel in) {
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.course = in.readDouble();
        this.altitude = in.readDouble();
        this.accuracy = in.readDouble();
        this.timestamp = in.readInt();
        this.speed = in.readDouble();
        this.cadence = in.readDouble();
        this.floorsAscended = in.readInt();
        this.floorsDescended = in.readInt();
    }

    public static final Parcelable.Creator<RawLocation> CREATOR = new Parcelable.Creator<RawLocation>() {
        @Override
        public RawLocation createFromParcel(Parcel source) {
            return new RawLocation(source);
        }

        @Override
        public RawLocation[] newArray(int size) {
            return new RawLocation[size];
        }
    };
}
