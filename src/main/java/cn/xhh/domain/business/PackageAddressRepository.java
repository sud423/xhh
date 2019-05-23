package cn.xhh.domain.business;


import java.util.List;

public interface PackageAddressRepository {
    /**
     * 新增揽件地址
     * @param packageAddress 揽件地址信息
     * @return
     */
    int add(PackageAddress packageAddress);

    /**
     * 修改揽件地址
     * @param packageAddress 揽件地址信息
     * @return
     */
    int update(PackageAddress packageAddress);

    /**
     * 根据主键编号获取揽件地址信息
     * @param id 编号
     * @return
     */
    PackageAddress get(int id);

    /**
     * 根据用户编号获取揽件地址列表
     * @param userId 用户编号
     * @return
     */
    List<PackageAddress> findByUserId(int userId);

    /**
     * 根据用户编号获取默认揽件地址
     * @param userId 用户编号
     * @return
     */
    PackageAddress find(int userId);

    /**
     * 根据编号删除揽件地址
     * @param id 编号
     * @return
     */
    int delete(int id);

    /**
     * 将已有的默认地址更改为false
     * @param userId 用户编号
     * @return
     */
    int updateDefault(int userId);
}
