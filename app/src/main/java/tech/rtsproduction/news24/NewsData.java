package tech.rtsproduction.news24;

public class NewsData {

    private String mTitle;
    private String mSection;
    private String mURL;
    private String mDate;
    private String mAuthorName;

    public NewsData() {
        //Empty Constructor
    }

    public NewsData(String mTitle, String mSection, String mURL, String mDate,String mAuthorName) {
        this.mTitle = mTitle;
        this.mSection = mSection;
        this.mURL = mURL;
        this.mDate = mDate;
        this.mAuthorName = mAuthorName;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmURL() {
        return mURL;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmAuthorName() {
        return mAuthorName;
    }
}
