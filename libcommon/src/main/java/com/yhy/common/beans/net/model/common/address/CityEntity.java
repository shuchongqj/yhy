package com.yhy.common.beans.net.model.common.address;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityEntity {
	private static final String TAG = CityEntity.class.getSimpleName();
	
	public static final String TAG_LIST = "city";
	
	public static final String TAG_PROVINCE_CODE = "provinceCode";
	
	public static final String TAG_PROVINCE_NAME = "provinceName";
	
	public static final String TAG_CITY_CODE = "cityCode";
	
	public static final String TAG_CITY_NAME = "cityName";
	
	public static final String TAG_AREA_CODE = "areaCode";
	
	public static final String TAG_AREA_NAME = "areaName";
	
	public static final String TAG_CITY_LIST = "mallCityList";
	
	public static final String TAG_AREA_LIST = "mallAreaList";
	
	public List<Province> mProvinceList ;
	public HashMap<String, List<City>> mCityHm = new HashMap<String, List<City>>();
	public HashMap<String, List<Area>> mAreaHm = new HashMap<String, List<Area>>();
	
	public CityEntity(Context context){
		init(context);
	}

	public void init(Context context) {
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		StringBuilder sb=new StringBuilder(2000);
		try {
			inputReader = new InputStreamReader(context
					.getAssets().open("proCityArea.json"));
			bufReader = new BufferedReader(inputReader);
			String line = "";

			while ((line = bufReader.readLine()) != null){
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != inputReader){
					inputReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(null != bufReader){
					bufReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JSONObject jObject = null;
		try {
			jObject = new JSONObject(sb.toString());
			JSONArray provinceArrayObject = null;
			if(jObject.has(TAG_LIST)){
				provinceArrayObject = jObject.getJSONArray(TAG_LIST);
			}
			if(null == provinceArrayObject || provinceArrayObject.length() == 0){
				return;
			}
			
			int provinceArrayLen =  provinceArrayObject.length();
			for(int i =0; i<provinceArrayLen; i++){
				JSONObject provinceObject = provinceArrayObject.getJSONObject(i);
				Province provinceEntity = new Province();
				if(null == provinceObject){
					break;
				}
				if(provinceObject.has(TAG_PROVINCE_CODE)){
					provinceEntity.provinceCode = provinceObject.getString(TAG_PROVINCE_CODE);
				}
				if(provinceObject.has(TAG_PROVINCE_NAME)){
					provinceEntity.provinceName = provinceObject.getString(TAG_PROVINCE_NAME);
				}
				
				JSONArray cityObjectArray = null;
				if(provinceObject.has(TAG_CITY_LIST)){
					cityObjectArray = provinceObject.getJSONArray(TAG_CITY_LIST);
				}
				if(null == cityObjectArray || cityObjectArray.length() == 0){
					break;
				}
				int cityObjectArrayLen = cityObjectArray.length();
				for(int k = 0; k<cityObjectArrayLen; k++){
					JSONObject cityObject = cityObjectArray.getJSONObject(k);
					City cityEntity = new City();
					if(null == cityObject){
						break;
					}
					if(cityObject.has(TAG_CITY_NAME)){
						cityEntity.cityName = cityObject.getString(TAG_CITY_NAME);
					}
					if(cityObject.has(TAG_CITY_CODE)){
						cityEntity.cityCode = cityObject.getString(TAG_CITY_CODE);
					}
					
					//areaList
					JSONArray areaObjectArray = null;
					if(cityObject.has(TAG_AREA_LIST)){
						areaObjectArray = cityObject.getJSONArray(TAG_AREA_LIST);
					}
					if(null == areaObjectArray || areaObjectArray.length() == 0){
//						continue;
					}else{
						int areaObjectArrayLen = areaObjectArray.length();
						for(int j=0; j<areaObjectArrayLen;j++){
							JSONObject areaObject = areaObjectArray.getJSONObject(j);
							Area areaEntity = new Area();
							if(areaObject.has(TAG_AREA_NAME)){
								areaEntity.areaName = areaObject.getString(TAG_AREA_NAME);
							}
							if(areaObject.has(TAG_AREA_CODE)){
								areaEntity.areaCode = areaObject.getString(TAG_AREA_CODE);
							}
							
							if(null == cityEntity.mallAreaList){
								cityEntity.mallAreaList = new ArrayList<Area>();
							}
							
							cityEntity.mallAreaList.add(areaEntity);
						}
					}
					
					mAreaHm.put(cityEntity.cityCode, cityEntity.mallAreaList);
					
					if(null == provinceEntity.mallCityList){
						provinceEntity.mallCityList = new ArrayList<City>();
					}
					provinceEntity.mallCityList.add(cityEntity);
				}
				
				if(null == mProvinceList){
					mProvinceList = new ArrayList<Province>();
				}
				mProvinceList.add(provinceEntity);
				mCityHm.put(provinceEntity.provinceCode, provinceEntity.mallCityList);
			}
//			initLvMamaCityList(context);//DEBUG
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
