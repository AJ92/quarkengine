package xyz.sigsegowl.quarkengine.components;

import xyz.sigsegowl.quarkengine.components.gfx.ITexture;
import xyz.sigsegowl.quarkengine.components.gfx.Shader;
import xyz.sigsegowl.quarkengine.components.gfx.Texture2D;

/**
 * Sprite is a composition of gfx components
 * Contains a Texture2D and Shader Component
 * In future it might contains multiple Textures
 * for animations and or multi-texture-shaders
 * Created by AJ on 02.07.2016.
 */
public class Sprite extends Component {
    private Component _shader   = null;
    private Component _texture  = null;
    private boolean _visible = true;

    public Sprite(Component shader, Component texture){
        if(!(shader instanceof Shader)){
            return;
        }
        if(!(texture instanceof ITexture)){
            return;
        }
        _shader = shader;
        _texture = texture;
    }

    public Component getShader(){
        return _shader;
    }

    public Component getTexture(){
        return _texture;
    }

    public void setShader(Component shader){
        if(shader == null){
            return;
        }
        if(!(shader instanceof Shader)){
            return;
        }
        _shader = shader;
    }
    public void setTexture(Component texture){
        if(texture == null){
            return;
        }
        if(!(texture instanceof ITexture)){
            return;
        }
        _texture = texture;
    }

    public void setVisibile(boolean visibile){
        _visible = visibile;
    }

    public boolean isVisible(){
        return _visible;
    }

}
