package cn.itcast.service.impl;

import cn.itcast.dao.IUserDao;
import cn.itcast.domain.Role;
import cn.itcast.domain.UserInfo;
import cn.itcast.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class IUserServiceImpl implements IUserService {
    @Autowired
    private IUserDao iUserDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo=null;
        try {
            userInfo = iUserDao.findByUsername(username);
            System.out.println(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("密码:"+userInfo.getPassword());
        //处理自己的用户对象封装成UserDetails
        User user = new User(userInfo.getUsername(),/*"{noop}"+*/userInfo.getPassword(),userInfo.getStatus()==0?false:true,true,true,true, getAuthority(userInfo.getRoles()));
        return user ;
    }
    //作用就是返回一个List集合,集合中装入的是角色描述.
    public List<SimpleGrantedAuthority> getAuthority(List<Role> roles){
        List<SimpleGrantedAuthority> list =new ArrayList<>();
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        }
        return list;
    }
}
