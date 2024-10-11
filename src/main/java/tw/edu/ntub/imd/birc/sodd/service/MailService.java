package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.MailBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTasks;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;

import java.math.BigDecimal;
import java.util.List;

public interface MailService extends BaseService<MailBean, Integer> {
    List<MailBean> searchByStatus(String userId, String status);

    void sendSystemAssignedTask(AssignedTasks assignedTasks,
                                Chart chart,
                                BigDecimal ratio,
                                UserAccount userAccount,
                                String status,
                                Double standard);
}
