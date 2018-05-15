package xyz.sigsegowl.quarkengine.components;

import xyz.sigsegowl.quarkengine.components.gfx.Shader;
import xyz.sigsegowl.quarkengine.components.gfx.Texture;

/**
 * Sprite is a composition of gfx components
 * Contains a Texture and Shader Component
 * In future it might contains multiple Textures
 * for animations and or multi-texture-shaders
 * Created by AJ on 02.07.2016.
 */
public class Sprite extends Component {
    private Component _shader   = null;
    private Component _texture  = null;

    public Sprite(Component shader, Component texture){
        if(shader.getClass() != Shader.class){
            return;
        }
        if(texture.getClass() != Texture.class){
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
        if(shader.getClass() != Shader.class){
            return;
        }
        _shader = shader;
    }
    public void setTexture(Component texture){
        if(texture == null){
            return;
        }
        if(texture.getClass() != Texture.class){
            return;
        }
        _texture = texture;
    }

}
