package net.glassstones.thediarymagazine.services;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by Thompson on 19/06/2016.
 * For The Diary Magazine
 */
public class UpdateLocalPostsService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
