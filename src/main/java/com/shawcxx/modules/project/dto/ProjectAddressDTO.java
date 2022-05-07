package com.shawcxx.modules.project.dto;

import lombok.Data;

import java.util.List;

/**
 * @author cjl
 * @date 2022/5/7 11:13
 * @description
 */
@Data
public class ProjectAddressDTO {
    private String id;
    private Integer type;
    private String name;
    private List<ProjectAddressDTO> child;
}
