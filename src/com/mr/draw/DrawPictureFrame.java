package com.mr.draw;//类所在的包名
import javax.swing.JFrame;//引入窗体类
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import com.mr.util.DrawImageUtil;

/**
 * 画图主窗体。
 */
public class DrawPictureFrame extends JFrame {// 继承窗体类
    // 创建一个8 位 RGB 颜色分量的图像
    BufferedImage image = new BufferedImage(570, 390, BufferedImage.TYPE_INT_BGR);
    Graphics gs = image.getGraphics(); // 获得图像的绘图对象
    Graphics2D g = (Graphics2D) gs; // 将绘图对象转换为Graphics2D类型
    DrawPictureCanvas canvas = new DrawPictureCanvas(); // 创建画布对象
    Color foreColor = Color.BLACK; // 定义前景色
    Color backgroundColor = Color.WHITE; // 定义背景色
    int x = -1; // 上一次鼠标绘制点的横坐标
    int y = -1; // 上一次鼠标绘制点的纵坐标
    boolean rubber = false; // 橡皮标识变量

    /**
     * 构造方法。添加组件监听方法。
     */
    public DrawPictureFrame() {
        setResizable(false);// 窗体不能改变大小
        setTitle("画图程序");// 设置标题
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 窗体关闭则停止程序
        setBounds(500, 100, 574, 460);// 设置窗口位置和宽高
        init();// 组件初始化
        addListener();// 添加组件监听
    }// DrawPictureFrame()结束

    /**
     * 组件初始化。
     */
    private void init() {
        g.setColor(backgroundColor); // 用背景色设置绘图对象的颜色
        g.fillRect(0, 0, 570, 390); // 用背景色填充整个画布
        g.setColor(foreColor); // 用前景色设置绘图对象的颜色
        canvas.setImage(image); // 设置画布的图像
        getContentPane().add(canvas); // 将画布添加到窗体容器默认布局的中部位置
    }// init()结束

    /**
     * 为组件添加动作监听。
     */
    private void addListener() {
        // 画板添加鼠标移动事件监听
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(final MouseEvent e) {// 当鼠标拖拽时
                if (x > 0 && y > 0) {// 如果x和y存在鼠标记录
                    if (rubber) {// 橡皮标识为true，表示使用橡皮
                        g.setColor(backgroundColor); // 绘图工具使用背景色
                        g.fillRect(x, y, 10, 10); // 在鼠标划过的位置画填充的正方型
                    } else { // 如果橡皮标识为false，表示用画笔画图
                        g.drawLine(x, y, e.getX(), e.getY());// 在鼠标划过的位置画直线
                    } // else结束
                } // if结束
                x = e.getX(); // 上一次鼠标绘制点的横坐标
                y = e.getY(); // 上一次鼠标绘制点的纵坐标
                canvas.repaint(); // 更新画布
            }// mouseDragged()结束
        });// canvas.addMouseMotionListener()结束

        canvas.addMouseListener(new MouseAdapter() {// 画板添加鼠标点击事件监听
            public void mouseReleased(final MouseEvent arg0) {// 当按键抬起时
                x = -1; // 将记录上一次鼠标绘制点的横坐标恢复成-1
                y = -1; // 将记录上一次鼠标绘制点的纵坐标恢复成-1
            }// mouseReleased()结束
        });// canvas.addMouseListener()结束
    }// addListener() 结束

    /**
     * 程序运行主方法。
     *
     * @param args
     * – 运行时参数，本程序用不到
     */
    public static void main(String[] args) {
        DrawPictureFrame frame = new DrawPictureFrame();// 创建窗体对象
        frame.setVisible(true);// 让窗体可见
    }// main()结束
}// DrawPictureFrame类结束