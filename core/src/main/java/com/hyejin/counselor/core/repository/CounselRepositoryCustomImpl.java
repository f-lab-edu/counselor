package com.hyejin.counselor.core.repository;

import com.hyejin.counselor.core.dto.CounselSlice;
import com.hyejin.counselor.core.dto.CounselCursorRequest;
import com.hyejin.counselor.core.entity.Counsel;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CounselRepositoryCustomImpl implements CounselRepositoryCustom{
    private final MongoTemplate mongoTemplate;

    public CounselRepositoryCustomImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CounselSlice findCounselsByCursor(CounselCursorRequest request) {
        Criteria cursorCriteria = createFullCriteria(request);
        Query query = new Query(cursorCriteria);
        Sort sort = Sort.by(
                Sort.Direction.DESC, "regDate"
        );
        query.with(sort);
        query.limit(request.getPageSize() + 1);
        List<Counsel> results = mongoTemplate.find(query, Counsel.class);

        return buildUserSlice(results, request.getPageSize());
    }

    private Criteria createFullCriteria(CounselCursorRequest request) {
        Criteria cursorCriteria = createCursorCriteria(request);

        // 상태에 따른 리스트 조회
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            cursorCriteria.and("status").is(request.getStatus());
        }

        return cursorCriteria;
    }

    private Criteria createCursorCriteria(CounselCursorRequest request) {
        // 첫 페이지 요청
        if (request.getLastRegDate() == null || request.getLastRegDate().isEmpty()) {
            return new Criteria(); // 조건 없이 전체 조회
        }
        Criteria condition = Criteria.where("regDate").lt(request.getLastRegDate());
        return condition;
    }

    private CounselSlice buildUserSlice(List<Counsel> results, int pageSize) {
        boolean hasNext = results.size() > pageSize;
        // 다음 페이지 확인용으로 가져온 마지막 항목을 제거
        List<Counsel> content = hasNext ? results.subList(0, pageSize) : results;

        if (content.isEmpty()) {
            return new CounselSlice(List.of(), null, false);
        }

        // 현재 페이지의 마지막 항목을 다음 커서로 사용
        Counsel lastCounsel = content.get(content.size() - 1);

        return new CounselSlice(
                content,
                hasNext ? lastCounsel.getRegDate() : null,
                hasNext
        );
    }
}
