package com.example.jeogieottae.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class SystemInfoProvider {

    @Value("${server-name:unknown}")
    private String serverName;

    public String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }
    public String getServerName() {
        return serverName;
    }
}