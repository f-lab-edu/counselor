package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.common.enums.CounselType;
import com.hyejin.counselor.core.common.enums.ErrorCode;
import com.hyejin.counselor.core.dto.CounselSlice;
import com.hyejin.counselor.core.dto.CounselCursorRequest;
import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.entity.User;
import com.hyejin.counselor.core.repository.CounselRepository;
import com.hyejin.counselor.core.repository.CounselRepositoryCustomImpl;
import com.hyejin.counselor.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.hyejin.counselor.core.common.util.DateUtil.nowDate;


@Service
@RequiredArgsConstructor
public class CounselService {
    private final CounselRepository counselRepository;
    private final CounselRepositoryCustomImpl counselRepositoryCustom;
    private final UserRepository userRepository;

    public Counsel save(Counsel counsel) {
        if (counsel.getUserId() == null || counsel.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.INVALID_COUNSEL_INFO.getCode());
        }
        return counselRepository.save(counsel);
    }

    public Counsel createCounsel(Counsel counsel) {
        counsel.setStatus(CounselType.READY.getCode());
        counsel.setRegDate(nowDate());
        return counsel;
    }

    public CounselSlice counselList(CounselCursorRequest cursorRequest) {

        return counselRepositoryCustom.findCounselsByCursor(cursorRequest);
    }

    public User userSearch(String counselId) throws Exception {
        Counsel counsel = counselRepository.findById(counselId).orElseThrow(() -> new Exception(ErrorCode.NULL_DATA.getCode() + ":" + counselId));
        User user = userRepository.findById(counsel.getUserId()).orElseThrow(() -> new Exception(ErrorCode.NULL_DATA.getCode() + ":" + counsel.getUserId()));

        return user;
    }

    public CounselCursorRequest createCounselCursorRequest(String status, String lastRegDate) {
        CounselCursorRequest cursorRequest = new CounselCursorRequest();
        cursorRequest.setStatus(status);
        cursorRequest.setLastRegDate(lastRegDate);

        return cursorRequest;
    }
}

