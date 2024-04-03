package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "department", schema = "113-smart_opeartion_digital_dashboard", catalog = "")
public class DepartmentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "available")
    private String available;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentEntity that = (DepartmentEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(available, that.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, available);
    }
}
