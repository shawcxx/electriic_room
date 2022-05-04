package com.shawcxx.modules.sys.form;

import com.shawcxx.common.base.BasePageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: dj
 * @create: 2021-01-09 15:41
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserQueryForm extends BasePageForm {

    private String name;

    private String username;

}
