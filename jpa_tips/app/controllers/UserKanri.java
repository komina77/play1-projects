package controllers;

import java.util.Date;
import java.util.List;

import models.UserMst;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.db.jpa.Transactional;
import play.mvc.Controller;

public class UserKanri extends Controller {

    @Transactional(readOnly = true)
    public static void list() {
        flash.clear();

        List<UserMst> list = UserMst.findAll();
        render(list);
    }

    @Transactional(readOnly = true)
    public static void view(Long id) {
        flash.clear();
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

    @Transactional(readOnly = false)
    public static void edit(@Valid @Required UserMst user, Long version) {
        flash.clear();
        if (Validation.hasErrors()) {
            flash.error("入力エラーがあります.");
        } else if (user != null) {
            if (version == null && user.id == null) {
                user.更新日時 = new Date();
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
}
