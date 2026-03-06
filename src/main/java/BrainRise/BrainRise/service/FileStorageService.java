package BrainRise.BrainRise.service.impls;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // Şəkillərin yadda saxlanacağı qovluq yolu
    private final Path fileStorageLocation = Paths.get("src/main/resources/static/uploads");

    public String saveFile(MultipartFile file) {
        try {
            // Qovluq yoxdursa yarat
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            // Faylın adını unikal et (Məs: 550e8400_sekil.jpg)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path targetLocation = fileStorageLocation.resolve(fileName);

            // Faylı kopyala
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Fayl yadda saxlanarkən xəta baş verdi!", ex);
        }
    }
}