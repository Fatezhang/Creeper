package com.demo.graphql.demographql.creeper.douban;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.demo.graphql.demographql.creeper.douban.DocumentUtil.cookieToMap;
import static com.demo.graphql.demographql.creeper.douban.DoubanCreeper.getMovieInfo;
import static org.assertj.core.api.Assertions.assertThat;

class DoubanCreeperTest {

    private static final String WATCHED_MOVIES_URL = "https://movie.douban.com/people/%s/collect?start=%dsort=time&rating=all&filter=all&mode=list";
    private static final String p1 = "79656032";
    private static final SimpleModule JAVA_TIME_MODULE = new JavaTimeModule().addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(JAVA_TIME_MODULE);
    @Test
    void test() throws Exception {
        var now = Map.of("date", LocalDate.parse("2021-08-20"));
        assertThat(MAPPER.writeValueAsString(now)).isEqualTo("{\"date\":\"2021-08-20\"}");
    }

    @Test
    void testFetch() throws Exception {
        var filename = "test-fetch-watched-movies.json";
        var movie = getMovieInfo("https://movie.douban.com/subject/26348103/?tag=%E7%88%B1%E6%83%85&from=gaia_video/explore/subject/1300741/", "name");
        var json = MAPPER.writeValueAsString(movie);
        var fileWriter = new FileWriter(filename);
        fileWriter.write(json);
        fileWriter.close();
    }

    @Test
    void testCookieToMap() {
        var cookieToMap = cookieToMap();
        assertThat(cookieToMap.get("bid")).isEqualTo("n5FEfo4if_8");
    }

    @Test
    void testLocalDateParse() {
        var a = "2019";
        var date = LocalDate.parse(a);
        System.out.println("date = " + date);
    }

    @Test
    void testProxyIp() throws IOException {
        var doc = Jsoup.connect("https://movie.douban.com/subject/26348103/?tag=%E7%88%B1%E6%83%85&from=gaia_video/explore/subject/1300741/")
                .proxy("182.34.18.112", 26365)
                .cookies(cookieToMap())
                .header("Referer", "https://www.douban.com/")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36 Edg/92.0.902.67")
                .timeout(50000).get();

        var a = DocumentUtil.extractMovieItemsInfo(doc, "国家");
    }
}
