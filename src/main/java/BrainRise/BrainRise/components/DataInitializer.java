package BrainRise.BrainRise.components;

import BrainRise.BrainRise.models.Banner;
import BrainRise.BrainRise.models.Course;
import BrainRise.BrainRise.models.Mentor;
import BrainRise.BrainRise.repository.BannerRepository;
import BrainRise.BrainRise.repository.CourseRepository;
import BrainRise.BrainRise.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MentorRepository mentorRepository;
    private final CourseRepository kurslarRepository;
    private final BannerRepository bannerRepository;

    @Override
    public void run(String... args) throws Exception {
        if (bannerRepository.count() == 0) {

            Banner banner = new Banner();
            banner.setTitle("Gələcəyin Kodunu Bizimlə Yaz!");
            banner.setSubTitle("BrainRise ilə professional proqramlaşdırma təhsili.");
            bannerRepository.save(banner);

            Mentor mentor = new Mentor();
            mentor.setName("Əli Məmmədov");
            mentor.setSpecialty("Senior Java Developer");
            mentor.setImgUrl("https://randomuser.me/api/portraits/men/1.jpg");
            mentorRepository.save(mentor);

            Course kurs = new Course();
            kurs.setName("Java Backend");
            kurs.setDescription("Spring Boot və Mikroxidmət arxitekturası.");
            kurs.setIconClass("fab fa-java");
            kurslarRepository.save(kurs);

            System.out.println(">> Baza boş idi, test məlumatları uğurla yükləndi!");
        }
    }
}