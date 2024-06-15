package pl.lotto.cache.redis;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.lotto.BaseIntegrationTest;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RedisTicketCacheIntegrationTest extends BaseIntegrationTest {

    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    NumberReceiverFacade numberReceiverFacade;

    @Autowired
    CacheManager cacheManager;

//    @BeforeEach
//    public void clearCacheBeforeTest() {
//        Cache cache = cacheManager.getCache("jobOffers");
//        if (cache != null) {
//            cache.clear();
//        }
//    }
    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    public void should_save_ticket_to_cache_and_then_invalidate_by_time_to_live() throws Exception {
        // step 1: user input numbers
        // given & when
        ResultActions registerAction = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": [1,2,3,4,5,6]
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        registerAction.andExpect(status().isOk());
        MvcResult mvcResult = registerAction.andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        NumberReceiverResponseDto numberReceiverResponseDto = objectMapper.readValue(json, NumberReceiverResponseDto.class);
        String ticketId = numberReceiverResponseDto.ticketDto().ticketId();

        // step 2: user wants to see his ticket and system should save ticket to cache
        // given && when
        mockMvc.perform(get("/info/" + ticketId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        verify(numberReceiverFacade, times(1)).retrieveTicketByTicketId(ticketId);
        assertThat(cacheManager.getCacheNames().contains("ticketRepository")).isTrue();

        // step 4: cache should be invalidated
        // given && when && then
        await()
                .atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                            mockMvc.perform(get("/info/" + ticketId)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                            );
                            verify(numberReceiverFacade, atLeast(2)).retrieveTicketByTicketId(ticketId);
                        }
                );

    }

}
