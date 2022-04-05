package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import play.data.binding.As;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity(name="ユーザ管理")
public class UserMst extends Model {

    @Required
    @MaxSize(10)
    @Column(nullable = false)
    public String 名前;
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
}
