package com.finance.PersonalFinanceManager.controller;

import com.finance.PersonalFinanceManager.model.Manager;
import com.finance.PersonalFinanceManager.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping("/all")
    public List<Manager> getAllManagers() {
        return managerService.getAllManagers();
    }

    @PostMapping("/add")
    public Manager addManager(@RequestBody Manager manager) {
        return managerService.saveManager(manager);
    }
}
