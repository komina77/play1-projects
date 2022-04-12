package jobs;

import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart(async = true)
public class OnApplicationStartJob extends Job {

    @Override
    public void doJob() throws Exception {
        new AttachmentsCleaner().setPerFiles(1000).now();
    }

}
