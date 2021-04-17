package com.example.demo.business.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class User {
    @Id
    @GenericGenerator(name = "generator", strategy ="identity")
    @GeneratedValue(generator = "generator")
    private Integer id;
    // 用户名
    @Column(name = "name",length = 10)
    private String name;
    // 电话
    @Column(name = "phone",length = 20)
    private String phone;
    // 年龄
    @Column(name = "age")
    private Integer age;
    // 性别（0 男 1 女）
    @Column(name = "sex")
    private Integer sex;
    // 类型（0 正常用户 1 租户）
    @Column(name = "type")
    private Integer type;
    // 删除标志（0 正常 1删除）
    @Column(name = "is_del")
    private Integer isDel;
    // 创建时间
    @Column(name = "create_date")
    private Date createDate;
    // 更新时间
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "memo", length = 500)
    private String memo;


}
