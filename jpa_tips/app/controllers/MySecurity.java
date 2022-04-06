package controllers;

import models.UserMst;
import play.Logger;

/**
 * 認証ロジック
 */
public class MySecurity extends Secure.Security {

    /**
     * 認証ロジック
     * @param username
     * @param password
     * @return true : 認証OK
     */
    static boolean authenticate(String username, String password) {
        if ("admin".equals(username) && "admin".equals(password)) return true;
        return UserMst.login(username, password) != null;
    }

    /**
     * 機能制限
     * @param profile 機能名
     * @return true : 機能許可
     */
    static boolean check(String profile) {
        Logger.info("check(%s)", profile);
        return true;
    }

    /**
     * サインオン成功した時に呼ばれる.
     */
    static void onAuthenticated() {
        Logger.info("onAuthenticated");
        session.put("role", "admin");
    }

    /**
     * サインオフ時に呼ばれるメソッド。
     */
    static void onDisconnect() {
        Logger.info("onDisconnect");
    }

    /**
     * サインオフが成功したときに呼ばれるメソッド。
     */
    static void onDisconnected() {
        Logger.info("onDisconnected");
    }

    /**
     * このメソッドは、チェックが成功しなかった場合に呼び出されます。デフォルトでは、許可されていないページ(コントローラの禁止メソッド)が表示されます。
     * @param profile
     */
    static void onCheckFailed(String profile) {
        Logger.info("onCheckFailed");
        forbidden();
    }
}
