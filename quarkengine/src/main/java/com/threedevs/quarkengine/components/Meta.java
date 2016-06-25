package com.threedevs.quarkengine.components;

/**
 * Created by AJ on 25.06.2016.
 */
public class Meta extends Component{

    private String name = "";

    public Meta(){
        //convenience default constructor
    }

    public Meta(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
