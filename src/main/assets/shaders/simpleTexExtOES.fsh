#extension GL_OES_EGL_image_external : require

precision mediump float;
uniform samplerExternalOES tex_sampler;
varying lowp vec3 texc;

void main ()
{
    vec4 cameraColor = texture2D(tex_sampler, texc.st);
    gl_FragColor = cameraColor;
}