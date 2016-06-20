package net.glassstones.thediarymagazine.services;

import android.util.Log;

import net.glassstones.thediarymagazine.tasks.GetImageTask;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by Thompson on 19/06/2016.
 * For The Diary Magazine
 */
public class UpdateLocalPostsImageService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("TAG", "Job started");
        new GetImageTask().mContext(this).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
