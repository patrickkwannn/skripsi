package com.skripsi.skripsiservice.repository;

import com.skripsi.skripsiservice.model.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTableRepository extends JpaRepository<UserTable,String> {

    List<UserTable> getAllByUserRoleAndIsDeleted(String role,String isDeleted);

    UserTable findUserTableByUserIdAndIsDeleted(String id,String isDeleted);

    UserTable findUserTableByEmailAndIsDeleted(String email,String isDeleted);
}
