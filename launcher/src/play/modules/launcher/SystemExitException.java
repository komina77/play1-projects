package play.modules.launcher;

/**
 * System.exitの代替
 */
public class SystemExitException extends Exception {
    /**
     * 終了コード
     */
    private int exitCode = 0;
    /**
     * コンストラクタ
     * @param exitCode 終了コード
     */
    public SystemExitException(int exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * コンストラクタ
     * @param exitCode 終了コード
     * @param cause 例外
     */
    public SystemExitException(int exitCode, Throwable cause) {
        super(cause);
        this.exitCode = exitCode;
    }

    /**
     * @return 設定された終了コードを取得する.
     */
    public int getExitCode() {
        return exitCode;
    }

}
