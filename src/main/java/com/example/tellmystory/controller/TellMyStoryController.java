package com.example.tellmystory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tellmystory.constant.ApiConstant;
import com.example.tellmystory.dto.AccountDetailsDto;
import com.example.tellmystory.dto.LoginDto;
import com.example.tellmystory.dto.PostDetailsDto;
import com.example.tellmystory.dto.ResponseDto;
import com.example.tellmystory.service.MyStoryService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

@RestController
@RequestMapping("/api/v1")
@OpenAPIDefinition(info = @Info(title = "TELL MY STORY", description = "FIND PUBLISHER AND POST YOUR STORIES", version = "V1"))
@CrossOrigin(origins = "*")
public class TellMyStoryController {

	@Autowired
	private MyStoryService storyService;

	@PostMapping("/register")
	@Operation(summary = "Register Your Account API")
	public ResponseEntity<ResponseDto> register(@RequestBody AccountDetailsDto accountDto) {

		return ResponseEntity.ok(
				new ResponseDto(false, ApiConstant.ACCOUNT_SUCCESSFULLY_REGISTERED, storyService.register(accountDto)));
	}

	@PostMapping("/login")
	@Operation(summary = "Account Login API")
	public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto) {
		return ResponseEntity.ok(new ResponseDto(false, ApiConstant.LOGIN_SUCCESSFULL, storyService.login(loginDto)));

	}

	@PostMapping("/addPost")
	@Operation(summary = "Post Your Story API")
	public ResponseEntity<ResponseDto> addPost(@RequestBody PostDetailsDto postDetailsDto) {

		return ResponseEntity
				.ok(new ResponseDto(false, ApiConstant.POST_ADDED_SUCCESSFULLY, storyService.addPost(postDetailsDto)));
	}

	@GetMapping("getPublisherDropdown")
	@Operation(summary = "Get All Publisher DropDown API")
	public ResponseEntity<ResponseDto> getAllPublisherDropdown() {

		return ResponseEntity
				.ok(new ResponseDto(false, ApiConstant.GET_PUBLISHER_SUCCESS, storyService.getAllPublisherDropDown()));

	}

	@GetMapping("/getPost")
	@Operation(summary = "Get All Post API")
	public ResponseEntity<ResponseDto> getMyPost(@RequestParam Integer userId) {
		return ResponseEntity.ok(new ResponseDto(false, ApiConstant.GET_POST_SUCCESS, storyService.getPost(userId)));

	}

	@PutMapping("/updateStatus")
	@Operation(summary = "Update Post Status API")
	public ResponseEntity<ResponseDto> updateStatus(@RequestBody PostDetailsDto postDetailsDto) {

		return ResponseEntity.ok(
				new ResponseDto(false, ApiConstant.UPDATE_POST_STATUS_SUCCESS, storyService.updateStatus(postDetailsDto)));

	}

	@PutMapping("/updateMyProfile")
	@Operation(summary = "Update Your Profile API")
	public ResponseEntity<ResponseDto> updateMyProfile(@RequestBody AccountDetailsDto accountDto) {

		return ResponseEntity
				.ok(new ResponseDto(false, ApiConstant.UPDATE_ACCOUNT_SUCCESS, storyService.updateMyProfile(accountDto)));

	}

	@GetMapping("/allFeeds")
	@Operation(summary = "Get All Feeds API")
	public ResponseEntity<ResponseDto> getAllApprovedPosts() {

		return ResponseEntity
				.ok(new ResponseDto(false, ApiConstant.GET_ALL_APPROVED_POST_SUCCESS, storyService.getAllApprovedPosts()));

	}

}
