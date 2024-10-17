package com.example.tellmystory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tellmystory.constant.ExceptionConstant;
import com.example.tellmystory.dto.AccountDetailsDto;
import com.example.tellmystory.dto.LoginDto;
import com.example.tellmystory.dto.PostDetailsDto;
import com.example.tellmystory.dto.PublisherListDto;
import com.example.tellmystory.entity.AccountDetails;
import com.example.tellmystory.entity.PostDetails;
import com.example.tellmystory.exception.AccountExistsException;
import com.example.tellmystory.exception.DataNotFoundException;
import com.example.tellmystory.repository.MyStoryRepository;
import com.example.tellmystory.repository.NewPostRepository;

@Service
public class MyStoryServiceImpl implements MyStoryService {

	@Autowired
	private MyStoryRepository accountRepository;

	@Autowired
	private NewPostRepository postRepository;

	@Override
	public AccountDetailsDto register(AccountDetailsDto accountDto) {
		Optional<AccountDetails> findByEmail = accountRepository.findByEmail(accountDto.getEmail());
		if (findByEmail.isPresent()) {
			throw new AccountExistsException(ExceptionConstant.ACCOUNT_ALREADY_PRESENT);
		} else {
			AccountDetails accountDetails = new AccountDetails();
			BeanUtils.copyProperties(accountDto, accountDetails);
			AccountDetails save = accountRepository.save(accountDetails);
			AccountDetailsDto myStoryAccountDto = new AccountDetailsDto();
			BeanUtils.copyProperties(save, myStoryAccountDto);
			return myStoryAccountDto;
		}
	}

	@Override
	public PostDetailsDto addPost(PostDetailsDto postDetailsDto) {
		Optional<AccountDetails> findByEmail = Optional
				.ofNullable(accountRepository.findByEmail(postDetailsDto.getEmail())
						.orElseThrow(() -> new DataNotFoundException(ExceptionConstant.ACCOUNT_NOT_FOUND)));
		AccountDetails userAccount = findByEmail.get();

		Optional<AccountDetails> findById = Optional
				.ofNullable(accountRepository.findById(postDetailsDto.getPublisherId())
						.orElseThrow(() -> new DataNotFoundException(ExceptionConstant.PUBLISHERS_NOT_FOUND)));
		AccountDetails publisherAccount = findById.get();

		List<PostDetails> list = new ArrayList<>();
		PostDetails post = new PostDetails();
		BeanUtils.copyProperties(postDetailsDto, post);
		list.add(post);

		userAccount.setPostToUser(list);
		publisherAccount.setPostToPublisher(list);

		List<PostDetails> newPostsList = userAccount.getPostToUser();// all posts list

		for (PostDetails newPost : newPostsList) {
			newPost.setAccountUser(userAccount);
			newPost.setAccountPublisher(publisherAccount);
		}
		accountRepository.save(userAccount);
		return postDetailsDto;

	}

	@Override
	public List<PublisherListDto> getAllPublisherDropDown() {
		List<AccountDetails> findAll = accountRepository.findByRole("Publisher");
		List<PublisherListDto> publisherList = new ArrayList<>();
		if (findAll.isEmpty()) {
			throw new DataNotFoundException(ExceptionConstant.PUBLISHERS_NOT_FOUND);
		}
		for (AccountDetails accountDetails : findAll) {
			PublisherListDto listDto = new PublisherListDto();
			listDto.setAccountId(accountDetails.getAccountId());
			listDto.setName(accountDetails.getName());
			publisherList.add(listDto);
		}
		return publisherList;
	}

	@Override
	public List<PostDetailsDto> getPost(Integer userId) {
		List<PostDetails> findByUserId = postRepository.findByAccountUserAccountIdOrAccountPublisherAccountId(userId,
				userId);
		if (findByUserId.isEmpty()) {
			throw new DataNotFoundException(ExceptionConstant.INVALID_CREDENTIALS);
		}
		List<PostDetailsDto> postDtos = new ArrayList<>();
		findByUserId.forEach(post -> {
			PostDetailsDto postDetailsDto = new PostDetailsDto();
			BeanUtils.copyProperties(post, postDetailsDto);
			postDetailsDto.setEmail(post.getAccountUser().getEmail());
			postDetailsDto.setUserId(post.getAccountUser().getAccountId());
			postDetailsDto.setUserName(post.getAccountUser().getName());
			postDetailsDto.setPublisherId(post.getAccountPublisher().getAccountId());
			postDetailsDto.setPublisherName(post.getAccountPublisher().getName());
			postDtos.add(postDetailsDto);
		});
		return postDtos;

	}

	@Override
	public AccountDetailsDto updateMyProfile(AccountDetailsDto accountDto) {
		Optional<AccountDetails> findByEmail = accountRepository.findByEmail(accountDto.getEmail());
		if (findByEmail.isPresent()) {
			AccountDetails accountDetails = findByEmail.get();
			BeanUtils.copyProperties(accountDto, accountDetails);
			AccountDetails save = accountRepository.save(accountDetails);
			BeanUtils.copyProperties(save, accountDto);
			return accountDto;
		}
		throw new DataNotFoundException(ExceptionConstant.ACCOUNT_NOT_FOUND);
	}

	@Override
	public List<PostDetailsDto> getAllApprovedPosts() {
		List<PostDetails> findByStatus = postRepository.findByStatus("Approved");
		if (findByStatus.isEmpty()) {
			throw new DataNotFoundException(ExceptionConstant.POST_NOT_FOUND);
		}
		List<PostDetailsDto> postDtoList = new ArrayList<>();
		findByStatus.forEach(post -> {
			PostDetailsDto postDetailsDto = new PostDetailsDto();
			BeanUtils.copyProperties(post, postDetailsDto);
			postDetailsDto.setUserName(post.getAccountUser().getName());
			postDetailsDto.setPublisherName(post.getAccountPublisher().getName());
			postDtoList.add(postDetailsDto);
		});
		return postDtoList;
	}

	@Override
	public PostDetailsDto updateStatus(PostDetailsDto postDetailsDto) {
		Optional<PostDetails> findById = postRepository.findById(postDetailsDto.getPostId());
		if (findById.isPresent()) {
			PostDetails newPost = findById.get();
			newPost.setStatus(postDetailsDto.getStatus());
			PostDetails save = postRepository.save(newPost);
			BeanUtils.copyProperties(save, postDetailsDto);
			return postDetailsDto;
		}
		throw new DataNotFoundException(ExceptionConstant.INVALID_CREDENTIALS);
	}

	@Override
	public AccountDetailsDto login(LoginDto loginDto) {
		Optional<AccountDetails> findByEmailAndPassword = accountRepository.findByEmailAndPassword(loginDto.getEmail(),
				loginDto.getPassword());
		if (findByEmailAndPassword.isPresent()) {
			AccountDetails accountDetails = findByEmailAndPassword.get();
			AccountDetailsDto myStoryAccountDto = new AccountDetailsDto();
			BeanUtils.copyProperties(accountDetails, myStoryAccountDto);
			myStoryAccountDto.setPassword(null);
			return myStoryAccountDto;
		}
		throw new DataNotFoundException(ExceptionConstant.INVALID_CREDENTIALS);
	}

}
