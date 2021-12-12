package xyz.sigsegowl.quarkengine.components.gfx;

import android.opengl.GLES20;
import android.util.Log;

public class FrameBuffer {

    private String TAG = "FrameBuffer";

    private int fboId = 0;
    private int renderBufferId = 0;
    private ITexture color;
    private int size = 0;

    /**
     * Creates a framebuffer with colorAttachment as colorAttachment attachment 0
     * colorAttachment should be texture2D
     * framebuffer will use same size as colorAttachment
     * @param colorAttachment
     */
    public FrameBuffer(ITexture colorAttachment){
        if(colorAttachment.width == 0 || colorAttachment.height == 0 || colorAttachment._textureID == 0){
            Log.e(TAG, "FrameBuffer can not be created...");
            return;
        }

        color = colorAttachment;
        color.bind();

        int[] temp = new int[1];

        GLES20.glGenFramebuffers(1, temp, 0);
        fboId = temp[0];

        GLES20.glGenRenderbuffers(1, temp, 0);
        renderBufferId = temp[0];

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);

        //Bind render buffer and define buffer dimension
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBufferId);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, color.width, color.height);
        //Attach texture FBO colorAttachment attachment
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, color._textureID, 0);
        //Attach render buffer to depth attachment
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, renderBufferId);
        //we are done, reset
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public FrameBuffer(int width, int height){
        if(width <= 0 || height <= 0){
            Log.e(TAG, "FrameBuffer can not be created...");
            return;
        }

        color = new ITexture();
        color.genGlTexture();
        color.bind();
        color.loadGlTextureRgbaEmpty(width, height);
        color.setFilterSmooth();

        int[] temp = new int[1];

        GLES20.glGenFramebuffers(1, temp, 0);
        fboId = temp[0];

        GLES20.glGenRenderbuffers(1, temp, 0);
        renderBufferId = temp[0];

        bindFrameBuffer();

        //Bind render buffer and define buffer dimension
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBufferId);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);
        //Attach texture FBO color attachment
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, color._textureID, 0);
        //Attach render buffer to depth attachment
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, renderBufferId);
        //we are done, reset
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public void bindFrameBuffer(){
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glViewport(0, 0, color.width, color.height);
    }

    public void unbindFrameBuffer(){
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public void bindColorTexture(){
        color.bind();
    }

    public void bindFrameBufferTexture(){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboId);
    }

    public void clear(){
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    }

    public ITexture getTexture() {
        return color;
    }
}
