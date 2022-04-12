package controllers;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.lang.StringUtils;

import models.ImageFile;
import models.UserMst;
import play.cache.CacheFor;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.db.jpa.Blob;
import play.libs.Images;
import play.mvc.Controller;
import play.mvc.With;

@With({ CommonController.class, Secure.class })
public class ImageFileKanri extends Controller {

    public static void index(Long userId) {
        if (userId == null || UserMst.findById(userId) == null) {
            flash.error("不正なユーザIDです.");
            UserKanri.list();
        }
        UserMst user = UserMst.findById(userId);
        renderArgs.put("user", user);
        render();
    }

    /**
     * アップロードされたファイルをリサイズして格納する.
     * @param imageFile
     */
    public static void upload(@Valid ImageFile imageFile) {
        File originalImage = null;
        try {
            checkAuthenticity();
            if (Validation.hasErrors()) {
                flash.error("アップロードできません.");
            } else if (imageFile.所有ユーザ.画像リスト.size() >= 3) {
                flash.error("画像は3枚までです.");
            } else {
                Blob to = new Blob();
                to.set(new ClosedInputStream(), imageFile.ファイル.type());
                originalImage = imageFile.ファイル.getFile();

                Images.resize(originalImage, to.getFile(), 120, 120, true);
                imageFile.ファイル = to;
                imageFile.save();
                flash.success("画像をアップロードしました.");
            }
            renderArgs.put("user", imageFile.所有ユーザ.refresh());
            render("@index");

        } finally {
            if (originalImage != null) {
                originalImage.delete();
            }
        }
    }

    /**
     * 格納された画像ファイルを返す
     * @param id
     */
    @CacheFor()
    public static void store(Long id) {
        if (id == null) {
            forbidden();
        }
        if (id != null) {
            ImageFile imageFile = ImageFile.findById(id);
            if (imageFile != null) {
                renderBinary(imageFile.ファイル.getFile());
            }
        }
        notFound();
    }

    /**
     * デフォルト画像ファイルの変更
     * @param userId 対象ユーザのID
     * @param defaultImage デフォルト画像ID
     * @param caption キャプション編集
     * @param delImage 削除要求の画像ID
     */
    public static void edit(Long userId, Long defaultImage, Map<Long, String> caption, Set<Long> delImage) {
        checkAuthenticity();

        if (delImage != null) {
            for (Long key : delImage) {
                ImageFile img = ImageFile.findById(key);
                if (img != null) {
                    if (img.所有ユーザ.デフォルト画像 == img) {
                        img.所有ユーザ.デフォルト画像 = null;
                        img.所有ユーザ.save();
                    }
                    img.delete();
                    flash.success("画像を削除しました.");
                }
            }
        }

        // キャプション変更
        for(Long key : caption.keySet()) {
            if (StringUtils.isEmpty(caption.get(key))) {
                flash.error("キャプションは必須項目です.", key);
                Validation.addError("caption[" + key + "]", "Required.");
            } else {
                ImageFile img = ImageFile.findById(key);
                if (img != null && !img.キャプション.equals(caption.get(key))) {
                    img.キャプション = caption.get(key);
                    img.save();
                    flash.success("キャプションを変更しました.");
                }
            }
        }

        // デフォルト画像変更
        UserMst user = UserMst.findById(userId);
        if (user != null) {
            ImageFile img = null;
            if (defaultImage != null) {
                img = ImageFile.findById(defaultImage);
            }

            if ((img != null && !img.equals(user.デフォルト画像))
                    || (img == null && user.デフォルト画像 != null)) {
                user.デフォルト画像 = img;
                user.save();
                flash.success("%sさんのデフォルト画像を変更しました.", user.名前);
                UserKanri.list();
            }
        }
        renderArgs.put("user", user);
        render("@index");
    }

}
