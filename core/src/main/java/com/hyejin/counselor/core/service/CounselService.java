package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.common.eNum.ErrorCode;
import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounselService {
    private final CounselRepository counselRepository;

    public Counsel save(Counsel counsel) {
        if(counsel.getUserId()==null || counsel.getUserId().trim().isEmpty()){
            throw new IllegalArgumentException(ErrorCode.INVALID_COUNSEL_INFO.getCode());
        }
      return counselRepository.save(counsel);
    }
}
