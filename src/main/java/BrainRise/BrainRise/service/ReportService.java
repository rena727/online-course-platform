package BrainRise.BrainRise.service;

import BrainRise.BrainRise.models.Report;
import java.util.List;

public interface ReportService {
    void saveReport(Report report);
    List<Report> getAllReports();
    void markAsRead(Long id);
    void deleteReport(Long id);
    long getUnreadCount();
}