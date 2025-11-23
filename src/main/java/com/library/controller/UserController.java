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

    @PostMapping("/register")
    public Result<UserDTO> register(@Valid @RequestBody UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        User savedUser = userService.register(user);
        UserDTO result = convertToDTO(savedUser);
        // 不返回密码
        result.setPassword(null);
        return Result.success(result);
    }

    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        User user = userService.findByUsername(loginDTO.getUsername());
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        // 密码验证
        boolean passwordMatches = false;
        if (passwordEncoder != null) {
            // 使用加密验证
            passwordMatches = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
        } else {
            // 简单明文验证（仅用于开发测试）
            passwordMatches = user.getPassword().equals(loginDTO.getPassword());
        }
        
        if (!passwordMatches) {
            return Result.error("用户名或密码错误");
        }
        
        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
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
    public Result<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserDTO result = convertToDTO(user);
        return Result.success(result);
    }

    @GetMapping
    public Result<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> result = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(result);
    }

    @PutMapping("/{id}")
    public Result<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        UserDTO result = convertToDTO(updatedUser);
        // 不返回密码
        result.setPassword(null);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@PathVariable Long id) {
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

