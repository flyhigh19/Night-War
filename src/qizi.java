import java.util.*;
import java.awt.*;
import javax.swing.*;

public class qizi {
	private Color color;//�������ӵ���ɫ
	private String name;//�������ӵ�����
	private int x;//������������λ�ã�x,y��
	private int y;
	private boolean focus=false; //�Ƿ�ѡ�и�����
	public qizi(){
		
	}
	public qizi(Color color,String name,int x,int y){ //������
		this.color=color;
		this.name=name;
		this.x=x;
		this.y=y;
		this.focus=false;
	}
	public Color getColor() {  //��ȡ������ɫ�ķ���
		return this.color;
	}
	public void setColor(Color color) {  //����������ɫ�ķ���
		this.color = color;
	}
	public String getName() {   //��ȡ�������ֵķ���
		return this.name;
	}
	public void setName(String name) { //�����������ֵķ���
		this.name = name;
	}
	public int getX() {  //��ȡ��������λ�õķ���
		return this.x;
	}
	public void setX(int x) {  //�������ӵ�����λ��
		this.x = x;
	}
	public int getY() {
		return this.y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean getFocus() {   //��ȡ�����Ƿ�ѡ�еķ���
		return focus;
	}
	public void setFocus(boolean focus) {   //�������ӵ�ѡ��
		this.focus = focus;
	}
}
