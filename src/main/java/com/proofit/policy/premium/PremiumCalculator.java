package com.proofit.policy.premium;

import com.proofit.policy.premium.dto.Policy;
import com.proofit.policy.premium.dto.PolicyObject;
import com.proofit.policy.premium.dto.PolicySubObject;
import com.proofit.policy.premium.dto.RiskType;
import com.proofit.policy.premium.exception.UnknownRiskTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@Slf4j
public class PremiumCalculator {

    @Value("${premium.fire.default.coefficient}")
    private double fireDefaultCoef;
    @Value("${premium.fire.exceeded.coefficient}")
    private double fireExceededCoef;
    @Value("${premium.fire.insured.sum.limit}")
    private double fireInsuredSumLimit;
    @Value("${premium.water.default.coefficient}")
    private double waterDefaultCoef;
    @Value("${premium.water.exceeded.coefficient}")
    private double waterExceededCoef;
    @Value("${premium.water.insured.sum.limit}")
    private double waterInsuredSumThreshold;

    Policy calculate(Policy policy){
        double premium = Optional.ofNullable(policy.getPolicyObjects())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::calculateObjectPremium)
                .reduce((double) 0, Double::sum);
        policy.setPremium(premium);
        return policy;
    }

    private double calculateObjectPremium(PolicyObject policyObject) {
        return Optional.ofNullable(policyObject.getSubObjects())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::calculateSubObjectPremium)
                .reduce((double) 0, Double::sum);
    }

    private double calculateSubObjectPremium(PolicySubObject subObject) {
        double sumInsured = subObject.getSumInsured();
        RiskType riskType = subObject.getRiskType();
        if (riskType == null) {
            log.error("Risk type cannot be null");
            throw new UnknownRiskTypeException("Could not resolve risk type: null");
        }
        switch (riskType) {
            case FIRE: return sumInsured * (sumInsured > fireInsuredSumLimit ?
                    fireExceededCoef : fireDefaultCoef);
            case WATER: return sumInsured * (sumInsured >= waterInsuredSumThreshold ?
                    waterExceededCoef : waterDefaultCoef);
            default: {
                log.error("Could not calculate premium for unknown risk type: {}; Returning 0.", riskType);
                throw new UnknownRiskTypeException("Received unknown risktype: " + riskType);
            }

        }
    }

}
