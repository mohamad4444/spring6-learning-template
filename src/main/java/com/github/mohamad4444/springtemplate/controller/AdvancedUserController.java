// package com.github.mohamad4444.springtemplate.controller;

// import com.github.mohamad4444.springtemplate.model.User;
// import com.github.mohamad4444.springtemplate.security.JwtUtil;
// import com.github.mohamad4444.springtemplate.service.UserService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import java.util.Map;
// import java.util.Optional;

// @RestController
// @RequestMapping("/users")
// public class AdvancedUserController {
//     @Autowired
//     private UserService userService;
//     @Autowired
//     private JwtUtil jwtUtil;

//     @PostMapping("/register")
//     public ResponseEntity<User> register(@RequestBody User user) {
//         return ResponseEntity.ok(userService.registerUser(user));
//     }

//     @PostMapping("/login")
//     public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
//         Optional<User> dbUser = userService.findByUsername(user.getUsername());
//         if (dbUser.isPresent() && new BCryptPasswordEncoder().matches(user.getPassword(), dbUser.get().getPassword())) {
//             String token = jwtUtil.generateToken(user.getUsername());
//             return ResponseEntity.ok(Map.of("token", token));
//         }
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//     }
// }
