package com.example.tellmystory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tellmystory.entity.AccountDetails;

public interface MyStoryRepository extends JpaRepository<AccountDetails, Integer> {

	Optional<AccountDetails> findByEmail(String email);

	List<AccountDetails> findByRole(String accountRole);

	Optional<AccountDetails> findByEmailAndPassword(String email, String password);

	List<AccountDetails> findByAccountId(Integer accountId);
}
