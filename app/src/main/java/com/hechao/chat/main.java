package com.hechao.chat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2018/7/10.
 */

public class main extends Activity {

    //获取地图控件引用
    MapView mMapView = null;
    BaiduMap mBaiduMap = null;
    private Marker marker = null;
    private Marker mMarkerD = null;
    private TransitRouteLine route = null;


    boolean First = true;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    StationData stationData = new StationData();
    private LatLng point = null;
    boolean isFirstLoc = true;

    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
    BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
    BitmapDescriptor bitmap3 = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
    private TextView textView;

    @InjectView(R.id.myrun)
    ImageButton myrun;
    double speed1 = 0;
    private boolean isstoped = false;
    private boolean isSwitched = false;
    private boolean runningstatus = false;
    private ImageView personpic;
    private TextView personname;
    private TextView personinfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ButterKnife.inject(main.this);
        //RongIM登录
        connect();
        isFirstLoc=true;
        mMapView = (MapView) findViewById(R.id.bmapView);
//        设置地图形式
        setMapType();
        initLocation();
//        LocationClient定位配置
//        initLocationClient();
//        initTimer();
        //加油站
//        initStationdata();

// 将底图标注设置为隐藏，方法如下：
//        mBaiduMap.showMapPoi(false);


        //周边
//        initAround();


        //活跃状态监听
        online();


//        getFragmentManager().beginTransaction().replace(R.id.runinfofragment,fragment).commit();


    }

    void online() {


        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            /**
             * 设置连接状态变化的监听器.
             */
            RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener());
        }


        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return true;
            }
        });

        listener = new MarkerClickedListener();
        mBaiduMap.setOnMarkerClickListener(listener);


    }


    MarkerClickedListener listener = null;


    /**
     * infowindow监听器
     */
    private class MarkerClickedListener implements BaiduMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
//            Toast.makeText(main.this, "打个招呼", Toast.LENGTH_SHORT).show();

            Button button = new Button(getApplicationContext());
            button.setBackgroundResource(R.drawable.popup);
//            FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(220, 180);
//            lytp.gravity = Gravity.CENTER;
//            button.setLayoutParams(lytp);
//定义用于显示该InfoWindow的坐标点
            LatLng pt = marker.getPosition();
//            marker.getExtraInfo();
//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
//            View view= LayoutInflater.from(main.this).inflate(R.layout.infowindow,null);

            TextView infor = new TextView(main.this);
            infor.setBackgroundResource(R.drawable.popup);
            infor.setText("Check people around you");
            infor.setTextColor(getResources().getColor(R.color.accent_material_dark));
//            InfoWindow mInfoWindow = new InfoWindow(infor, pt,-47);


//            new InfoWindow.OnInfoWindowClickListener() {
//                @Override
//                public void onInfoWindowClick() {
//
//                }
//            };


            View linlayout = main.this.getLayoutInflater().inflate(R.layout.infowindow, null);//定义用于显示该InfoWindow的坐标点
//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
            InfoWindow mInfoWindow = new InfoWindow(linlayout, pt, -47);

            personpic = (ImageView) linlayout.findViewById(R.id.personpic);
            personname = (TextView) linlayout.findViewById(R.id.personname);
            personinfo = (TextView) linlayout.findViewById(R.id.personinfo);

            final Bundle extraInfo = marker.getExtraInfo();

            try {
                setImage("http://" + App.ip + "/chat/pic/" + extraInfo.getString("username") + ".png");
            } catch (Exception e) {
                e.printStackTrace();
            }

            personname.setText(extraInfo.getString("name"));
//            personname.setText("呵呵哒");

            personinfo.setText("Check his/her page");
            personpic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(main.this, FriendProfile.class);
                    Bundle args = new Bundle();
                    args.putString("username", extraInfo.getString("username"));
                    intent.putExtras(args);
                    startActivity(intent);
                }
            });


