package com.ra.presentation;

import com.ra.service.ICustomerService;

public class LoginMenu {

    private final ICustomerService customerService;

    public LoginMenu(ICustomerService customerService) {
        this.customerService = customerService;
    }


}
