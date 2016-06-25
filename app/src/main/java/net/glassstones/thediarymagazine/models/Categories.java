package net.glassstones.thediarymagazine.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Thompson on 20/06/2016.
 * For The Diary Magazine
 */
public class Categories extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private boolean isTitleSaved;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTitleSaved() {
        return isTitleSaved;
    }

    public void setTitleSaved(boolean titleSaved) {
        isTitleSaved = titleSaved;
    }
}
