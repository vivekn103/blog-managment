package com.example.tellmystory.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

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

@SpringBootTest
class MyStoryServiceImplTest {

	@Mock
	private MyStoryRepository accountRepository;

	@Mock
	private NewPostRepository postRepository;

	@InjectMocks
	private MyStoryServiceImpl serviceImpl;

	AccountDetails account = new AccountDetails(1, "Sumeet", "s@gmail.com", "123", "User", null, null);
	AccountDetails account2 = new AccountDetails(2, "Sushma", "sus@gmail.com", "123", "Publisher", null, null);
	List<AccountDetails> list = new ArrayList<>();
	AccountDetailsDto accountDto = new AccountDetailsDto(1, "Sumeet", "s@gmail.com", "123", "User");
	LoginDto loginDto = new LoginDto("s@gmailcom", "123");
	PostDetailsDto approvePostDto = new PostDetailsDto(1, "Java", "Java is Object oriented Langiuage", "s@gmail.com", 1,
			"Sumeet", "Approve", "IT", 1, "Sushma", null);
	PostDetails newPost = new PostDetails(1, "Java", "Java is Object oriented Langiuage", "Approve", "IT", null,
			account, account2);
	List<PostDetails> newPosts = new ArrayList<>();
	PublisherListDto publisherListDto = new PublisherListDto(1, "Sushma");
	List<PublisherListDto> listDtos = new ArrayList<>();

	@Test
	void register_success() {
		Optional<AccountDetails> optional = Optional.empty();
		when(accountRepository.findByEmail(anyString())).thenReturn(optional);
		when(accountRepository.save(any())).thenReturn(account);
		AccountDetailsDto register = serviceImpl.register(accountDto);
		assertEquals("Sumeet", register.getName());

	}

	@Test
	void register_fail() {
		Optional<AccountDetails> optional = Optional.of(account);
		when(accountRepository.findByEmail(anyString())).thenReturn(optional);
		assertThatThrownBy(() -> serviceImpl.register(accountDto)).isInstanceOf(AccountExistsException.class);
	}

