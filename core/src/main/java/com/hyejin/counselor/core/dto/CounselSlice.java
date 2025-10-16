package com.hyejin.counselor.core.dto;

import com.hyejin.counselor.core.entity.Counsel;
import lombok.Getter;

import java.util.List;

@Getter
public class CounselSlice {

    private final List<Counsel> content;
    private final String nextCursorRegDate;
    private final boolean hasNext;

    public CounselSlice(List<Counsel> content, String nextCursorRegDate, boolean hasNext) {
        this.content = content;
        this.nextCursorRegDate = nextCursorRegDate;
        this.hasNext = hasNext;
    }

}
