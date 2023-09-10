package com.momentum.assesment.service.event;

import com.momentum.assesment.entities.Withdrawal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherService {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void publishEvent(Withdrawal withdrawal) {
        CustomEvent customEvent = new CustomEvent(this, withdrawal);
        eventPublisher.publishEvent(customEvent);
    }
}
