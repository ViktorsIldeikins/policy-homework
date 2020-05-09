package com.proofit.policy.premium.dto;

import lombok.Data;

import java.util.List;

@Data
public class Policy {

    private String policyNumber;
    private List<PolicyObject> policyObjects;
    private double premium;

}
