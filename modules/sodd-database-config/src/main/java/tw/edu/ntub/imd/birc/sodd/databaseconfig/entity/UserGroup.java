package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.BooleanTo1And0Converter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener.UserGroupListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 使用者-群組
 *
 * @since 1.0.0
 */
@Data
@Entity
@EntityListeners(UserGroupListener.class)
@IdClass(UserGroupId.class)
@Table(name = "user_group", schema = Config.DATABASE_NAME)
public class UserGroup {
    /**
     * 使用者ID
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "user_id", length = 45, nullable = false)
    private String userId;
    /**
     * 群組ID
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "group_id", nullable = false)
    private Integer groupId;
    /**
     * 是否啟用(0: 不啟用, 1: 啟用)
     *
     * @since 1.0.0
     */
    @Convert(converter = BooleanTo1And0Converter.class)
    @Column(name = "available", nullable = false)
    private Boolean available;
    /**
     * 創建人ID
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
     * 修改人ID
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
    /**
     * 使用者帳號
     *
     * @since 1.0.0
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private UserAccount userAccount;
}
