package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.service.WorkInfoService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static isr.naya.admiralproj.web.util.LogUtil.logMessage;

@Slf4j
@CorsRestController
@RequestMapping("/backend/admin/info")
@AllArgsConstructor
public class JsonReportController {

    private WorkInfoService workInfoService;

    @GetMapping("/pivotal")
    public List<WorkInfo> getPivotalReport(@AuthenticationPrincipal AuthorizedUser admin,
                                           @RequestParam("from") LocalDate from,
                                           @RequestParam("to") LocalDate to,
                                           @RequestParam("employeeId") Optional<Integer> employeeId,
                                           @RequestParam("departmentId") Optional<Integer> departmentId,
                                           @RequestParam("projectId") Optional<Integer> projectId,
                                           @RequestParam("clientId") Optional<Integer> clientId) {
        log.info(logMessage("pivotal", "json", employeeId, departmentId, projectId, clientId),
                admin.getFullName(), from, to,
                employeeId.orElseGet(() -> departmentId.orElseGet(() -> null)),
                projectId.orElseGet(() -> clientId.orElseGet(() -> null)));
        return workInfoService.getWorkInfos(from, to, employeeId, departmentId, projectId, clientId);
    }

    @GetMapping("/income")
    public List<WorkInfo> getIncomeReport(@AuthenticationPrincipal AuthorizedUser admin,
                                          @RequestParam("from") LocalDate from,
                                          @RequestParam("to") LocalDate to,
                                          @RequestParam("employeeId") Optional<Integer> employeeId,
                                          @RequestParam("departmentId") Optional<Integer> departmentId,
                                          @RequestParam("projectId") Optional<Integer> projectId,
                                          @RequestParam("clientId") Optional<Integer> clientId) {
        log.info(logMessage("income", "json", employeeId, departmentId, projectId, clientId),
                admin.getFullName(), from, to,
                employeeId.orElseGet(() -> departmentId.orElseGet(() -> null)),
                projectId.orElseGet(() -> clientId.orElseGet(() -> null)));
        return workInfoService.getIncomeReports(from, to, employeeId, departmentId, projectId, clientId);
    }

    @GetMapping("/partial")
    public List<WorkInfo> getPartialDaysReport(@AuthenticationPrincipal AuthorizedUser admin,
                                               @RequestParam("from") LocalDate from,
                                               @RequestParam("to") LocalDate to,
                                               @RequestParam("limit") Integer limit,
                                               @RequestParam("employeeId") Optional<Integer> employeeId,
                                               @RequestParam("departmentId") Optional<Integer> departmentId) {
        List<WorkInfo> infos = workInfoService.getPartialWorkInfos(from, to, limit, employeeId, departmentId);
        log.info(logMessage("partial", "json", employeeId, departmentId),
                admin.getFullName(), from, to,
                employeeId.orElseGet(() -> departmentId.orElseGet(() -> null)));
        return infos;
    }

    @GetMapping("/missing")
    public List<WorkInfo> getMissingDaysReport(@AuthenticationPrincipal AuthorizedUser admin,
                                               @RequestParam("from") LocalDate from,
                                               @RequestParam("to") LocalDate to,
                                               @RequestParam("employeeId") Optional<Integer> employeeId,
                                               @RequestParam("departmentId") Optional<Integer> departmentId) {
        log.info(logMessage("missing", "json", employeeId, departmentId),
                admin.getFullName(), from, to,
                employeeId.orElseGet(() -> departmentId.orElseGet(() -> null)));
        return workInfoService.getMissingWorkInfos(from, to, employeeId, departmentId);
    }
}
