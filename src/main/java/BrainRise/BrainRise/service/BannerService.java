package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.BannerDto;

public interface BannerService {
 BannerDto getBanner();
 BannerDto updateBanner(Long id,BannerDto bannerDto);
}
