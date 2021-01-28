package com.epam.voucherifyintegrator.client;

import com.epam.voucherifyintegrator.service.VoucherifyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyVoucherifyClientTest {
    @Autowired
    private MyVoucherifyClient client;

    @Autowired
    private VoucherifyService voucherifyService;

    @Test
    public void testEndpoint() {
        voucherifyService.getSumOfPoints();
    }
}