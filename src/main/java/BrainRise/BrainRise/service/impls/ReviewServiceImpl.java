package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.ReviewDto;
import BrainRise.BrainRise.models.Mentor;
import BrainRise.BrainRise.models.Review;
import BrainRise.BrainRise.models.User; // User modelini əlavə et
import BrainRise.BrainRise.repository.MentorRepository;
import BrainRise.BrainRise.repository.ReviewRepository;
import BrainRise.BrainRise.repository.UserRepository; // Repository-ni əlavə et
import BrainRise.BrainRise.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MentorRepository mentorRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReviewDto addReview(ReviewDto reviewDto) {
        Review review = modelMapper.map(reviewDto, Review.class);

        Mentor mentor = mentorRepository.findById(reviewDto.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor tapılmadı"));
        review.setMentor(mentor);

        if (reviewDto.getUserName() != null) {
            User user = userRepository.findByUsername(reviewDto.getUserName())
                    .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));
            review.setUser(user);
        }

        Review savedReview = reviewRepository.save(review);
        return modelMapper.map(savedReview, ReviewDto.class);
    }

    @Override
    public List<ReviewDto> getReviewsByMentorId(Long mentorId) {
        List<Review> reviews = reviewRepository.findByMentorId(mentorId);

        return reviews.stream().map(review -> ReviewDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .mentorId(review.getMentor().getId())
                .userFullName(review.getUser() != null ? review.getUser().getFullName() : "Gizli User")
                .build()
        ).toList();
    }
}