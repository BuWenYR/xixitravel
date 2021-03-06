package com.example.zhengjun.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import overlayutil.WalkingRouteOverlay;

public class MerchantLocationActivity extends Activity {

    private ImageView mer_back;
    private TextView mer_title;
    private String latatitude = "";
    private String longtitude = "";
    private String title;
    private BaiduMap mBaiduMap = null;
    private MapView mMapView = null;
    private LocationClient mLocationClient;
    private RoutePlanSearch routSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);
        init();
    }

    private void init() {
        title = getIntent().getExtras().getString("mer_name");
        longtitude = getIntent().getExtras().getString("mer_long");
        latatitude = getIntent().getExtras().getString("mer_lat");
        mer_back = (ImageView) findViewById(R.id.merchant_back);
        mer_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        mer_title = (TextView) findViewById(R.id.mer_title);
        mer_title.setText(title);

        mMapView = (MapView) findViewById(R.id.mer_map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        LatLng point = new LatLng(30.326912, 120.35043);
        //?????? Marker ??????
        BitmapDescriptor bitmap =
                BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        //?????? MarkerOption??????????????????????????? Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //?????????????????? Marker????????????
        mBaiduMap.addOverlay(option);

        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(this);
        initLocation();
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                Log.e("location Type", location.getLocType() + "");
                Toast.makeText(MerchantLocationActivity.this,
                        "location type" + location.getLocType(), Toast.LENGTH_SHORT).show();
//mapView ???????????????????????????????????????
                if (location == null || mMapView == null){
                    return;
                }

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(location.getDirection()) // ????????????????????????????????????????????????????????? 0-360
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                mBaiduMap.setMyLocationData(locData);
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        });
        mLocationClient.start();

        routSearch = RoutePlanSearch.newInstance();
        LatLng endCenter = new LatLng((Float.valueOf(latatitude) / 10000000), (Float.valueOf(longtitude) / 10000000));
        PlanNode startNode = PlanNode.withCityNameAndPlaceName("??????", "??????????????????");
        PlanNode endNode = PlanNode.withLocation(endCenter);
        routSearch.setOnGetRoutePlanResultListener(routListener);
        routSearch.walkingSearch((new WalkingRoutePlanOption()).from(startNode).to(endNode));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //??? activity ?????? onResume ????????? mMapView. onResume ()?????????????????????????????????
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //??? activity ?????? onPause ????????? mMapView. onPause ()?????????????????????????????????
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        if (routSearch != null) {
            routSearch.destroy();
        }
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//??????????????? false,?????????????????? gps
        option.setScanSpan(0);//??????????????? 0??????????????????????????????????????????????????????????????????????????? 1000ms ???????????????
        option.setCoorType("bd09ll");//??????????????? gcj02???????????????????????????????????????
        mLocationClient.setLocOption(option);
    }

    OnGetRoutePlanResultListener routListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            // TODO Auto-generated method stub
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MerchantLocationActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //?????????????????????????????????????????????????????????????????????????????????
                //result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                WalkingRouteLine routeLine = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(routeLine);
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }
        @Override
        public void onGetTransitRouteResult(TransitRouteResult arg0) {

        }
        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult arg0) {

        }
        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
        }
        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
        }
        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        }
    };

}