package com.proofit.policy.premium;

import com.proofit.policy.premium.dto.Policy;
import com.proofit.policy.premium.dto.PolicySubObject;
import com.proofit.policy.premium.dto.RiskType;
import com.proofit.policy.premium.exception.UnknownRiskTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.emptyList;

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
        double totalPremium = 0;

        for (RiskType riskType : RiskType.values()) {
            double sumInsuredForRiskType = emptyIfNull(policy.getPolicyObjects())
                    .stream()
                    .flatMap(policyObject -> emptyIfNull(policyObject.getSubObjects()).stream())
                    .filter(subObject -> riskType.equals(subObject.getRiskType()))
                    .mapToDouble(PolicySubObject::getSumInsured)
                    .sum();
            totalPremium += calculatePremiumForRiskType(riskType, sumInsuredForRiskType);
        }

        log.info("Calculated premium for policy. Policy number: {}, premium: {}",
                policy.getPolicyNumber(), totalPremium);
        policy.setPremium(totalPremium);
        return policy;
    }

    private double calculatePremiumForRiskType(RiskType riskType, double sumInsured) {
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

    private <T> List<T> emptyIfNull(List<T> list) {
        return list == null ? emptyList() : list;
    }

}
