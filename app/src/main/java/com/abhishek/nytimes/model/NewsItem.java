package com.abhishek.nytimes.model;

import com.google.gson.annotations.SerializedName;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsItem {

    private static SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.US);
    private static SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, hh:mm a", Locale.US);

    private String formattedDate;
    @SerializedName("web_url")
    private String url;

    @SerializedName("snippet")
    private String summary;

    private List<MultiMedia> multimedia;

    private Headline headline;

    @SerializedName("pub_date")
    private String publicationDate;

    @SerializedName("byline")
    private Credit credit;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<MultiMedia> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<MultiMedia> multimedia) {
        this.multimedia = multimedia;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public String getPublicationDate() {
        if (formattedDate == null) {
            if (publicationDate != null) {
                Date date = parser.parse(publicationDate, new ParsePosition(0));
                if (date != null)
                    formattedDate = formatter.format(date);
            }
        }
        return formattedDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public String getMediaUri() {
        if (getMultimedia().isEmpty())
            return null;
        else
            return "https://static01.nyt.com/" + getMultimedia().get(0).getSubUrl();
    }
}
