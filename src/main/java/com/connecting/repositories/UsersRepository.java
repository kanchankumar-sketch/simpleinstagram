package com.connecting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.connecting.model.Users;
@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

	Users findByUsername(String name);

}
