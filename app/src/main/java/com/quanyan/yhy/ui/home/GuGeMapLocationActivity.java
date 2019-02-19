package com.quanyan.yhy.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavTitleView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.home.view.ChoiceNavigationPopView;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 高德地图定位 on 2015/11/10.
 */
public class GuGeMapLocationActivity extends BaseActivity implements AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener,
        AMap.OnMarkerDragListener, AMap.OnMapLoadedListener, OnClickListener, AMap.InfoWindowAdapter {

    private MarkerOptions markerOption, markerCurrent;

    private AMap aMap;
    private MapView mapView;
    private Marker marker2;// 有跳动效果的marker对象
    private LatLng latlng = new LatLng(36.061, 103.834);
    private double longitude, latitude;
    private LatLng HotelLatLng;
    private boolean type;
    private String address;
    private String mTitle;
    /**
     * 查看大的地图
     *
     * @param context
     */
    public static void gotoViewBigMapView(Context context, String title,double lat, double lng, boolean type, String address) {
        Intent intent = new Intent(context, GuGeMapLocationActivity.class);
        if(!StringUtil.isEmpty(title)){
            intent.putExtra(SPUtils.EXTRA_TITLE, title);
        }
        intent.putExtra(SPUtils.EXTRA_CURRENT_LAT, lat);
        intent.putExtra(SPUtils.EXTRA_CURRENT_LNG, lng);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_ADDRESS, address);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this,R.layout.activity_multy_location, null);
    }

    private BaseNavTitleView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavTitleView(this);
        mBaseNavView.setRightImg(R.mipmap.icon_map_navigation);
        mBaseNavView.setRightImgClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCacheDialog();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        try {
            latitude = intent.getDoubleExtra(SPUtils.EXTRA_CURRENT_LAT, 0);
            longitude = intent.getDoubleExtra(SPUtils.EXTRA_CURRENT_LNG, 0);
            address = intent.getStringExtra(SPUtils.EXTRA_ADDRESS);
            mTitle = intent.getStringExtra(SPUtils.EXTRA_TITLE);
        } catch (Exception e) {
            ToastUtil.showToast(GuGeMapLocationActivity.this, "坐标错误");
            finish();
        }
        type = intent.getBooleanExtra(SPUtils.EXTRA_TYPE, false);//是否显示当前位置mark，true显示
        HotelLatLng = new LatLng(latitude, longitude);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        init();
        if(!StringUtil.isEmpty(mTitle)){
            mBaseNavView.setTitleText(mTitle);
        }
    }

    /**
     * 初始化AMap对象
     */
    private void init() {

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
        addMarkersToMap();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        markerOption = new MarkerOptions();
        markerOption.position(HotelLatLng);
        if (!StringUtil.isEmpty(address)) {
            markerOption.title("1").snippet("");
//            markerOption.perspective(true);
            markerOption.draggable(true);
        }
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.location_marker)));
//        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
//        markerOptionlst.add(markerOption);
        aMap.addMarker(markerOption);

        try {
            LatLng current = new LatLng(Double.valueOf(SPUtils.getExtraCurrentLat(GuGeMapLocationActivity.this)), Double.valueOf(SPUtils.getExtraCurrentLon(GuGeMapLocationActivity.this)));
            markerCurrent = new MarkerOptions()
                    .position(current)
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                            R.mipmap.location_marker_current)));
        } catch (Exception e) {
        }

        if (type == false || markerCurrent == null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(HotelLatLng.latitude, HotelLatLng.longitude), 14));
        } else {
//            markerOptionlst.add(markerCurrent);
            marker2= aMap.addMarker(markerCurrent);
            marker2.showInfoWindow();
        }

