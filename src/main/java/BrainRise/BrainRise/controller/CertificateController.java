package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.models.UserExamResult;
import BrainRise.BrainRise.repository.UserExamResultRepository; // Repository import olunmalıdır
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // Bu mütləqdir
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.TemplateEngine; // Bu import mütləqdir
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

@Controller
@RequiredArgsConstructor
public class CertificateController {

    private final UserExamResultRepository resultRepository;
    private final TemplateEngine templateEngine;

    @GetMapping("/download-certificate/{resultId}")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long resultId) {

        UserExamResult result = resultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Nəticə tapılmadı!"));

        Context context = new Context();
        context.setVariable("fullName", result.getUser().getFullName());
        context.setVariable("examName", result.getExam().getName());
        context.setVariable("score", result.getScore());
        context.setVariable("date", result.getExamDate().toLocalDate().toString());

        String htmlContent = templateEngine.process("certificate-template", context);

        // 3. HTML-i PDF-ə çeviririk
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);
            builder.run();

            // 4. PDF-i yükləmə kimi göndəririk
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sertifikat.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}