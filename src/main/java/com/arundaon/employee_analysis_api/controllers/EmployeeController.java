package com.arundaon.employee_analysis_api.controllers;

import com.arundaon.employee_analysis_api.models.*;
import com.arundaon.employee_analysis_api.services.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/count")
    public CountResponse getCount() throws IOException {
        return new CountResponse(employeeService.getCount());
    }

    @GetMapping("/avg-salary")
    public AverageSalaryResponse getAvgSalary() throws IOException{
        return new AverageSalaryResponse(employeeService.getAverageSalary());
    }

    @GetMapping("/minmax-salary")
    public MinMaxResponse getMinMaxSalary() throws IOException{
        return employeeService.getMinMaxSalary();
    }

    @GetMapping("/age-distribution")
    public List<AgeBucket> getAgeDistribution() throws IOException{
        return employeeService.getAgeDistributionHistogram();
    }

    @GetMapping("/marital-status-distribution")
    public List<MaritalBucket> getMaritalDistribution() throws IOException{
        return employeeService.getMaritalStatusDistribution();
    }

    @GetMapping("/date-of-joining-distribution")
    public List<DateBucket> getDateOfJoiningDistribution() throws IOException{
        return employeeService.getDateOfJoiningHistogram();
    }

    @GetMapping("/top-interests-distribution")
    public List<InterestBucket> getTopInterestsDistribution() throws IOException{
        return employeeService.getTopInterestsDistribution();
    }

    @GetMapping("/designation-distribution")
    public List<DesignationBucket> getDesignationDistribution() throws IOException{
        return employeeService.getDesignationDistribution();
    }
}
