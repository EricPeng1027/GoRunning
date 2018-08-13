package com.hechao.chat;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2018/7/10.
 */
public class Myprofile extends Activity {


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        ButterKnife.inject(this);


        setdata();


    }

    private void setdata() {


        AsyncHttpClient client= new AsyncHttpClient();
        String url="http://"+App.ip+"/chat/getAllUserInfor.php?username="+App.username;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                String response=new String(bytes) ;
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String p1=jsonObject.getString("name");
                    String p2=jsonObject.getString("sexual");
                    textView1.setText("Nickname："+p1+" ("+p2+")");
                    String p3=jsonObject.getString("age");

                    String p4=jsonObject.getString("mywords");
                    textView3.setText("Signature："+p4);
                    String p5=jsonObject.getString("class");
                    textView2.setText("Major："+p5);
                    String p6=jsonObject.getString("totaldistance");
                    textView4.setText("Total Distance："+p6+"km");
                    String p7=jsonObject.getString("totaltime");
                    textView5.setText("Time Spended："+p7+ "sec");
                    String p8=jsonObject.getString("totalenergy");
                    textView7.setText("Weight lost："+p8+" g");
                    String p9=jsonObject.getString("totalrunning");
                    textView6.setText("Total Running Times："+p9+" times");




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
