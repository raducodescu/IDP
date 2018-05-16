package idp.cs.pub.ro.proiect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    String myid;
    String friendid;
    ListView messages;
    EditText message_text;
    Button send;
    private ArrayAdapter<Message> adapter;
    private ArrayList<Message> messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messages = findViewById(R.id.messages);
        message_text = findViewById(R.id.message);
        send = findViewById(R.id.submit);
        message_text.setText("");

        myid = getIntent().getStringExtra("myid");
        friendid = getIntent().getStringExtra("friendid");

        messagesList = new ArrayList<>();

        adapter = new MessageAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, messagesList);
        messages.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                String messageData = message_text.getText().toString();

                if (messageData.length() == 0)
                    return;

                Message message = new Message(messageData, "Codescu Radu", myid, friendid);

                SendMessageThread thread = new SendMessageThread(message,getString(R.string.sendMessage));
                new Thread(thread).start();

                messagesList.add(message);
                adapter.notifyDataSetChanged();
                message_text.setText("");
            }
        });



    }
}
