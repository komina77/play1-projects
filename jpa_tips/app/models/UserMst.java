package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import play.Logger;
import play.data.binding.As;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import play.libs.Crypto;

@Entity(name="ユーザ管理")
@Table(indexes = @Index(columnList = "名前,パスワードハッシュ", unique = true))
public class UserMst extends Model {

    @Required
    @MaxSize(10)
    @Unique
    @Column(nullable = false, unique = true)
    public String 名前;
    /**
     * パスワードハッシュ値
     */
    @Column(nullable = false)
    public String パスワードハッシュ;
    /**
     * パスワード（入力用） 永続化しない
     */
    @Transient
    public String パスワード = "";
    /**
     * Date型の扱いを@Temporalにより、日付のみ／時刻のみ／日付＋時刻、の３タイプから指定できる。
     */
    @Required
    @As("yyyyMMdd")
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    public Date 生年月日;
    /**
     * 長いテキストを保存する場合は@Lob
     */
    @Lob
    public String 備考;
    /**
     * 更新日時を使ってバージョン管理（編集中に他者に更新されていないか検証）する.
     */
    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date 更新日時;

    /** fieldタグのvalueでgetter必要 */
    public String get名前() {
        return this.名前;
    }
    /** fieldタグのvalueでgetter必要 */
    public Date get生年月日() {
        return this.生年月日;
    }
    /** fieldタグのvalueでgetter必要 */
    public String get備考() {
        return this.備考;
    }
    /**
     * パスワードが設定されたらハッシュを計算しなおす
     */
    public void setパスワード(String s) {
        パスワード = s;
        if (s != null && s.length() > 0) {
            パスワードハッシュ = generatePasswordHash(パスワード);
            Logger.info("パスワードハッシュ再算出.");
        }
    }

    /** コールバック機能のテスト */
    @PostLoad
    public void onPostLoad() { Logger.info("onPostLoad: SELECT後"); }
    @PrePersist
    public void onPrePersist() { Logger.info("onPrePersist: INSERT前"); }
    @PostPersist
    public void onPostPersist() { Logger.info("onPostPersist: INSERT後"); }
    @PreUpdate
    public void onPreUpdate() { Logger.info("onPreUpdate: UPDATE前"); }
    @PostUpdate
    public void onPostUpdate() { Logger.info("onPostUpdate: UPDATE後"); }
    @PreRemove
    public void onPreRemove() { Logger.info("onPreRemove: DELETE前"); }
    @PostRemove
    public void onPostRemove() { Logger.info("onPostRemove: DELETE後"); }

    /**
     * @param password
     * @return パスワードハッシュ生成
     */
    static String generatePasswordHash(String password) {
        return Crypto.passwordHash(password);
    }

    /**
     * 名前とパスワードでユーザ検索
     * @param name
     * @param password
     * @return
     */
    public static UserMst login(String name, String password) {
        UserMst user = find("名前 = :name AND パスワードハッシュ = :passhash")
                .setParameter("name", name)
                .setParameter("passhash", generatePasswordHash(password))
                .first();
        return user;
    }

}
