package run.halo.toolbox.finder;

import reactor.core.publisher.Mono;
import run.halo.toolbox.entity.RecentlyPlayedGamesEntity;

public interface SteamFinder {
    Mono<RecentlyPlayedGamesEntity> get();
}
