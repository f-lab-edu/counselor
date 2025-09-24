import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyejin.counselor.core.entity.User;
import com.hyejin.counselor.core.service.UserService;
import config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.given;

@DisplayName("회원 컨트롤러")
@SpringBootTest(classes = com.hyejin.counselor.Application.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;


    @DisplayName("user 정보 저장 후 성공여부 확인")
    @Test
    void userSaveTest() throws Exception{

        //Given
        User requestUser = new User("testUser","01022220000");

        //When
        given(userService.save(requestUser)).willReturn(true);

        //Then
        MvcResult result = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getStatus());
    }

}
