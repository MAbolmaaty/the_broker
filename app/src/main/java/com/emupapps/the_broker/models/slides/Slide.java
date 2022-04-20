package com.emupapps.the_broker.models.slides;

public class Slide {

    private String image;

    public Slide(String image) {
        this.image = image;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }
}
