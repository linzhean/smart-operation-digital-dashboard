package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@IdClass(LRPTAPK.class)
public class LRPTA {
    @Id
    @Column(name = "COMPANY")
    private String company;
    @Id
    @Column(name = "CREATOR")
    private String creator;
    @Id
    @Column(name = "USR_GROUP")
    private String usrGroup;
    @Id
    @Column(name = "CREATE_DATE")
    private String createDate;
    @Basic
    @Column(name = "MODIFIER")
    private String modifier;
    @Basic
    @Column(name = "MODI_DATE")
    private String modiDate;
    @Basic
    @Column(name = "FLAG")
    private BigDecimal flag;
    @Basic
    @Column(name = "TA001")
    private String ta001;
    @Basic
    @Column(name = "TA002")
    private String ta002;
    @Basic
    @Column(name = "TA003")
    private String ta003;
    @Basic
    @Column(name = "TA004")
    private String ta004;
    @Basic
    @Column(name = "TA005")
    private String ta005;
    @Basic
    @Column(name = "TA006")
    private BigDecimal ta006;
    @Basic
    @Column(name = "TA007")
    private String ta007;
    @Basic
    @Column(name = "TA008")
    private String ta008;
    @Basic
    @Column(name = "TA009")
    private String ta009;
    @Basic
    @Column(name = "TA010")
    private String ta010;
    @Basic
    @Column(name = "TA011")
    private BigDecimal ta011;
    @Basic
    @Column(name = "TA012")
    private BigDecimal ta012;
    @Basic
    @Column(name = "TA013")
    private BigDecimal ta013;
    @Basic
    @Column(name = "TA014")
    private BigDecimal ta014;
    @Basic
    @Column(name = "TA015")
    private BigDecimal ta015;
    @Basic
    @Column(name = "TA016")
    private BigDecimal ta016;
    @Basic
    @Column(name = "TA017")
    private BigDecimal ta017;
    @Basic
    @Column(name = "TA018")
    private BigDecimal ta018;
    @Basic
    @Column(name = "TA019")
    private BigDecimal ta019;
    @Basic
    @Column(name = "TA020")
    private BigDecimal ta020;
    @Basic
    @Column(name = "TA021")
    private BigDecimal ta021;
    @Basic
    @Column(name = "TA022")
    private String ta022;
    @Basic
    @Column(name = "TA023")
    private String ta023;
    @Basic
    @Column(name = "TA024")
    private String ta024;
    @Basic
    @Column(name = "TA025")
    private String ta025;
    @Basic
    @Column(name = "TA026")
    private String ta026;
    @Basic
    @Column(name = "TA027")
    private String ta027;
    @Basic
    @Column(name = "TA028")
    private String ta028;
    @Basic
    @Column(name = "TA029")
    private String ta029;
    @Basic
    @Column(name = "TA030")
    private BigDecimal ta030;
    @Basic
    @Column(name = "TA031")
    private String ta031;
    @Basic
    @Column(name = "TA032")
    private BigDecimal ta032;
    @Basic
    @Column(name = "TA033")
    private String ta033;
    @Basic
    @Column(name = "TA034")
    private BigDecimal ta034;
    @Basic
    @Column(name = "TA035")
    private BigDecimal ta035;
    @Basic
    @Column(name = "TA036")
    private BigDecimal ta036;
    @Basic
    @Column(name = "TA037")
    private String ta037;
    @Basic
    @Column(name = "TA038")
    private String ta038;
    @Basic
    @Column(name = "TA039")
    private String ta039;
    @Basic
    @Column(name = "TA040")
    private String ta040;
    @Basic
    @Column(name = "TA041")
    private String ta041;
    @Basic
    @Column(name = "TA042")
    private String ta042;
    @Basic
    @Column(name = "TA043")
    private String ta043;
    @Basic
    @Column(name = "TA044")
    private String ta044;
    @Basic
    @Column(name = "TA045")
    private String ta045;
    @Basic
    @Column(name = "TA046")
    private String ta046;
    @Basic
    @Column(name = "TA047")
    private String ta047;
    @Basic
    @Column(name = "TA048")
    private String ta048;
    @Basic
    @Column(name = "TA049")
    private BigDecimal ta049;
    @Basic
    @Column(name = "TA050")
    private String ta050;
    @Basic
    @Column(name = "UDF01")
    private String udf01;
    @Basic
    @Column(name = "UDF02")
    private String udf02;
    @Basic
    @Column(name = "UDF03")
    private String udf03;
    @Basic
    @Column(name = "UDF04")
    private String udf04;
    @Basic
    @Column(name = "UDF05")
    private String udf05;
    @Basic
    @Column(name = "UDF06")
    private BigDecimal udf06;
    @Basic
    @Column(name = "UDF07")
    private BigDecimal udf07;
    @Basic
    @Column(name = "UDF08")
    private BigDecimal udf08;
    @Basic
    @Column(name = "UDF09")
    private BigDecimal udf09;
    @Basic
    @Column(name = "UDF10")
    private BigDecimal udf10;
}
