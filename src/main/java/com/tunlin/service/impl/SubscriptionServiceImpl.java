package com.tunlin.service.impl;

import com.tunlin.modal.PlanType;
import com.tunlin.modal.Subscription;
import com.tunlin.modal.User;
import com.tunlin.repository.SubscriptionRepository;
import com.tunlin.service.SubscriptionService;
import com.tunlin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription createSubscription(User user) {

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlanTye(PlanType.FREE);
        return subscriptionRepository.save(subscription);

    }

    @Override
    public Subscription getUserSubscription(Long userId) throws Exception {

        Subscription subscription = subscriptionRepository.findByUserId(userId);
        if (!isValid(subscription)){
            subscription.setPlanTye(PlanType.FREE);
            subscription.setSubscriptionStartDate(LocalDate.now());
            subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(12));
        }
        return subscriptionRepository.save(subscription);

    }

    @Override
    public Subscription upgradeSubscription(Long userId, PlanType planType) {

        Subscription subscription = subscriptionRepository.findByUserId(userId);
        subscription.setPlanTye(planType);
        subscription.setSubscriptionStartDate(LocalDate.now());
        if (planType.equals(PlanType.ANNUALLY)){
            subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(12));
        }else {
            subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(1));
        }
        return subscriptionRepository.save(subscription);

    }

    @Override
    public boolean isValid(Subscription subscription) {

        if (subscription.getPlanTye().equals(PlanType.FREE)){
            return true;
        }
        LocalDate endDate = subscription.getGetSubscriptionEndDate();
        LocalDate currentDate = LocalDate.now();

        return endDate.isAfter(currentDate) || endDate.isEqual(currentDate);

    }
}
