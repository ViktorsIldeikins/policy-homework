package com.proofit.policy.premium;

import com.proofit.policy.premium.dto.Policy;
import org.springframework.stereotype.Component;

@Component
public class PremiumCalculator {

    public Policy calculate(Policy policy){
        return policy;
    }

}