	@Test
	void login_success() {
		Optional<AccountDetails> optional = Optional.of(account);
		when(accountRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(optional);
		AccountDetailsDto register = serviceImpl.login(loginDto);
		assertEquals("Sumeet", register.getName());

	}

	@Test
	void login_fail() {
		Optional<AccountDetails> optional = Optional.empty();
		when(accountRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(optional);
		assertThatThrownBy(() -> serviceImpl.login(loginDto)).isInstanceOf(DataNotFoundException.class);
	}

	@Test
	void addPost_success() {
		Optional<AccountDetails> optionalUser = Optional.of(account);
		Optional<AccountDetails> optionalPublisher = Optional.of(account);
		newPosts.add(newPost);
		when(accountRepository.findByEmail(anyString())).thenReturn(optionalUser);
		when(accountRepository.findById(anyInt())).thenReturn(optionalPublisher);
		optionalUser.get().setPostToUser(newPosts);
		optionalPublisher.get().setPostToPublisher(newPosts);
		newPosts.get(0).setAccountUser(account);
		newPosts.get(0).setAccountPublisher(account);
		when(accountRepository.save(optionalUser.get())).thenReturn(account);
		PostDetailsDto addPost = serviceImpl.addPost(approvePostDto);
		assertEquals("Java", addPost.getTitle());

	}

	@Test
	void addPost_fail() {
		Optional<AccountDetails> optionalUser = Optional.of(account);
		Optional<AccountDetails> optionalPublisher = Optional.empty();
		when(accountRepository.findByEmail(anyString())).thenReturn(optionalUser);
		when(accountRepository.findById(anyInt())).thenReturn(optionalPublisher);
		assertThatThrownBy(() -> serviceImpl.addPost(approvePostDto)).isInstanceOf(DataNotFoundException.class);
	}

	@Test
	void addPost_fail1() {
		Optional<AccountDetails> optionalPublisher = Optional.of(account);
		Optional<AccountDetails> optionalUser = Optional.empty();
		when(accountRepository.findByEmail(anyString())).thenReturn(optionalUser);
		when(accountRepository.findById(anyInt())).thenReturn(optionalPublisher);
		assertThatThrownBy(() -> serviceImpl.addPost(approvePostDto)).isInstanceOf(DataNotFoundException.class);
	}

	@Test
	void getAllPublisherDropDown_success() {
		list.add(account);
		when(accountRepository.findByRole(anyString())).thenReturn(list);
		List<PublisherListDto> allPublisherDropDown = serviceImpl.getAllPublisherDropDown();
		assertEquals("Sumeet", allPublisherDropDown.get(0).getName());

	}

	@Test
	void getAllPublisherDropDown_fail() {
		List<AccountDetails> accounts = new ArrayList<>();
		when(accountRepository.findByRole(anyString())).thenReturn(accounts);
		assertThatThrownBy(() -> serviceImpl.getAllPublisherDropDown()).isInstanceOf(DataNotFoundException.class);
	}

	@Test
	void getPost_success() {
		newPosts.add(newPost);
		when(postRepository.findByAccountUserAccountIdOrAccountPublisherAccountId(anyInt(), anyInt()))
				.thenReturn(newPosts);
		List<PostDetailsDto> post = serviceImpl.getPost(1);
		assertEquals("Java", post.get(0).getTitle());

	}

	@Test
	void getPost_fail() {
		List<PostDetails> newPosts = new ArrayList<>();
		when(postRepository.findByAccountUserAccountId(anyInt())).thenReturn(newPosts);
		assertThatThrownBy(() -> serviceImpl.getPost(1)).isInstanceOf(DataNotFoundException.class);
	}

	@Test
	void getAllApprovedPosts_success() {
		newPosts.add(newPost);
		when(postRepository.findByStatus(anyString())).thenReturn(newPosts);
		List<PostDetailsDto> allApprovedPosts = serviceImpl.getAllApprovedPosts();
		assertEquals("Approve", allApprovedPosts.get(0).getStatus());

	}

	@Test
	void getAllApprovedPosts_fail() {
		List<PostDetails> newPosts = new ArrayList<>();
		when(postRepository.findByStatus(anyString())).thenReturn(newPosts);
		assertThatThrownBy(() -> serviceImpl.getAllApprovedPosts()).isInstanceOf(DataNotFoundException.class);
	}

	@Test
	void updateMyProfile_success() {
		Optional<AccountDetails> optional = Optional.of(account);
		when(accountRepository.findByEmail(anyString())).thenReturn(optional);
		when(accountRepository.save(any())).thenReturn(account);
		AccountDetailsDto updateMyProfile = serviceImpl.updateMyProfile(accountDto);
		assertEquals("Sumeet", updateMyProfile.getName());

	}

	@Test
	void updateMyProfile_fail() {
		Optional<AccountDetails> optional = Optional.empty();
		when(accountRepository.findByEmail(anyString())).thenReturn(optional);
		assertThatThrownBy(() -> serviceImpl.updateMyProfile(accountDto)).isInstanceOf(DataNotFoundException.class);
	}

	@Test
	void updateStatus_success() {
		Optional<PostDetails> optional = Optional.of(new PostDetails(1, "Java", "Java is Object Oriented Language",
				"Approve", "It", null, account, account2));
		when(postRepository.findById(anyInt())).thenReturn(optional);
		optional.get().setStatus("Approve");
		when(postRepository.save(any())).thenReturn(newPost);
		PostDetailsDto updateStatus = serviceImpl.updateStatus(approvePostDto);
		assertEquals("Approve", updateStatus.getStatus());

	}

	@Test
	void updateStatus_fail() {
		Optional<PostDetails> optional = Optional.empty();
		when(postRepository.findById(anyInt())).thenReturn(optional);
		assertThatThrownBy(() -> serviceImpl.updateStatus(approvePostDto)).isInstanceOf(DataNotFoundException.class);
	}
}
