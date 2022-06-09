package com.connecting.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.connecting.model.UserProfile;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

	UserProfile findByUserId(Long id);
	
	@Query(value = "select * from user_profile where user_id!=:id and user_id NOT IN (select follwed_user_id from user_follow where user_id=:id) limit 5",nativeQuery = true)
	List<UserProfile> findByNotUserId(Long id);

	@Transactional
	@Modifying
	void deleteByUserId(Long id);

	@Query(value = "select * from user_profile where user_id!=:id and user_id  IN (select follwed_user_id from user_follow where user_id=:id) ",nativeQuery = true)
	List<UserProfile> findByFollowing(Long id);

	@Query(value = "select * from user_profile where user_id!=:id and user_id  IN (select user_id from user_follow where follwed_user_id=:id) ",nativeQuery = true)
	List<UserProfile> findByFollowers(Long id);
	
	
}
