package com.hechao.chat;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;


public class MainActivity extends Activity {

    @InjectView(R.id.profile)
    Button profile;
    @InjectView(R.id.conversationlist)
    Button conversationList;
    @InjectView(R.id.btn)
    Button button;
    @InjectView(R.id.getList)
    Button getList;
    @InjectView(R.id.add)
    Button addFriend;

    @InjectView(R.id.navigatorlistview)
    ListView mDrawerList;

    ArrayList<String> list;

    android.support.v4.app.ActionBarDrawerToggle mActionBarDrawerToggle;

    private String[] mPlanetTitles;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //butterKnife注册
        ButterKnife.inject(this);

        //RongIM登录
        connect();

//抽屉导航
//        initMenu();




    }




    /**
     * 打开主页
     */
    @OnClick(R.id.profile)
    public void profile() {
        Intent intent = new Intent(MainActivity.this, UploadPicActivity.class);
        startActivity(intent);
    }


    /**
     * 打开会话列表
     */

    @OnClick(R.id.conversationlist)
    public void getList() {
        RongIM.getInstance().startConversationList(MainActivity.this);
    }


    /**
     * 打开测试对话框
     */
    @OnClick(R.id.btn)
    public void testConversation() {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().startPrivateChat(MainActivity.this, "1275", "hello");
        }
    }


    /**
     * 打开好友列表
     */
    @OnClick(R.id.getList)
    void getFriendList1() {
        Intent intent = new Intent(MainActivity.this, FriendListActivity.class);
        startActivity(intent);
    }


    /**
     * 添加好友
     */
    @OnClick(R.id.add)
    public void addFriend() {
        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }

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
                    Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
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




}













