package com.demogroup.demoweb.repository;

import com.demogroup.demoweb.domain.Mango;
import com.demogroup.demoweb.domain.dto.MangoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MangoRepository extends JpaRepository<Mango, Long> {
    List<Mango> findAllByUser_Uid(Long uid);
}
