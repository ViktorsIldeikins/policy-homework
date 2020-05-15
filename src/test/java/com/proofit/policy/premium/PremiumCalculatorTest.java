package com.proofit.policy.premium;

import com.proofit.policy.premium.dto.Policy;
import com.proofit.policy.premium.dto.PolicyObject;
import com.proofit.policy.premium.dto.PolicySubObject;
import com.proofit.policy.premium.dto.RiskType;
import com.proofit.policy.premium.exception.UnknownRiskTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static com.proofit.policy.premium.dto.RiskType.FIRE;
import static com.proofit.policy.premium.dto.RiskType.WATER;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Application.class})
@TestPropertySource(locations = "classpath:application.properties")
class PremiumCalculatorTest {

    @Autowired
    private PremiumCalculator premiumCalculator;

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

    @Test
    void noObjects_expectPremium0() {
        Policy policy = new Policy();
        premiumCalculator.calculate(policy);
        assertEquals(0, policy.getPremium());
    }

    @Test
    void noSubObjects_expectPremium0() {
        Policy policy = new Policy();
        premiumCalculator.calculate(policy);
        assertEquals(0, policy.getPremium());
    }

    @Test
    void riskTypeNull_exceptionExpected(){
        Policy policy = new Policy();
        PolicyObject policyObject = new PolicyObject();
        policy.setPolicyObjects(Collections.singletonList(policyObject));
        policyObject.setSubObjects(asList(createSubObject(FIRE, 500), createSubObject(null, 100)));
        assertThrows(UnknownRiskTypeException.class, () -> premiumCalculator.calculate(policy));
    }

    ///////////////////////////////////////////////////

    private static PolicySubObject createSubObject(RiskType riskType, double sumInsured) {
        PolicySubObject subObject = new PolicySubObject();
        subObject.setRiskType(riskType);
        subObject.setSumInsured(sumInsured);
        return subObject;
    }

}
