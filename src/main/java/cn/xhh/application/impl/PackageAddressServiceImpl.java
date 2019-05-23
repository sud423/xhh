package cn.xhh.application.impl;

import cn.xhh.application.PackageAddressService;
import cn.xhh.domain.business.PackageAddress;
import cn.xhh.domain.business.PackageAddressRepository;
import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.infrastructure.OptResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PackageAddressServiceImpl implements PackageAddressService {

    @Autowired
    private PackageAddressRepository packageAddressRepository;

    @Override
    public OptResult save(PackageAddress packageAddress) {

        if(packageAddress.getId()==0) {
            //判断新增是否为默认地址，如果是将其它改为false
            if(packageAddress.isDefault())
                packageAddressRepository.updateDefault(SessionManager.getUserId());

            //设置当前用户编号
            packageAddress.setUserId(SessionManager.getUserId());
            if(packageAddressRepository.add(packageAddress)>0)
                return  OptResult.Successed();
            else
                return OptResult.Failed("地址新增失败");
        }
        else{
            if(packageAddressRepository.update(packageAddress)>0)
                return  OptResult.Successed();
            else
                return OptResult.Failed("地址新增失败");
        }

    }
}
