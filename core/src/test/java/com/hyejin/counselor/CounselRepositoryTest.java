package com.hyejin.counselor;

import com.hyejin.counselor.core.common.eNum.CounselType;
import com.hyejin.counselor.core.common.util.DateUtil;
import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.repository.CounselRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("상담 서비스")
@DataMongoTest
@ContextConfiguration(classes = Application.class)
public class CounselRepositoryTest {

    @Autowired
    private CounselRepository counselRepository;


    @Test
    @DisplayName("상담 등록")
    void createCounselTest() {
        //Given
        Counsel counsel = Counsel.builder()
                .userId("repo1")
                .status(CounselType.READY.getCode())
                .regDate(DateUtil.nowDate())
                .build();

        // When
        Counsel saveCounsel = counselRepository.save(counsel);

        // Then
        assertNotNull(saveCounsel);
        assertNotNull(saveCounsel.getId());
        assertTrue(counsel.getUserId().equals(saveCounsel.getUserId()));
        assertTrue(counsel.getStatus().equals(saveCounsel.getStatus()));

    }
}
