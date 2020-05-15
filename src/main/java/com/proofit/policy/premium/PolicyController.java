package com.proofit.policy.premium;

import com.proofit.policy.premium.dto.Policy;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PolicyController {

    private final PremiumCalculator premiumCalculator;

    @PostMapping("/policy/premium")
    public Policy calculatePolicyPremium(@RequestBody Policy policy) {
        return premiumCalculator.calculate(policy);
    }
}
