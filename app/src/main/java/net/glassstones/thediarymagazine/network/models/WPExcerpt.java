package net.glassstones.thediarymagazine.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thompson on 16/06/2016.
 * For The Diary Magazine
 */
public class WPExcerpt implements Parcelable {

    @SerializedName("rendered")
    private String excerpt;

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(this.excerpt);
    }

    public WPExcerpt () {
    }

    protected WPExcerpt (Parcel in) {
        this.excerpt = in.readString();
    }

    public static final Parcelable.Creator<WPExcerpt> CREATOR = new Parcelable.Creator<WPExcerpt>() {
        @Override
        public WPExcerpt createFromParcel (Parcel source) {
            return new WPExcerpt(source);
        }

        @Override
        public WPExcerpt[] newArray (int size) {
            return new WPExcerpt[size];
        }
    };
}
