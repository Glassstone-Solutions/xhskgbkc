package net.glassstones.thediarymagazine.network;

import net.glassstones.thediarymagazine.interfaces.network.NetworkOperations;
import net.glassstones.thediarymagazine.models.NI;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public class Request {
    NetworkOperations callback;
    Call<ArrayList<NI>> call;

    public Request(Call<ArrayList<NI>> call) {
        this.call = call;
    }

    public void execute(){
        call.enqueue(new Callback<ArrayList<NI>>() {
            @Override
            public void onResponse(Response<ArrayList<NI>> response, Retrofit retrofit) {
                callback.onPostResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onPostRequestFailure(t);
            }
        });
    }

    public NetworkOperations getCallback() {
        return callback;
    }

    public void setCallback(NetworkOperations callback) {
        this.callback = callback;
    }
}
