package idp.cs.pub.ro.proiect;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;
    private int resource;
    private ArrayList<Message> messages;

    public MessageAdapter(Context context, int resource, ArrayList<Message> messages) {
        super(context, resource, messages);
        this.context = context;
        this.resource = resource;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.chat_thread, null);
        }
        Message message = messages.get(position);
        view.setId(position);
        if (message != null) {
            TextView name = view.findViewById(R.id.textViewMessage);
            TextView time = view.findViewById(R.id.textViewTime);

            name.setText(message.getMessageText());
            time.setText(message.getMessageUser() + " " + message.getMessageTime());

        }
        return view;
    }
}
