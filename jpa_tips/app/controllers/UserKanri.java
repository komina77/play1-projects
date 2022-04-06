package controllers;

import java.util.Date;
import java.util.List;

import models.UserMst;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.With;

@With({ CommonController.class, Secure.class })
public class UserKanri extends Controller {

    /**
     * ユーザ一覧の表示
     */
    @Transactional(readOnly = true)
    public static void list() {
        List<UserMst> list = UserMst.find("ORDER BY id").fetch();
        render(list);
    }

    /**
     * ユーザ表示
     * @param id
     */
    @Transactional(readOnly = true)
    public static void view(Long id) {
        UserMst user = null;
        if (id == null) {
            // 新規作成パターン
            user = new UserMst();
        } else {
            // 編集パターン
            user = UserMst.findById(id);
        }
        renderArgs.put("user", user);
        render("UserKanri/edit.html");
    }

    /**
     * ユーザマスタへの登録／更新
     * @param user
     * @param version
     */
    @Transactional(readOnly = false)
    public static void edit(@Valid @Required UserMst user, Long version) {
        if (Validation.hasErrors()) {
            flash.error("入力エラーがあります.");
        } else if (user != null) {
            if (version == null && user.id == null) {
                user.save();
                flash.success("登録に成功しました.");
            } else if (user.更新日時.getTime() == version) {
                user.save();
                flash.success("更新に成功しました.");
            } else {
                flash.error("更新に失敗しました.");
            }
        }
        renderArgs.put("user", user);
        render();
    }

    /**
     * ユーザの削除
     * @param id
     * @param version
     */
    public static void delete(Long id, Long version) {
        UserMst user = UserMst.findById(id);
        if (user != null) {
            if (user.名前.equals(session.get("username"))) {
                flash.error("ログイン中ユーザは削除できません.");
                view(id);
            }
            if (user.更新日時.getTime() == version) {
                user.delete();
                flash.success("%sさんのデータの削除に成功しました.", user.名前);
                render();
            }
        }
        flash.error("削除に失敗しました.");
        view(id);
    }
}
