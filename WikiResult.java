package coding_club_java_course.utility;

public class WikiResult {
    public WikiResult() {

    }

    public WikiResult(String query, String text_result, String image_url) {
        this.query = query;
        this.text_result = text_result;
        this.image_url = image_url;
    }

    private String query;
    private String text_result;
    private String image_url;
}
