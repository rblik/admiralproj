package isr.naya.admiralproj.web;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.service.WorkInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/admin/info")
@AllArgsConstructor
public class JsonReportController {

    private WorkInfoService workInfoService;

    @GetMapping("/partial")
    public List<WorkInfo> getPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                               @RequestParam("to") LocalDate to,
                                               @RequestParam("limit") Integer limit) {
        List<WorkInfo> days = workInfoService.getPartialDays(from, to, limit);
        log.info("Admin {} is creating json partial report from {} to {}", AuthorizedUser.fullName(), from, to);
        return days;
    }

    @GetMapping("/missing")
    public List<WorkInfo> getMissingDaysReport(@RequestParam("from") LocalDate from,
                                               @RequestParam("to") LocalDate to) {
        List<WorkInfo> days = workInfoService.getMissingDays(from, to);
        log.info("Admin {} is creating json missing report from {} to {}", AuthorizedUser.fullName(), from, to);
        return days;
    }

    @GetMapping("/pivotal")
    public List<WorkInfo> getPivotalReport(@RequestParam("from") LocalDate from,
                                           @RequestParam("to") LocalDate to,
                                           @RequestParam("employeeId") Optional<Integer> employeeId,
                                           @RequestParam("projectId") Optional<Integer> projectId) {
        log.info("Admin {} is creating json pivotal report from {} to {}" +
                (employeeId.isPresent() ? "for employee (id = {})" : "") +
                (projectId.isPresent() ? "and project (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        return workInfoService.getWorkInfos(from, to, employeeId, projectId);
    }
}
