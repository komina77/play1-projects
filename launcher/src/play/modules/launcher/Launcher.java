package play.modules.launcher;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import play.Logger;
import play.Play;

/**
 * 任意のクラスをPlayframeworkベースでの起動をサポートする
 */
public class Launcher {

    /**
     * 実行したいクラス指定オプション
     */
    static final String OPTN_LAUNCH_CLASS = "--class=";


    /**
     *
     * @param args play launcher:execute --class=com.example.Test -arg1 -arg2 ==> args[] = { '--class=com.example.Test', '-arg1', '-arg2' } .
     */
    public static void main(String[] args) throws Exception {

        if (System.getProperty("precompiled", "false").equals("true")) {
            Play.usePrecompiled = true;
        }
        File root = new File(System.getProperty("application.path"));
        Play.init(root, System.getProperty("play.id", ""));

        //new Server(args);   // Without 9000 port listening.

        Play.start();

        if (Logger.isDebugEnabled()) {
            for (String x : args) {
                Logger.debug("found arg: %s", x);
            }
        }

        boolean called = false;
        int exitCode = 0;
        for (String x : args) {
            if (x.startsWith(OPTN_LAUNCH_CLASS)) {
                String c = x.substring(OPTN_LAUNCH_CLASS.length());

                try {
                    // Try to get from playframework dominating classes.
                    Class clazz = Play.classloader.getClassIgnoreCase(c);
                    if (clazz == null) {
                        // Try to get from normal classes.
                        clazz = Class.forName(c);
                    }
                    Method method = clazz.getMethod("main", String[].class);
                    method.invoke(null, (Object) args);

                } catch (ClassNotFoundException e) {
                    Logger.error(e, "%s is not found.", c);
                    exitCode = -1;

                } catch (NoSuchMethodException e) {
                    Logger.error(e, "%s.main() is not found.", c);
                    exitCode = -1;

                } catch (InvocationTargetException e) {
                    Throwable ex = e.getTargetException();
                    if (ex instanceof SystemExitException) {
                        exitCode = ((SystemExitException) ex).getExitCode();
                        Logger.info("%s.main() returns %d.", c, exitCode);
                    } else {
                        Logger.error(e, "%s.main() throw exception.", c);
                        exitCode = -1;
                    }
                } catch (Exception e) {
                    Logger.error(e, "An exception was thrown.");
                    exitCode = -1;
                }
                called = true;
                Logger.info("Application '%s' is ended !", Play.configuration.getProperty("application.name", ""));
                break;
            }
        }

        if (!called) {
            Logger.warn("Please specify the class to execute. (ex. --class=com.example.MyClass )");
        }

        Play.stop();
        System.exit(exitCode);
    }

}
