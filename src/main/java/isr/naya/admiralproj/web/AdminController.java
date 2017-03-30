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

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private WorkInfoService service;

    @GetMapping("/partial")
    public List<WorkInfo> getWorkUnits(@RequestParam(value = "from") LocalDate from,
                                       @RequestParam(value = "to") LocalDate to,
                                       @RequestParam(value = "limit") Integer limit) {
        return service.getPartialDays(from, to, limit);
    }
}
