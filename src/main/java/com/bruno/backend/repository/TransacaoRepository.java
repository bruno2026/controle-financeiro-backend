package com.bruno.backend.repository;

import com.bruno.backend.entity.Transacao;
import com.bruno.backend.enums.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("SELECT t FROM Transacao t LEFT JOIN FETCH t.categoria WHERE t.id = :id")
    Optional<Transacao> findByIdWithCategoria(@Param("id") Long id);

    List<Transacao> findByTipo(TipoTransacao tipo);

    @Query("""
        SELECT t FROM Transacao t
        LEFT JOIN FETCH t.categoria
        WHERE YEAR(t.data) = :ano AND MONTH(t.data) = :mes
        ORDER BY t.data DESC
    """)
    List<Transacao> findByAnoMes(@Param("ano") int ano, @Param("mes") int mes);

    @Query("""
        SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t
        WHERE t.tipo = :tipo
        AND YEAR(t.data) = :ano AND MONTH(t.data) = :mes
    """)
    BigDecimal sumByTipoAndAnoMes(@Param("tipo") TipoTransacao tipo,
                                  @Param("ano") int ano,
                                  @Param("mes") int mes);
}

