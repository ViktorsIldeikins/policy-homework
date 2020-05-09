package com.proofit.policy.premium;

import com.proofit.policy.premium.dto.Policy;
import com.proofit.policy.premium.dto.PolicyObject;
import com.proofit.policy.premium.dto.PolicySubObject;
import org.springframework.stereotype.Component;

@Component
public class PremiumCalculator {

    //TODO retrieve coefficients from configuration file
    private static final double FIRE_DEFAULT_COEF = 0.013;
    private static final double FIRE_EXCEEDED_COEF = 0.023;
    private static final double FIRE_SUM_INSURED_THRESHOLD = 100;

    private static final double WATER_DEFAULT_COEF = 0.1;
    private static final double WATER_EXCEEDED_COEF = 0.05;
    private static final double WATER_SUM_INSURED_THRESHOLD = 10;

    public Policy calculate(Policy policy){
        double premium = policy.getPolicyObjects().stream()
                .map(PremiumCalculator::calculateObjectPremium)
                .reduce((double) 0, Double::sum);
        policy.setPremium(premium);
        return policy;
    }

    private static double calculateObjectPremium(PolicyObject policyObject) {
        return policyObject.getSubObjects().stream()
                .map(PremiumCalculator::calculateSubObjectPremium)
                .reduce((double) 0, Double::sum);

    }

    private static double calculateSubObjectPremium(PolicySubObject subObject) {
        double sumInsured = subObject.getSumInsured();
        switch (subObject.getRiskType()) {
            case FIRE: return sumInsured * (sumInsured > FIRE_SUM_INSURED_THRESHOLD ?
                    FIRE_EXCEEDED_COEF : FIRE_DEFAULT_COEF);
            case WATER: return sumInsured * (sumInsured >= WATER_SUM_INSURED_THRESHOLD ?
                    WATER_EXCEEDED_COEF : WATER_DEFAULT_COEF);
            default: return 0;
            //TODO add logger for default case ("Unrecognized risk type")
        }
    }

}
