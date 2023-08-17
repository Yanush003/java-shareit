package ru.practicum.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final Long TESTUSERID = 1L;
    private final UserDto mockUserDto = UserDto.builder()
            .id(TESTUSERID)
            .name("Test User")
            .email("test@example.com")
            .build();

    @Before
    public void setUp() {
        when(userService.create(any(UserDto.class))).thenReturn(mockUserDto);
        when(userService.getUser(TESTUSERID)).thenReturn(mockUserDto);
        when(userService.update(eq(TESTUSERID), any(UserDto.class))).thenReturn(mockUserDto);
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(mockUserDto));
        doNothing().when(userService).remove(TESTUSERID);
    }

    @Test
    public void createTest() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test User\", \"email\": \"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TESTUSERID))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void getTest() throws Exception {
        mockMvc.perform(get("/users/" + TESTUSERID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TESTUSERID))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void updateTest() throws Exception {
        mockMvc.perform(patch("/users/" + TESTUSERID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated User\", \"email\": \"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TESTUSERID))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void removeTest() throws Exception {
        mockMvc.perform(delete("/users/" + TESTUSERID))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(TESTUSERID))
                .andExpect(jsonPath("$[0].name").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }
}
