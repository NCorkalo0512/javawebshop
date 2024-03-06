package com.example.hralgebrajavawebshop.controller;

import com.example.hralgebrajavawebshop.models.LoginLog;
import com.example.hralgebrajavawebshop.models.User;
import com.example.hralgebrajavawebshop.repository.LoginLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/api/login-logs")
@AllArgsConstructor
public class LoginLogController {

    private final LoginLogRepository loginLogRepository;

   @GetMapping
    public String listLoginLogs(Model model){
        List<LoginLog>loginLogs= loginLogRepository.findAll();
        model.addAttribute("loginLogs", loginLogs);
        return "listLogLogs";
   }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/by-user")
    public String getLoginLogsByUser(@ModelAttribute("user") User user, Model model) {
        List<LoginLog> loginLogs = loginLogRepository.findByUser(user);
        model.addAttribute("loginLogs", loginLogs);
        return "listLogLogs";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/by-time-range")
    public String getLoginLogsByTimeRange(@RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model
    ) {
        if (start.after(end)) {
            model.addAttribute("error", "Start date must be before the end date.");
            return "listLogLogs";
        }

        List<LoginLog> loginLogs = loginLogRepository.findByLoginTimeBetween(start, end);
        model.addAttribute("loginLogs", loginLogs);
        return "listLogLogs";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/by-ip")
    public String getLoginLogsByIP(@RequestParam("ipAddress") String ipAddress, Model model) {
        List<LoginLog> loginLogs = loginLogRepository.findByIPAddress(ipAddress);
        model.addAttribute("loginLogs", loginLogs);
        return "listLogLogs";
    }


}
