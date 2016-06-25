package net.glassstones.thediarymagazine.interfaces.network;

import net.glassstones.thediarymagazine.models.NI;

import java.util.ArrayList;

import retrofit.Response;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public interface NetworkOperations {
    void onPostResponse(Response<ArrayList<NI>> respose);

    void onPostRequestFailure(Throwable t);
}
