package com.demo.graphql.demographql.creeper.douban;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class DoubanCreeper {

    private static final String WATCHED_MOVIES_URL = "https://movie.douban.com/people/%s/collect?start=%dsort=time&rating=all&filter=all&mode=list";

    private static final String p1 = "79656032";

    public static void downloadWatchedMoviesData() throws IOException {
        var connect = Jsoup.connect(String.format(WATCHED_MOVIES_URL, p1, 0));
        var document = connect.get();
        var title = document.title();
        System.out.println("title = " + title);

        var listViews = document.select(".list-view li");
        for (Element itemView : listViews) {

            var itemTitle = itemView.select(".title a");
            var movieName = itemTitle.text();
            var movieUrl = itemTitle.attr("href");

            var rate = itemView.select(".date");

        }
    }

    public static void main(String[] args) throws Exception {
        downloadWatchedMoviesData();
    }
}