//        List<Marker> markerlst = aMap.addMarkers(markerOptionlst, true);
//        if(markerlst!=null&&markerlst.size()>0){
//            marker2 = markerlst.get(0);
//            marker2.showInfoWindow();
//        }

    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(marker2)) {
            if (aMap != null) {
            }
        }
        return false;
    }


    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    /**
     * 监听拖动marker时事件回调
     */
    @Override
    public void onMarkerDrag(Marker marker) {

    }

    /**
     * 监听拖动marker结束事件回调
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
    }

    /**
     * 监听开始拖动marker事件回调
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {

//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(HotelLatLng.latitude, HotelLatLng.longitude), 14));

    }

    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    @Override
    public View getInfoContents(Marker marker) {

        View infoContent = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        render(marker, infoContent);
        return infoContent;
    }

    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    @Override
    public View getInfoWindow(Marker marker) {

        View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);

        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        TextView titleUi = ((TextView) view.findViewById(R.id.win_hotel_snippet));
        titleUi.setText(address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    /**
     * 弹出是否缓存的确认对话框
     */
    private ChoiceNavigationPopView mCacheDlg;

    /***
     * 导航选择
     */
    private void showCacheDialog() {
        if (mCacheDlg == null) {
            mCacheDlg = new ChoiceNavigationPopView(this, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.dialog_map_baidu:

                            ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask();
                            asyncTask.execute(1000);
                            mCacheDlg.dismiss();
                            break;
                        case R.id.dialog_map_gaode:
                            openGaoDeMap();
                            mCacheDlg.dismiss();
                            break;
                    }
                }
            });
        }
        if (!isAppInstalled("com.autonavi.minimap")&&!isAppInstalled("com.baidu.BaiduMap")) {
            ToastUtil.showToast(this, getString(R.string.label_map_isinstall));
        } else {
            //显示窗口
            mCacheDlg.showAtLocation(mapView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

    }

    private boolean isAppInstalled(String uri) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }


    /***
     * 高德导航
     */
    private void openGaoDeMap() {
        try {
            String strUrl = "androidamap://navi?sourceApplication=appname&poiname=" + "" + "&lat=" + latitude + "&lon=" + longitude + "&dev=1&style=2";
            Uri uri = Uri.parse(strUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setPackage("com.autonavi.minimap");
            intent.setData(uri);
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    /***
     * 百度导航
     */
    private void openBaiDuMap(String url) {
        try {

            Intent intent = Intent.getIntent(url);
            startActivity(intent);

        } catch (Exception e) {
        }
    }


    public class ProgressBarAsyncTask extends AsyncTask<Integer, Integer, String> {

        public ProgressBarAsyncTask() {
            super();

        }

        @Override
        protected String doInBackground(Integer... params) {

            return translatedBaiDuMap();
        }


        @Override
        protected void onPostExecute(String result) {
            hideLoadingView();
            openBaiDuMap(result);
        }


        @Override
        protected void onPreExecute() {
            showLoadingView(getString(R.string.loading_text));
        }
    }


    /***
     * 百度坐标转换
     */
    private String translatedBaiDuMap() {
        String url = "http://api.map.baidu.com/geoconv/v1/?coords=" + SPUtils.getExtraCurrentLat(this) + "," + SPUtils.getExtraCurrentLon(this) + ";" + latitude + "," + longitude + "&from=3&to=5&ak=" + ValueConstants.BAIDU_MAP_AK + "&output=json&mcode=" + ValueConstants.BAIDU_MAP_MCODE;
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse response = httpClient.execute(httpGet);
             showResponseResult(response);
            if (!StringUtil.isEmpty(startLat)&&!StringUtil.isEmpty(startLong)&&!StringUtil.isEmpty(endLat)&&!StringUtil.isEmpty(endLong)) {
                String URL = "intent://map/direction?origin=latlng:" + startLat+","+startLong + "|name:起点&destination=latlng:" + endLat+","+endLong + "|name:终点&mode=driving&src=" + getString(R.string.label_quanyan) + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
                return URL;
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    /**
     * @param response
     */
    String startLat, startLong, endLat, endLong;

    private void showResponseResult(HttpResponse response) {
        if (null == response) {
            return ;
        }

        HttpEntity httpEntity = response.getEntity();
        try {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine())) {
                result += line;
            }

            JSONObject jsonObject = new JSONObject(result);
            int resultCode = jsonObject.getInt("status");
            if (resultCode == 0) {
                JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                JSONObject startResultJsonObject = resultJsonArray.getJSONObject(0);
                if (startResultJsonObject != null) {
                    startLat = startResultJsonObject.getString("x");
                    startLong =  startResultJsonObject.getString("y");
                }

                JSONObject endResultJsonObject = resultJsonArray.getJSONObject(1);
                if (endResultJsonObject != null) {
                    endLat =  endResultJsonObject.getString("x");
                    endLong = endResultJsonObject.getString("y");
                }
            } else {
                //转换失败
//                ToastUtil.showToast(GuGeMapLocationActivity.this, getString(R.string.label_map_translated_xy));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
