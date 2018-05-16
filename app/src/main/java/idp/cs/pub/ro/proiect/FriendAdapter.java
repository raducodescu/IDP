package idp.cs.pub.ro.proiect;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<Friend> {
    private Context context;
    private int resource;
    private ArrayList<Friend> friends;

    public FriendAdapter(Context context, int resource, ArrayList<Friend> friends) {
        super(context, resource, friends);
        this.context = context;
        this.resource = resource;
        this.friends = friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.friend, null);
        }
        Friend friend = friends.get(position);
        view.setId(position);
        if (friend != null) {
            Button msg = view.findViewById(R.id.chat);
            TextView name = view.findViewById(R.id.friendName);
            name.setText(friend.getName());
            TextView distance = view.findViewById(R.id.distance);
            Location location = friend.getLocation();

            float distanceInMeters;

            if (MenuActivity.location == null || location == null || (location.getLatitude() == 0 && location.getLongitude() == 0)) {
                distanceInMeters = 0;
            }
            else {
                double distanceKM = MenuActivity.location.distanceTo(location) / 1000;
                distanceInMeters = MenuActivity.location.distanceTo(location);
            }
            distance.setText(String.valueOf(distanceInMeters));

            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                }
            });
        }
        return view;
    }

    public void addFriend(Friend f) {
        friends.add(f);
    }

}