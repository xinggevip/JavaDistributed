package com.fmjava.core.service;

import com.fmjava.core.pojo.seller.Seller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class UserDetailServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /* 先定义一个权限集合 */
        ArrayList<GrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        if (username == null){
            return null;
        }
        // 到数据库中查询用户对象

        Seller seller = sellerService.findOne(username);
        if (seller != null){
            if ("1".equals(seller.getStatus())){
                // 说明用户已激活
                return new User(username,"{noop}" + seller.getPassword(),authList);
            }
        }

        return null;
    }


}
