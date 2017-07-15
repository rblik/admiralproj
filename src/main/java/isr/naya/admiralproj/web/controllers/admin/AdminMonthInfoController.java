package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.MonthDefaultHoursUpdateDto;
import isr.naya.admiralproj.model.MonthInfo;
import isr.naya.admiralproj.service.MonthInfoService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Month;
import java.util.List;

@Slf4j
@CorsRestController
@AllArgsConstructor
@RequestMapping("/backend/admin/dashboard/monthinfo")
public class AdminMonthInfoController {

    private MonthInfoService monthInfoService;

    /*@GetMapping
    public boolean isExist(@AuthenticationPrincipal AuthorizedUser admin,
                                  @RequestParam("employeeId") Integer employeeId,
                                  @RequestParam("year") Integer year,
                                  @RequestParam("month") Integer month) {
//        boolean lock = lockService.isLockExists(employeeId, year, month);
        boolean lock = monthInfoService.isLockExists(employeeId, year, month);
        log.info("Admin {} is ckecking lock of employee (id = {}) for {} {}", admin.getFullName(), employeeId, month.toString(), year.toString());
        return lock;
    }*/

    @GetMapping
    public MonthInfo getMonthInfo(@AuthenticationPrincipal AuthorizedUser admin,
                                  @RequestParam("employeeId") Integer employeeId,
                                  @RequestParam("year") Integer year,
                                  @RequestParam("month") Integer month) {
        MonthInfo monthInfo = monthInfoService.getOrNew(employeeId, year, month);
        log.info("Admin {} is retrieving his month info for {} {}", admin.getFullName(), month.toString(), year.toString());
        return monthInfo;
    }

    @PostMapping
    public MonthInfo saveMonthInfo(@AuthenticationPrincipal AuthorizedUser admin,
                             @RequestParam("employeeId") Integer employeeId,
                             @Valid @RequestBody MonthInfo monthInfo) {
//        DateLock saved = lockService.saveLock(lock, employeeId);
        MonthInfo saved = monthInfoService.save(monthInfo, employeeId);
        log.info("Admin {} is saving new month info for employee (id = {}) for month {}", admin.getFullName(), employeeId, Month.of(saved.getMonth()));
        return saved;
    }

    @DeleteMapping
    public void removeLock(@AuthenticationPrincipal AuthorizedUser admin,
                           @RequestParam("employeeId") Integer employeeId,
                           @RequestParam("year") Integer year,
                           @RequestParam("month") Integer month) {
//        lockService.removeLock(employeeId, year, month);
        monthInfoService.removeLock(employeeId, year, month);
        log.info("Admin {} is removing lock for Employee (id = {}) year {} month", admin.getFullName(), employeeId, year, Month.of(month));
    }

    @PutMapping
    public Iterable<MonthInfo> updateHoursSumForAllEmployees(@AuthenticationPrincipal AuthorizedUser admin,
                                                             @RequestBody MonthDefaultHoursUpdateDto monthDefaultHoursUpdateDto) {
        MonthInfo monthInfo = monthDefaultHoursUpdateDto.getMonthInfo();
        List<Integer> employeeIds = monthDefaultHoursUpdateDto.getEmployeeIds();
        Iterable<MonthInfo> monthInfos = monthInfoService.updateHoursSumForAllEmployees(monthInfo, employeeIds);
        log.info("Admin {} is updating hours sum for employees year {} month {}", admin.getFullName(), monthInfo.getYear(), Month.of(monthInfo.getMonth()));
        return monthInfos;
    }
}
