package com.example.jeogieottae.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckSystemResponse {

    private final String hostname;
    private final String instanceId;
    private final String servername;

}
