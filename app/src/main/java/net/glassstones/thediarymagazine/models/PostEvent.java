package net.glassstones.thediarymagazine.models;

/**
 * Created by Thompson on 17/06/2016.
 * For The Diary Magazine
 */
public class PostEvent {
    public static final int LIST_CHANGE = -1;
    public static final int SAVED = 0;
    public static final int POST_LIST = 10;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static final int SINGLE_POST = 11;
    int id;
    int status;
    int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PostEvent id(int id) {
        this.id = id;
        return this;
    }

    public PostEvent status(int status) {
        this.status = status;
        return this;
    }

    public PostEvent type(int type) {
        this.type = type;
        return this;
    }
}
