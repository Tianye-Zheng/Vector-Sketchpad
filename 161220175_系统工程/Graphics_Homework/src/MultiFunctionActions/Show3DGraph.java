package MultiFunctionActions;

import Coordinates.SpatialCoor;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Show3DGraph implements GLEventListener {

    private GLU glu = new GLU();
    private float rquad=0.0f;
    private String path;
    public Show3DGraph(String path) {
        this.path = path;
    }

    @Override
    public void display( GLAutoDrawable drawable ) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();  // 恢复初始坐标系
        gl.glTranslatef( 0f, 0f, -5.0f );         // 平移
        gl.glRotatef( rquad, 1.0f, 1.0f, 1.0f ); // 旋转

        // 开始处理文件
        boolean success = true;
        try {
            FileReader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader);
            br.readLine(); // 略去第一行的OFF
            String[] temp = br.readLine().split(" ");
            int vertexNum = Integer.parseInt(temp[0]);
            int faceNum = Integer.parseInt(temp[1]);
            // 顶点表
            List<SpatialCoor> vertex = new ArrayList<>();
            for (int i = 0; i < vertexNum; i++) {
                temp = br.readLine().split(" ");
                vertex.add(new SpatialCoor(Float.parseFloat(temp[0]),
                        Float.parseFloat(temp[1]),Float.parseFloat(temp[2])));
            }
            gl.glBegin( GL2.GL_TRIANGLES ); // 开始绘制
            float r = 0.0f, g = 0.0f, b = 0.0f;
            gl.glColor3f( r,g,b );
            // 读取面表，开始绘图
            for (int i = 0; i < faceNum; i++) {
                temp = br.readLine().split(" "); // temp[0] 为 '3'
                int index1 = Integer.parseInt(temp[1]);
                int index2 = Integer.parseInt(temp[2]);
                int index3 = Integer.parseInt(temp[3]);
                gl.glVertex3f(vertex.get(index1).x, vertex.get(index1).y, vertex.get(index1).z);
                gl.glVertex3f(vertex.get(index2).x, vertex.get(index2).y, vertex.get(index2).z);
                gl.glVertex3f(vertex.get(index3).x, vertex.get(index3).y, vertex.get(index3).z);
                // 变一个颜色
                if(Math.abs(b - 1.0f) < 1e-5) {
                    b = 0.0f;
                    if(Math.abs(g - 1.0f)<1e-5) {
                        g = 0.0f;
                        if(Math.abs(r - 1.0f)<1e-5) {
                            r = 0.0f;
                        } else {
                            r += 0.1f;
                        }
                    } else {
                        g += 0.1f;
                    }
                }
                else {
                    b += 0.1f;
                }
                gl.glColor3f( r,g,b );
            }

            gl.glEnd();
            gl.glFlush();
            rquad -=0.15f;

            br.close();
            reader.close();
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    @Override
    public void dispose( GLAutoDrawable drawable ) {}
    @Override
    public void init( GLAutoDrawable drawable ) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor( 1.0f, 1.0f, 1.0f, 0f ); // 设置颜色缓存的清除值
        gl.glClearDepth( 1.0f );       // 深度缓存进行初始化
        gl.glEnable( GL2.GL_DEPTH_TEST ); // 启用各种功能
        gl.glDepthFunc( GL2.GL_LEQUAL );  // 指定用于深度缓冲的比较值
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
    }
    @Override
    public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height ) {
        final GL2 gl = drawable.getGL().getGL2();
        if( height <=0 )
            height =1;
        final float h = ( float ) width / ( float ) height;
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        glu.gluPerspective( 45.0f, h, 1.0, 20.0 );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }
}