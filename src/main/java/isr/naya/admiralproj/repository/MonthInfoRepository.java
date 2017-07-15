package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.model.MonthInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jonathan on 14/07/17.
 */
public interface MonthInfoRepository extends JpaRepository<MonthInfo, Integer> {

    @Query("select info from MonthInfo info where info.year=?2 and info.month=?3 and info.employee.id=?1")
    List<MonthInfo> getByEmployeeIdAndYearAndMonth(Integer employeeId, Integer year, Integer month);

    @Modifying
    @Query("update MonthInfo info set info.locked = true where info.employee.id=?1 and info.year=?2 and info.month=?3")
    int deleteLock(Integer employeeId, Integer year, Integer month);
}
