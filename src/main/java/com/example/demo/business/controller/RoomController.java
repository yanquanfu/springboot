package com.example.demo.business.controller;

import com.example.demo.business.entity.Page;
import com.example.demo.business.entity.Room;
import com.example.demo.business.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @RequestMapping(value = "list")
    public ModelAndView list(){
        return new ModelAndView("room/room");
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Object data(@RequestBody Page page){
        Map<String,Object> result = new LinkedHashMap<>();
//        Iterable<Room> all = roomRepository.findAll();
        Pageable pageable =  PageRequest.of(page.getCurrentPage()-1, page.getPageSize(), Sort.by("floorName"));
        Integer integer = roomRepository.countFindCurrentRoom();
        page.setTotal(integer);
        List<Room> currentRoom = roomRepository.findCurrentRoom(pageable);
        result.put("data",currentRoom);
        result.put("page",page);

        return result;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request, @RequestBody Room room){
        room.setIsDel(0);
        if (null == room.getId()){
            room.setCreateDate(new Date());
        }else{
            room.setUpdateDate(new Date());
        }
        Room save = roomRepository.save(room);
        Map<String,Object> result = new LinkedHashMap<>();
        result.put("success", true);
        return result;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(Room room){
        return null;
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Object delete(Integer ids){

        Optional<Room> room = roomRepository.findById(ids);
        room.get().setIsDel(1);
        roomRepository.save(room.get());

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("success", true);
        return result;
    }

    @RequestMapping(value = "findAllRoom")
    public Object findAllRoom(){
        return roomRepository.findAllRoom();
    }

}
