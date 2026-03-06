package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.ExamsDto;

import java.util.List;

public interface ExamsService {
    ExamsDto getById(Long id);
    List<ExamsDto> getAllExam();
    ExamsDto updateExam(Long id, ExamsDto imtahanDto);
    ExamsDto createExam(ExamsDto imtahanDto);
    void deleteExam(long id);
}
