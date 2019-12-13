package com.papi.spotifyreader.repository;
import com.papi.spotifyreader.domain.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Playlist entity.
 */
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    @Query(value = "select distinct playlist from Playlist playlist left join fetch playlist.tracks",
        countQuery = "select count(distinct playlist) from Playlist playlist")
    Page<Playlist> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct playlist from Playlist playlist left join fetch playlist.tracks")
    List<Playlist> findAllWithEagerRelationships();

    @Query("select playlist from Playlist playlist left join fetch playlist.tracks where playlist.id =:id")
    Optional<Playlist> findOneWithEagerRelationships(@Param("id") Long id);

}
