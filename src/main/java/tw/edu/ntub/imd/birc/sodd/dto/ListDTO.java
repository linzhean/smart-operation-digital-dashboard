package tw.edu.ntub.imd.birc.sodd.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListDTO {
    private List<String> sponsorList;
    private List<String> exporterList;
    private List<Integer> dashboardCharts;
}
