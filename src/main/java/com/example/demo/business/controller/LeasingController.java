package com.example.demo.business.controller;

import com.example.demo.business.entity.Leasing;
import com.example.demo.business.entity.Page;
import com.example.demo.business.entity.Room;
import com.example.demo.business.entity.User;
import com.example.demo.business.repository.LeasingRepository;
import com.example.demo.business.repository.RoomRepository;
import com.example.demo.business.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.sqlite.date.DateFormatUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/leasing")
public class LeasingController {

    @Autowired
    private LeasingRepository leasingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "list")
    public ModelAndView list(){
        return new ModelAndView("");
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Object data(@RequestBody Page page){
        Map<String,Object> result = new LinkedHashMap<>();
        Pageable pageable =  PageRequest.of(page.getCurrentPage()-1, page.getPageSize(),
                Sort.by(Sort.Direction.DESC,"createDate"));
        List<Leasing> leasings = null;
        Integer total = 0;
        if (null != page.getRoomId()){
            leasings = leasingRepository.findCurrentLeasingByRoomId(page.getRoomId(), pageable);
            total = leasingRepository.countFindCurrentLeasingByRoomId(page.getRoomId());
        }
        if (null != page.getUserId()){
            leasings = leasingRepository.findCurrentLeasingByUserId(page.getUserId(), pageable);
            total = leasingRepository.countFindCurrentLeasingByUserId(page.getUserId());
        }
        page.setTotal(total);
        result.put("data",leasings);
        result.put("page",page);
        return result;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request, @RequestBody Leasing leasing){
        Map<String,Object> result = new LinkedHashMap<>();
        leasing.setIsDel(0);
        leasing.setCreateDate(new Date());
        // 查询用户及房间名称
        if (Objects.isNull(leasing.getRoomId())){
            result.put("msg","保存出错,未找到房间信息");
            result.put("success", false);
            return result;
        }
        if (Objects.isNull(leasing.getUserId())){
            result.put("msg","保存出错,未找到用户信息");
            result.put("success", false);
            return result;
        }
        Optional<Room> room = roomRepository.findById(leasing.getRoomId());
        Optional<User> user = userRepository.findById(leasing.getUserId());
        if (null != room && room.get() != null){
            leasing.setFloorName(room.get().getFloorName());
            leasing.setRoomName(room.get().getName());
        }
        if (null != user && null != user.get()){
            leasing.setUserName(user.get().getName());
        }
        leasingRepository.save(leasing);
        result.put("success", true);
        return result;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(Leasing leasing){
        return null;
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Object delete(Integer ids){

//        Optional<Room> room = roomRepository.findById(ids);
//        room.get().setIsDel(1);
//        roomRepository.save(room.get());

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("success", true);
        return result;
    }

    @RequestMapping(value = "backLog")
    public ModelAndView backLog(){
        return new ModelAndView("leasing/backLog");
    }

    @RequestMapping(value = "backLog", method = RequestMethod.POST)
    public Object backLogData(){
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,-1); //把日期往后增加一天,整数  往后推,负数往前移动
        date=calendar.getTime();
        String beginDate = getStartOfDay(date);
        calendar.setTime(date);
        calendar.add(calendar.DATE,2); //把日期往后增加一天,整数  往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果

        String endDate = getEndOfDay(date);
        List<Leasing> leasings = leasingRepository.queryBackLog(beginDate, endDate);
        return leasings;
    }

    public static String getStartOfDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }
    // 返回时间格式如：2020-02-19 23:59:59
    public static String getEndOfDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

}
