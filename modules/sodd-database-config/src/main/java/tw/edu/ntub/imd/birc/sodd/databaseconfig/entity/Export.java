package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "export", schema = "113-smart_opeartion_digital_dashboard", catalog = "")
public class ExportEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "chart_id")
    private int chartId;
    @Basic
    @Column(name = "exporter")
    private String exporter;
    @Basic
    @Column(name = "available")
    private String available;
    @Basic
    @Column(name = "create_id")
    private String createId;
    @Basic
    @Column(name = "create_date")
    private Timestamp createDate;
    @Basic
    @Column(name = "modify_id")
    private String modifyId;
    @Basic
    @Column(name = "modify_date")
    private Timestamp modifyDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChartId() {
        return chartId;
    }

    public void setChartId(int chartId) {
        this.chartId = chartId;
    }

    public String getExporter() {
        return exporter;
    }

    public void setExporter(String exporter) {
        this.exporter = exporter;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExportEntity that = (ExportEntity) o;
        return id == that.id && chartId == that.chartId && Objects.equals(exporter, that.exporter) && Objects.equals(available, that.available) && Objects.equals(createId, that.createId) && Objects.equals(createDate, that.createDate) && Objects.equals(modifyId, that.modifyId) && Objects.equals(modifyDate, that.modifyDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chartId, exporter, available, createId, createDate, modifyId, modifyDate);
    }
}
