package com.hechao.chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/7/10.
 */
public class FriendProfile extends Activity {


    String username = null;
    TextView friendname = null;
    Button startPrivate = null;


    @InjectView(R.id.p_nicheng)
    TextView textView1;
    @InjectView(R.id.p_class)
    TextView textView2;
    @InjectView(R.id.p_mywords)
    TextView textView3;
    @InjectView(R.id.p_totaldistance)
    TextView textView4;
    @InjectView(R.id.p_totaltime)
    TextView textView5;
    @InjectView(R.id.p_totalCount)
    TextView textView6;
    @InjectView(R.id.p_totalEnergy)
    TextView textView7;


    String name = null;

    @InjectView(R.id.addfriend1)
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendprofile);

        ButterKnife.inject(FriendProfile.this);


        Bundle info = getIntent().getExtras();
        username = info.getString("username");
        name = info.getString("name");

        friendname = (TextView) findViewById(R.id.friendname);
        friendname.setText(name);
        startPrivate = (Button) findViewById(R.id.startprivate);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://" + App.ip + "/chat/isfriend.php?username1=" + App.username + "&username2=" + username;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                String response = new String(bytes);
                if (response.equals("success")) {
                    startPrivate.setClickable(true);
                    button.setClickable(false);
                    button.setVisibility(View.GONE);
                } else {
                    startPrivate.setClickable(false);
                    startPrivate.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

//        startPrivate.setClickable(false);
//        startPrivate.setTextColor(getResources().getColor(R.color.highlighted_text_material_dark));


        try {
            setImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setdata();
    }


    @InjectView(R.id.image456)
    ImageView image456;


    void setImage() throws Exception {
        //通过代码 模拟器浏览器访问图片的流程
        String address = "http://" + App.ip + "/chat/pic/" + username + ".png";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(address, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image456.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });


    }



    @OnClick(R.id.startprivate)
    void setStartPrivateF() {

        RongIM.getInstance().startPrivateChat(FriendProfile.this, username,name);

    }


    @OnClick(R.id.addfriend1)
    void addfriend() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://" + App.ip + "/chat/addFriend1.php?username=" + App.username + "&target=" + username;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String response = new String(bytes);

                Toast.makeText(FriendProfile.this,"Added Successfully",Toast.LENGTH_SHORT).show();
                Log.e("Eric", response);
                friendname.setText("You've become friends");
                friendname.setTextColor(getResources().getColor(R.color.abc_primary_text_disable_only_material_light));

                onCreate(null);
//                startPrivate.setClickable(true);
//                startPrivate.setTextSize(15);
//                startPrivate.setTextColor(getResources().getColor(R.color.rc_normal_bg));




            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });


    }


    private void setdata() {


        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://" + App.ip + "/chat/getAllUserInfor.php?username=" + username;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                String response = new String(bytes);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.e("Eric", jsonObject.toString());
                    String p1 = jsonObject.getString("name");
                    String p2 = jsonObject.getString("sexual");
                    textView1.setText(p2);

                    friendname.setText("Welcome to" + p1 + " profile");
                    name = p1;
                    String p3 = jsonObject.getString("age");

                    String p4 = jsonObject.getString("mywords");
                    textView3.setText("" + p4);
                    String p5 = jsonObject.getString("class");
                    textView2.setText("" + p5);
                    String p6 = jsonObject.getString("totaldistance");
                    textView4.setText("Total Distance：" + p6 + "km");
                    String p7 = jsonObject.getString("totaltime");
                    textView5.setText("Time Spended：" + p7 + "sec");
                    String p8 = jsonObject.getString("totalenergy");
                    textView7.setText("Weight lost：" + p8 + "g");
                    String p9 = jsonObject.getString("totalrunning");
                    textView6.setText("Total running times：" + p9 + " times");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

    }


}


