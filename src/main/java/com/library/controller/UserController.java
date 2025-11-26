package com.library.controller;

import com.library.common.Result;
import com.library.dto.LoginDTO;
import com.library.dto.LoginResponseDTO;
import com.library.dto.UserDTO;
import com.library.entity.User;
import com.library.service.UserService;
import com.library.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public Result<UserDTO> register(@Valid @RequestBody UserDTO userDTO) {
        User user = new User();
        // 仅复制允许的字段，避免用户在注册时指定角色
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        User savedUser = userService.register(user);
        UserDTO result = convertToDTO(savedUser);
        // 不返回密码
        result.setPassword(null);
        return Result.success(result);
    }

    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        logger.debug("Login attempt for username='{}'", loginDTO.getUsername());
        User user = userService.findByUsername(loginDTO.getUsername());
        if (user != null) {
            String stored = user.getPassword();
            int storedLen = stored != null ? stored.length() : 0;
            logger.debug("Found user='{}', storedPasswordLength={}, passwordEncoderPresent={}", loginDTO.getUsername(), storedLen, (passwordEncoder != null));
        } else {
            logger.debug("User '{}' not found", loginDTO.getUsername());
        }
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        // 密码验证
        boolean passwordMatches = false;
        if (passwordEncoder != null) {
            // 使用加密验证
            passwordMatches = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
            logger.debug("Password matches (using encoder): {}", passwordMatches);
        } else {
            // 简单明文验证（仅用于开发测试）
            passwordMatches = user.getPassword().equals(loginDTO.getPassword());
            logger.debug("Password matches (plain compare): {}", passwordMatches);
        }
        
        if (!passwordMatches) {
            return Result.error("用户名或密码错误");
        }
        
        // 生成JWT Token（捕获因 secret 未配置或过短导致的异常，返回可操作的错误提示）
        String token;
        try {
            token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        } catch (IllegalStateException ise) {
            // 明确告知是 JWT secret 配置问题，而不是用户名/密码错误
            return Result.error(500, "JWT 配置错误: " + ise.getMessage());
        } catch (Exception e) {
            // 其他异常交由全局处理（仍记录友好错误）
            return Result.error(500, "生成 Token 失败，请检查服务日志");
        }
        
        UserDTO userDTO = convertToDTO(user);
        LoginResponseDTO response = new LoginResponseDTO(token, userDTO);
        return Result.success(response);
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        // JWT是无状态的，客户端删除token即可
        // 这里可以添加token黑名单机制（可选）
        return Result.success("退出成功");
    }

    @GetMapping("/{id}")
    public Result<UserDTO> getUserById(@PathVariable("id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth != null ? auth.getName() : null;
        User currentUser = currentUsername != null ? userService.findByUsername(currentUsername) : null;

        // 仅管理员或自身可以查看用户信息
        if (currentUser != null && ("admin".equals(currentUser.getRole()) || currentUser.getId().equals(id))) {
            User user = userService.findById(id);
            UserDTO result = convertToDTO(user);
            return Result.success(result);
        }
        return Result.error(403, "无权限查看该用户信息");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> result = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(result);
    }

    @PutMapping("/{id}")
    public Result<UserDTO> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth != null ? auth.getName() : null;
        User currentUser = currentUsername != null ? userService.findByUsername(currentUsername) : null;

        // 只有管理员或用户本人可以修改
        if (currentUser == null) {
            return Result.error(403, "无权限");
        }
        if (!"admin".equals(currentUser.getRole()) && !currentUser.getId().equals(id)) {
            return Result.error(403, "无权限修改该用户");
        }

        User user = new User();
        // 管理员可以修改角色，否则忽略角色字段
        user.setId(id);
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        if ("admin".equals(currentUser.getRole()) && userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }

        User updatedUser = userService.updateUser(user);
        UserDTO result = convertToDTO(updatedUser);
        // 不返回密码
        result.setPassword(null);
        return Result.success(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        // 不返回密码
        dto.setPassword(null);
        return dto;
    }
}

