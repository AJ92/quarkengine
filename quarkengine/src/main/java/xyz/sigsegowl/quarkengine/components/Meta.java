package xyz.sigsegowl.quarkengine.components;

/**
 * Created by AJ on 25.06.2016.
 */
public class Meta extends Component{

    private String _name = "";

    public Meta(){
        //convenience default constructor
    }

    public Meta(String name){
        _name = name;
    }

    public String getName(){
        return _name;
    }

    public void setName(String name){
        _name = name;
    }
}
