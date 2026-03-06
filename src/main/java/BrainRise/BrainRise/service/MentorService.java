package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.MentorDto;

import java.util.List;

public interface MentorService {
    MentorDto getById(Long id);
    List <MentorDto> getAllMentor();
    MentorDto cerateMentor(MentorDto mentorDto);
    MentorDto updateMentor(Long id, MentorDto mentorDto);
    void  mentorDelete(long id);
}
