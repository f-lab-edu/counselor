package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.common.eNum.CounselType;
import com.hyejin.counselor.core.common.eNum.ErrorCode;
import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.hyejin.counselor.core.common.util.DateUtil.nowDate;

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

    public Counsel createCounsel(Counsel counsel) {
        counsel.setStatus(CounselType.READY.getCode());
        counsel.setRegDate(nowDate());
        return counsel;
    }
}
