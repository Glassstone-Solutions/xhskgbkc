package net.glassstones.thediarymagazine.ui.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import net.glassstones.thediarymagazine.ui.widgets.NestedWebView;

/**
 * Created by Thompson on 21/06/2016.
 * For The Diary Magazine
 */
public class NestedWebViewScrollBehavior extends CoordinatorLayout.Behavior<NestedWebView> {

    public NestedWebViewScrollBehavior() {
    }

    public NestedWebViewScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, NestedWebView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    //This is called for each change to a dependent view
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent,
                                          NestedWebView child,
                                          View dependency) {
        int offset = -dependency.getTop();
        child.setTranslationY(offset);
        return true;
    }

}
