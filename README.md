# quarkengine
Android openGL ES 2.0  3D-Engine

Entity-Component-System based 3D-Engine for android running on openGL ES 2.0 or higher.

Currently in a very early stage, but entity-component-system architecture is almost done. It's already possible to render sprites :)
This project is focused on creating a highly customizable graphics engine with a super simple to use structure.


This is a complete example of rendering a fullscreen sprite:
```java
package xyz.sigsegowl.quark;

import android.content.Context;
import android.util.AttributeSet;

import xyz.sigsegowl.quarkengine.core.OpenGLSurfaceView;

public class CustomOpenGLSurfaceView extends OpenGLSurfaceView{
    public CustomOpenGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onInit(){
        renderer.spriteFactory.createSprite("bitmaps/default.png");
    }

    @Override
    public void onPreDrawFrame(){
        
    }

    @Override
    public void onPostDrawFrame(){
        
    }
}
```


# setup

Add the submodule
```
git submodule add https://github.com/AJ92/quarkengine.git
```

Add the module to your **build.gradle** (app) dependencies
```
implementation project(":quarkengine")
```

add the module to the include of **settings.gradle**
```
include ':app', ':quarkengine'
```

# example application

See https://github.com/AJ92/quarkexample for example usage in an android studio project.