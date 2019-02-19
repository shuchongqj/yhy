package com.yhy.common.beans.city.bean;

import android.text.TextUtils;

import com.yhy.common.beans.net.model.trip.CityInfo;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.pinyin.PinyinUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddressBean implements Serializable{
    private static final long serialVersionUID = 5315751347257136010L;
    private String cityCode;
    private String fromDestId;
    private String id;
    private boolean isHot;
    private String name;
    private String pinyin;
    private int seq;
    private String shortPinyin;
    private String subName;

    public String getFromDestId() {
        return fromDestId;
    }

    public void setFromDestId(String fromDestId) {
        this.fromDestId = fromDestId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setIsHot(boolean isHot) {
        this.isHot = isHot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getShortPinyin() {
        return shortPinyin;
    }

    public void setShortPinyin(String shortPinyin) {
        this.shortPinyin = shortPinyin;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * 实体转化
     * @param cityInfos
     * @return
     */
    public static List<AddressBean> transferCityInfoToAddressBean(List<CityInfo> cityInfos){
        List<AddressBean> addressBeanList = new ArrayList<>();
        if(cityInfos == null){
            return addressBeanList;
        }
        for(CityInfo cityInfo:cityInfos){
            AddressBean addressBean = new AddressBean();
            addressBean.cityCode = String.valueOf(cityInfo.cityCode);
            addressBean.setName(cityInfo.name);
            if(TextUtils.isEmpty(cityInfo.pinyin)){
                String pinyin = PinyinUtil.getPinyin(addressBean.getName());
                addressBean.setPinyin(pinyin);
            }else {
                addressBean.setPinyin(cityInfo.pinyin);
            }

            addressBean.setShortPinyin(cityInfo.shortPinyin);
            addressBeanList.add(addressBean);
        }
        return addressBeanList;
    }

    /**
     * 实体转化
     * @param cityInfos
     * @return
     */
    public static List<AddressBean> transferNewCityInfoToAddressBean(List<Destination> cityInfos){
        List<AddressBean> addressBeanList = new ArrayList<>();
        if(cityInfos == null){
            return addressBeanList;
        }
        for(Destination cityInfo:cityInfos){
            if(cityInfo.childList != null) {
                for (Destination dcn : cityInfo.childList) {
                    AddressBean addressBean = new AddressBean();
                    addressBean.cityCode = String.valueOf(dcn.code);
                    addressBean.setName(dcn.name);
                    String pinyin = PinyinUtil.getPinyin(addressBean.getName());
                    addressBean.setPinyin(pinyin);
                    addressBeanList.add(addressBean);
                }
            }
        }
        return addressBeanList;
    }

    /**
     * 酒店景区目的地
     * @param cityInfos
     * @return
     */
    public static List<AddressBean> transferNewCityInfoToAddressBeanNotChild(List<Destination> cityInfos){
        List<AddressBean> addressBeanList = new ArrayList<>();
        if(cityInfos == null){
            return addressBeanList;
        }
        for(Destination cityInfo:cityInfos){
            AddressBean addressBean = new AddressBean();
            addressBean.cityCode = String.valueOf(cityInfo.code);
            addressBean.setName(cityInfo.name);
            String pinyin = PinyinUtil.getPinyin(addressBean.getName());
            addressBean.setPinyin(pinyin);
            addressBeanList.add(addressBean);
        }
        return addressBeanList;
    }

    public String toJsonString(){
        return JSONUtils.toJson(this);
    }
}
