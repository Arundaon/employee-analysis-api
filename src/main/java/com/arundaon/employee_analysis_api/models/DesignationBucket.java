package com.arundaon.employee_analysis_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DesignationBucket {
    private String designation;
    private Long count;
}
