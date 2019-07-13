import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.net.*;

public class QiPan extends JPanel implements MouseListener {     //�̳�������ʵ�����ļ���
	private int width;  //��������֮��ľ���
	boolean focus=false;  //���ӵ�״̬
	int jiang1_i=4;  //����i����
	int jiang1_j=0;  //����j����
	int jiang2_i=4;  //����i����
	int jiang2_j=9;  //����j����
	int startI=-1;   //�������ӵĳ�ʼλ��
	int startJ=-1;
	int endI=-1;     //�������ӵ���ֹλ��
	int endJ=-1;
	public qizi qizi[][];//���ӵ�����
	XiangQi xq=null;  //�������������xq
	GuiZe guize;  //���������
	public QiPan(qizi qizi[][],int width,XiangQi xq){   //���̹�������ʼ��
		this.qizi=qizi;   //���ӵ�����
		this.width=width;  //����֮��ľ���
		this.xq=xq;      //�����������
		guize=new GuiZe(qizi);  
		this.addMouseListener(this);   //���������������
		this.setBounds(0,0,700,700);   //�������̵Ĵ�С
		this.setLayout(null);   //��Ϊ�ղ���
	}
	public void paint(Graphics g1){
		Graphics2D g=(Graphics2D)g1;   //���Graphics2D����
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);   //�򿪿��ܳ�
		Color c=g.getColor();//��û�����ɫ
		g.setColor(XiangQi.bgcolor);   //��������Ϊ����ɫ
		g.fill3DRect(60,30,580,630,false);   //��������Χ�ľ�������
		g.setColor(Color.white);  //��������Ϊ��ɫ
		for(int i=80;i<=620;i+=60){
			g.drawLine(110,i,590,i);   //���������еĺ���
		}
		g.drawLine(110,80,110,620);   //���������
		g.drawLine(590,80,590,620);   //�����ұ���
		for(int i=170;i<=530;i+=60){
			g.drawLine(i,80,i,320);   //���������е�����
			g.drawLine(i,380,i,620);
		}
		g.drawLine(290,80,410,200);
		g.drawLine(290,200,410,80);
		g.drawLine(290,500,410,620);
		g.drawLine(290,620,410,500);  //�������ߵ�б��
		this.smallLine(g,1,2);      //���ƺ������ڵı�־
		this.smallLine(g,7,2);      //���ƺ������ڵı�־
		this.smallLine(g,0,3);      //���Ʊ����ڵ�λ��
		this.smallLine(g,2,3);		//���Ʊ����ڵ�λ��
		this.smallLine(g,4,3);		//���Ʊ����ڵ�λ��
		this.smallLine(g,6,3);		//���Ʊ����ڵ�λ��
		this.smallLine(g,8,3);		//���Ʊ����ڵ�λ��
		this.smallLine(g,0,6);		//���������ڵ�λ��
		this.smallLine(g,2,6);		//���������ڵ�λ��
		this.smallLine(g,4,6);		//���������ڵ�λ��
		this.smallLine(g,6,6);		//���������ڵ�λ��
		this.smallLine(g,8,6);		//���������ڵ�λ��
		this.smallLine(g,1,7);		//���ư������ڵ�λ��
		this.smallLine(g,7,7);    //���ư������ڵ�λ��
		g.setColor(Color.black);    //��������Ϊ��ɫ
		Font font1=new Font("����",Font.BOLD,50);  //��������
		g.setFont(font1);
		g.drawString("����",170,365);
		g.drawString("�h��",430,365);   //���Ƴ��Ӻ͝h��
		Font font2=new Font("����",Font.BOLD,30);
		g.setFont(font2);
		for(int i=0;i<9;i++){
			for(int j=0;j<10;j++){   //��������
				if(qizi[i][j]!=null){
					if(this.qizi[i][j].getFocus()!=false){   //�Ƿ�ѡ��
						g.setColor(XiangQi.focusbg);   //����ѡ��֮��ı���ɫ
						g.fillOval(110+i*60-25,80+j*60-25,50,50);
						g.setColor(XiangQi.focusz);   //�ֵ���ɫ
					}else{
						g.fillOval(110+i*60-25,80+j*60-25,50,50);  //���Ƹ�����
						g.setColor(qizi[i][j].getColor());    //���û�����ɫ
					}
					g.drawString(qizi[i][j].getName(),110+i*60-15,80+j*60+10);
					g.setColor(Color.black);   //��Ϊ��ɫ
				}
			}
		}
		g.setColor(c);  //��ԭ������ɫ
	}
	public void mouseClicked(MouseEvent e){
		if(this.xq.cp==true){//�ж��Ƿ��ֵ����������
			int i=-1,j=-1;
			int[] pos=getPos(e);
			i=pos[0];
			j=pos[1];
			if(i>=0&&i<=8&&j>=0&&j<=9){//��������̷�Χ��
				if(focus==false){//�������û��ѡ������
					this.noFocus(i,j);
				}
				else{//�����ǰѡ�й�����
					if(qizi[i][j]!=null){//����ô�������
						if(qizi[i][j].getColor()==qizi[startI][startJ].getColor())
						{//������Լ�������
							qizi[startI][startJ].setFocus(false);
							qizi[i][j].setFocus(true);//����ѡ�ж���
							startI=i;startJ=j;//�����޸�
						}
						else{//����ǶԷ�����
							endI=i;//����õ�
							endJ=j;
							String name=qizi[startI][startJ].getName();//��ø����ӵ�����
							//���Ƿ�����ƶ�
							boolean canMove= guize.canMove(startI,startJ,endI,endJ,name);
							if(canMove)//��������ƶ�
							{
								try{//�����ƶ���Ϣ���͸��Է�
									this.xq.cat.w.writeUTF("<#MOVE>"+
									this.xq.cat.tiaozhanzhe+startI+startJ+endI+endJ);
									this.xq.cp=false;
								    if(qizi[endI][endJ].getName().equals("��")||
								       qizi[endI][endJ].getName().equals("��"))
								    {//����յ㴦�ǶԷ���"��"
								    	this.success();
								    }
								    else{//����յ㲻�ǶԷ���"��"
								    	this.noJiang();
								    }
								}
								catch(Exception ee){ee.printStackTrace();}
							}
						}
					}
					else{//���û������
						endI=i;
						endJ=j;//�����յ�
						String name=qizi[startI][startJ].getName();//��ø��������
						boolean canMove=guize.canMove(startI,startJ,endI,endJ,name);//�ж��Ƿ����
						if(canMove){//��������ƶ�
							this.noqizi();
						}
					}
				}
			}
			this.xq.repaint();//�ػ�
		}
	}
	public int[] getPos(MouseEvent e){
		int[] pos=new int[2];
		pos[0]=-1;
		pos[1]=-1;
		Point p=e.getPoint();//����¼������������
		double x=p.getX();
		double y=p.getY();
		if(Math.abs((x-110)/1%60)<=25){//��ö�Ӧ������x�±��λ��
			pos[0]=Math.round((float)(x-110))/60;
		}
		else if(Math.abs((x-110)/1%60)>=35){
			pos[0]=Math.round((float)(x-110))/60+1;
		}
		if(Math.abs((y-80)/1%60)<=25){//��ö�Ӧ������y�±��λ��
			pos[1]=Math.round((float)(y-80))/60;
		}
		else if(Math.abs((y-80)/1%60)>=35){
			pos[1]=Math.round((float)(y-80))/60+1;
		}
		return pos;
	}
	public void noFocus(int i,int j){
		if(this.qizi[i][j]!=null)//�����λ��������
		{
			if(this.xq.color==0)//����Ǻ췽
			{
				if(this.qizi[i][j].getColor().equals(XiangQi.color1))//��������Ǻ�ɫ
				{
					this.qizi[i][j].setFocus(true);//����������Ϊѡ��״̬
					focus=true;//��focus��Ϊtrue
					startI=i;//����������
					startJ=j;
				}
			}
			else//����ǰ׷�
			{
				if(this.qizi[i][j].getColor().equals(XiangQi.color2))//����������ǰ�ɫ
				{
					this.qizi[i][j].setFocus(true);//����������Ϊѡ��״̬
					focus=true;//��focus��Ϊtrue
					startI=i;//����������
		            startJ=j;
				}
			}
		}
	}
	public void success(){
		qizi[endI][endJ]=qizi[startI][startJ];//�Ե�������
		qizi[startI][startJ]=null;//��ԭ����λ����Ϊ��
		this.xq.repaint();//�ػ�
		JOptionPane.showMessageDialog(this.xq,"��ϲ��������ʤ��","��ʾ",
		                JOptionPane.INFORMATION_MESSAGE);//������ʤ��Ϣ
		this.xq.cat.tiaozhanzhe=null;
		this.xq.color=0;
		this.xq.cp=false;
		this.xq.next();//��ԭ���̣�������һ��
		this.xq.zjm.setEnabled(false);
		this.xq.dkh.setEnabled(false);//���ø��ؼ���״̬
		this.xq.nc.setEnabled(false);
		this.xq.lj.setEnabled(false);
		this.xq.dk.setEnabled(true);
		this.xq.tz.setEnabled(true);
		this.xq.jstz.setEnabled(false);
		this.xq.jjtz.setEnabled(false);
		this.xq.rs.setEnabled(false);
		startI=-1;//��ԭ�����
		startJ=-1;
		endI=-1;
		endJ=-1;
		jiang1_i=4;//"��"��i����
		jiang1_j=0;//"��"��j����
		jiang2_i=4;//"��"��i����
		jiang2_j=9;//"��"��j����
		focus=false;
	}
	public void noJiang(){
		qizi[endI][endJ]=qizi[startI][startJ];
		qizi[startI][startJ]=null;//����
		qizi[endI][endJ].setFocus(false);//��������Ϊ��ѡ��״̬
		this.xq.repaint();//�ػ�
		if(qizi[endI][endJ].getName().equals("��")){//����ƶ�����"��"
			jiang1_i=endI;//����"��"��λ������
			jiang1_j=endJ;
		}
		else if(qizi[endI][endJ].getName().equals("��")){//����ƶ�����"��"
			jiang2_i=endI;//����"��"��λ������
			jiang2_j=endJ;
		}
		if(jiang1_i==jiang2_i){//���"��"��"��"��һ��������
			int count=0;
			for(int jiang_j=jiang1_j+1;jiang_j<jiang2_j;jiang_j++){//������������
				if(qizi[jiang1_i][jiang_j]!=null){
					count++;break;
				}
			}
			if(count==0){//������������ս�
		    	JOptionPane.showMessageDialog(this.xq,"�ս���������ʧ���ˣ�����","��ʾ",
		    	            JOptionPane.INFORMATION_MESSAGE);//����ʧ����Ϣ
		    	this.xq.cat.tiaozhanzhe=null;
				this.xq.color=0;//��ԭ���̣�������һ��
				this.xq.cp=false;
				this.xq.next();//������һ��
				this.xq.zjm.setEnabled(false);
				this.xq.dkh.setEnabled(false);//���ø��ؼ�״̬
				this.xq.nc.setEnabled(false);
				this.xq.lj.setEnabled(false);
				this.xq.dk.setEnabled(true);
				this.xq.tz.setEnabled(true);
				this.xq.jstz.setEnabled(false);
				this.xq.jjtz.setEnabled(false);
				this.xq.rs.setEnabled(false);
				jiang1_i=4;//"��"��i����
				jiang1_j=0;//"��"��j����
				jiang2_i=4;//"��"��i����
				jiang2_j=9;//"��"��j����
			}
		}
		startI=-1;
		startJ=-1;//��ԭ�����
		endI=-1;
		endJ=-1;
		focus=false;
	}
	public void noqizi(){
		try{//�����ƶ���Ϣ���͸��Է�
			this.xq.cat.w.writeUTF("<#MOVE>"+this.xq.cat.tiaozhanzhe+startI+startJ+endI+endJ);
			this.xq.cp=false;
			qizi[endI][endJ]=qizi[startI][startJ];
			qizi[startI][startJ]=null;//����
			qizi[endI][endJ].setFocus(false);//��������Ϊ��ѡ��״̬
			this.xq.repaint();//�ػ�
			if(qizi[endI][endJ].getName().equals("��")){//����ƶ�����"��"
				jiang1_i=endI;//����"��"��λ������
				jiang1_j=endJ;
			}
			else if(qizi[endI][endJ].getName().equals("��")){//����ƶ�����"��"
				jiang2_i=endI;//����"��"��λ������
				jiang2_j=endJ;
			}
			if(jiang1_i==jiang2_i)//���"��"��"��"��һ��������
			{
				int count=0;
				for(int jiang_j=jiang1_j+1;jiang_j<jiang2_j;jiang_j++){//������������
					if(qizi[jiang1_i][jiang_j]!=null){
						count++;break;
					}
				}
				if(count==0){//������������ս�
			    	JOptionPane.showMessageDialog(this.xq,"�ս���������ʧ���ˣ�����","��ʾ",
			    	            JOptionPane.INFORMATION_MESSAGE);//����ʧ����Ϣ
			    	this.xq.cat.tiaozhanzhe=null;
					this.xq.color=0;//��ԭ���̣�������һ��
					this.xq.cp=false;
					this.xq.next();//������һ��
					this.xq.zjm.setEnabled(false);
					this.xq.dkh.setEnabled(false);
					this.xq.nc.setEnabled(false);//���ø��ؼ�״̬
					this.xq.lj.setEnabled(false);
					this.xq.dk.setEnabled(true);
					this.xq.tz.setEnabled(true);
					this.xq.jstz.setEnabled(false);
					this.xq.jjtz.setEnabled(false);
					this.xq.rs.setEnabled(false);
					jiang1_i=4;//"��"��i����
					jiang1_j=0;//"��"��j����
					jiang2_i=4;//"��"��i����
					jiang2_j=9;//"��"��j����
				}
			}
			startI=-1;
			startJ=-1;//��ԭ�����
			endI=-1;
			endJ=-1;
			focus=false;
		}
		catch(Exception ee){ee.printStackTrace();}
	}
