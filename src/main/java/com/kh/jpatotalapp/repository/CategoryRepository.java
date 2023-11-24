package com.kh.jpatotalapp.repository;

import com.kh.jpatotalapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByMemberEmail(String email); // 주어진 이메일(String email)로 멤버 이메일 찾아줘 <Category>에서 찾아서 목록만들어줘
}
