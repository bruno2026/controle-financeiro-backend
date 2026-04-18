package com.bruno.backend.repository;

import com.bruno.backend.entity.CategoriaKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoriaKeywordRepository extends JpaRepository<CategoriaKeyword, Long> {

    @Query("SELECT ck FROM CategoriaKeyword ck JOIN FETCH ck.categoria")
    List<CategoriaKeyword> findAllWithCategoria();
}

