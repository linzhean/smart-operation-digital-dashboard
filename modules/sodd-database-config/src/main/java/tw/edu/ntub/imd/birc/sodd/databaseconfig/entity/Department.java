package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.BooleanTo1And0Converter;

import javax.persistence.*;
import java.util.Objects;

/**
 * 部門
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "department", schema = Config.DATABASE_NAME)
public class Department {
    /**
     * 部門ID
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private String id;
    /**
     * 部門名稱
     *
     * @since 1.0.0
     */
    @Column(name = "name", length = 45, nullable = false)
    private String name;
    /**
     * 是否啟用(0: 不啟用 1:啟用)
     *
     * @since 1.0.0
     */
    @Convert(converter = BooleanTo1And0Converter.class)
    @Column(name = "available", nullable = false)
    private Boolean available;
}
