package apptive.team5.file.service;

import apptive.team5.file.domain.TemporalFile;
import apptive.team5.file.repository.TemporalFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TemporalLowService {

    private final TemporalFileRepository temporalFileRepository;

    public TemporalFile save(TemporalFile temporalFile) {
        return temporalFileRepository.save(temporalFile);
    }

    public List<TemporalFile> findOrphanFiles(LocalDateTime cutoff) {
        return temporalFileRepository.findOrphanFiles(cutoff);
    }

    public void deleteByIds(List<Long> ids) {
        temporalFileRepository.deleteByIds(ids);
    }

    public void deleteById(Long id) {
        temporalFileRepository.deleteById(id);
    }


}
