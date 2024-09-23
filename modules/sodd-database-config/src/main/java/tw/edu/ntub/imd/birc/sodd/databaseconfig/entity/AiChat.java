package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.AIGenTypeConverter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.BooleanTo1And0Converter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.AIGenType;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener.AiChatListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * AI分析
 *
 * @since 1.0.0
 */
@Data
@Entity
@EntityListeners(AiChatListener.class)
@Table(name = "ai_chat", schema = Config.DATABASE_NAME)
public class AiChat {
    /**
     * AI分析ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    /**
     * 圖表ID
     *
     * @since 1.0.0
     */
    @Column(name = "chart_id", nullable = false)
    private Integer chartId;
    /**
     * 上則訊息ID
     *
     * @since 1.0.0
     */
    @Column(name = "message_id")
    private Integer messageId;
    /**
     * 生成方(0: AI，1: 使用者)
     *
     * @since 1.0.0
     */
    @Convert(converter = AIGenTypeConverter.class)
    @Column(name = "generator", nullable = false)
    private AIGenType generator;
    /**
     * 訊息內容
     *
     * @since 1.0.0
     */
    @Column(name = "content", nullable = false)
    private String content;
    /**
     * 是否啟用(0: 停用，1: 啟用)
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
