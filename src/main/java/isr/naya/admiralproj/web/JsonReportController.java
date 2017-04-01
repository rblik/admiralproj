package isr.naya.admiralproj.web;

import isr.naya.admiralproj.dto.WorkInfo;
import isr.naya.admiralproj.service.WorkInfoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/admin/info")
@AllArgsConstructor
public class JsonReportController {

    private WorkInfoService workInfoService;

    @GetMapping("/partial")
    public List<WorkInfo> getPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                               @RequestParam("to") LocalDate to,
                                               @RequestParam("limit") Integer limit) {
        return workInfoService.getPartialDays(from, to, limit);
    }

    @GetMapping("/missing")
    public List<WorkInfo> getMissingDaysReport(@RequestParam("from") LocalDate from,
                                               @RequestParam("to") LocalDate to) {
        return workInfoService.getMissingDays(from, to);
    }

    @GetMapping("/pivotal")
    public List<WorkInfo> getPivotalReport(@RequestParam("from") LocalDate from,
                                           @RequestParam("to") LocalDate to,
                                           @RequestParam("employeeId") Optional<Integer> employeeId,
                                           @RequestParam("projectId") Optional<Integer> projectId) {
        return workInfoService.getWorkInfos(from, to, employeeId, projectId);
    }
}
