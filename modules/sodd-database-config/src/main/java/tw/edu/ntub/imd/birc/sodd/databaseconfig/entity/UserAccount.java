package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.BooleanTo1And0Converter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.IdentityConverter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener.UserAccountListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 使用者帳號
 *
 * @since 1.0.0
 */
@Data
@Entity
@EntityListeners(UserAccountListener.class)
@Table(name = "user_account", schema = Config.DATABASE_NAME)
public class UserAccount {
    /**
     * 使用者ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", length = 254, nullable = false)
    private String userId;
    /**
     * 使用者名稱
     *
     * @since 1.0.0
     */
    @Column(name = "user_name", length = 45, nullable = false)
    private String userName;
    /**
     * 部門ID
     *
     * @since 1.0.0
     */
    @Column(name = "department_id", length = 2, nullable = false)
    private String departmentId;
    /**
     * googleID
     *
     * @since 1.0.0
     */
    @Column(name = "google_id", length = 25)
    private String googleId;
    /**
     * gmail
     *
     * @since 1.0.0
     */
    @Column(name = "gmail", length = 254, nullable = false)
    private String gmail;
    /**
     * 權限(0: 無權限，1: 一般使用者，2: 管理員)
     *
     * @since 1.0.0
     */
    @Convert(converter = IdentityConverter.class)
    @Column(name = "identity", length = 1, nullable = false)
    private Identity identity;
    /**
     * 職務
     *
     * @since 1.0.0
     */
    @Column(name = "position", length = 45, nullable = false)
    private String position;
    /**
     * 是否啟用(0: 不啟用 1:啟用)
     *
     * @since 1.0.0
     */
    @Convert(converter = BooleanTo1And0Converter.class)
    @Column(name = "available", nullable = false)
    private Boolean available;
    /**
     * 創建者ID
     *
     * @since 1.0.0
     */
    @Column(name = "create_id", length = 254, nullable = false)
    private String createId;
    /**
     * 創建日期
     *
     * @since 1.0.0
     */
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    /**
     * 修改者ID
     *
     * @since 1.0.0
     */
    @Column(name = "modify_id", length = 254, nullable = false)
    private String modifyId;
    /**
     * 修改日期
     *
     * @since 1.0.0
     */
    @Column(name = "modify_date", nullable = false)
    private LocalDateTime modifyDate;
}
