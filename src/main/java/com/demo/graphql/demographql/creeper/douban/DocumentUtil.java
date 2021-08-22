package com.demo.graphql.demographql.creeper.douban;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

import java.util.*;

@Slf4j
public class DocumentUtil {

    public static Map<String, String> cookieToMap() {
        var cookie = "ll=\"118371\"; bid=n5FEfo4if_8; __utmv=30149280.13159; __yadk_uid=PpaqR7q0VLO6VDsiGdCcDdwA75L6mDru; _vwo_uuid_v2=D29D6B3219F20BC95694F8E988BCD67A0|df9fce0ae9291b034166048d15d1d6b2; douban-fav-remind=1; __utmz=30149280.1625237670.17.8.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; dbcl2=\"131598921:BwAyL4QB9VA\"; push_noty_num=0; push_doumail_num=0; ct=y; ck=81hQ; ap_v=0,6.0; __utma=30149280.599677466.1619273639.1629425379.1629465481.27; __utmc=30149280; __utmb=30149280.4.10.1629465481; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1629466849%2C%22https%3A%2F%2Fwww.douban.com%2Fpeople%2F79656032%2F%22%5D; _pk_ses.100001.4cf6=*; __utma=223695111.661040828.1619273719.1629425379.1629466849.19; __utmb=223695111.0.10.1629466849; __utmc=223695111; __utmz=223695111.1629466849.19.12.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/people/79656032/; _pk_id.100001.4cf6=16acddf137460320.1619273719.21.1629468653.1629427182.";
        return Arrays.stream(cookie.split(";")).map((str) -> {
            var ss = str.trim().split("=");
            return Map.of(ss[0], ss[1]);
        }).reduce(new HashMap<>(), (mapA, mapB) -> {
            mapA.putAll(mapB);
            return mapA;
        });
    }

    public static Document getDocumentWithRetry(String url) {
        Document document = null;
        var retryTimes = 0;
        while (true) {
            if (retryTimes > 2) {
                break;
            }
            try {
                Thread.sleep(5000);
                log.info("Waited 5s to get document from {}", url);
                document = Jsoup.connect(url)
                        .cookies(cookieToMap())
                        .header("Referer", "https://www.douban.com/")
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36 Edg/92.0.902.67")
                        .timeout(50000).get();
                break;
            } catch (Exception e) {
                log.info("Call url[{}] got exception, skip it..., error:[{}]", url, e.getLocalizedMessage());
                retryTimes++;
            }
        }
        return document;
    }

    public static List<String> extractMovieItemsInfo(Document document, String itemName) {
        var optional = document.select(".pl").stream()
                .filter(span -> span.text().contains(itemName))
                .findFirst();
        var list = new ArrayList<String>();
        if (optional.isPresent()) {
            var itemSpan = optional.get();
            if (!itemName.contains("国家") && !itemName.contains("语言")) {
                while (true) {
                    var nextElement = itemSpan.nextElementSibling();
                    if (nextElement == null) {
                        break;
                    }
                    var tagName = nextElement.tagName();
                    if (tagName.equals("br")) {
                        break;
                    }
                    var text = nextElement.text().trim();
                    if (StringUtils.isNoneBlank(text) && !"/".equals(text)) {
                        list.add(text);
                    }
                    itemSpan = nextElement;
                }
                return list;
            }
            var text = Optional.ofNullable(((TextNode) itemSpan.nextSibling()))
                    .map(TextNode::getWholeText).orElse("").trim();
            list.add(text);
        }
        return list;
    }
}
