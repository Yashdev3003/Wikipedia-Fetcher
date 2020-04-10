package coding_club_java_course.utility;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Scanner;

public class WikipediaFetcher implements Runnable{
    private String query;
    public WikipediaFetcher(String query) {
        this.query=query;
    }

    private String fetch() throws Exception {
        String url=getWikipediaUrl();
        String html=HttpURLConnectionExample.sendGet(url);
        return html;
    }

    private String getWikipediaUrl() {
        String clean_keyword=query.replaceAll("[ ]+",  "_");
        return "https://en.wikipedia.org/wiki/"+clean_keyword;
    }

    public WikiResult getResult() throws Exception {
        String html=fetch();
        String result="";
        WikiResult wikiResult=null;
        if(html!=null && html.length()>0) {
            Document document= Jsoup.parse(html, "https://en.wikipedia.org/wiki/");
            Elements paragraphs=document.body().select(".mw-parser-output > *");
            int c=0;
            for(Element element: paragraphs) {
                if(element.tagName().equals("table") && c==0) {
                    c=1;
                }
                if(element.className().equals("mw-empty-elt")) {
                    continue;
                } else if(c==1) {
                    if(element.tagName().equals("p")) {
                        result = element.text();
                        String image_url = null;
                        try {
                            image_url = document.body().select(".infobox img").get(0).attr("src");
                        } catch (Exception e) {

                        }
                        wikiResult = new WikiResult(query, result, image_url);
                        break;
                    }
                }
            }
        }
        return wikiResult;
    }

    public static void main(String[] args) throws Exception {
        System.out.print("Enter Your Query: ");
        Scanner scan=new Scanner(System.in);
        String query=scan.nextLine();
        TaskManager taskManager=new TaskManager(100);
        WikipediaFetcher wiki=new WikipediaFetcher(query);
        taskManager.waitTillIsFreeAndAddTask(wiki);
    }

    @Override
    public void run() {
        WikiResult wikiResult= null;
        try {
            wikiResult = getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(new Gson().toJson(wikiResult));
    }
}
