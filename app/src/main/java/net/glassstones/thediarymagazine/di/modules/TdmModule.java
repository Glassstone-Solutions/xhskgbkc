package net.glassstones.thediarymagazine.di.modules;

import net.glassstones.thediarymagazine.di.scope.UserScope;
import net.glassstones.thediarymagazine.interfaces.network.TDMAPIClient;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
@Module
public class TdmModule {

    @Provides
    @UserScope
    public TDMAPIClient providesTDMAPIInterface(Retrofit retrofit){
        return retrofit.create(TDMAPIClient.class);
    }

}
