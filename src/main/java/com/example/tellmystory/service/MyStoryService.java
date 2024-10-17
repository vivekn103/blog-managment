package com.example.tellmystory.service;

import java.util.List;

import com.example.tellmystory.dto.AccountDetailsDto;
import com.example.tellmystory.dto.LoginDto;
import com.example.tellmystory.dto.PostDetailsDto;
import com.example.tellmystory.dto.PublisherListDto;

public interface MyStoryService {

	AccountDetailsDto register(AccountDetailsDto accountDto);

	AccountDetailsDto updateMyProfile(AccountDetailsDto accountDto);

	PostDetailsDto addPost(PostDetailsDto postDetailsDto);

	List<PostDetailsDto> getPost(Integer userId);

	List<PostDetailsDto> getAllApprovedPosts();

	PostDetailsDto updateStatus(PostDetailsDto postDetailsDto);

	List<PublisherListDto> getAllPublisherDropDown();

	AccountDetailsDto login(LoginDto loginDto);

}
