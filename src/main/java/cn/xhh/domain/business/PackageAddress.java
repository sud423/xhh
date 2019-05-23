package cn.xhh.domain.business;

import cn.xhh.domain.Entity;

public class PackageAddress  implements Entity<PackageAddress> {

    private int id;

    private String province;

    private String city;

    private String area;

    private String detail;

    private boolean isDefault;

    private String contact;

    private String sex;

    private String cell;

    private int userId;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    @Override
    public int hashCode() {
        return Integer.toString(id).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        final PackageAddress other = (PackageAddress) obj;
        return sameIdentityAs(other);
    }

    @Override
    public boolean sameIdentityAs(PackageAddress other) {
        return other != null && id == other.getId();
    }
}
