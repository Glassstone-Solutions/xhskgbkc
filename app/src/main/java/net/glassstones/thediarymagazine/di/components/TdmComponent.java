package net.glassstones.thediarymagazine.di.components;

import net.glassstones.thediarymagazine.di.modules.TdmModule;
import net.glassstones.thediarymagazine.di.scope.UserScope;
import net.glassstones.thediarymagazine.ui.activities.NewsFeedActivity;
import net.glassstones.thediarymagazine.ui.adapters.NewsFlipAdapter;
import net.glassstones.thediarymagazine.ui.fragments.BaseNewsFragment;

import dagger.Component;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
@UserScope
@Component(dependencies = NetComponent.class, modules = TdmModule.class)
public interface TdmComponent {
    void inject(NewsFeedActivity activity);

    void inject(NewsFlipAdapter adapter);

    void inject(BaseNewsFragment fragment);
}
