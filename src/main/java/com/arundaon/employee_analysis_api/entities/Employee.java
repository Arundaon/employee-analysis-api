package com.arundaon.employee_analysis_api.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Employee {

    private String firstName;
    private String lastName;
    private String designation;
    private int salary;
    private String dateOfJoining;
    private String address;
    private String gender;
    private int age;
    private String maritalStatus;
    private String[] interests;

}