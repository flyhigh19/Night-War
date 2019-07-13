import java.util.*;
import java.awt.*;
import javax.swing.*;

public class qizi {
	private Color color;//定义棋子的颜色
	private String name;//定义棋子的名字
	private int x;//定义棋子所处位置（x,y）
	private int y;
	private boolean focus=false; //是否选中该棋子
	public qizi(){
		
	}
	public qizi(Color color,String name,int x,int y){ //构造器
		this.color=color;
		this.name=name;
		this.x=x;
		this.y=y;
		this.focus=false;
	}
	public Color getColor() {  //获取棋子颜色的方法
		return this.color;
	}
	public void setColor(Color color) {  //设置棋子颜色的方法
		this.color = color;
	}
	public String getName() {   //获取棋子名字的方法
		return this.name;
	}
	public void setName(String name) { //设置棋子名字的方法
		this.name = name;
	}
	public int getX() {  //获取棋子所处位置的方法
		return this.x;
	}
	public void setX(int x) {  //设置棋子的所在位置
		this.x = x;
	}
	public int getY() {
		return this.y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean getFocus() {   //获取棋子是否被选中的方法
		return focus;
	}
	public void setFocus(boolean focus) {   //设置棋子的选中
		this.focus = focus;
	}
}
