package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.Config;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.BooleanTo1And0Converter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter.IdentityConverter;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "user_chart_summary", schema = Config.DATABASE_NAME)
@Immutable
public class UserChartSummary {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_name")
    private String userName;
    @Convert(converter = IdentityConverter.class)
    @Column(name = "identity")
    private Identity identity;
    @Column(name = "chart_id")
    private Integer chartId;
    @Column(name = "chart_name")
    private String chartName;
    @Column(name = "group_id")
    private Integer groupId;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "user_group_id")
    private Integer userGroupId;
    @Convert(converter = BooleanTo1And0Converter.class)
    @Column(name = "user_in_group")
    private Boolean userInGroup;
    @Column(name = "chart_group_id")
    private Integer chartGroupId;
    @Convert(converter = BooleanTo1And0Converter.class)
    @Column(name = "chart_in_group")
    private Boolean chartInGroup;
}
