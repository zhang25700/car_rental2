package com.example.carrental.config;

import com.example.carrental.mapper.SysUserMapper;
import com.example.carrental.model.entity.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final SysUserMapper sysUserMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        SysUser admin = sysUserMapper.selectByUsername("admin");
        String encodedPassword = passwordEncoder.encode("password");
        if (admin == null) {
            SysUser user = new SysUser();
            user.setUsername("admin");
            user.setPassword(encodedPassword);
            user.setRealName("系统管理员");
            user.setRole("ADMIN");
            user.setStatus(1);
            sysUserMapper.insert(user);
            log.info("initialized default admin user");
            return;
        }

        admin.setPassword(encodedPassword);
        admin.setRealName("系统管理员");
        admin.setRole("ADMIN");
        admin.setStatus(1);
        sysUserMapper.update(admin);
        log.info("reset default admin user password");
    }
}
