package jobs;

import play.Logger;
import play.modules.launcher.GroovyLauncher;
import play.modules.launcher.SystemExitException;
import play.vfs.VirtualFile;

public class SampleJob2 {

    public static void main(String[] args) throws SystemExitException {
        try {
            VirtualFile vf = VirtualFile.fromRelativePath("scripts/SayHello.groovy");
            Logger.info("Launch script: %s", vf.relativePath());
            new GroovyLauncher().setParameter("args", args).compile(vf).executeScript();
        } catch (Exception e) {
            Logger.error(e, "Catch exception.");
            throw new SystemExitException(-1, e);
        }
        throw new SystemExitException(0);
    }

}
