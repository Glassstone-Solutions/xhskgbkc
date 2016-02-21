package net.glassstones.thediarymagazine;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.wang.avi.AVLoadingIndicatorView;

import net.glassstones.thediarymagazine.models.NewsCluster;
import net.glassstones.thediarymagazine.models.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thompson on 04/02/2016.
 * For The Diary Magazine
 */
public class Common extends Application {

    public static volatile Context applicationContext;
    public static final String KEY_FIRST_RUN = "first_run";
    public static final String[] imageUrl = {
            "http://www.bellanaija.com/wp-content/uploads/2016/02/image-7-600x953.jpg",
            "http://i.dailymail.co.uk/fff2/images/2016/2/9/63fb8d18b66fe7e306c41d8705e8240b.jpg",
            "http://www.360nobs.com/wp-content/uploads/2015/03/TUFACE.jpg",
            "http://idey.me/wp-content/uploads/2014/12/Falz.jpg",
            "http://net.tooxclusive.com/wp-content/uploads/2014/12/adekunleGOLD-Sade-Art-tooXclusive.com_.jpg",
            "http://static.pulse.ng/img/incoming/origs3382965/2590482634-w980-h640/don-jazzy1.png",
            "http://i.dailymail.co.uk/i/pix/2015/08/30/20/2BCFDA0A00000578-3216297-Wayne_Rooney_Memphis_Depay_and_Daley_Blind_look_dejected_followi-a-61_1440964101266.jpg",
            "http://www.orderpaper.ng/wp-content/uploads/2015/11/CBN-2.jpg",
            "http://america.aljazeera.com/content/dam/ajam/images/articles_2015/03/buhari_nigeria_election_kano_0330.jpg",
            "http://www.planet101fm.ng/wp-content/uploads/2015/10/sundau-oliseh.jpg",
            "http://cdn23.us1.fansshare.com/photos/nike/black-green-rainbow-colors-nike-shoes-shoes-459080530.jpg",
            "http://arysports.tv/wp-content/uploads/2015/12/Mourinho.jpg",
            "http://cdn2.pcadvisor.co.uk/cmsdata/features/3617889/Galaxy_S6_Edge.jpg",
            "http://net.tooxclusive.com/wp-content/uploads/2014/12/Seyi-Shay.jpg",
            "https://consequenceofsound.files.wordpress.com/2015/07/kendrick-lamar-responds-fox-geraldo.jpg",
            "http://www.wepluggoodmusic.com/wp-content/uploads/2013/10/Wizkid-II.jpg",
            "http://www.politicoscope.com/wp-content/uploads/2015/07/Ben-Murray-Bruce-In-Headline-Now.jpg",
            "http://loopassets.s3.amazonaws.com/uzo_aduba.jpg",
            "http://pixel.nymag.com/imgs/fashion/daily/2014/12/08/08-leonardo-dicaprio.w1200.h630.jpg",
            "http://assets.rollingstone.com/assets/2015/article/flashback-kenny-rogers-scores-number-one-with-brave-coward-20150105/179671/large_rect/1420496762/1401x788-85364387.jpg",
            "http://i1.wp.com/www.stylevitae.com/wp-content/uploads/2016/02/Afro-Mod-Trends-womenswear-collection-9.jpg?resize=672%2C970",
            "http://www.eonline.com/eol_images/Entire_Site/2014115/rs_1024x759-141205133422-1024.Trevor-Noah-The-Daily-Show.ms.120514.jpg",
            "http://cdn4.thr.com/sites/default/files/2014/03/50_cent_power_a_l.jpg",
            "http://www.bellanaija.com/wp-content/uploads/2012/10/DJ-Jimmy-Jatt-3.jpg",
            "https://consequenceofsound.files.wordpress.com/2015/04/britney-spears.jpg?w=807"
    };
    public static final String[] titles = {
            "Ade Bakare Debuts Colourful Ankara Range Collection + Lookbook Review",
            "Look furry nice in Kendall’s luxurious outerwear",
            "Tuface wrote his new hit song while driving",
            "Falz and Simi hookup!",
            "DOWNLOAD: adekunleGOLD - Sade",
            "Mohits reunite! Don Jazzy and D'banj get back to record a new single",
            "AXED!!! LVG sacked after massive loss",
            "CBN governor caught in insider trading scandal",
            "Nigerians now have uninterrupted power supply",
            "'I will marry when I feel like' - Sunday Oliseh",
            "Sales! Nike sneaker at half the price",
            "Jose snubbed for Manchester United job",
            "Samsung recalls one million Galaxy s7",
            "Nollywood hunk, Daniel K Daniel dapper in new pics, reveals his love for Seyi Shay",
            "K.Dot gets Grammy nod",
            "Wizkid baby mama palaver: New women surface",
            "Omojuwa eviscerates Sen. Ben Bruce on Twitter",
            "Uzo Aduba in Nollywood flick",
            "Leo snubbed, AGAIN!",
            "Kenny Rogers finally retires",
            "Spring collection",
            "Trevor Noah hit with sexual harassment suit",
            "50 cent NOT broke - Baby mama",
            "Jimmy Jatt has a new Hat line and it sucks",
            "Britney back to rehab!"
    };

    public static void loadingStatus(AVLoadingIndicatorView loadingView, boolean isLoading) {
        loadingView.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    public static List<NewsItem> getDemoNews() {
        List<NewsItem> list = new ArrayList<>();
        for (int i = 0; i < imageUrl.length; i++) {
            NewsItem item = new NewsItem();
            item.setImageUrl(imageUrl[i]);
            item.setTitle(titles[i]);
            list.add(item);
        }
        return list;
    }

    public static List<NewsCluster> getNewsCluster() {

        List<NewsItem> list = new ArrayList<>();
        for (int i = 0; i < imageUrl.length; i++) {
            NewsItem item = new NewsItem();
            item.setImageUrl(imageUrl[i]);
            item.setTitle(titles[i]);
            list.add(item);
        }

        List<NewsCluster> cluster = new ArrayList<>();


        int currentIndex = 0;
        int currentKey = 0;

        int currentNewsSize = list.size();
        int[] keys = {2, 3, 2, 3, 1, 2, 3, 2, 3, 2, 1};
        int key = 0;

        NewsCluster nc;
        nc = new NewsCluster();
        nc.getItems().add(list.get(0));
        cluster.add(nc);
        currentIndex++;
        do {
            nc = new NewsCluster();
            for (int i = 0; i < keys[currentKey]; i++) {
                try {
                    nc.getItems().add(list.get(currentIndex));
                    if (!cluster.contains(nc)) {
                        cluster.add(nc);
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
                currentIndex++;
                currentNewsSize--;
            }
            currentKey++;
        } while (currentIndex > 0 && currentIndex < list.size() && currentNewsSize > 0);

        return cluster;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId")
                .server(Keys.KEY_SERVER)
                .build());
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().increment("RunCount");
        ParseUser.getCurrentUser().saveInBackground();
    }


}
