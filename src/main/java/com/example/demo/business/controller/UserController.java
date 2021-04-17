package com.example.demo.business.controller;

import com.example.demo.business.entity.Page;
import com.example.demo.business.entity.Room;
import com.example.demo.business.entity.User;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "list")
    public ModelAndView list(){
        return new ModelAndView("user/user");
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Object data(@RequestBody Page page){
        Map<String,Object> result = new LinkedHashMap<>();
        Pageable pageable =  PageRequest.of(page.getCurrentPage()-1, page.getPageSize(), Sort.by("createDate"));
        Integer integer = userRepository.countFindCurrentUser();
        page.setTotal(integer);
        List<User> currentRoom = userRepository.findCurrentUser(pageable);
        result.put("data",currentRoom);
        result.put("page",page);

        return result;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request, @RequestBody User user){
        if (null == user.getId()){
            user.setCreateDate(new Date());
            user.setIsDel(0);
            user.setType(1);
        }else{
            user.setUpdateDate(new Date());
        }
        User save = userRepository.save(user);
        Map<String,Object> result = new LinkedHashMap<>();
        result.put("success", true);
        return result;
    }

    @RequestMapping(value = "findAllUser")
    public Object findAllUser(){
        return userRepository.findAllUser();
    }




}
