package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.model.DateLock;
import isr.naya.admiralproj.service.LockService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Month;

@Slf4j
@CorsRestController
@AllArgsConstructor
@RequestMapping("/backend/admin/dashboard/locks")
public class AdminLocksController {

    private LockService lockService;

    @GetMapping
    public boolean isExist(@AuthenticationPrincipal AuthorizedUser admin,
                                  @RequestParam("employeeId") Integer employeeId,
                                  @RequestParam("year") Integer year,
                                  @RequestParam("month") Integer month) {
        boolean lock = lockService.isLockExists(employeeId, year, month);
        log.info("Admin {} is ckecking lock of employee (id = {}) for {} {}", admin.getFullName(), employeeId, month.toString(), year.toString());
        return lock;
    }

    @PostMapping
    public DateLock saveLock(@AuthenticationPrincipal AuthorizedUser admin,
                             @RequestParam("employeeId") Integer employeeId,
                             @Valid @RequestBody DateLock lock) {
        DateLock saved = lockService.saveLock(lock, employeeId);
        log.info("Admin {} is saving new lock for employee (id = {}) for month {}", admin.getFullName(), employeeId, Month.of(saved.getMonth()));
        return saved;
    }

    @DeleteMapping
    public void removeLock(@AuthenticationPrincipal AuthorizedUser admin,
                           @RequestParam("employeeId") Integer employeeId,
                           @RequestParam("year") Integer year,
                           @RequestParam("month") Integer month) {
        lockService.removeLock(employeeId, year, month);
        log.info("Admin {} is removing lock for Employee (id = {}) year {} month", admin.getFullName(), employeeId, year, Month.of(month));
    }
}
