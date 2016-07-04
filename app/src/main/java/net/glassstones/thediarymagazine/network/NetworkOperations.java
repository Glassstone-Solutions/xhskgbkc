package net.glassstones.thediarymagazine.network;

import net.glassstones.thediarymagazine.network.models.NI;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public interface NetworkOperations {
    void onPostResponse(Response<ArrayList<NI>> respose);

    void onPostRequestFailure(Throwable t);
}
