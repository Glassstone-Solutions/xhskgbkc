package net.glassstones.thediarymagazine.network.callbacks;


import net.glassstones.thediarymagazine.network.models.NI;

import java.util.ArrayList;

/**
 * Created by Thompson on 02/07/2016.
 * For The Diary Magazine
 */
public interface PostsFetchCallback extends ServerCallback {
    void fetchFailed ();

    void onPostsFound (ArrayList<NI> posts);
}
