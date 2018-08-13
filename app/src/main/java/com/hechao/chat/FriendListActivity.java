package com.hechao.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import io.rong.imkit.RongIM;


/**
 * 好友列表
 */

public class FriendListActivity extends Activity {

    List<String> friendList = new ArrayList<String>();

    @InjectView(R.id.friendlist1)
    ListView listView;

    @InjectView(R.id.refresh)
    Button refresh;

    MyAdapter myAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlist);
        ButterKnife.inject(FriendListActivity.this);
        refreshFriendList();
        myAdapter = new MyAdapter(friendList, FriendListActivity.this);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                AsyncHttpClient client= new AsyncHttpClient();
                final String string = friendList.get(position);
                String url="http://"+App.ip+"/chat/getname.php?username="+string;
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {


                        Intent intent = new Intent(FriendListActivity.this,FriendProfile.class);
                        Bundle args = new Bundle();
                        args.putString("username",string);
                        args.putString("name",new String(bytes) );
                        intent.putExtras(args);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                    }
                });

//                String target = (String) listView.getAdapter().getItem(position);
//                RongIM.getInstance().startPrivateChat(FriendListActivity.this, target, null);
            }
        });


    }

    @OnClick(R.id.refresh)
    void refresh() {
        refreshFriendList();
    }


    private void refreshFriendList() {

        AsyncHttpClient client = new AsyncHttpClient();

        String url = "http://"+App.ip+"/chat/getFriendList.php?username=" + App.username;
        Log.e("Eric",url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String response = new String(bytes);
                Log.e("Eric", "friend list response:" + response);
                friendList.clear();

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i1 = 0; i1 < array.length(); i1++) {
                        friendList.add((String) array.get(i1));
                        Log.e("Eric", (String) array.get(i1));
                    }

                    myAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.e("Eric", "friendlist is not initiated ");
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshFriendList();

    }


}
