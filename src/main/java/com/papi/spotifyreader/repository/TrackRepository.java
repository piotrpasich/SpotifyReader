package com.papi.spotifyreader.repository;
import com.papi.spotifyreader.domain.Track;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Track entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

}
