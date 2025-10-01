package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounselService {
    private final CounselRepository counselRepository;

    public Counsel save(Counsel counsel) {
      return counselRepository.save(counsel);
    }
}
