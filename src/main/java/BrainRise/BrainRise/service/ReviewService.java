package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    ReviewDto addReview(ReviewDto reviewDto);
    List<ReviewDto> getReviewsByMentorId(Long mentorId);
}
