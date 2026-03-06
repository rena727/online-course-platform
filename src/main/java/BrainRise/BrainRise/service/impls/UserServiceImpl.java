package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.UserDto;
import BrainRise.BrainRise.models.Mentor;
import BrainRise.BrainRise.models.Role;
import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.repository.MentorRepository;
import BrainRise.BrainRise.repository.RoleRepository;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MentorRepository mentorRepository;


    @Override
    @Transactional
    public UserDto registerUser(UserDto dto, String password, String roleName) {
        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Xəta: " + roleName + " bazada tapılmadı!"));

        user.setRoles(Collections.singleton(userRole));
        User savedUser = userRepository.save(user);


        if ("ROLE_MENTOR".equals(roleName)) {
            Mentor mentor = new Mentor();

            mentor.setUser(savedUser);

            mentor.setName(savedUser.getFullName());
            mentor.setEmail(savedUser.getEmail());
            mentor.setSpecialty(dto.getSpecialty());
            mentor.setImgUrl(dto.getImgUrl());

            mentorRepository.save(mentor);
        }


        return modelMapper.map(savedUser, UserDto.class);
    }
    @Override
    public UserDto getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));
        return modelMapper.map(user, UserDto.class);
    }

    public User getByUsernameEntity(String login) {
        return userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));
    }


    @Override
    @Transactional
    public void update(UserDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .or(() -> userRepository.findByUsername(dto.getUsername()))
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        if (dto.getImgUrl() != null && !dto.getImgUrl().trim().isEmpty()) {
            user.setImgUrl(dto.getImgUrl().trim());
        }

        user.setFullName(dto.getFullName());
        user.setBio(dto.getBio());
        user.setSpecialty(dto.getSpecialty());
        user.setLinkedinUrl(dto.getLinkedinUrl());
        user.setGithubUrl(dto.getGithubUrl());

        user.setVerified(true);

        userRepository.save(user);

        mentorRepository.findByUser(user).ifPresent(mentor -> {
            mentor.setName(user.getFullName());
            mentor.setSpecialty(user.getSpecialty());
            if (dto.getImgUrl() != null && !dto.getImgUrl().trim().isEmpty()) {
                mentor.setImgUrl(dto.getImgUrl().trim());
            }
            mentor.setLinkedinUrl(user.getLinkedinUrl());
            mentor.setGitUrl(user.getGithubUrl());
            mentorRepository.save(mentor);
        });
    }

    @Override
    public List<UserDto> getUnverifiedMentors() {
        // Həm rolu MENTOR olan, həm də isVerified = false olanları gətirir
        return userRepository.findUnverifiedUsersByRole("ROLE_MENTOR").stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void verifyMentor(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));
        user.setVerified(true);
        userRepository.save(user);
    }
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDto dto = modelMapper.map(user, UserDto.class);
                    // Rolları String Set-i kimi DTO-ya ötürürük
                    Set<String> roleNames = user.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet());
                    dto.setRoles(roleNames);
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void makeAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Tapılmadı"));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN bazada yoxdur!"));
        user.getRoles().add(adminRole);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Tapılmadı"));
        user.getRoles().removeIf(role -> role.getName().equals("ROLE_ADMIN"));
        userRepository.save(user);
    }
}