package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.MonthInfo;
import isr.naya.admiralproj.model.DateLock;
import isr.naya.admiralproj.model.MonthlyStandard;
import isr.naya.admiralproj.service.LockService;
import isr.naya.admiralproj.service.MonthInfoService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@CorsRestController
@AllArgsConstructor
@RequestMapping("/backend/admin/dashboard/monthinfo")
public class AdminMonthInfoController {

    private MonthInfoService monthInfoService;
    private LockService lockService;

    @GetMapping(params = "employeeId")
    public MonthInfo getMonthInfo(@AuthenticationPrincipal AuthorizedUser admin,
                                  @RequestParam("employeeId") Integer employeeId,
                                  @RequestParam("year") Integer year,
                                  @RequestParam("month") Integer month) {
        MonthlyStandard monthStandard = monthInfoService.getStandardForMonth(year, month);
        DateLock monthLock = lockService.getLock(employeeId, year, month);
        MonthInfo monthInfo = new MonthInfo(monthStandard, monthLock);
        log.info("Admin {} is retrieving his month info for {} {}", admin.getFullName(), month.toString(), year.toString());
        return monthInfo;
    }

    @GetMapping
    public List<MonthlyStandard> getMonthlyStandarts(@AuthenticationPrincipal AuthorizedUser admin,
                                                     @RequestParam(value = "year", required = false) Integer year) {
        log.info("Admin {} is retrieving monthly standarts", admin.getFullName());
        return year != null? monthInfoService.getStandardsForYear(year) : monthInfoService.getAllStandards();
    }

    @PostMapping
    public DateLock saveLock(@AuthenticationPrincipal AuthorizedUser admin,
                             @RequestParam(value = "employeeId", required = false) Integer employeeId,
                             @RequestParam("year") Integer year,
                             @RequestParam("month") Integer month) {
        if (employeeId != null) {
            DateLock saved = lockService.saveLock(employeeId, year, month);
            log.info("Admin {} is saving new month info for employee (id = {}) for month {}", admin.getFullName(), employeeId, Month.of(saved.getYearMonth().getMonthValue()));
            return saved;
        } else {
            lockService.saveLockForAll(year, month);
            log.info("Admin {} is saving new month info for all employees  for month {}", admin.getFullName(), Month.of(month));
            return DateLock.builder().yearMonth(YearMonth.of(year, month)).build();
        }

    }

    @DeleteMapping
    public void removeLock(@AuthenticationPrincipal AuthorizedUser admin,
                           @RequestParam(value = "employeeId", required = false) Integer employeeId,
                           @RequestParam("year") Integer year,
                           @RequestParam("month") Integer month) {
        if (employeeId != null) {
            lockService.removeLock(employeeId, year, month);
            log.info("Admin {} is removing lock for Employee (id = {}) year {} month", admin.getFullName(), employeeId, year, Month.of(month));
        } else {
            log.info("Admin {} is removing lock for all employees year {} month", admin.getFullName(), year, Month.of(month));
            lockService.removeLockForAll(year,month);
        }
    }

    @PutMapping
    public MonthInfo updateHoursSumForAllEmployees(@AuthenticationPrincipal AuthorizedUser admin,
                                                   @RequestParam("year") Integer year,
                                                   @RequestParam("month") Integer month,
                                                   @RequestParam("hoursSum") Double hoursSum) {
        MonthlyStandard monthlyStandard = monthInfoService.saveStandardForMonth(year, month, hoursSum);
        log.info("Admin {} is updating hours sum for employees year {} month {}", admin.getFullName(), year, month);
        return new MonthInfo(monthlyStandard);
    }
}
