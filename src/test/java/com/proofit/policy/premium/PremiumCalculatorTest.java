package com.proofit.policy.premium;

import com.proofit.policy.premium.dto.Policy;
import com.proofit.policy.premium.dto.PolicyObject;
import com.proofit.policy.premium.dto.PolicySubObject;
import com.proofit.policy.premium.dto.RiskType;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.proofit.policy.premium.dto.RiskType.FIRE;
import static com.proofit.policy.premium.dto.RiskType.WATER;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PremiumCalculatorTest {

    private PremiumCalculator premiumCalculator = new PremiumCalculator();

    @Test
    void happyPath_1() {
        Policy policy = new Policy();
        PolicyObject policyObject = new PolicyObject();
        policy.setPolicyObjects(Collections.singletonList(policyObject));
        policyObject.setSubObjects(asList(createSubObject(FIRE, 100), createSubObject(WATER, 8)));
        premiumCalculator.calculate(policy);
        assertEquals(2.1, policy.getPremium());
    }

    @Test
    void happyPath_2() {
        Policy policy = new Policy();
        PolicyObject policyObject = new PolicyObject();
        policy.setPolicyObjects(Collections.singletonList(policyObject));
        policyObject.setSubObjects(asList(createSubObject(FIRE, 500), createSubObject(WATER, 100)));
        premiumCalculator.calculate(policy);
        assertEquals(16.5, policy.getPremium());
    }

    private static PolicySubObject createSubObject(RiskType riskType, double sumInsured) {
        PolicySubObject subObject = new PolicySubObject();
        subObject.setRiskType(riskType);
        subObject.setSumInsured(sumInsured);
        return subObject;
    }

}
