package com.example.tr.tourhear;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.tr.tourhear.utils.AMapUtil;
import com.example.tr.tourhear.utils.PoiOverlay;
import com.example.tr.tourhear.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangYan on 2017/7/25.
 */

public class CarGroupMapActivity extends Activity implements AMap.OnMyLocationChangeListener, View.OnKeyListener, TextWatcher, AMap.InfoWindowAdapter, PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener, AMap.OnCameraChangeListener {
    MapView mMapView = null;
    AMap aMap;
    MyLocationStyle myLocationStyle;
    boolean FirstLocationChange = true;
    double lat = 0, lon = 0;
    Marker marker;
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private String editCity = "成都";// 要输入的城市名字或者城市区号
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    DisplayMetrics dm = new DisplayMetrics();
    private AMap.OnCameraChangeListener cameraChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_group_map);
        LinearLayout bt_back = (LinearLayout) findViewById(R.id.btn_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnCameraChangeListener(this);
    }

    private void setUpMap() {
        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle);

        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //定位模式
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER));

        searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
        searchText.setOnKeyListener(this);
        searchText.addTextChangedListener(this);
        /*aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件*/
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
    }

    /*AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            View infoWindow = getLayoutInflater().inflate(R.layout.item_info_window, null);//display为自定义layout文件
            TextView name = (TextView) infoWindow.findViewById(R.id.name);
            name.setText(marker.getTitle());
            LatLng l = marker.getPosition();// 获取标签的位置
            TextView dis = (TextView) infoWindow.findViewById(R.id.mess);
            float distance = ((float) (int) AMapUtils.calculateLineDistance(l, new LatLng(lat, lon))) / 1000;// 调用函数计算距离
            dis.setText("距离 " + distance + " 千米");
            ImageView img = (ImageView) infoWindow.findViewById(R.id.image);
            img.setImageResource(R.drawable.timg);
            //此处省去长篇代码
            return infoWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    };*/

/*    private void SetMarker(double _lat, double _lon, String name) {
        aMap.setInfoWindowAdapter(infoWindowAdapter);
        LatLng latlng = new LatLng(_lat, _lon);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latlng);
        markerOption.title(name);
        markerOption.visible(true);
        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.marker_member)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();

    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            if (FirstLocationChange) {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14));
                marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_here))));
                marker.setPositionByPixels(dm.widthPixels / 2, dm.heightPixels / 2);
                /*for (int i = 0; i < num; i++)
                    SetMarker(_lat[i], _lon[i], name[i]);*/
                FirstLocationChange = false;
            }
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                /*
                errorCode
                errorInfo
                locationType
                */
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
        }
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", editCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(1);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public View getInfoWindow(Marker marker) {

        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        return view;
   //     return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, editCity);
            Inputtips inputTips = new Inputtips(CarGroupMapActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }


    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                   /* List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息*/

                    if (poiItems != null && poiItems.size() > 0) {
                        /*aMap.clear();// 清理之前的图标*/
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        /*poiOverlay.removeFromMap();
                        poiOverlay.addToMap();*/
                        poiOverlay.zoomToSpan();
                    } else {
                        ToastUtil.show(CarGroupMapActivity.this,
                                "对不起，没有搜索到相关数据！");
                    }
                }
            } else {
                ToastUtil.show(CarGroupMapActivity.this,
                        "对不起，没有搜索到相关数据！");
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showerror(this, rCode);
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        //原searchbutton
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            keyWord = AMapUtil.checkEditText(searchText);
            if ("".equals(keyWord)) {
                ToastUtil.show(CarGroupMapActivity.this, "请输入搜索关键字");
                return false;
            } else {
                doSearchQuery();
            }
        }
        return false;
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.e("amap", "change");
        marker.hideInfoWindow();
        marker.setTitle("加载中...");
        marker.setSnippet(null);
        marker.showInfoWindow();
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        SearchAround(cameraPosition);
    }

    public void SearchAround(CameraPosition position) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);// 传入context
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(position.target.latitude, position.target.longitude), 1000, GeocodeSearch.AMAP);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            /**
             * 逆地理编码回调
             */
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                List<PoiItem> poiItems = regeocodeResult.getRegeocodeAddress().getPois();
                for (PoiItem poiItem : poiItems) {
                    Log.e("amap", poiItem.getTitle());
                    marker.hideInfoWindow();
                    marker.setTitle(poiItem.getTitle());
                    marker.setSnippet(poiItem.getSnippet());
                    marker.showInfoWindow();
                    break;
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        geocodeSearch.getFromLocationAsyn(query);
    }
}
