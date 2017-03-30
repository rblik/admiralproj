package isr.naya.admiralproj.web;

import com.sun.istack.internal.Nullable;
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
import java.util.Set;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private WorkInfoService service;

    @GetMapping("/partial")
    public List<WorkInfo> getPartialDaysReport(@RequestParam(value = "from") LocalDate from,
                                               @RequestParam("to") LocalDate to,
                                               @RequestParam("limit") Integer limit) {
        return service.getPartialDays(from, to, limit);
    }

    @GetMapping("/missing")
    public Set<WorkInfo> getMissingDaysReport(@RequestParam("from") LocalDate from,
                                              @RequestParam("to") LocalDate to) {
        return service.getMissingDays(from, to);
    }

    @GetMapping("/pivotal")
    public List<WorkInfo> getPivotalReport(@RequestParam("from") LocalDate from,
                                           @RequestParam("to") LocalDate to,
                                           @RequestParam("employeeId") @Nullable Optional<Integer> employeeId,
                                           @RequestParam("projectId") @Nullable Optional<Integer> projectId) {

        return service.getAllUnitsByDateAndEmployeeAndProject(from, to, employeeId.get(), projectId.get());
    }
}
