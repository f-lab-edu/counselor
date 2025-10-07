package com.hyejin.counselor.app;

import static com.hyejin.counselor.core.common.util.DateUtil.nowDate;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyejin.counselor.Application;
import com.hyejin.counselor.core.common.enums.CounselType;
import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.repository.CounselRepository;
import com.hyejin.counselor.core.service.CounselService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

@DisplayName("상담 컨트롤러")
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class CounselControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CounselService counselService;
    @MockitoBean
    private CounselRepository counselRepository;


    @Test
    @DisplayName("상담 등록 컨트롤러 테스트")
    void controllerTest() throws Exception {

        String userId = "testId";


        Counsel counsel = new Counsel();
        counsel.setUserId(userId);
        counsel.setStatus(CounselType.READY.getCode());
        counsel.setRegDate(nowDate());

        given(counselRepository.save(Mockito.any(Counsel.class))).willReturn(new Counsel());
        when(counselService.save(counsel)).thenReturn(counsel);

        this.mockMvc.perform(post("/counsel").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(counsel)))
                .andDo(print()) // 결과출력
                .andExpect(status().isOk());
    }
}
