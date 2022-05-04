package com.shawcxx.modules.sys.form;

import com.shawcxx.common.base.BasePageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author cjl
 * @date 2021/3/11 15:01
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleQueryForm extends BasePageForm {

    private String roleName;

}
