package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.BannerDto;
import BrainRise.BrainRise.models.Banner;
import BrainRise.BrainRise.repository.BannerRepository;
import BrainRise.BrainRise.service.BannerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service

@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;

    @Override
    public BannerDto getBanner() {
        Banner banner = bannerRepository.findAll().stream()
                .findFirst()
                .orElse(new Banner(1L, "Default Banner", "Default Subtitle"));
        return modelMapper.map(banner, BannerDto.class);
    }

    @Override
    public BannerDto updateBanner(Long id, BannerDto bannerDto) {
        Banner banner = bannerRepository.findAll().stream()
                .findFirst()
                .orElse(new Banner(id, bannerDto.getTitle(), bannerDto.getSubTitle()));
        modelMapper.map(bannerDto, banner);
        Banner update = bannerRepository.save(banner);
        return modelMapper.map(update, BannerDto.class);
    }

}
