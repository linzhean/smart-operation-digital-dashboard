package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.BooleanTo1And0Converter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.ProcessStatusConverter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ProcessStatus;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener.MailListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 郵件
 *
 * @since 1.0.0
 */
@Data
@Entity
@EntityListeners(MailListener.class)
@Table(name = "mail", schema = Config.DATABASE_NAME)
public class Mail {
    /**
     * 郵件ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    /**
     * 交辦事項ID
     *
     * @since 1.0.0
     */
    @Column(name = "assigned_task_id", nullable = false)
    private Integer assignedTaskId;
    /**
     * 圖表ID
     *
     * @since 1.0.0
     */
    @Column(name = "chart_id", nullable = false)
    private Integer chartId;
    /**
     * 郵件名稱
     *
     * @since 1.0.0
     */
    @Column(name = "name", length = 254, nullable = false)
    private String name;
    /**
     * 郵件狀態
     *
     * @since 1.0.0
     */
    @Convert(converter = ProcessStatusConverter.class)
    @Column(name = "status", nullable = false)
    private ProcessStatus status;
    /**
     * 郵件發起人
     *
     * @since 1.0.0
     */
    @Column(name = "publisher", length = 254, nullable = false)
    private String publisher;
    /**
     * 郵件接收人
     *
     * @since 1.0.0
     */
    @Column(name = "receiver", length = 254, nullable = false)
    private String receiver;
    /**
     * 郵件發送時間
     *
     * @since 1.0.0
     */
    @Column(name = "email_send_time", nullable = false)
    private LocalDateTime emailSendTime;
    /**
     * 是否啟用(0: 不啟用 1:啟用)
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
}
