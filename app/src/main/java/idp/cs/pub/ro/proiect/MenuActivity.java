package idp.cs.pub.ro.proiect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import butterknife.BindView;

public class MenuActivity extends AppCompatActivity {

    private static AccessToken token;
    static Location location;
    @BindView(R.id.btn_logout) Button log_out;
    Button friends_btn;
    ListView friends_list;
    private ArrayAdapter<Friend> adapter;
    private ArrayList<Friend> friendsArrayList;
    Intent updateLocationService;
    ImageView profilePicture;
    Button showOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FacebookSdk.sdkInitialize(getApplicationContext());

        log_out = findViewById(R.id.btn_logout);
        friends_btn = findViewById(R.id.friends_btn);
        friends_list = findViewById(R.id.friends_list);
        profilePicture = findViewById(R.id.profilePicture);
        showOnMap = findViewById(R.id.show_friends);

        friendsArrayList = new ArrayList<>();

        adapter = new FriendAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, friendsArrayList);
        friends_list.setAdapter(adapter);

//        friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object o = friendsArrayList.getItemAtPosition(position);
//                prestationEco str = (prestationEco)o; //As you are using Default String Adapter
//                Toast.makeText(getBaseContext(),str.getTitle(),Toast.LENGTH_SHORT).show();
//            }
//        });

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
                                    friendsArrayList.clear();
                                    adapter.notifyDataSetChanged();
                                    for (int i = 0; i < friendsJson.length(); i++) {
                                        JSONObject friend = friendsJson.getJSONObject(i);
                                        GetLocation locationCallable = new GetLocation(friend.getString("id"));
                                        FutureTask<Location> futureTask = new FutureTask<>(locationCallable);
                                        Thread t = new Thread(futureTask);
                                        t.start();
                                        Location location = futureTask.get();
                                        Friend friendObj = new Friend(friend.getString("name"), friend.getString("id"), location, token.getUserId());
                                        friendsArrayList.add(friendObj);
                                        adapter.notifyDataSetChanged();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                ).executeAsync();
            }
        });

        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (friendsArrayList.size() == 0)
                    return;

                Intent intent = new Intent(getApplicationContext(), FriendsMap.class);
                startActivity(intent);

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

    public class GetLocation implements Callable<Location> {
        private String userid;

        public GetLocation(String userid) {
            this.userid = userid;
        }

        public Location call() {
            URL url;
            HttpURLConnection client;
            try {
                String getLocationURL = getString(R.string.getLocation);
                url = new URL(getLocationURL + "?user=" + userid);
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");

                BufferedReader rd = new BufferedReader(new InputStreamReader(client.getInputStream()));
                Location targetLocation = new Location("");
                String line = rd.readLine();
                Double lat = Double.valueOf(line.split(",")[0]);
                Double lng = Double.valueOf(line.split(",")[1]);
                targetLocation.setLatitude(lat);//your coords of course
                targetLocation.setLongitude(lng);

                return targetLocation;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


}