//显示InfoWindow
//            mBaiduMap.showInfoWindow(mInfoWindow);
//显示InfoWindow
            mBaiduMap.showInfoWindow(mInfoWindow);

            return true;

        }
    }


    void setImage(String address) throws Exception {
        //通过代码 模拟器浏览器访问图片的流程

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(address, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                personpic.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });


    }

    private class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {
        @Override
        public void onChanged(ConnectionStatus connectionStatus) {
            switch (connectionStatus) {
                case CONNECTED://连接成功。
//                    Log.e("hechao", "CONNECTED");
                    break;
                case DISCONNECTED://断开连接。
                    Log.e("hechao", "DISCONNECTED");
                    break;
                case CONNECTING://连接中。
                    Log.e("hechao", "CONNECTING");
                    break;
                case NETWORK_UNAVAILABLE://网络不可用。
                    Log.e("hechao", "NETWORK_UNAVAILABLE");
                    break;
                case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                    Log.e("hechao", "User login elsewhere, loging out");
                    break;
            }
        }
    }


    double time = 0;

    /**
     * RongIM登录
     */
    private void connect() {
        String token = App.token;
        if (App.isLogin) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
//                    Toast.makeText(MainActivity.this, "no success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String s) {
                    Toast.makeText(main.this, "Login Successfully", Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {


//                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });


//            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//                @Override
//                public UserInfo getUserInfo(String s) {
//                    UserInfo userInfo = new UserInfo(s, s, Uri.parse("http://img.ltn.com.tw/Upload/ent/page/800/2015/04/03/phpHOkTWG.jpg"));
//                    return userInfo;
//                }
//            }, false);

        }
    }

//    @InjectView(R.id.start)
//    GifView gifView;

    @InjectView(R.id.start)
    ImageButton start;


    @OnClick(R.id.start)
    void start() {
        runningstatus = !runningstatus;
        start.setBackgroundResource(R.drawable.run);
        textView = (TextView) findViewById(R.id.totalDistance);
        speed = (TextView) findViewById(R.id.speed);

        if (!runningstatus) {


            start.setBackgroundResource(R.drawable.start);
            isstoped = true;


            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("speed", speed1 + "");
            params.add("far", (int) totalDistance + "");
            params.add("time", (int) time + "");
            params.add("username", App.username);
            String url = "http://" + App.ip + "/chat/saveRecord.php";


            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String response = new String(bytes);
                    Log.e("hechao", "saverecord success " + response);
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                }

            });


            AlertDialog alertDialog = new AlertDialog.Builder(main.this).create();
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.dialog_main_info);
            TextView tv_title = (TextView) window.findViewById(R.id.information);
            tv_title.setText("Congratulations！！！ \n \nYou've run " + (int) totalDistance + "meters，Avg speed isc" + speed1 + "m/min，Spend " + (int) (time / 60) + "mins " + ((int) time - (int) (time / 60)) + "secs \n \n");


            speed1 = 0;
            totalDistance = 0;
            time = 0;


//            OverlayOptions option = new MarkerOptions().position(new LatLng(App.x, App.y)).icon(bitmap2);
//            mBaiduMap.addOverlay(option);

        } else {
            initLocationClient();
            initTimer();
        }

        initOnline();
    }

    /**
     * 查看活跃人数
     */
    void initOnline() {


    }


    @OnClick(R.id.myrun)
    void setMyrun() {

        Intent intent = new Intent(main.this, Myrun.class);
        startActivity(intent);

    }


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
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        //卫星地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);

        //开启交通图
//        mBaiduMap.setTrafficEnabled(true);

        //开启城市交通热力图
