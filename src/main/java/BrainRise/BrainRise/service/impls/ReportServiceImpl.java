package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.models.Report;
import BrainRise.BrainRise.repository.ReportRepository;
import BrainRise.BrainRise.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    @Transactional
    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        reportRepository.findById(id).ifPresent(report -> {
            report.setRead(true);
            reportRepository.save(report);
        });
    }

    @Override
    @Transactional
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    @Override
    public long getUnreadCount() {
        return reportRepository.countByIsReadFalse();
    }
}