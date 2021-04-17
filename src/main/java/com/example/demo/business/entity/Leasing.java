package com.example.demo.business.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 租凭记录
 */
@Data
@Entity
public class Leasing {
    @Id
    @GenericGenerator(name = "generator", strategy ="identity")
    @GeneratedValue(generator = "generator")
    private Integer id;
    // 房间id
    private Integer roomId;
    // 用户id
    private Integer userId;
    // 房间名称
    @Column(name = "room_name",length = 20)
    private String roomName;
    // 楼层名称
    @Column(name = "floor_name",length = 20)
    private String floorName;
    // 用户姓名
    @Column(name = "user_name",length = 10)
    private String userName;
    // 创建时间
    private Date createDate;
    // 水量
    private Double water;
    // 电量
    private Double electric;
    // 物业费
    private Double tenement;
    // 合同订立时间
    private Date contractDate;
    // 预计到期时间
    private Date scheduledDate;
    // 规则(1按月份2按季度3按年)
    private Integer regular;
    // 删除标志
    private Integer isDel;
    @Transient
    private Double payment;


}
