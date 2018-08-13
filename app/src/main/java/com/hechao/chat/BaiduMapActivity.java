package com.hechao.chat;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Gradient;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.utils.*;
import com.hechao.baidu.PoiOverlay;
import com.hechao.baidu.TransitRouteOverlay;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import butterknife.InjectView;

import static com.hechao.chat.R.*;

public class BaiduMapActivity extends Activity {
    //获取地图控件引用
    MapView mMapView = null;
    BaiduMap mBaiduMap = null;
    private Marker marker = null;
    private Marker mMarkerD = null;
    private TransitRouteLine route = null;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    StationData stationData = new StationData();
    private LatLng point = null;
    boolean isFirstLoc = true;

    TextView mylocation;


    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);
    BitmapDescriptor bitmap3 = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baidumap);
        mMapView = (MapView) findViewById(id.bmapView);
//        设置地图形式
        setMapType();
//        LocationClient定位配置
        initLocationClient();
        initTimer();
        //加油站
//        initStationdata();

// 将底图标注设置为隐藏，方法如下：
//        mBaiduMap.showMapPoi(false);


        //周边
//        initAround();


//        MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
//        LocationData locData = new LocationData();
////手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）
//        locData.latitude = 39.945;
//        locData.longitude = 116.404;
//        locData.direction = 2.0f;
//        myLocationOverlay.setData(locData);
//        mMapView.getOverlays().add(myLocationOverlay);
//        mMapView.refresh();
//        mMapView.getController().animateTo(new GeoPoint((int)(locData.latitude*1e6),
//                (int)(locData.longitude* 1e6)));


//
//        // 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);
//// 构造定位数据
//        MyLocationData locData = new MyLocationData.Builder()
//                .accuracy(location.getRadius())
//                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(100).latitude(location.getLatitude())
//                .longitude(location.getLongitude()).build();
//// 设置定位数据
//        mBaiduMap.setMyLocationData(locData);
//// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);
//        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
//        mBaiduMap.setMyLocationConfiguration();
//// 当不需要定位图层时关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);


        //定义Maker坐标点
//        setMaker();

        //可拖拽图标
//        setDragable();


        //绘制map
//        paintMap();

        //设置动画
//        setAnim();

//        折线多段颜色绘制能力
//        paintLines();

//        文字，在地图中也是一种覆盖物，开发者可利用相关的接口，快速实现在地图上书写文字的需求。实现方式如下：
//        paintText();

//        弹出窗覆盖物的实现方式如下，开发者可利用此接口，构建具有更强交互性的地图页面。
//        paintWindow();

//        地形图图层（GroundOverlay），又可叫做图片图层，即开发者可在地图的指定位置上添加图片。该图片可随地图的平移
//        、缩放、旋转等操作做相应的变换。该图层是一种特殊的Overlay， 它位于底图和底图标注层之间（即该图层不会遮挡地图标注信息）。
//        在地图中添加使用地形图覆盖物的方式如下：
//        paintPIC();

//        热力图是用不同颜色的区块叠加在地图上描述人群分布、密度和变化趋势的一个产品，百度地图SDK将绘制热力图的能力为广大开发者开放，帮助开发者利用自有数据，构建属于自己的热力图，提供丰富的展示效果。
//        利用热力图功能构建自有数据热力图的方式如下：
//        第一步，设置颜色变化：
//        paintHeat();


    }

    /**
     * 周边
     */
    private void initAround() {

        RadarSearchManager mManager = RadarSearchManager.getInstance();


    }


    /**
     * 初始化周边加油站
     */

    Button showStations;
    List<Station> stations;

    private void initStationdata() {
        stationData.juheOil();

        showStations = (Button) findViewById(id.showStations);
        showStations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stations = new ArrayList<Station>();
                stations = stationData.getStationList();
                List<LatLng> pts = new ArrayList<LatLng>();


                for (Station s : stations) {
                    point = new LatLng(s.getLat(), s.getLon());
                    pts.add(point);
                    OverlayOptions option = new MarkerOptions().position(point).icon(bitmap).draggable(true);
                    marker = (Marker) mBaiduMap.addOverlay(option);

                    TextView info = new TextView(getApplicationContext());
                    info.setText(s.getAddress());
                    info.setTextColor(Color.BLACK);
                    final InfoWindow mInfoWindow = new InfoWindow(info, point, -47);
                    //显示InfoWindow
                    mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            mBaiduMap.showInfoWindow(mInfoWindow);
                            return true;
                        }
                    });


                }


                //构建用户绘制多边形的Option对象
                OverlayOptions polygonOption = new PolygonOptions()
                        .points(pts)
                        .stroke(new Stroke(5, 0xAA00FF00))
                        .fillColor(0xAAFFFF00);
                //在地图上添加多边形Option，用于显示
