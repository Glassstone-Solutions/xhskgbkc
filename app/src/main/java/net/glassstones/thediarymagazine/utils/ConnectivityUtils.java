package net.glassstones.thediarymagazine.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Thompson on 30/05/2016.
 * For The Diary Magazine
 */
public class ConnectivityUtils {
    private ConnectivityUtils() {
    }

    public static boolean isOnline(final Context context) {
        final ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            final NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean isConnectedWifi(final Context context) {
        final ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = connectivity.getActiveNetworkInfo();
        return ((info != null) && info.isConnected() && (info.getType() == ConnectivityManager.TYPE_WIFI));
    }

    public boolean isConnectedMobile(final Context context) {
        final ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = connectivity.getActiveNetworkInfo();
        return ((info != null) && info.isConnected() && (info.getType() == ConnectivityManager.TYPE_MOBILE));
    }
}
