package com.carrey.mydemo.guide.vectorguide;

/**
 * Created by angelo on 2015/6/17.
 */
public class DataConfig {
    private final int pageNum;
    private int[] backgroundColors;
    private int[] imageResources;
    private String[] titles;
    private String[] contents;
    private int[] backgroundResources;

    public DataConfig(int pageNum) {
        this.pageNum = pageNum;
        backgroundResources = new int[pageNum];
        backgroundColors = new int[pageNum];
        imageResources = new int[pageNum];
        titles = new String[pageNum];
        contents = new String[pageNum];
    }

    public int getPageNum() {
        return pageNum;
    }

    public int[] getBackgroundColors() {
        return backgroundColors;
    }

    public void setBackgroundColors(int[] backgroundColors) {
        if (backgroundColors.length < pageNum) {
            System.arraycopy(backgroundColors, 0, this.backgroundColors, 0, backgroundColors.length);
            return;
        }
        this.backgroundColors = backgroundColors;
    }

    public int[] getImageResources() {
        return imageResources;
    }

    public void setImageResources(int[] imageResources) {
        if (imageResources.length < pageNum) {
            System.arraycopy(imageResources, 0, this.imageResources, 0, imageResources.length);
            return;
        }
        this.imageResources = imageResources;
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        if (titles.length < pageNum) {
            System.arraycopy(titles, 0, this.titles, 0, titles.length);
            return;
        }
        this.titles = titles;
    }

    public String[] getContents() {
        return contents;
    }

    public void setContents(String[] contents) {
        if (contents.length < pageNum) {
            System.arraycopy(contents, 0, this.contents, 0, contents.length);
            return;
        }
        this.contents = contents;
    }

    public int[] getBackgroundResources() {
        return backgroundResources;
    }

    public void setBackgroundResources(int[] backgroundResources) {
        if (backgroundResources.length < pageNum) {
            System.arraycopy(backgroundResources, 0, this.backgroundResources, 0, backgroundResources.length);
            return;
        }
        this.backgroundResources = backgroundResources;
    }
}