//                mBaiduMap.addOverlay(polygonOption);


                //构造纹理资源
//                BitmapDescriptor custom1 = BitmapDescriptorFactory.fromResource(drawable.rc_ic_text);
                //构造对象
                OverlayOptions ooPolyline = new PolylineOptions().width(30).color(0xAAFF0000).points(pts);
                //添加到地图
                mBaiduMap.addOverlay(ooPolyline);


                //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
                mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
                    public void onMarkerDrag(Marker marker) {
                        //拖拽中
                    }

                    public void onMarkerDragEnd(Marker marker) {
                        //拖拽结束
                    }

                    public void onMarkerDragStart(Marker marker) {
                        //开始拖拽
                    }
                });

            }
        });

    }


    /**
     * 定时器
     */
    long currentTime=0;
    void initTimer(){
        Date now = new Date();
        currentTime=now.getTime();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }


    /**
     * 定位监听器
     */

    private int i = 0;
    double distance = 0;
    List<LatLng> pts = new ArrayList<LatLng>();

    double totalDistance = 0;
    TextView speed;

    private class MyLocationListener implements BDLocationListener {

        public void onReceiveLocation(BDLocation location) {
//            Log.e("Eric", "onReceive...");
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                Log.e("Eric", "location == null || mMapView == null ");
                return;
            }

            if (pts.size() > 1) {
                if (pts.get(pts.size() - 1).latitude != location.getLatitude() ||
                        pts.get(pts.size() - 1).longitude != location.getLongitude()) {
//                    Toast.makeText(getApplicationContext(), "正在移动" + location.getLocationDescribe(), Toast.LENGTH_LONG).show();
                }

            }


//            pts.add(new LatLng(30.123123,114.123123));

            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(16.7f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            } else {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//                Log.e("hechao","首次定位"+ll.toString());
                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(ll);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
//            mylocation.setText(location.getAddrStr());


            //标出当前位置
            point = new LatLng(location.getLatitude(), location.getLongitude());

//            Log.e("hechao", i + point.toString());
            i++;
//            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
//            Toast.makeText(getApplicationContext(), "第" + i + "次定位" + point.toString(), Toast.LENGTH_SHORT).show();
//            mBaiduMap.addOverlay(option);
//            mMapView.removeAllViews();
            long n= new Date().getTime();
            DecimalFormat df = new DecimalFormat( "0.0 ");
            if (pts.size() >= 2) {
                double d = com.baidu.mapapi.utils.DistanceUtil.getDistance(pts.get(pts.size() - 1), point);
                totalDistance+=d;
                Log.e("Eric", "has run " + totalDistance + " meters");
//                Toast.makeText(getApplicationContext(), "第" + i + "次跑了 " + d + " 米", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "跑了 " + totalDistance + " 米", Toast.LENGTH_SHORT).show();
                textView.setText("finished： " + df.format(totalDistance)+ " meters");
                int min=  (  (int)((n-currentTime)/1000)  /60 ) ;
                int sec= (int) ((n-currentTime)/1000-min*60);
                speed.setText("time："+min +"min"+sec+"sec  \n"+"speed："+(double)(Math.round( totalDistance/min *100)/100.0)  +" m/min");
                OverlayOptions ooPolyline = new PolylineOptions().width(20).color(0xAAFF0000).points(pts);
                //添加到地图
                mBaiduMap.addOverlay(ooPolyline);
            }
            pts.add(point);
//            ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
//            giflist.add(bitmap);
//            giflist.add(bitmap2);
//            giflist.add(bitmap3);

//            MarkerOptions ooD = new MarkerOptions().position(point).icons(giflist).zIndex(0).period(10);
//            // 生长动画
//            ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
//            Marker mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));


        }


    }


    /**
     * location
     */
    private void initLocationClient() {
        textView= (TextView) findViewById(id.totalDistance);
        speed= (TextView) findViewById(id.speed);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数


//        mBaiduMap.setMyLocationConfigeration( new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, mCurrentMarker) );


//        LocationClientOption类，该类用来设置定位SDK的定位方式，e.g.：
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);

        //开始定位
        mLocationClient.start();
    }

    /**
     * 绘制热力图
     */
    private void paintHeat() {
        //设置渐变颜色值
        int[] DEFAULT_GRADIENT_COLORS = {Color.rgb(102, 225, 0), Color.rgb(255, 0, 0)};
//设置渐变颜色起始值
        float[] DEFAULT_GRADIENT_START_POINTS = {0.2f, 1f};
//构造颜色渐变对象
        Gradient gradient = new Gradient(DEFAULT_GRADIENT_COLORS, DEFAULT_GRADIENT_START_POINTS);
//        第二步，准备数据：
//以下数据为随机生成地理位置点，开发者根据自己的实际业务，传入自有位置数据即可
        List<LatLng> randomList = new ArrayList<LatLng>();
        Random r = new Random();
        for (int i = 0; i < 500; i++) {
            // 116.220000,39.780000 116.570000,40.150000
            int rlat = r.nextInt(370000);
            int rlng = r.nextInt(370000);
            int lat = 39780000 + rlat;
            int lng = 116220000 + rlng;
            LatLng ll = new LatLng(lat / 1E6, lng / 1E6);
            randomList.add(ll);
        }
//        第三步，添加、显示热力图：
//在大量热力图数据情况下，build过程相对较慢，建议放在新建线程实现
        HeatMap heatmap = new HeatMap.Builder()
                .data(randomList)
                .gradient(gradient)
                .build();
//在地图上添加热力图
        mBaiduMap.addHeatMap(heatmap);
    }


    /**
     * 绘制动画
     */
    private void setAnim() {
        //        自v3.3.0版本起，SDK提供了给Marker增加动画的能力，具体实现方法如下：
// 通过marker的icons设置一组图片，再通过period设置多少帧刷新一次图片资源
//        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
//        giflist.add(bitmap);
//        giflist.add(bitmap);
//        giflist.add(bitmap);
//        OverlayOptions ooD = new MarkerOptions().position(point).icons(giflist)
//                .zIndex(0).period(10);
//        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
//
    }

    /**
     * 地图上添加图片
     */
    private void paintPIC() {
        //定义Ground的显示地理范围
        LatLng southwest = new LatLng(39.92235, 116.380338);
        LatLng northeast = new LatLng(39.947246, 116.414977);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(northeast)
                .include(southwest)
                .build();
//定义Ground显示的图片
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(drawable.ground_overlay);
//定义Ground覆盖物选项
        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(bdGround)
                .transparency(0.8f);
//在地图中添加Ground覆盖物
        mBaiduMap.addOverlay(ooGround);
    }


    /**
     * 地图上弹出窗口
     */
    private void paintWindow() {
        //创建InfoWindow展示的view
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(drawable.popup);
//定义用于显示该InfoWindow的坐标点
        LatLng pt = new LatLng(39.869113, 116.397128);
//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
//显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
    }


    /**
     * 文字
     */
    private void paintText() {
        //定义文字所显示的坐标点
        LatLng llText = new LatLng(39.86123, 116.397448);
//构建文字Option对象，用于在地图上添加文字
        OverlayOptions textOption = new TextOptions()
                .bgColor(0xAAFFFF00)
                .fontSize(24)
                .fontColor(0xFFFF00FF)
                .text("BaiduMapSDK")
                .rotate(-30)
                .position(llText);
//在地图上添加该文字对象并显示
        mBaiduMap.addOverlay(textOption);
    }


    /**
     * 绘制线段
     */
    private void paintLines() {
        BitmapDescriptor custom1 = BitmapDescriptorFactory
                .fromResource(drawable.button_on);
        BitmapDescriptor custom2 = BitmapDescriptorFactory
                .fromResource(drawable.button_down);
        BitmapDescriptor custom3 = BitmapDescriptorFactory
                .fromResource(drawable.button_down);
// 定义点
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        LatLng pt3 = new LatLng(39.89923, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        LatLng pt5 = new LatLng(39.91923, 116.387428);

//构造纹理队列
        List<BitmapDescriptor> customList = new ArrayList<BitmapDescriptor>();
        customList.add(custom1);
        customList.add(custom2);
        customList.add(custom3);

        List<LatLng> points = new ArrayList<LatLng>();
        List<Integer> index = new ArrayList<Integer>();
        points.add(pt1);//点元素
        index.add(0);//设置该点的纹理索引
        points.add(pt2);//点元素
        index.add(0);//设置该点的纹理索引
        points.add(pt3);//点元素
        index.add(1);//设置该点的纹理索引
        points.add(pt4);//点元素
        index.add(2);//设置该点的纹理索引
        points.add(pt5);//点元素
//构造对象
        OverlayOptions ooPolyline = new PolylineOptions().width(15).color(0xAAFF0000).points(points).customTextureList(customList).textureIndex(index);
//添加到地图
        mBaiduMap.addOverlay(ooPolyline);
    }

    /**
     * 绘制图形覆盖
     */

    private void paintMap() {
        //        地图SDK提供多种结合图形覆盖物，利用这些图形，可帮助您构建更加丰富多彩的地图应用。目前提供的几何图形有：点（Dot）、折线（Polyline）、弧线（Arc）、圆（Circle）、多边形（Polygon）。
//        下面以多边形为例，向大家介绍如何使用几何图形覆盖物：
//定义多边形的五个顶点
//        LatLng pt1 = new LatLng(39.93923, 116.357428);
//        LatLng pt2 = new LatLng(39.91923, 116.327428);
//        LatLng pt3 = new LatLng(39.89923, 116.347428);
//        LatLng pt4 = new LatLng(39.89923, 116.367428);
//        LatLng pt5 = new LatLng(39.91923, 116.387428);
//        List<LatLng> pts = new ArrayList<LatLng>();
//        pts.add(pt1);
//        pts.add(pt2);
//        pts.add(pt3);
//        pts.add(pt4);
//        pts.add(pt5);
////构建用户绘制多边形的Option对象
//        OverlayOptions polygonOption = new PolygonOptions()
//                .points(pts)
//                .stroke(new Stroke(5, 0xAA00FF00))
//                .fillColor(0xAAFFFF00);
////在地图上添加多边形Option，用于显示
//        mBaiduMap.addOverlay(polygonOption);
//
    }


    /**
     * 设置图标
     */
    private void setMaker() {
        point = new LatLng(30.374455, 114.338671);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(drawable.location);
        //构建MarkerOption，用于在地图上添加Marker
//        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
//        mBaiduMap.addOverlay(option);
    }


    /**
     * 设置可拖拽
     */

    private void setDragable() {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(drawable.location);
        //        第一步，设置可拖拽：
        OverlayOptions options = new MarkerOptions()
                .position(point)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽
        //将marker添加到地图上
        marker = (Marker) (mBaiduMap.addOverlay(options));

        //调用Marker对象的remove方法实现指定marker的删除
        //        marker.remove();


//        第二步，设置监听方法

        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                //拖拽中

            }

            public void onMarkerDragEnd(Marker marker) {
                //拖拽结束
            }

            public void onMarkerDragStart(Marker marker) {
                //开始拖拽
            }
        });
    }


    //    第二步，配置定位SDK参数
