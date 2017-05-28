package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.model.DateLock;
import isr.naya.admiralproj.service.LockService;
import isr.naya.admiralproj.web.security.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

@Slf4j
@CorsRestController
@AllArgsConstructor
@RequestMapping("/admin/dashboard/locks")
public class AdminLocksController {

    private LockService lockService;

    @GetMapping
    public Set<DateLock> getLocks(@RequestParam("employeeId") Integer employeeId,
                                  @RequestParam("from") LocalDate from,
                                  @RequestParam("to") LocalDate to) {
        Set<DateLock> locks = lockService.getAllLocks(employeeId, from, to);
        log.info("Admin {} is retrieving locks of employee (id = {}) from {} to {}", AuthorizedUser.fullName(), employeeId, from.toString(), to.toString());
        return locks;
    }

    @PostMapping
    public DateLock saveLock(@RequestParam("employeeId") Integer employeeId,
                             @Valid @RequestBody DateLock lock) {
        DateLock saved = lockService.saveLock(lock, employeeId);
        log.info("Admin {} is saving new lock for employee (id = {}) for month {}", AuthorizedUser.fullName(), employeeId, Month.of(saved.getMonth()));
        return saved;
    }

    @DeleteMapping
    public void removeLock(@RequestParam("employeeId") Integer employeeId,
                           @RequestParam("year") Integer year,
                           @RequestParam("month") Integer month) {
        lockService.removeLock(employeeId, year, month);
        log.info("Admin {} is removing lock for Employee (id = {}) year {} month", AuthorizedUser.fullName(), employeeId, year, Month.of(month));
    }
}