public void move(int startI,int startJ,int endI,int endJ){
	if(qizi[endI][endJ]!=null&&(qizi[endI][endJ].getName().equals("��")||
	             qizi[endI][endJ].getName().equals("��"))){//���"��"������
    	qizi[endI][endJ]=qizi[startI][startJ];
	    qizi[startI][startJ]=null;//����
	    this.xq.repaint();//�ػ�
    	JOptionPane.showMessageDialog(this.xq,"���ź�����ʧ���ˣ�����","��ʾ",
    	            JOptionPane.INFORMATION_MESSAGE);//����ʧ����Ϣ
    	this.xq.cat.tiaozhanzhe=null;
		this.xq.color=0;//��ԭ���̽�����һ��
		this.xq.cp=false;
		this.xq.next();
		this.xq.zjm.setEnabled(false);//��ԭ�����ؼ���״̬
		this.xq.dkh.setEnabled(false);
		this.xq.nc.setEnabled(false);
		this.xq.lj.setEnabled(false);
		this.xq.dk.setEnabled(true);
		this.xq.tz.setEnabled(true);
		this.xq.jstz.setEnabled(false);
		this.xq.jjtz.setEnabled(false);
		this.xq.rs.setEnabled(false);
		jiang1_i=4;//"��"��i����
		jiang1_j=0;//"��"��j����
		jiang2_i=4;//"��"��i����
		jiang2_j=9;//"��"��j����
    }
    else{//�������"��"
    	qizi[endI][endJ]=qizi[startI][startJ];
	    qizi[startI][startJ]=null;//����
	    this.xq.repaint();//�ػ�
	    if(qizi[endI][endJ].getName().equals("��")){
			jiang1_i=endI;//�����"��"
			jiang1_j=endJ;
		}
		else if(qizi[endI][endJ].getName().equals("��")){
			jiang2_i=endI;//�����"��"
			jiang2_j=endJ;
		}
		if(jiang1_i==jiang2_i){//���������һ������
			int count=0;
			for(int jiang_j=jiang1_j+1;jiang_j<jiang2_j;jiang_j++){
				if(qizi[jiang1_i][jiang_j]!=null){//������
					count++;break;
				}
			}
			if(count==0){
		    	JOptionPane.showMessageDialog(this.xq,"�Է��ս���������ʤ���ˣ�����",
		    	           "��ʾ",JOptionPane.INFORMATION_MESSAGE);//����ʧ����Ϣ
		    	this.xq.cat.tiaozhanzhe=null;
				this.xq.color=0;//��ԭ���̣�������һ��
				this.xq.cp=false;
				this.xq.next();
				this.xq.zjm.setEnabled(false);
				this.xq.dkh.setEnabled(false);//���ø��ռ�λ��
				this.xq.nc.setEnabled(false);
				this.xq.lj.setEnabled(false);
				this.xq.dk.setEnabled(true);
				this.xq.tz.setEnabled(true);
				this.xq.jstz.setEnabled(false);
				this.xq.jjtz.setEnabled(false);
				this.xq.rs.setEnabled(false);
				jiang1_i=4;//"��"��i����
				jiang1_j=0;//"��"��j����
				jiang2_i=4;//"��"��i����
				jiang2_j=9;//"��"��j����
			}
		}
    }
	this.xq.repaint();//�ػ�
}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void smallLine(Graphics2D g,int i,int j){
		int x=110+60*i;//��������
		int y=80+60*j;
		if(i>0){//�������Ϸ��ı�־
			g.drawLine(x-3,y-3,x-20,y-3);
			g.drawLine(x-3,y-3,x-3,y-20);
		}
		if(i<8){//�������Ϸ��ı�־
			g.drawLine(x+3,y-3,x+20,y-3);
			g.drawLine(x+3,y-3,x+3,y-20);
		}
		if(i>0){//�������·��ı�־
			g.drawLine(x-3,y+3,x-20,y+3);
			g.drawLine(x-3,y+3,x-3,y+20);
		}
		if(i<8){//�������·��ı�־
			g.drawLine(x+3,y+3,x+20,y+3);
			g.drawLine(x+3,y+3,x+3,y+20);
		}
	}
}
