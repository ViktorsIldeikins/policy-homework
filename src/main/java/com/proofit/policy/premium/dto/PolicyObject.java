package com.proofit.policy.premium.dto;

import lombok.Data;

import java.util.List;

@Data
public class PolicyObject {

    private String name;
    private List<PolicySubObject> subObjects;

}
