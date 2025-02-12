package run.halo.toolbox.finder.impl;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ReactiveSettingFetcher;
import run.halo.app.theme.finders.Finder;
import run.halo.toolbox.config.SteamSettingConfig;
import run.halo.toolbox.entity.RecentlyPlayedGamesEntity;
import run.halo.toolbox.finder.SteamFinder;
import run.halo.toolbox.utils.SteamUtils;

@Finder("pluginToolBoxFinder")
@AllArgsConstructor
public class SteamFinderImpl implements SteamFinder {
    private final ReactiveSettingFetcher settingFetcher;
    private static final WebClient webClient = WebClient.builder().baseUrl("http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/").build();


    @Override
    public Mono<RecentlyPlayedGamesEntity> get() {

        return settingFetcher.get(SteamSettingConfig.GROUP).flatMap(setting -> {
            String key = setting.get(SteamSettingConfig.TOKEN).asText();
            String id = setting.get(SteamSettingConfig.STEAM_ID).asText();

            String url = String.format("?key=%s&steamid=%s",key,id);
            return webClient.get().uri(url).retrieve().bodyToMono(RecentlyPlayedGamesEntity.class).flatMap(
                entity -> {
                    List<RecentlyPlayedGamesEntity.GamesItem> updatedGames  =  entity.getResponse().getGames().stream().peek(
                        item ->{
                            //获取对应游戏的缩略图
                            item.setImg_icon_url(SteamUtils.steamImage(item.getAppid(),item.getImg_icon_url()));
                        }).toList();
                    entity.getResponse().setGames(updatedGames);
                    return Mono.just(entity);
                }
            );
        });
    }
}
