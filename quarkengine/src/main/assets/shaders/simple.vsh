uniform lowp mat4 u_MVPMatrix;
attribute lowp vec4 a_Position;
attribute lowp vec3 a_TexCoord;
attribute lowp vec3 a_Normal;
varying lowp vec3 texc;
//simple normal shading...
varying lowp float dir;
void main()
{
	texc = a_TexCoord;
	dir = max(dot(a_Normal,vec3(1.0,1.0,1.0)),0.0);
   	gl_Position = u_MVPMatrix * a_Position;
}