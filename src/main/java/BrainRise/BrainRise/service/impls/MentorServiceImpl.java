package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.CourseDto;
import BrainRise.BrainRise.dto.MentorDto;
import BrainRise.BrainRise.dto.ReviewDto;
import BrainRise.BrainRise.models.Mentor;
import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.repository.CourseRepository;
import BrainRise.BrainRise.repository.MentorRepository;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {
    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;



    @Override
    @Transactional(readOnly = true)
    public MentorDto getById(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor Tapılmadı"));

        MentorDto dto = new MentorDto();
        dto.setId(mentor.getId());
        dto.setName(mentor.getName());
        dto.setImgUrl(mentor.getImgUrl());
        dto.setSpecialty(mentor.getSpecialty());

        if (mentor.getUser() != null) {
            Long userId = mentor.getUser().getId();

            List<CourseDto> mentorCourses = courseRepository.findAll().stream()
                    .filter(c -> c.isApproved())
                    .filter(c -> c.getMentors() != null &&
                            c.getMentors().stream().anyMatch(m -> m.getId().equals(userId)))
                    .map(course -> {
                        CourseDto cDto = new CourseDto();
                        cDto.setId(course.getId());
                        cDto.setName(course.getName());
                        cDto.setPrice(course.getPrice());
                        cDto.setIconClass(course.getIconClass());
                        cDto.setApproved(course.isApproved());
                        return cDto;
                    })
                    .collect(Collectors.toList());

            dto.setCourses(mentorCourses);
        } else {
            dto.setCourses(new ArrayList<>());
        }

        return dto;
    }
    @Override
    public List<MentorDto> getAllMentor() {
        return mentorRepository.findAll().stream()
                .map(m -> {
                    MentorDto dto = modelMapper.map(m, MentorDto.class);
                    if (m.getUser() != null && m.getUser().getMentoredCourses() != null) {
                        dto.setCourseCount(m.getUser().getMentoredCourses().size());
                    }
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public MentorDto cerateMentor(MentorDto mentorDto) {
        Mentor mentor = modelMapper.map(mentorDto, Mentor.class);
        Mentor savedMentor = mentorRepository.save(mentor);
        return modelMapper.map(savedMentor, MentorDto.class);
    }

    @Override
    public MentorDto updateMentor(Long id, MentorDto mentorDto) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor tapılmadı"));
        modelMapper.map(mentorDto, mentor);
        mentor.setId(id);
        Mentor update = mentorRepository.save(mentor);
        return modelMapper.map(update, MentorDto.class);
    }

    @Override
    @Transactional
    public void mentorDelete(long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor tapılmadı"));

        if (mentor.getCourses() != null) {
            mentor.getCourses().clear();
        }

        User user = mentor.getUser();

        mentorRepository.delete(mentor);

        if (user != null) {
            userRepository.delete(user);
        }
    }
}