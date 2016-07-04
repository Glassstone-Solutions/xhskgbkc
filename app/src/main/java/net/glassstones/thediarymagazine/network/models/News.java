package net.glassstones.thediarymagazine.network.models;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Thompson on 28/06/2016.
 * For The Diary Magazine
 */
public class News {
    String title, date;
    Element firstParagraph;
    Elements otherParagraphs;

    public News title (String title) {
        this.title = title;
        return this;
    }

    public News date (String date) {
        this.date = date;
        return this;
    }

    public News firstParagraph (Element firstParagraph) {
        this.firstParagraph = firstParagraph;
        return this;
    }

    public News otherParagraphs (Elements otherParagraphs) {
        this.otherParagraphs = otherParagraphs;
        return this;
    }

    public String getTitle () {
        return title;
    }

    public String getDate () {
        return date;
    }

    public Element getFirstParagraph () {
        return firstParagraph;
    }

    public Elements getOtherParagraphs () {
        return otherParagraphs;
    }
}
