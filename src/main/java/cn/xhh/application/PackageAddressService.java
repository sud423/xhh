package cn.xhh.application;

import cn.xhh.domain.business.PackageAddress;
import cn.xhh.infrastructure.OptResult;

public interface PackageAddressService {

    /**
     * 保存揽件地址
     * @return
     */
    OptResult save(PackageAddress packageAddress);
}
