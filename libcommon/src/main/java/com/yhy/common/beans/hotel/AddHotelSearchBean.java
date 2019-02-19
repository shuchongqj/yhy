package com.yhy.common.beans.hotel;


import com.yhy.common.beans.net.model.trip.HotelInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class AddHotelSearchBean implements Serializable, Cloneable {
    private static final long serialVersionUID = 5219558856886147637L;
    private String endName;//目的地
    private String mTimeUserStart;//用户使用开始时间
    private String mTimeUserEnd;//用户使用结束时间
    private long mTimeProgramStart;//程序使用开始时间
    private long mTimeProgramEnd;//程序使用结束时间
    private String hotelNameorPosition;//酒店名称或者位置
    private String searchKeyWord;//关键字搜索返回值
    private String priceOrStar;//价格或者星级文字描述
    private int priceSmall = 0;//最小价格标识
    private int priceBig = 20;//最大价格标识
    private List<HotelInfo> mHotelList;
    private List<HotelInfo> mHotelSearchList;
    private int mDestinationCityCode;//目的地城市编码
    private boolean mIsCbCheckedUnlimited = true;//是否选中 不限
    private boolean mIsCbChecked2;
    private boolean mIsCbChecked3;
    private int mDistance = 0;//type距离范围
    private boolean mIsCbChecked4;
    private boolean mIsCbChecked5;
    private String typeStartLeve = "";
    //主题Id
    private String tagId;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public List<HotelInfo> getmHotelSearchList() {
        return mHotelSearchList;
    }

    public void setmHotelSearchList(List<HotelInfo> mHotelSearchList) {
        this.mHotelSearchList = mHotelSearchList;
    }

    public int getmDistance() {
        return mDistance;
    }

    public void setmDistance(int mDistance) {
        this.mDistance = mDistance;
    }

    public String getTypeStartLeve() {
        return typeStartLeve;
    }

    public void setTypeStartLeve(String typeStartLeve) {
        this.typeStartLeve = typeStartLeve;
    }


    public int getmDestinationCityCode() {
        return mDestinationCityCode;
    }

    public void setmDestinationCityCode(int mDestinationCityCode) {
        this.mDestinationCityCode = mDestinationCityCode;
    }


    public List<HotelInfo> getmHotelList() {
        return mHotelList;
    }

    public void setmHotelList(List<HotelInfo> mHotelList) {
        this.mHotelList = mHotelList;
    }

    public List<HotelInfo> getmLnnList() {
        return mLnnList;
    }

    public void setmLnnList(List<HotelInfo> mLnnList) {
        this.mLnnList = mLnnList;
    }

    private List<HotelInfo> mLnnList;

    public int getmPriceSmall() {
        return priceSmall;
    }

    public void setmPriceSmall(int mPriceSmall) {
        this.priceSmall = mPriceSmall;
    }

    public int getmPriceBig() {
        return priceBig;
    }

    public void setmPriceBig(int mPriceBig) {
        this.priceBig = mPriceBig;
    }

    public boolean isCbChecked5() {
        return mIsCbChecked5;
    }

    public void setIsCbChecked5(boolean isCbChecked5) {
        this.mIsCbChecked5 = isCbChecked5;
    }

    public boolean isCbCheckedUnlimited() {
        return mIsCbCheckedUnlimited;
    }

    public void setIsCbCheckedUnlimited(boolean isCbCheckedUnlimited) {
        this.mIsCbCheckedUnlimited = isCbCheckedUnlimited;
    }

    public boolean isCbChecked2() {
        return mIsCbChecked2;
    }

    public void setIsCbChecked2(boolean isCbChecked2) {
        this.mIsCbChecked2 = isCbChecked2;
    }

    public boolean isCbChecked3() {
        return mIsCbChecked3;
    }

    public void setIsCbChecked3(boolean isCbChecked3) {
        this.mIsCbChecked3 = isCbChecked3;
    }

    public boolean isCbChecked4() {
        return mIsCbChecked4;
    }

    public void setIsCbChecked4(boolean isCbChecked4) {
        this.mIsCbChecked4 = isCbChecked4;
    }


    public String getmPriceOrStar() {
        return priceOrStar;
    }

    public void setmPriceOrStar(String mPriceOrStar) {
        this.priceOrStar = mPriceOrStar;
    }

    public String getmSearchKeyWord() {
        return searchKeyWord;
    }

    public void setmSearchKeyWord(String mSearchKeyWord) {
        this.searchKeyWord = mSearchKeyWord;
    }

    public long getTimeProgramStart() {
        return mTimeProgramStart;
    }

    public void setTimeProgramStart(long timeProgramStart) {
        this.mTimeProgramStart = timeProgramStart;
    }

    public long getTimeProgramEnd() {
        return mTimeProgramEnd;
    }

    public void setTimeProgramEnd(long timeProgramEnd) {
        this.mTimeProgramEnd = timeProgramEnd;
    }

    public String getTimeUserStart() {
        return mTimeUserStart;
    }

    public void setTimeUserStart(String timeUserStart) {
        this.mTimeUserStart = timeUserStart;
    }

    public String getTimeUserEnd() {
        return mTimeUserEnd;
    }

    public void setTimeUserEnd(String timeUserEnd) {
        this.mTimeUserEnd = timeUserEnd;
    }


    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }


    public String getHotelNameorPosition() {
        return hotelNameorPosition;
    }

    public void setHotelNameorPosition(String hotelNameorPosition) {
        this.hotelNameorPosition = hotelNameorPosition;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static AddHotelSearchBean deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static AddHotelSearchBean deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            AddHotelSearchBean result = new AddHotelSearchBean();
            if (!json.isNull("endName")) {
                result.endName = json.optString("endName", null);
            }
            if (!json.isNull("hotelNameorPosition")) {
                result.hotelNameorPosition = json.optString("hotelNameorPosition", null);
            }
            if (!json.isNull("searchKeyWord")) {
                result.searchKeyWord = json.optString("searchKeyWord", null);
            }
            if (!json.isNull("priceOrStar")) {
                result.priceOrStar = json.optString("priceOrStar", null);
            }
            result.priceSmall = json.optInt("priceSmall");
            result.priceBig = json.optInt("priceBig");
            if (!json.isNull("typeStartLeve")) {
                result.typeStartLeve = json.optString("typeStartLeve", null);
            }
            result.mDestinationCityCode = json.optInt("mDestinationCityCode");

            result.mIsCbCheckedUnlimited = json.optBoolean("mIsCbCheckedUnlimited");
            result.mIsCbChecked2 = json.optBoolean("mIsCbChecked2");
            result.mIsCbChecked3 = json.optBoolean("mIsCbChecked3");
            result.mIsCbChecked4 = json.optBoolean("mIsCbChecked4");
            result.mIsCbChecked5 = json.optBoolean("mIsCbChecked5");

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        if (this.endName != null) {
            json.put("endName", this.endName);
        }

        if (this.hotelNameorPosition != null) {
            json.put("hotelNameorPosition", this.hotelNameorPosition);
        }

        if (this.searchKeyWord != null) {
            json.put("searchKeyWord", this.searchKeyWord);
        }

        if (this.priceOrStar != null) {
            json.put("priceOrStar", this.priceOrStar);
        }
        if (this.typeStartLeve != null) {
            json.put("typeStartLeve", this.typeStartLeve);
        }
        json.put("priceSmall", this.priceSmall);
        json.put("mDestinationCityCode", this.mDestinationCityCode);
        json.put("priceBig", this.priceBig);

        json.put("mIsCbCheckedUnlimited", this.mIsCbCheckedUnlimited);
        json.put("mIsCbChecked2", this.mIsCbChecked2);
        json.put("mIsCbChecked3", this.mIsCbChecked3);
        json.put("mIsCbChecked4", this.mIsCbChecked4);
        json.put("mIsCbChecked5", this.mIsCbChecked5);
        return json;
    }
}
