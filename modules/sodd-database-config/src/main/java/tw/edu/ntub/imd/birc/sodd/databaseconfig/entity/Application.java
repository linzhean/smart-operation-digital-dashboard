package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.ApplyConverter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.BooleanTo1And0Converter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Apply;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 暫時查看圖表申請
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "application", schema = Config.DATABASE_NAME)
public class Application {
    /**
     * 暫時查看圖表申請流水號
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
     * 申請人
     *
     * @since 1.0.0
     */
    @Column(name = "applicant", nullable = false)
    private String applicant;
    /**
     * 保證人
     *
     * @since 1.0.0
     */
    @Column(name = "guarantor", nullable = false)
    private String guarantor;
    /**
     * 開始日期
     *
     * @since 1.0.0
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    /**
     * 結束日期
     *
     * @since 1.0.0
     */
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    /**
     * 申請原因
     *
     * @since 1.0.0
     */
    @Column(name = "reason", nullable = false)
    private String reason;
    /**
     * 申請狀態(0: 未通過, 1: 通過)
     *
     * @since 1.0.0
     */
    @Convert(converter = ApplyConverter.class)
    @Column(name = "apply_status", length = 1, nullable = false)
    private Apply applyStatus;
    /**
     * 是否啟用(0: 不啟用，1: 啟用)
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
