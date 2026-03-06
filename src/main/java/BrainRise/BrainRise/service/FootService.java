package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.FootDto;

public interface FootService {
    FootDto getFoot();
    FootDto updateFood(long id, FootDto footDto);
}
