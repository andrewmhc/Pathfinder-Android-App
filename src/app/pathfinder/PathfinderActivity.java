package app.pathfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
public class PathfinderActivity extends Activity{
        private static final int[]dy={0,1,1,1,0,-1,-1,-1};//@formatter:off
        final static String[]data={//TODO add more levels (data and styles); see DIRS and first switch statement

                "b    "+
                "   b "+
                " 7   "+
                "     "+
                "1    "+
                "k   o"+
                "     "+
                " o   "+
                "  o  "+
                "o    ",
                
                "     "+
                " 6 b "+
                "     "+
                "    c"+
                "1    "+
                "k3   "+
                "     "+
                "     "+
                "     "+
                "     ",
                
                ",    "+
                "o    "+
                "b    "+
                "     "+
                "   70"+
                "     "+
                "     "+
                "     "+
                "     "+
                "     ",


                "          "+
                "          "+
                "          "+
                "          "+
                " b  7     "+
                "  ,   b   "+
                "          "+
                "       0  "+
                "          "+
                "     2    "+
                "          "+
                "          "+
                "          "+
                "          ",
                
                "   oo     "+
                "  b 1 3   "+
                "  o   o   "+
                "          "+
                "   77     "+
                "  5oo  o  "+
                "       b  "+
                "          "+
                "   b      "+
                "     1 c  "+
                "          "+
                " b o   o  "+
                "       b  "+
                " o  5   o "+
                "k   3   b "+
                "          "+
                "          "+
                "          "+
                "          "+
                "          ",
                
                "k     6   "+
                "oooooo ooo"+
                "     o o  "+
                "  3  o o  "+
                " b3   1o  "+
                "     o o  "+
                "  oo      "+
                "   o      "+
                "          "+
                "  oo      "+
                "  co    o "+
                "          "+
                "   7      "+
                "          "+
                "  ooo     "+
                "   b      "+
                "          "+
                "          "+
                "          "+
                "          ",
                
                "         5 b  "+
                "   b     b    "+
                "k7b  ooo  3   "+
                "  1           "+
                "   o b   b o  "+
                "       1      "+
                "   1 b o  6   "+
                " o     b    b "+
                " o            "+
                "       o      "+
                "              "+
                "  o       o   "+
                "    o o       "+
                "     o o o    "+
                "              "+
                "          o   "+
                "              "+
                "              "+
                "              "+
                "  o    o   o  ",
                

                "      m   "+
                "    7 5   "+
                "    o b   "+
                "c    o  b "+
                "o    o    "+
                "  3 7 7   "+
                "   o b    "+
                "b  7 o    "+
                "    3   1 "+
                "     o    "+
                "     3   b"+
                " b3       "+
                "          "+
                "          "+
                " o o o o o"+
                "o o o o o "+
                "          "+
                "          "+
                "          "+
                "          ",
        	
            "              "+
            "   oo    oo   "+
            "       o      "+
            "ooo         oo"+
            "              "+
            "     ob       "+
            "    6o   6 b  "+
            " o2  2  ,     "+
            "ob    4    6  "+
            "  co  2  7b   "+
            " 46o  o       "+
            "  b b   0     "+
            "          7   "+
            "              "+
            " o o o o o o o"+
            "              "+
            "              "+
            "              "+
            "              "+
            "              "

                
                
        };//@formatter:on
        private final static int[][]styles=new int[][]{//width,height,line,light (store width for verification)
                {5,10,Color.BLACK,Color.RED},
                {5,10,Color.BLACK,0xffff7f00},
                {5,10,Color.BLACK,Color.YELLOW},
                {10,14,Color.BLACK,Color.GREEN},
                {10,20,Color.BLACK,Color.BLUE},
                {10,20,Color.BLACK,0xffff00ff},
            {14,20,Color.BLACK,Color.RED},
                {10,20,Color.BLACK,0xffff7f00},
            {14,20,Color.BLACK,Color.RED},

        };
        private final static String DIRS="k,mnhyui";
        @Override public void onCreate(Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
                setContentView(new View(this){
                        float[]pts=new float[0];
                        final Paint line=new Paint(),light=new Paint();
                        char[][]map;
                        int w=0,h=0,sx,sy,ax,ay,stops,lvl=0,ad;
                        MotionEvent move;
                        private float d,iy,ix;
                        private final AlertDialog.Builder ADB=new AlertDialog.Builder(PathfinderActivity.this);
                        final Drawable mirror=getResources().getDrawable(R.drawable.mirror),
                                source=getResources().getDrawable(R.drawable.laser),
                                target=getResources().getDrawable(R.drawable.window),
                                destination=getResources().getDrawable(R.drawable.tintedwindow),
                                obstacle=getResources().getDrawable(R.drawable.rock);
                        {init();}
                        public void init(){
                                if(lvl<data.length){
                                        sx=-1;sy=-1;ax=-1;ay=-1;stops=0;
                                        line.setColor(styles[lvl][2]);
                                        light.setColor(styles[lvl][3]);
                                        map=new char[styles[lvl][0]][styles[lvl][1]];
                        assert data.length==styles.length&&data[lvl].length()==styles[lvl][0]*styles[lvl][1];
                        for(int i=0;i<map.length;++i)
                                for(int j=0;j<map[i].length;++j){
                                        final int idx=DIRS.indexOf(map[i][j]=data[lvl].charAt(i+j*map.length));
                                        if(idx>=0){
                                                assert ax<0&&ay<0;
                                                ad=idx;
                                                map[ax=i][ay=j]='a';
                                        }else if(map[i][j]=='b'||map[i][j]=='c')
                                                ++stops;
                                }
                        w=h=-1;
                        invalidate();
                                }else finish();
                        }
                        @Override protected void onDraw(Canvas c){
                                final int newW=getWidth(),newH=getHeight();
                                c.drawColor(Color.WHITE);
                                if(lvl>=map.length)
                                        return;
                                if(newW!=w||newH!=h){
                                        final float rw=(w=newW)/styles[lvl][0],rh=(h=newH)/styles[lvl][1];
                                        ix=w*(1-(d=Math.min(rw,rh))/rw)/2;
                                        iy=h*(1-d/rh)/2;
                                        pts=new float[styles[lvl][0]+styles[lvl][1]-2<<2];
                                        for(int i=1;i<styles[lvl][0];++i){
                                                pts[(i-1<<2)|2]=pts[i-1<<2]=ix+i*d;
                                                pts[(i-1<<2)|3]=(pts[(i-1<<2)|1]=iy)+styles[lvl][1]*d;
                                        }
                                        for(int i=1;i<styles[lvl][1];++i){
                                                pts[(styles[lvl][0]+i-2<<2)|3]=pts[(styles[lvl][0]+i-2<<2)|1]=iy+i*d;
                                                pts[(styles[lvl][0]+i-2<<2)|2]=(pts[styles[lvl][0]+i-2<<2]=ix)+styles[lvl][0]*d;
                                        }
                                }
                                c.drawLines(pts,line);
                                for(int i=0;i<styles[lvl][0];++i)
                                        for(int j=0;j<styles[lvl][1];++j){
                                                c.save();
                                                if(i==sx&&j==sy)
                                                        c.translate(move.getX(),move.getY());
                                                else c.translate(ix+(i+.5f)*d,iy+(j+.5f)*d);
                                                Drawable dr=null;
                                                switch(map[i][j]){
                                                        case 'a':c.rotate(45f*ad);
                                                        case 'b':case 'c':
                                                                dr=new Drawable[]{source,target,destination}[map[i][j]-'a'];
                                                        case ' ':break;
                                                        case 'o':dr=obstacle;break;
                                                        default:
                                                                c.rotate(45f*(map[i][j]-'0'));
                                                                dr=mirror;
                                                }
                                                if(dr!=null){
                                                        dr.setBounds(-1,-1,1,1);
                                                        c.scale(d/2,d/2);
                                                        dr.draw(c);
                                                }
                                                c.restore();
                                        }
                                boolean dirty[][][]=new boolean[styles[lvl][0]][styles[lvl][1]][5];
                                for(int i=0;i<styles[lvl][0];++i)
                                        for(int j=0;j<styles[lvl][1];++j)
                                                for(int k=0;k<5;++k)
                                                        dirty[i][j][k]=true;
                                int hits=stops;
                                for(int cx=ax,cy=ay,di=ad;cx>=0&&cx<styles[lvl][0]&&cy>=0&&cy<styles[lvl][1]&&dirty[cx][cy][di>>1]&&map[cx][cy]<='c';){
                                        dirty[cx][cy][di>>1]=false;
                                        final int n=map[cx][cy]-'0';
                                        if(n>'a'-'0'&&dirty[cx][cy][4]){
                                                --hits;
                                                dirty[cx][cy][4]=false;
                                        }
                                        if(n=='c'-'0')break;
                                        if(n>=0&&n<=7&&((di-n)*(di-n)<2||(di-n)*(di-n)>48))
                                                di=(n*2-di+12)&7;
                                        c.drawLine(ix+(cx+.5f)*d,iy+(cy+.5f)*d,ix+((cx=cx+dy[(di+2)&7])+.5f)*d,iy+((cy=cy+dy[di])+.5f)*d,light);
                                }
                                if(hits==0&&move==null)//correct and let go
                                        ADB.setMessage("You beat level "+ ++lvl + "!").show().setOnDismissListener(new OnDismissListener(){
                                                public void onDismiss(DialogInterface dialog){init();}});
                        }
                        @Override public boolean onTouchEvent(MotionEvent e){
                                final int x=Math.min(map.length-1,Math.max(0,(int)((e.getX()-ix)/d))),
                                        y=Math.min(map[x].length-1,Math.max(0,(int)((e.getY()-iy)/d)));
                                switch(e.getAction()){
                                        case MotionEvent.ACTION_DOWN:
                                                if(move!=null||map[x][y]<'0'||map[x][y]>'7')
                                                        return false;
                                                move=e;
                                                sx=x;
                                                sy=y;
                                        case MotionEvent.ACTION_MOVE://analogous to letting go and picking up
                                                if(map[x][y]==' '){
                                                        map[x][y]=map[sx][sy];
                                                        map[sx][sy]=' ';
                                                        sx=x;
                                                        sy=y;
                                                }
                                                break;
                                        case MotionEvent.ACTION_UP:
                                                if(map[x][y]==' '){
                                                        map[x][y]=map[sx][sy];
                                                        map[sx][sy]=' ';
                                                }
                                        case MotionEvent.ACTION_CANCEL:
                                                move=null;
                                                sx=-1;
                                                sy=-1;
                                                break;
                                        default:return false;
                                }
                                invalidate();
                                return true;
                        }
                },new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        }
}

