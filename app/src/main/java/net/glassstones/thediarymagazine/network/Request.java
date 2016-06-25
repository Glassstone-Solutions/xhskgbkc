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
 *
 * TODO: 15/06/2016 Make smarter to accept different types of calls
 */
public class Request {
    NetworkOperations callback;
    Call<ArrayList<NI>> listCall;

    public Request(Call<ArrayList<NI>> call) {
        this.listCall = call;
    }


    public void execute() {
        listCall.enqueue(new Callback<ArrayList<NI>>() {
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

    public Request callback(NetworkOperations callback) {
        this.callback = callback;
        return this;
    }

    public Request listCall(Call<ArrayList<NI>> listCall) {
        this.listCall = listCall;
        return this;
    }
}
