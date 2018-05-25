uniform lowp mat4 u_MVPMatrix;
attribute lowp vec4 a_Position;
attribute lowp vec3 a_TexCoord;
attribute lowp vec3 a_Normal;
varying lowp vec3 texc;

void main()
{
    texc = a_TexCoord;
    gl_Position = u_MVPMatrix * a_Position;
}