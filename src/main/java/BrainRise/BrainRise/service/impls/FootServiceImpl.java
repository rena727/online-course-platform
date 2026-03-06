package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.FootDto;
import BrainRise.BrainRise.models.Foot;
import BrainRise.BrainRise.repository.FootRepository;
import BrainRise.BrainRise.service.FootService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FootServiceImpl implements FootService {
     private final FootRepository footRepository;
    private  final ModelMapper modelMapper;

    @Override
    public FootDto getFoot() {
        Foot foot = footRepository.findAll().stream()
                .findFirst()
                .orElse(new Foot(1L, "Default Footer", "", "", ""));
        return modelMapper.map(foot, FootDto.class);
    }

    @Override
    public FootDto updateFood(long id, FootDto footDto) {
        Foot foot = footRepository.findAll().stream()
                .findFirst()
                .orElse(new Foot());

        foot.setTitle(footDto.getTitle());
        foot.setInstagramUrl(footDto.getInstagramUrl());
        foot.setLinkedinUrl(footDto.getLinkedinUrl());
        foot.setFacebookUrl(footDto.getFacebookUrl());


        Foot updatedFoot = footRepository.save(foot);

        return modelMapper.map(updatedFoot, FootDto.class);
    }


}
