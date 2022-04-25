package uz.pdp.jwtemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import uz.pdp.jwtemail.entity.Role;
import uz.pdp.jwtemail.entity.User;
import uz.pdp.jwtemail.entity.enam.RoleName;
import uz.pdp.jwtemail.payload.ApiResponse;
import uz.pdp.jwtemail.payload.LoginDto;
import uz.pdp.jwtemail.payload.RegisterDto;
import uz.pdp.jwtemail.repository.RoleRepository;
import uz.pdp.jwtemail.repository.UserRepository;
import uz.pdp.jwtemail.security.JwtProvider;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;

    public ApiResponse registerStaff(RegisterDto dto) {
        boolean existsByEmail = userRepository.existsByEmail(dto.getEmail());

        if (existsByEmail) {
            return new ApiResponse("This email is already exist !", false);
        }

        User user = new User();
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(roleRepository.findAllByRoleName(RoleName.STAFF.name()));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        //EMAILGA HABAR YUBORISH
        Boolean sendEmail = sendEmail(user.getEmail(), user.getEmailCode());

        return new ApiResponse("Muvaffaqiyatli ro'yxatdan o'tdingiz, Akkountizni aktivlashtirish uchun emailngizni tasdiqlang !", true);
    }

    public ApiResponse registerHrManager(RegisterDto dto) {
        boolean existsByEmail = userRepository.existsByEmail(dto.getEmail());

        if (existsByEmail) {
            return new ApiResponse("This email is already exist !", false);
        }

        User user = new User();
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(roleRepository.findAllByRoleName(RoleName.HR_MANAGER.name()));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        //EMAILGA HABAR YUBORISH
        Boolean sendEmail = sendEmail(user.getEmail(), user.getEmailCode());

        return new ApiResponse("Muvaffaqiyatli ro'yxatdan o'tdingiz, Akkountizni aktivlashtirish uchun emailngizni tasdiqlang !", true);
    }

    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Karimberdi");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Akkuntni tasdiqlash");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Tasdiqlsh</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Akkaunt Tasdiqlandi", true);
        }
        return new ApiResponse("Akkaunt allqachon tasdiqlangan", false);
    }

    public ApiResponse login(LoginDto dto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    dto.getUsername(),
                    dto.getPassword()));
            User user = (User) authenticate.getPrincipal();

            String token = jwtProvider.generateToken(dto.getUsername(), (Set<Role>) user.getRole());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("Parol yoki login xato", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException(username + " topilmadi !");
    }
}
