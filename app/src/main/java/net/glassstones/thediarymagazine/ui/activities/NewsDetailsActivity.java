package net.glassstones.thediarymagazine.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLink;

import net.glassstones.thediarymagazine.R;

import butterknife.InjectView;

@DeepLink({"tdm://posts/{id}", "http://www.thediarymagazine.com/{slug}"})
public class NewsDetailsActivity extends BaseActivity {

    @InjectView(R.id.root)
    CoordinatorLayout mRoot;

    @Override
    public Class clazz() {
        return this.getClass();
    }

    @Override
    public int resourceId() {
        return R.layout.activity_news_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle parameters = getIntent().getExtras();
            Log.d(TAG, "Deeplink params: " + parameters);

            String idString = parameters.getString("id");
            String name = parameters.getString("slug");

            if (!TextUtils.isEmpty(idString)) {
                showToast("class id== " + idString + " and name==" + name);
            } else if (!TextUtils.isEmpty(name)) {
                showToast("name==" + name);
            } else {
                showToast("no id in the deeplink");
            }
        } else {
            showToast("no deep link :( ");
        }
    }

    private void showToast(String s) {
        Snackbar.make(mRoot, s, Snackbar.LENGTH_LONG).show();
    }

}
