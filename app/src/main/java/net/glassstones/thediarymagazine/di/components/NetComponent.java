package net.glassstones.thediarymagazine.di.components;

import android.content.SharedPreferences;

import com.squareup.okhttp.OkHttpClient;

import net.glassstones.thediarymagazine.di.modules.AppModule;
import net.glassstones.thediarymagazine.di.modules.NetModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit.Retrofit;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    // downstream components need these exposed
    Retrofit retrofit();
    OkHttpClient okHttpClient();
    SharedPreferences sharedPreferences();
}
