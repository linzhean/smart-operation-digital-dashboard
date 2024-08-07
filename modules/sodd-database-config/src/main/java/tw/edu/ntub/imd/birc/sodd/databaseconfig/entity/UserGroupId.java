package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupId implements Serializable {
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
}