//        mBaiduMap.setBaiduHeatMapEnabled(true);
    }


    @InjectView(R.id.switchMap)
    TextView s;

    @OnClick(R.id.switchMap)
    void switchMap() {
//        普通地图
        if (!isSwitched) {
            s.setText("Switch to satelite view");
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

            isSwitched = !isSwitched;
        } else {
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            isSwitched = !isSwitched;
            s.setText("Switch to classic view");
        }

    }


    @OnClick(R.id.friend)
    void friend() {
        Intent intent = new Intent(main.this, FriendStatusActivity.class);
        startActivity(intent);

    }


    /**
     * 定位监听器
     */

    private int i = 0;
    double distance = 0;
    List<LatLng> pts = new ArrayList<LatLng>();

    double totalDistance = 0;
    TextView speed;

    JSONArray jsonArray = new JSONArray();

    private class MyLocationListener implements BDLocationListener {

        public void onReceiveLocation(BDLocation location) {
//            Log.e("hechao", "onReceive...");
            // map view 销毁后不在处理新接收的位置


            App.x = location.getLatitude();
            App.y = location.getLongitude();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            if (runningstatus) {
                String url = "http://" + App.ip + "/chat/onlinelist.php?" + "username=" + App.username + "&x=" + App.x + "&y=" + App.y;
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                    Log.e("hechao", "位置已上传：username=" + App.username + "&x=" + App.x + "&y=" + App.y);
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                    }
                });
            }

            if (location == null || mMapView == null) {
                Log.e("hechao", "location == null || mMapView == null ");
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
                builder.target(ll).zoom(18.1f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                //显示活跃用户
//                String url = "http://" + App.ip + "/chat/getAllOnline.php";
//                client.get(url, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                        String response = new String(bytes);
//                        try {
//                            jsonArray = new JSONArray(response);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            JSONArray jarr = new JSONArray(response);
//                            for (int k = 0; k < jarr.length(); k++) {
//                                JSONObject json = (JSONObject) jarr.get(k);
//                                final String username = json.getString("username");
//                                double x = json.getDouble("x");
//                                double y = json.getDouble("y");
//                                String name = json.getString("name");
//                                LatLng l = new LatLng(x, y);
//                                Bundle bundle = new Bundle();
//
//                                bundle.putString("username", username);
//                                bundle.putString("name", name);
//
//                                OverlayOptions option = new MarkerOptions().position(l).icon(bitmap).extraInfo(bundle);
//                                mBaiduMap.addOverlay(option);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                    }
//                });


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

//
//            Log.e("hechao", First + "");
//            if (First) {
//                Log.e("hechao", jsonArray.toString());
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    try {
////                        Log.e("hechao", jsonArray.toString());
//                        JSONObject json = (JSONObject) jsonArray.get(i);
//                        String username = json.getString("username");
//                        double x = json.getDouble("x");
//                        double y = json.getDouble("y");
//
//                        Log.e("hechao", username + x + " " + y);
//                        LatLng l = new LatLng(x, y);
//                        OverlayOptions option = new MarkerOptions().position(l).icon(bitmap);
//                        mBaiduMap.addOverlay(option);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                First = false;
//            }

            long n = new Date().getTime();
            DecimalFormat df = new DecimalFormat("0.0 ");
            if (pts.size() >= 2 && runningstatus) {
                double d = com.baidu.mapapi.utils.DistanceUtil.getDistance(pts.get(pts.size() - 1), point);
                totalDistance += d;
//                Log.e("hechao", "跑了 " + totalDistance + " 米");
//                Toast.makeText(getApplicationContext(), "第" + i + "次跑了 " + d + " 米", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "跑了 " + totalDistance + " 米", Toast.LENGTH_SHORT).show();

                textView.setText("Finished： " + (int) totalDistance + " meters");

                int min = ((int) ((n - currentTime) / 1000) / 60);
                int sec = (int) ((n - currentTime) / 1000 - min * 60);
                time = min * 60 + sec;
                speed1 = Math.round(totalDistance / time * 100) / 100.0*60;

                speed.setText("Time：" + min + "min" + sec + "sec  \n" + "Speed：" + speed1 + " m/min");

                OverlayOptions ooPolyline = new PolylineOptions().width(20).color(0xAAFF0000).points(pts);
                //添加到地图
                mBaiduMap.addOverlay(ooPolyline);
            } else {
                textView.setText("");
                speed.setText("Click to start running, Click again to stop");
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

//        textView = (TextView) findViewById(R.id.totalDistance);
//        speed = (TextView) findViewById(R.id.speed);
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
     * location1
     */
    private void initLocation() {
        textView = (TextView) findViewById(R.id.totalDistance);
        speed = (TextView) findViewById(R.id.speed);
        // 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);

//        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
//        mLocationClient.registerLocationListener(myListener);    //注册监听函数


//        mBaiduMap.setMyLocationConfigeration( new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, mCurrentMarker) );


//        LocationClientOption类，该类用来设置定位SDK的定位方式，e.g.：
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
////        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        mLocationClient.setLocOption(option);

        LatLng ll = new LatLng(30.380489, 114.345394);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.1f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        //显示活跃用户
        String url = "http://" + App.ip + "/chat/getAllOnline.php";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String response = new String(bytes);
                try {
                    jsonArray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray jarr = new JSONArray(response);
                    for (int k = 0; k < jarr.length(); k++) {
                        JSONObject json = (JSONObject) jarr.get(k);
                        final String username = json.getString("username");
                        double x = json.getDouble("x");
                        double y = json.getDouble("y");
                        String name=json.getString("name");
                        LatLng l = new LatLng(x, y);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",name);
                        bundle.putString("username", username);
                        OverlayOptions option = new MarkerOptions().position(l).icon(bitmap).extraInfo(bundle).draggable(true);
                        mBaiduMap.addOverlay(option);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            }
        });


        //开始定位
//        mLocationClient.start();
    }


    /**
     * 定时器
     */
    long currentTime = 0;

    void initTimer() {
        Date now = new Date();
        currentTime = now.getTime();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }


}
