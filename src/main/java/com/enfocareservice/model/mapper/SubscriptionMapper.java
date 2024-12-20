package com.enfocareservice.model.mapper;

import org.springframework.stereotype.Component;

import com.enfocareservice.entity.SubscriptionEntity;
import com.enfocareservice.model.Subscription;

@Component
public class SubscriptionMapper {

	public Subscription map(SubscriptionEntity subscriptionEntity) {
		Subscription subscription = new Subscription();
		subscription.setId(subscriptionEntity.getId());
		subscription.setEmail(subscriptionEntity.getEmail());
		subscription.setExpiry(subscriptionEntity.getExpiry());
		return subscription;
	}

	public SubscriptionEntity mapToEntity(Subscription subscription) {

		SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
		subscriptionEntity.setId(subscription.getId());
		subscriptionEntity.setEmail(subscription.getEmail());
		subscriptionEntity.setExpiry(subscription.getExpiry());

		return subscriptionEntity;
	}

}
