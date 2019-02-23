package com.example.zhihuapplication;

public class ShortNewsData {//data使用map键表示 map中使用data作为键得到一个Shortnewsdata list
    private String title;
    private String id;
    private String images;
    private String date;


    public String getDate() {
        return date;
    }


    public String printMessage(){
        return title+id+images;
    }
    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getImages() {
        return images;
    }

    public ShortNewsData(String title, String id, String images, String date){
            this.title = title;
            this.id = id;
            this.images = images;
            this.date = date;
    }
    public ShortNewsData(String date){
        this.date = date;
    }
}
