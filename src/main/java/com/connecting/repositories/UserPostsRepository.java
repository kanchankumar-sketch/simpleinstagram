package com.connecting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.connecting.dto.UserPost;
import com.connecting.model.UserPosts;
@Repository
public interface UserPostsRepository extends JpaRepository<UserPosts,Long> {

	@Query(value = "SELECT pr.user_id as id,pr.profile_name as profileName,pr.profile_image_path as profilePath,po.image_post_path as postFilePath,po.description,po.location,po.id as idea FROM user_profile as pr,user_posts as po where po.user_id=pr.user_id and pr.user_id IN (SELECT x.follwed_user_id from user_follow as x where x.user_id=:id) order by po.id desc limit 5",nativeQuery = true)
	List<UserPost> getPostDeatils(Long id);
	
	@Query(value = "SELECT pr.user_id as id,pr.profile_name as profileName,pr.profile_image_path as profilePath,po.image_post_path as postFilePath,po.description,po.location,po.id as idea FROM user_profile as pr,user_posts as po where po.user_id=pr.user_id and pr.user_id=:id order by po.id desc",nativeQuery = true)
	List<UserPost> getPostDeatils1(Long id);
}
