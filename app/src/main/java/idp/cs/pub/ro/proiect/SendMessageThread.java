package idp.cs.pub.ro.proiect;

import android.location.Location;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendMessageThread implements Runnable{
    private Message message;
    private URL url;
    private HttpURLConnection client;
    private String serverURL;

    @Override
    public void run() {
        try {
            url = new URL(serverURL);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);

            OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(outputPost, "UTF-8"));

            writer.write("message:" + message.getMessageText());
            writer.write(",sender:" + message.getSender());
            writer.write(",receiver:" + message.getReceiver());

            writer.flush();
            writer.close();
            outputPost.close();

            int responseCode=client.getResponseCode();
            Log.e("MESSAGE","Response " + responseCode);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SendMessageThread(Message message, String serverIP) {
        this.message = message;
        this.serverURL = serverIP;
    }
}
