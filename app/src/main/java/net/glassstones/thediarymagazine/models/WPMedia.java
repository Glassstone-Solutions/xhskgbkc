package net.glassstones.thediarymagazine.models;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public class WPMedia {

    private int id;
    private String media_type;
    private String mime_type;
    private String source_url;
    private byte[] image_byte;

    public WPMedia() {
    }

    public byte[] getImageByte() {
        return image_byte;
    }

    public void setImageByte(byte[] image_byte) {
        this.image_byte = image_byte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getSourceUrl() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }
}
