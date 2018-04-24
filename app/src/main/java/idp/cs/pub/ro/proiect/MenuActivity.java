package idp.cs.pub.ro.proiect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;

public class MenuActivity extends AppCompatActivity {

    private static AccessToken token;

    @BindView(R.id.btn_logout) Button log_out;
    Button friends_btn;
    ListView friends_list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    Intent updateLocationService;
    Bitmap profilePictureBitmap;
    ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FacebookSdk.sdkInitialize(getApplicationContext());

        log_out = findViewById(R.id.btn_logout);
        friends_btn = findViewById(R.id.friends_btn);
        friends_list = findViewById(R.id.friends_list);
        profilePicture = findViewById(R.id.profilePicture);

        arrayList = new ArrayList();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        friends_list.setAdapter(adapter);

        token = (AccessToken)getIntent().getExtras().get("token");

        new SetPicture().execute(token.getUserId());

        updateLocationService = new Intent(this, UpdateLocation.class);
        updateLocationService.putExtra("id", token.getUserId());
        startService(updateLocationService); //start UpdateLocation service

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                stopService(updateLocationService);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        friends_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                        token,
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                System.out.println("Graph RESPONSE: \t" + response);
                                try {
                                    JSONArray friendsJson = response.getJSONObject().getJSONArray("data");
                                    arrayList.clear();
                                    adapter.notifyDataSetChanged();
                                    for (int i = 0; i < friendsJson.length(); i++) {
                                        JSONObject friend = friendsJson.getJSONObject(i);
                                        arrayList.add(friend.getString("name"));
                                        adapter.notifyDataSetChanged();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();
            }
        });

    }


    private class SetPicture extends AsyncTask<String, String, Bitmap> {

        public Bitmap getFacebookProfilePicture(String userID){
            URL imageURL = null;
            try {
                imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getFacebookProfilePicture(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView img = (ImageView) findViewById(R.id.profilePicture);
            img.setImageBitmap(result);
        }

        @Override
        protected void onPreExecute() {}

    }


}
