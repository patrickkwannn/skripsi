package com.skripsi.skripsiservice.repository;

import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.model.VerifiedNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifiedNumberRepository extends JpaRepository<VerifiedNumber, String>{

    VerifiedNumber getByUser(UserTable userTable);

}
