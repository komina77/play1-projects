package jobs;

import play.modules.launcher.SystemExitException;

public class SampleJob {

    public static void main(String[] args) throws SystemExitException {
        System.out.println("Hello world.");

        for (int i=0; i<args.length; i++) {
            System.out.printf("args[%d]:\t%s", i, args[i]);
            System.out.println();
        }

        throw new SystemExitException(0);
    }

}
