package idp.cs.pub.ro.proiect;

import android.location.Location;

public class Friend {
    private String myid;
    private String name;
    private String facebookid;
    private Location location;

    public Friend(String name, String facebookid, Location location, String myid) {
        this.name = name;
        this.facebookid = facebookid;
        this.location = location;
        this.myid = myid;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getFacebookid() {
        return facebookid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFacebookid(String facebookid) {
        this.facebookid = facebookid;
    }
}
