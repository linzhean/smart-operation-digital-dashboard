package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.IdentityConverter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.RequestTypeConverter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.RequestType;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.listener.RequestListener;


import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 請求紀錄
 *
 * @since 1.0.0
 */
@Data
@Entity
@EntityListeners(RequestListener.class)
@Table(name = "request", schema = Config.DATABASE_NAME)
public class Request {
    /**
     * 請求流水號
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Integer requestId;
    /**
     * 請求(0:GET/1:POST/2:PATCH/3:DELETE)
     *
     * @since 1.0.0
     */
    @Convert(converter = RequestTypeConverter.class)
    @Column(name = "mapping", length = 1, nullable = false)
    private RequestType mapping;
    /**
     * 使用者代理
     *
     * @since 1.0.0
     */
    @Column(name = "user_agent", length = 200)
    private String userAgent;
    /**
     * url
     *
     * @since 1.0.0
     */
    @Column(name = "url", length = 50, nullable = false)
    private String url;
    /**
     * 檔案路徑
     *
     * @since 1.0.0
     */
    @Column(name = "file_path", length = 150)
    private String filePath;
    /**
     * 執行結果狀態
     *
     * @since 1.0.0
     */
    @Column(name = "status", length = 50, nullable = false)
    private String status;
    /**
     * 請求來源ip
     *
     * @since 1.0.0
     */
    @Column(name = "request_ip_from", length = 15)
    private String requestIpFrom;
    /**
     * 請求者身分
     *
     * @since 1.0.0
     */
    @Convert(converter = IdentityConverter.class)
    @Column(name = "identity", length = 1, nullable = false)
    private Identity identity;
    /**
     * 請求者ID
     *
     * @since 1.0.0
     */
    @Column(name = "request_userId", length = 50)
    private String requestUserId;
    /**
     * 請求時間
     *
     * @since 1.0.0
     */
    @Column(name = "request_time", nullable = false)
    private LocalDateTime requestTime;
}
