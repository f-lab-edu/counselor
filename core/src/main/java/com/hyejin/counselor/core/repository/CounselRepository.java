package com.hyejin.counselor.core.repository;

import com.hyejin.counselor.core.entity.Counsel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselRepository extends MongoRepository<Counsel, String> {
    Page<Counsel> findAllByStatus(String status, Pageable pageable);
}

