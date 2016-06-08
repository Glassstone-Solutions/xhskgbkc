package net.glassstones.thediarymagazine.utils;

import net.glassstones.thediarymagazine.Constants;

/**
 * Created by Thompson on 30/05/2016.
 * For The Diary Magazine
 */
public class TagFactoryUtils {
    private static final String TAG_PREFIX = Constants.APP_PREFIX;

    private TagFactoryUtils() {
    }

    public static String getTag(final Object obj) {
        final Class cls = obj.getClass();
        return getTag(cls.getSimpleName());

    }

    public static String getTag(final Class cls) {
        return getTag(cls.getSimpleName());

    }

    public static String getTag(final String suffix) {
        return TAG_PREFIX + suffix;
    }
}
