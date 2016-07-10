package net.glassstones.thediarymagazine.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thompson on 16/06/2016.
 * For The Diary Magazine
 */
public class WPTitle implements Parcelable {

    @SerializedName("rendered")
    private String title;

    public String title () {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(this.title);
    }

    public WPTitle () {
    }

    protected WPTitle (Parcel in) {
        this.title = in.readString();
    }

    public static final Parcelable.Creator<WPTitle> CREATOR = new Parcelable.Creator<WPTitle>() {
        @Override
        public WPTitle createFromParcel (Parcel source) {
            return new WPTitle(source);
        }

        @Override
        public WPTitle[] newArray (int size) {
            return new WPTitle[size];
        }
    };
}
