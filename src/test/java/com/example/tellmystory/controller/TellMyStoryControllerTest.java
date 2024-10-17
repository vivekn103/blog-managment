package com.example.tellmystory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.tellmystory.dto.AccountDetailsDto;
import com.example.tellmystory.dto.PostDetailsDto;
import com.example.tellmystory.dto.PublisherListDto;
import com.example.tellmystory.dto.ResponseDto;
import com.example.tellmystory.service.MyStoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class TellMyStoryControllerTest {

	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private MyStoryService service;

	@InjectMocks
	private TellMyStoryController controller;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	AccountDetailsDto accountDto = new AccountDetailsDto(1,"Sumeet", "s@gmail.com", "123", "User");
	PostDetailsDto postDetailsDto = new PostDetailsDto(1,"Java", "Java is Object oriented Langiuage", "s@gmail.com", 1, "Sumeet",
			"Pending", "IT", 1, null, null);
	PostDetailsDto approvePostDto = new PostDetailsDto(1,"Java", "Java is Object oriented Langiuage", "s@gmail.com", 1,"Sumeet","Approve",
			"IT", 1, "Sushma", null);
	List<PostDetailsDto> listDtos = new ArrayList<>();

	PublisherListDto publisherListDto = new PublisherListDto(2, "Sushma");
	List<PublisherListDto> dtosList = new ArrayList<>();

	@Test
	void register_success() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(service.register(any())).thenReturn(accountDto);
		String contentAsString = mockMvc
				.perform(post("/api/v1/register").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accountDto)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ResponseDto readValue = objectMapper.readValue(contentAsString, ResponseDto.class);
		assertEquals("Account Registration Successfull", readValue.getMsg());
	}

	@Test
	void login_success() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(service.login(any())).thenReturn(accountDto);
		String contentAsString = mockMvc
				.perform(post("/api/v1/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(accountDto)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ResponseDto readValue = objectMapper.readValue(contentAsString, ResponseDto.class);
		assertEquals("Successfully LoggedIn", readValue.getMsg()); 
	}

	@Test
	void addPost_success() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(service.addPost(any())).thenReturn(postDetailsDto);
		String contentAsString = mockMvc
				.perform(post("/api/v1/addPost").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(postDetailsDto)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ResponseDto readValue = objectMapper.readValue(contentAsString, ResponseDto.class);
		assertEquals("Post Submitted Successfully", readValue.getMsg());
	}

	@Test
	void getAllPublisherDropdown_success() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		dtosList.add(publisherListDto);
		when(service.getAllPublisherDropDown()).thenReturn(dtosList);
		String contentAsString = mockMvc
				.perform(get("/api/v1/getPublisherDropdown").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dtosList)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ResponseDto readValue = objectMapper.readValue(contentAsString, ResponseDto.class);
		assertEquals("Publisher Found Successfully", readValue.getMsg());
	}

	@Test
	void getMyPost_success() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		listDtos.add(postDetailsDto);
		when(service.getPost(anyInt())).thenReturn(listDtos);
		String contentAsString = mockMvc
				.perform(get("/api/v1/getPost").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(listDtos))
						.param("userId", "1"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ResponseDto readValue = objectMapper.readValue(contentAsString, ResponseDto.class);
		assertEquals("Post found Successfully", readValue.getMsg());
	}

	@Test
	void updateStatus_success() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(service.updateStatus(any())).thenReturn(postDetailsDto);
		String contentAsString = mockMvc
				.perform(put("/api/v1/updateStatus").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(postDetailsDto)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ResponseDto readValue = objectMapper.readValue(contentAsString, ResponseDto.class);
		assertEquals("Post Status updated successfully", readValue.getMsg());
	}

	@Test
	void updateMyProfile_success() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(service.updateMyProfile(accountDto)).thenReturn(accountDto);
		String contentAsString = mockMvc
				.perform(put("/api/v1/updateMyProfile").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accountDto)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ResponseDto readValue = objectMapper.readValue(contentAsString, ResponseDto.class);
		assertEquals("Account updated successfully", readValue.getMsg());
	}

	@Test
	void getAllApprovedPosts_success() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		listDtos.add(approvePostDto);
		when(service.getAllApprovedPosts()).thenReturn(listDtos);
		String contentAsString = mockMvc
				.perform(get("/api/v1/allFeeds").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(listDtos)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ResponseDto readValue = objectMapper.readValue(contentAsString, ResponseDto.class);
		assertEquals("All Approve Post found Successfully", readValue.getMsg());
	}

}
