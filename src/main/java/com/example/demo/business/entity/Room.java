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
public class Room {

    @Id
    @GenericGenerator(name = "generator", strategy ="identity")
    @GeneratedValue(generator = "generator")
    private Integer id;
    // 房屋名称
    @Column(name = "name",length = 20)
    private String name;
    // 楼层名称
    @Column(name = "floor_name",length = 20)
    private String floorName;
    // 删除标志
    @Column(name = "is_del")
    private Integer isDel;
    // 创建时间
    @Column(name = "create_date")
    private Date createDate;
    // 更新时间
    @Column(name = "update_date")
    private Date updateDate;
    // 租凭时间
    @Column(name = "leasing_date")
    private Date leasingDate;
    // 水量
    @Column(name = "water")
    private Double water;
    // 电量
    @Column(name = "electric")
    private Double electric;
    // 物业
    @Column(name = "tenement")
    private Double tenement;
    // 租凭时间
    @Column(name = "expiration_date")
    private Date expirationDate;

}
