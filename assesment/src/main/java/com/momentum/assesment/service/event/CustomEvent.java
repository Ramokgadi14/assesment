package com.momentum.assesment.service.event;

import com.momentum.assesment.entities.Withdrawal;
import org.springframework.context.ApplicationEvent;

public class CustomEvent extends ApplicationEvent {

    private final Withdrawal withdrawal;

    public CustomEvent(Object source, Withdrawal withdrawal) {
        super(source);
        this.withdrawal = withdrawal;
    }

    public Withdrawal getWithdrawal() {
        return withdrawal;
    }


}
