package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class AssignedTaskSponsorId implements Serializable {
    /**
     * 交辦事項ID
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "assigned_task_id", nullable = false)
    private Integer assignedTaskId;
    /**
     * 發起人ID
     *
     * @since 1.0.0
     */
    @Id
    @Column(name = "sponsor_user_id", length = 254, nullable = false)
    private String sponsorUserId;
}
