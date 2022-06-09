package com.connecting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.connecting.model.UserFollow;
@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow,Long> {

}
