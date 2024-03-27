package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 使用者帳號
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "useraccount", schema = Config.DATABASE_NAME)
public class UserAccount {
    /**
     * 帳號ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", length = 254, nullable = false)
    private String userId;
    /**
     * 帳號名稱
     *
     * @since 1.0.0
     */
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    /**
     * 是否啟用(0: 不啟用，1: 啟用)
     *
     * @since 1.0.0
     */
    @Column(name = "available", length = 1, nullable = false)
    private String available;
    /**
     * 最後登入時間
     *
     * @since 1.0.0
     */
    @Column(name = "last_login_time", nullable = false)
    private LocalDateTime lastLoginTime;
    /**
     * 創建時間
     *
     * @since 1.0.0
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
    /**
     * 最後修改人
     *
     * @since 1.0.0
     */
    @Column(name = "last_update_user_id", length = 254, nullable = false)
    private String lastUpdateUserId;
    /**
     * 最後修改時間
     *
     * @since 1.0.0
     */
    @Column(name = "last_update_time", nullable = false)
    private LocalDateTime lastUpdateTime;
}
