package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;

import javax.persistence.*;

/**
 * 使用者可視圖表
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "chart_user_saw", schema = Config.DATABASE_NAME)
@Immutable
public class ChartUserSaw {
    /**
     * 流水號
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    /**
     * 使用者ID
     *
     * @since 1.0.0
     */
    @Column(name = "user_id", nullable = false)
    private String userId;
    /**
     * 使用者名稱
     *
     * @since 1.0.0
     */
    @Column(name = "user_name", nullable = false)
    private String userName;
    /**
     * 權限(0: 無權限，1: 一般使用者，2: 管理員)
     *
     * @since 1.0.0
     */
    @Column(name = "identity", nullable = false)
    private String identity;
    /**
     * 圖表ID
     *
     * @since 1.0.0
     */
    @Column(name = "chart_id", nullable = false)
    private Integer chartId;
    /**
     * 圖表名稱
     *
     * @since 1.0.0
     */
    @Column(name = "chart_name", nullable = false)
    private String chartName;
    /**
     * 群組ID
     *
     * @since 1.0.0
     */
    @Column(name = "group_id", nullable = false)
    private Integer groupId;
    /**
     * 群組名稱
     *
     * @since 1.0.0
     */
    @Column(name = "group_name", nullable = false)
    private String groupName;
    /**
     * 使用者-群組ID
     *
     * @since 1.0.0
     */
    @Column(name = "user_group_id", nullable = false)
    private Integer userGroupId;
    /**
     * 圖表-群組ID
     *
     * @since 1.0.0
     */
    @Column(name = "chart_group_id", nullable = false)
    private Integer chartGroupId;
}
