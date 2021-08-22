package com.demo.graphql.demographql.creeper.douban;

import com.demo.graphql.demographql.creeper.douban.model.Movie;
import com.demo.graphql.demographql.creeper.douban.model.WatchedMovie;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.demo.graphql.demographql.creeper.douban.DocumentUtil.extractMovieItemsInfo;
import static com.demo.graphql.demographql.creeper.douban.DocumentUtil.getDocumentWithRetry;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Slf4j
public class DoubanCreeper {

    private static final String WATCHED_MOVIES_URL = "https://movie.douban.com/people/%s/collect?start=%d&sort=time&rating=all&filter=all&mode=list";
    private static final String PERSON_ID = "79656032";
    private static final SimpleModule JAVA_TIME_MODULE = new JavaTimeModule().addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(JAVA_TIME_MODULE);

    private static final ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(
            30,
            60,
            5,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(60)
    );


    public static ConcurrentLinkedQueue<WatchedMovie> downloadWatchedMoviesData() {
        var watchedMovieList = new ConcurrentLinkedQueue<WatchedMovie>();
        var pageStep = 30;
        var currentPageStart = 0;
        for (int i = 0; i < 61; i++) {
            final var urlPerPage = format(WATCHED_MOVIES_URL, PERSON_ID, currentPageStart);
            final var document = getDocumentWithRetry(urlPerPage);
            if (document == null) {
                continue;
            }
            final var title = document.title();
//            POOL_EXECUTOR.execute(() -> {

            var listViews = document.select(".list-view li");
            if (listViews.size() == 0) {
                break;
            }
            log.info("Getting 【 {} 】data from page-[{}], {} movies", title, i + 1, listViews.size());
            for (var itemView : listViews) {
                try {
                    var watchedMovie = getWatchedMovie(itemView);
                    watchedMovieList.add(watchedMovie);
//                    Thread.sleep(20000);
//                    log.info("Waited 20 seconds, added movie info for {}", watchedMovie.getMovie().getName());
                } catch (IOException e) {
                    log.info("Get watched movie failed..., error: {}", e.getLocalizedMessage());
                }
            }
//            });
//            try {
//                log.info("Finished to create thread to get data for page-{} ,waiting 5 seconds", i + 1);
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            currentPageStart += pageStep;
        }

        return watchedMovieList;
    }

    private static WatchedMovie getWatchedMovie(Element itemView) throws IOException {
        var itemTitle = itemView.select(".title a");
        var movieName = itemTitle.text();
        var movieUrl = itemTitle.attr("href");
        var movie = getMovieInfo(movieUrl, movieName);
        var rate = itemView.select(".date span")
                .attr("class")
                .replace("rating", "")
                .replace("-t", "");
        var watchDate = itemView.select(".date")
                .textNodes().stream()
                .map(TextNode::getWholeText)
                .filter(StringUtils::isNoneBlank)
                .map(LaunchDateUtil::extractDateFromString)
                .findFirst().orElse(null);
        var comment = itemView.select(".comment").text();
        return WatchedMovie.builder()
                .movie(movie)
                .comment(comment)
                .rate(StringUtils.isNoneBlank(rate) ? Integer.valueOf(rate) : null)
                .watchDate(watchDate)
                .build();
    }

    public static Movie getMovieInfo(String url, String name) {
        log.info("Getting movie info for [{}]", name);
        var document = getDocumentWithRetry(url);
        if (document == null) {
            return Movie.builder().build();
        }
        var rate = document.select(".rating_num").text().trim();
        var actors = document.select(".actor").select(".attrs").select("a").textNodes()
                .stream().map(textNode -> textNode.text().trim()).collect(toList());
        var categories = extractMovieItemsInfo(document, "类型");
        List<String> launchDateList = extractMovieItemsInfo(document, "首播");
        launchDateList.addAll(extractMovieItemsInfo(document, "上映"));
        var launchDate = launchDateList.stream()
                .map(LaunchDateUtil::extractDateFromString).findFirst().orElse(null);
        var region = extractMovieItemsInfo(document, "国家").stream().findFirst().orElse(null);
        var language = extractMovieItemsInfo(document, "语言").stream().findFirst().orElse(null);
        return Movie.builder()
                .url(url)
                .name(name)
                .rate(StringUtils.isNoneBlank(rate) ? Double.parseDouble(rate) : null)
                .actors(actors)
                .categories(categories)
                .launchDate(launchDate)
                .region(region)
                .language(language)
                .build();
    }

    public static void main(String[] args) {
        try {
            var map = downloadWatchedMoviesData();
            String json = MAPPER.writeValueAsString(map);
            String filename = PERSON_ID + "-watched-movies.json";
            var fileWriter = new FileWriter(filename);
            fileWriter.write(json);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
