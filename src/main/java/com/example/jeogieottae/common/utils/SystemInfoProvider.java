package com.example.jeogieottae.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

    public String getInstanceId() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://169.254.169.254/latest/meta-data/instance-id"))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            return "local";
        }
    }

    public String getServerName() {
        return serverName;
    }
}