package com.example.jeogieottae.common.controller;

import com.example.jeogieottae.common.dto.CheckSystemResponse;
import com.example.jeogieottae.common.utils.SystemInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SystemController {

    private final SystemInfoProvider systemInfoProvider;

    @GetMapping("/infra")
    public CheckSystemResponse getSystemInfo() {

        return new CheckSystemResponse(
                systemInfoProvider.getHostname(),
                systemInfoProvider.getInstanceId(),
                systemInfoProvider.getServerName()
        );
    }
}