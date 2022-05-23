package com.shawcxx.modules.project.dto;

import lombok.Data;

/**
 * @author Chen jl
 * @date 2022/5/21 16:36
 * @description
 **/
@Data
public class IndexDTO {
    private Long deviceNum;
    private Long onlineNum;
    private Long offlineNum;
    private Long alarmNum;
    private Long unDisposeNum;
}