//    设置定位参数包括：定位模式（高精度定位模式，低功耗定位模式和仅用设备定位模式），返回坐标类型，是否打开GPS，是否返回地址信息、位置语义化信息、POI信息等等。
//    LocationClientOption类，该类用来设置定位SDK的定位方式，e.g.：
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }


//        利用TransitRouteOverlay展示公交换乘结果：
//在公交线路规划回调方法中添加TransitRouteOverlay用于展示换乘信息

//    public void onGetTransitRouteResult(TransitRouteResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            //未找到结果
//            return;
//        }
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            //result.getSuggestAddrInfo()
//            return;
//        }
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            route = result.getRouteLines().get(0);
//            //创建公交路线规划线路覆盖物
//            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
//            //设置公交路线规划数据
//            overlay.setData(route);
//            //将公交路线规划覆盖物添加到地图中
//            overlay.addToMap();
//            overlay.zoomToSpan();
//        }
//    }
//


//        OpenGL绘制功能
//
//        百度地图SDK为广大开发者开放了OpenGL绘制接口，帮助开发者在地图上实现更灵活的样式绘制，丰富地图使用效果体验。
//        下面将以在地图上绘制折线为例，向大家介绍如何使用OpenGL绘制接口：
// 定义地图绘制每一帧时 OpenGL 绘制的回调接口
//        BaiduMap.OnMapDrawFrameCallback callback = new BaiduMap.OnMapDrawFrameCallback() {
//
//            public void onMapDrawFrame(GL10 gl, MapStatus drawingMapStatus) 	{
//                if (mBaiduMap.getProjection() != null) {
//                    // 计算折线的 opengl 坐标
//                    calPolylinePoint(drawingMapStatus);
//                    // 绘制折线
//                    drawPolyline(gl, Color.argb(255, 255, 0, 0), vertexBuffer, 10, 3, drawingMapStatus);
//                }
//            }
//        };
//
//// 设置地图绘制每一帧时的回调接口
//        mMapView = (MapView) findViewById(R.id.bmapView);
//        mBaiduMap = mMapView.getMap();
//        mBaiduMap.setOnMapDrawFrameCallback((BaiduMap.OnMapDrawFrameCallback) this);
//
//        // 计算折线 OpenGL 坐标
//        public void calPolylinePoint(MapStatus mspStatus) {
//            PointF[] polyPoints = new PointF[latLngPolygon.size()];
//            vertexs = new float[3 * latLngPolygon.size()];
//            int i = 0;
//            for (LatLng xy : latLngPolygon) {
//                // 将地理坐标转换成 openGL 坐标
//                polyPoints[i] = mBaiduMap.getProjection().toOpenGLLocation(xy, mspStatus);
//                vertexs[i * 3] = polyPoints[i].x;
//                vertexs[i * 3 + 1] = polyPoints[i].y;
//                vertexs[i * 3 + 2] = 0.0f;
//                i++;
//            }
//            for (int j = 0; j < vertexs.length; j++) {
//                Log.e("hechao", "vertexs[" + j + "]: " + vertexs[j]);
//            }
//            vertexBuffer = makeFloatBuffer(vertexs);
//        }
//
////创建OpenGL绘制时的顶点Buffer
//        private FloatBuffer makeFloatBuffer(float[] fs) {
//            ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
//            bb.order(ByteOrder.nativeOrder());
//            FloatBuffer fb = bb.asFloatBuffer();
//            fb.put(fs);
//            fb.position(0);
//            return fb;
//        }
//
//// 绘制折线
//        private void drawPolyline(GL10 gl, int color, FloatBuffer lineVertexBuffer, float lineWidth, int pointSize, MapStatus drawingMapStatus) {
//
//            gl.glEnable(GL10.GL_BLEND);
//            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//
//            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//
//            float colorA = Color.alpha(color) / 255f;
//            float colorR = Color.red(color) / 255f;
//            float colorG = Color.green(color) / 255f;
//            float colorB = Color.blue(color) / 255f;
//
//            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVertexBuffer);
//            gl.glColor4f(colorR, colorG, colorB, colorA);
//            gl.glLineWidth(lineWidth);
//            gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, pointSize);
//
//            gl.glDisable(GL10.GL_BLEND);
//            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        }
//
//


    /**
     * 设置地图形式
     */

    private void setMapType() {
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();

//        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
//        mBaiduMap.setMyLocationEnabled(true);

        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        //卫星地图
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);

        //开启交通图
//        mBaiduMap.setTrafficEnabled(true);

        //开启城市交通热力图
//        mBaiduMap.setBaiduHeatMapEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mMapView.onPause();
    }


}



















