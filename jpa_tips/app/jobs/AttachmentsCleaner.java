package jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import play.Logger;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;

@Every("cron.AttachmentsCleaner.every")
@On("cron.AttachmentsCleaner.on")
public class AttachmentsCleaner extends Job {

    private List<File> files = new ArrayList<>();

    /**
     * 1回の実行でチェックするファイルの件数
     */
    private int perFiles = 10;

    /**
     * デフォルトコンストラクタ
     */
    public AttachmentsCleaner() { }

    /**
     * @param perFils 1回のジョブ実行でチェックするファイル数
     * @return
     */
    public AttachmentsCleaner setPerFiles(int perFiles) {
        this.perFiles = perFiles;
        return this;
    }

    @Override
    public void doJob() throws Exception {
        if (files.size() == 0) {
            File dir = Blob.getStore();
            files.addAll(Arrays.asList(dir.listFiles()));
        }
        for (int i=0; i < perFiles; i++) {
            if (files.size() == 0) break;
            File file = files.remove(files.size() -1);

            Object id = JPA.em(JPA.DEFAULT).createNativeQuery(""
                    + "SELECT MIN(id) FROM 画像ファイル "
                    + "WHERE ファイル LIKE :NAME "
                    + "")
                    .setParameter("NAME", file.getName() + "|%")
                    .getSingleResult();

            if (id == null) {
                Logger.info("[AttachmentsCleaner] Delete unreferenced file [%s].", file.getName());
                file.delete();
            }
        }
    }

}
