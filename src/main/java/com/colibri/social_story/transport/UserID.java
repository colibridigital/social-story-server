package com.colibri.social_story.transport;

public class UserID {
    private String uid;
    public UserID() {}
    public UserID(String uid) {
        this.uid = uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserID userID = (UserID) o;

        return !(uid != null ? !uid.equals(userID.uid) : userID.uid != null);
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }
}
