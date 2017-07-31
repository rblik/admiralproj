package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.model.DateLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.YearMonth;
import java.util.List;

public interface LockRepository extends JpaRepository<DateLock, YearMonth> {

    @Query("select l from DateLock l where l.employee.id=?1 and l.yearMonth=?2")
    List<DateLock> getLockByEmployeeIdAndYearAndMonth(Integer employeeId, YearMonth yearMonth);

    @Modifying
    @Query("delete from DateLock l where l.employee.id=?1 and l.yearMonth=?2")
    int delete(Integer employeeId, YearMonth yearMonth);
}
