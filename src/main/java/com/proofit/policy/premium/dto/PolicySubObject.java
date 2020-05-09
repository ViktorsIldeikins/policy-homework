package com.proofit.policy.premium.dto;

import lombok.Data;

@Data
public class PolicySubObject {

    private String name;
    private double sumInsured;
    private RiskType riskType;

}
