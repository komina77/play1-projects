package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import play.db.jpa.Model;
import play.mvc.Scope.Session;

@MappedSuperclass
public class BaseEntity extends Model {
    /**
     * 登録日時
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date 登録日時;

    /**
     * 登録者
     */
    @Column(nullable = false)
    public String 登録者;

    /**
     * 更新日時を使ってバージョン管理（編集中に他者に更新されていないか検証）する.
     */
    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date 更新日時;

    /**
     * 更新者
     */
    @Column(nullable = false)
    public String 更新者;

    @PreUpdate
    void onPreUpdate() {
        this.更新者 = Session.current.get().get("username");
    }
    @PrePersist
    void onPrePersist() {
        this.登録日時 = new Date();
        this.登録者 = Session.current.get().get("username");
        this.更新者 = Session.current.get().get("username");
    }
}
