package net.glassstones.thediarymagazine.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thompson on 16/06/2016.
 * For The Diary Magazine
 */
public class WPContent implements Parcelable {
    @SerializedName("rendered")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(this.content);
    }

    public WPContent () {
    }

    protected WPContent (Parcel in) {
        this.content = in.readString();
    }

    public static final Parcelable.Creator<WPContent> CREATOR = new Parcelable.Creator<WPContent>() {
        @Override
        public WPContent createFromParcel (Parcel source) {
            return new WPContent(source);
        }

        @Override
        public WPContent[] newArray (int size) {
            return new WPContent[size];
        }
    };
}
