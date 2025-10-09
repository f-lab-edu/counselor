package com.hyejin.counselor;

import com.hyejin.counselor.core.common.enums.CounselType;
import com.hyejin.counselor.core.common.enums.ErrorCode;
import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.repository.CounselRepository;
import com.hyejin.counselor.core.service.CounselService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;


@DisplayName("상담 서비스")
@ExtendWith(MockitoExtension.class)
public class CounselServiceTest {

    @InjectMocks
    private CounselService counselService;
    @Mock
    private CounselRepository counselRepository;


    @Test
    @DisplayName("상담 등록시 userId 없을 경우 예외발생")
    void createCounselErrorTest() {
        //Given
        Counsel counsel = Counsel.builder()
                .status(CounselType.READY.getCode())
                .build();

        // When
        Throwable throwable = catchThrowable(() -> counselService.save(counsel));

        // Then
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.INVALID_COUNSEL_INFO.getCode());
    }

    @Test
    @DisplayName("채팅방에 있는 회원 조회")
    void userSearchErrorTest() {
        //Given
        String counselId = "";

        // When
        Throwable throwable = catchThrowable(() -> counselService.userSearch(counselId));

        // Then
        assertThat(throwable)
                .isInstanceOf(Exception.class)
                .hasMessageContaining(ErrorCode.NULL_DATA.getCode());
    }

}
