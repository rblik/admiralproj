package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.service.WorkInfoService;
import isr.naya.admiralproj.web.security.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@CorsRestController
@RequestMapping("/admin/info")
@AllArgsConstructor
public class JsonReportController {

    private WorkInfoService workInfoService;

    @GetMapping("/partial")
    public List<WorkInfo> getPartialDaysReport(@RequestParam("from") LocalDate from,
                                               @RequestParam("to") LocalDate to,
                                               @RequestParam("limit") Integer limit,
                                               @RequestParam("employeeId") Optional<Integer> employeeId,
                                               @RequestParam("departmentId") Optional<Integer> departmentId) {
        log.info("Admin {} is creating json partial report from {} to {}" +
                (employeeId.isPresent() ? " for employee (id = {})" : "") +
                (departmentId.isPresent() ? " and department (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        return workInfoService.getPartialWorkInfos(from, to, limit, employeeId, departmentId);
    }

    @GetMapping("/missing")
    public List<WorkInfo> getMissingDaysReport(@RequestParam("from") LocalDate from,
                                               @RequestParam("to") LocalDate to,
                                               @RequestParam("employeeId") Optional<Integer> employeeId,
                                               @RequestParam("departmentId") Optional<Integer> departmentId) {
        log.info("Admin {} is creating json missing report from {} to {}" +
                (employeeId.isPresent() ? " for employee (id = {})" : "") +
                (departmentId.isPresent() ? " and department (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        return workInfoService.getMissingWorkInfos(from, to, employeeId, departmentId);
    }

    @GetMapping("/pivotal")
    public List<WorkInfo> getPivotalReport(@RequestParam("from") LocalDate from,
                                           @RequestParam("to") LocalDate to,
                                           @RequestParam("employeeId") Optional<Integer> employeeId,
                                           @RequestParam("departmentId") Optional<Integer> departmentId,
                                           @RequestParam("projectId") Optional<Integer> projectId,
                                           @RequestParam("clientId") Optional<Integer> clientId) {
        log.info("Admin {} is creating json pivotal report from {} to {}" +
                (employeeId.isPresent() ? " for employee (id = {})" : "") +
                (projectId.isPresent() ? " and project (id = {})" : ""), AuthorizedUser.fullName(), from, to);
        return workInfoService.getWorkInfos(from, to, employeeId, departmentId, projectId, clientId);
    }
}
