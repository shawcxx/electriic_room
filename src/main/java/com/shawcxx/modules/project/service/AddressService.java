package com.shawcxx.modules.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.modules.device.service.DeviceService;
import com.shawcxx.modules.project.bo.AddressEnum;
import com.shawcxx.modules.project.dao.AddressDAO;
import com.shawcxx.modules.project.domain.AddressDO;
import com.shawcxx.modules.project.form.AddressForm;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.tomcat.jni.Address;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cjl
 * @date 2022/5/7 10:46
 * @description
 */
@Service
public class AddressService extends ServiceImpl<AddressDAO, AddressDO> {

    @Resource
    private DeviceService deviceService;

    public void add(AddressForm form) {
        String parentAddressId = form.getParentAddressId();
        AddressDO one = this.getOne(new LambdaQueryWrapper<AddressDO>().eq(AddressDO::getParentAddressId, parentAddressId).eq(AddressDO::getAddressName, form.getAddressName()));
        if (one != null) {
            throw new MyException("已存在的名称");
        }
        AddressEnum addressEnum = AddressEnum.getByType(form.getAddressType());
        if (addressEnum == null) {
            throw new MyException("不存在的房间类型");
        }
        AddressDO parentAddress = this.getById(parentAddressId);
        if (parentAddress == null) {
            throw new MyException("父级不存在");
        }
        AddressEnum parentAddressEnum = AddressEnum.getByType(parentAddress.getAddressType());
        this.checkAddressType(addressEnum, parentAddressEnum);
        AddressDO addressDO = new AddressDO();
        addressDO.setAddressType(addressEnum.getAddressType());
        addressDO.setParentAddressId(parentAddressId);
        addressDO.setAddressName(form.getAddressName());
        this.save(addressDO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        AddressDO addressDO = this.getById(id);
        List<AddressDO> childList = this.getByParentId(addressDO.getAddressId());
        this.removeById(addressDO.getAddressId());
        deviceService.removeByAddressId(addressDO.getAddressId());
        for (AddressDO child : childList) {
            this.delete(child.getAddressId());
        }
    }

    private List<AddressDO> getByParentId(String id) {
        LambdaQueryWrapper<AddressDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressDO::getParentAddressId, id);
        return this.list(queryWrapper);
    }

    private void checkAddressType(AddressEnum addressEnum, AddressEnum parentAddressEnum) {
        if (addressEnum.equals(AddressEnum.LINE)) {
            if (!parentAddressEnum.equals(AddressEnum.CABINET)) {
                throw new MyException(addressEnum.name() + "的父级应为" + AddressEnum.CABINET.name());
            }
        }
        if (addressEnum.equals(AddressEnum.CABINET)) {
            if (!parentAddressEnum.equals(AddressEnum.ROOM)) {
                throw new MyException(addressEnum.name() + "的父级应为" + AddressEnum.ROOM.name());
            }
        }
    }


}
