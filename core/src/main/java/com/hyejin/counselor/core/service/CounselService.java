package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CounselService {
    private final CounselRepository counselRepository;

    public Page<Counsel> counselList(String status, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "status");
        Pageable pageable = PageRequest.of(page, 10, sort);
        return counselRepository.findAllByStatus(status, pageable);
    }

}



