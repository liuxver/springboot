package com.example.mysql.repository;

import com.example.mysql.entity.Role;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * created by liuxv on 2018/08/21
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
    @Query("select t from Role t where t.name like :name")
    Page<Role> findByName(@Param("name") String name, Pageable pageRequest);
}
