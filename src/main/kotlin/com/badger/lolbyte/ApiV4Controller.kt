package com.badger.lolbyte

import com.badger.lolbyte.client.OriannaClient
import com.badger.lolbyte.config.RiotProperties
import com.badger.lolbyte.current.CurrentGameHandler
import com.badger.lolbyte.current.CurrentGameResponse
import com.badger.lolbyte.match.MatchHandler
import com.badger.lolbyte.match.MatchResponse
import com.badger.lolbyte.notification.NotificationHandler
import com.badger.lolbyte.notification.NotificationResponse
import com.badger.lolbyte.rank.RanksHandler
import com.badger.lolbyte.rank.RanksResponse
import com.badger.lolbyte.recent.RecentGamesHandler
import com.badger.lolbyte.recent.RecentGamesResponse
import com.badger.lolbyte.statistics.StatisticsHandler
import com.badger.lolbyte.statistics.StatisticsResponse
import com.badger.lolbyte.summoner.SummonerHandler
import com.badger.lolbyte.summoner.SummonerResponse
import com.badger.lolbyte.utils.Region
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class BadRequestException(msg: String) : RuntimeException(msg)

@ResponseStatus(code = HttpStatus.NOT_FOUND)
object NotFoundException : RuntimeException()

@RestController
@RequestMapping("api/v4")
class ApiV4Controller(
    @RequestParam(name = "region") region: String?,
    riotProperties: RiotProperties
) {
    private val client = OriannaClient(Region.fromString(region), riotProperties.apiKey)

    @GetMapping("/notifications")
    fun getNotification(): NotificationResponse {
        return NotificationHandler.getNotification()
    }

    @GetMapping("/summoners/{name}")
    fun getSummoner(
        @PathVariable name: String
    ): SummonerResponse {
        return SummonerHandler(client).getSummoner(name)
    }

    @GetMapping("/recentGames/{id}")
    fun getRecentGames(
        @PathVariable id: String,
        @RequestParam(name = "limit", required = false) limit: Int?,
        @RequestParam(name = "queueId", required = false) queueId: Int?,
    ): RecentGamesResponse {
        return RecentGamesHandler(client).getRecentGames(id, limit, queueId)
    }

    @GetMapping("/statistics/{id}")
    fun getStatistics(
        @PathVariable id: String,
        @RequestParam(name = "limit", required = false) limit: Int?,
        @RequestParam(name = "queueId", required = false) queueId: Int?,
    ): StatisticsResponse {
        return StatisticsHandler(client).getStatistics(id, limit, queueId)
    }

    @GetMapping("/ranks/{id}")
    fun getRanks(
        @PathVariable id: String,
    ): RanksResponse {
        return RanksHandler(client).getRanks(id)
    }

    @GetMapping("/current/{id}")
    fun getCurrentGame(
        @PathVariable id: String,
    ): CurrentGameResponse {
        return CurrentGameHandler(client).getCurrentGame(id)
    }

    @GetMapping("/matches/{id}")
    fun getMatch(
        @PathVariable id: Long,
        @RequestParam(name = "summonerId") summonerId: String
    ): MatchResponse {
        return MatchHandler(client).getMatch(id, summonerId)
    }
}
