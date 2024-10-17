package com.example.tellmystory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tellmystory.entity.PostDetails;

public interface NewPostRepository extends JpaRepository<PostDetails, Integer> {

	List<PostDetails> findByStatus(String string);

	List<PostDetails> findByAccountUserAccountId(Integer userId);

	List<PostDetails> findByAccountPublisherAccountId(Integer userId);

	List<PostDetails> findByAccountUserAccountIdOrAccountPublisherAccountId(Integer userId, Integer userId2);

}
