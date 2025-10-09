package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.common.enums.CounselType;
import com.hyejin.counselor.core.common.enums.ErrorCode;
import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.entity.User;
import com.hyejin.counselor.core.repository.CounselRepository;
import com.hyejin.counselor.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.hyejin.counselor.core.common.util.DateUtil.nowDate;


@Service
@RequiredArgsConstructor
public class CounselService {
    private final CounselRepository counselRepository;
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

    public Page<Counsel> counselList(String status, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "status");
        Pageable pageable = PageRequest.of(page, 10, sort);
        return counselRepository.findAllByStatus(status, pageable);
    }

    public User userSearch(String counselId) throws Exception {
        Counsel counsel = counselRepository.findById(counselId).orElseThrow(() -> new Exception(ErrorCode.NULL_DATA.getCode() + ":" + counselId));
        User user = userRepository.findById(counsel.getUserId()).orElseThrow(() -> new Exception(ErrorCode.NULL_DATA.getCode() + ":" + counsel.getUserId()));

        return user;
    }
}

