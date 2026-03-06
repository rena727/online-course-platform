package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.UserDto;
import BrainRise.BrainRise.models.User;

import java.util.List;

public interface UserService {
    UserDto getByUsername(String username);
    UserDto registerUser(UserDto userDto, String password, String roleName);
    User getByUsernameEntity(String username);
    void update(UserDto dto);
    List<UserDto> getUnverifiedMentors();
    void verifyMentor(Long id);

    List<UserDto> getAllUsers();
    void deleteUser(Long id);
    void makeAdmin(Long id);
    void removeAdmin(Long id);
}
