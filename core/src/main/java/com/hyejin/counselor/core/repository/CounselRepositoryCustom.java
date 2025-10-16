package com.hyejin.counselor.core.repository;

import com.hyejin.counselor.core.dto.CounselSlice;
import com.hyejin.counselor.core.dto.CounselCursorRequest;

public interface CounselRepositoryCustom {

    public CounselSlice findCounselsByCursor(CounselCursorRequest request);

}