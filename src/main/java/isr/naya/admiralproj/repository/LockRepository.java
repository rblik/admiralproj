package isr.naya.admiralproj.repository;

import isr.naya.admiralproj.model.DateLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface LockRepository extends JpaRepository<DateLock, Integer> {

    @Query("select l from DateLock l where l.employee.id=?1 and l.year >= ?2 and l.year <= ?4 and l.month >= ?3 and l.month <=?5")
    Set<DateLock> getAllLocksByEmployeeIdAndDateRange(Integer employeeId, Integer fromYear, Integer fromMonth, Integer toYear, Integer toMonth);

    @Modifying
    @Query("delete from DateLock l where l.employee.id=?1 and l.year=?2 and l.month=?3")
    int delete(Integer employeeId, Integer year, Integer month);
}
