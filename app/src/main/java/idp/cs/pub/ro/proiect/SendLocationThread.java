package idp.cs.pub.ro.proiect;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class SendLocationThread implements Runnable{
    private Location location;
    private URL url;
    private HttpURLConnection client;
    private String serverURL;
    private String userId;

    @Override
    public void run() {
        try {
            url = new URL(serverURL);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);

            OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(outputPost, "UTF-8"));

            writer.write("user:" + userId);
            writer.write(",lat:" + String.valueOf(location.getLatitude()));
            writer.write(",lng:" + String.valueOf(location.getLongitude()));

            writer.flush();
            writer.close();
            outputPost.close();

            int responseCode=client.getResponseCode();
            Log.e("LOCATION","Response " + responseCode);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SendLocationThread(Location location, String serverIP, String userId) {
        this.location = location;
        this.serverURL = serverIP;
        this.userId = userId;
    }
}
