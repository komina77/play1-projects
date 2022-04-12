package models;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity(name="画像ファイル")
@Table(indexes = @Index(columnList = "ファイル", unique = true))
public class ImageFile extends BaseEntity {
    @ManyToOne
    public UserMst 所有ユーザ;

    @Required
    @Column(nullable = false)
    public String キャプション;

    /**
     * play.db.jpa.Blob 型を使ってアップロードされたファイルを (データベースではなく) ファイルシステムに保存することができます。
     * サーバ側では、Play はアップロードされた画像をアプリケーションフォルダの中にある attachments/ フォルダのファイルに保存します。
     * このファイル名 ( UUID) と MIME タイプは、データベースの属性に VARCHAR SQL 型として保存されます。
     */
    @Required
    public Blob ファイル;

    /**
     * delete後にattachments/フォルダのファイルも削除する.
     */
    @PostRemove
    public void onPostRemove() {
        Logger.info("[ImageFile] onPostRemove: %s", this.ファイル.getUUID());
        this.ファイル.getFile().delete();
    }

}
