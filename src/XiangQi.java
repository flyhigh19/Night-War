import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.net.*;

public class XiangQi extends JFrame implements ActionListener{
	public static final Color bgcolor=new Color(0,0,0); //棋盘的背景色设置 final不能被继承，不能有主类
	public static final Color focusbg=new Color(242,242,242); //棋子选中之后的颜色
	public static final Color focusz=new Color(96,65,91);  //棋子选中之后里面字的颜色
	public static final Color color1=new Color(0,0,205); //红方颜色
	public static final Color color2=new Color(255,0,0);  //白方颜色
	JLabel zjm=new JLabel("主机名");  //设置标签“主机名”zjm
	JLabel dkh=new JLabel("端口号");  //设置标签“端口号”dkh
	JLabel nc=new JLabel("昵称");  //设置标签“昵称”nc
	JTextField zjmk=new JTextField("127.0.0.1"); //设置主机名文本框zjmk
	JTextField dkhk=new JTextField("9999");  //设置端口号文本框dkhk
	JTextField nck=new JTextField("play1");  //设置昵称文本框nck
	JButton lj=new JButton("连接");   //设置服务器连接按钮lj
	JButton dk=new JButton("断开");   //设置服务器断开按钮dk
	JComboBox xllb=new JComboBox();  //设置显示用户的下拉列表框
	JButton tz=new JButton("挑战");  //设置挑战按钮tz
	JButton rs=new JButton("认输");  //设置认输按钮rs
	JButton jstz=new JButton("接受挑战");  //设置接受挑战按钮jstz
	JButton jjtz=new JButton("拒绝挑战");  //设置拒绝挑战按钮jjtz
	int width=60;  //设置棋盘之间两线的距离
	qizi qizi[][]=new qizi[9][10];  //创建棋子数组
	QiPan jpz=new QiPan(qizi,width,this);//创建棋盘
	//JPanel qp=new JPanel();  //用一个JPanel代替棋盘显示
	JPanel jp=new JPanel();  //创建一个主JPanel
	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpz,jp); //创建窗体左右分隔
	boolean cp=false;  //是否走棋的标志位（棋盘不可用）
	int color=0;//0代表红方，1代表白方
	Socket sc;//声明Socket引用
	ClientAgentThread cat;//声明客户端代理线程的引用
	public XiangQi(){
		this.initialComponent(); //初始化控件
		this.addListener(); //为相应控件注册时间监听器
		this.initialState(); //初始化状态
		this.initialqizi();  //初始化棋子
		this.initialFrame(); //初始化窗体
	}
	public void initialComponent(){
		 jp.setLayout(null);//面板空布局
		 this.zjm.setBounds(25,10,50,20);
		 jp.add(this.zjm);  //主机名标签大小位置设置
		 this.zjmk.setBounds(80,10,90,20);
		 jp.add(this.zjmk); //主机名文本框大小和位置设置/
		 this.dkh.setBounds(25,40,50,20);
		 jp.add(this.dkh);  //设置端口号标签位置和大小
		 this.dkhk.setBounds(80,40,90,20);
		 jp.add(this.dkhk);  //设置端口号文本框的位置和大小/
		 this.nc.setBounds(25,70,50,20);
		 jp.add(this.nc);   //设置昵称标签的位置和大小
		 this.nck.setBounds(80,70,90,20);
		 jp.add(this.nck);   //设置昵称文本框的位置和大小/
		 this.lj.setBounds(10,100,80,20);
		 jp.add(this.lj);   //设置连接按钮的位置和大小
		 this.dk.setBounds(100,100,80,20);
		 jp.add(this.dk);   //设置断开按钮的位置和大小
		 this.xllb.setBounds(32,130,130,20);
		 jp.add(this.xllb);  //设置下拉条的位置和大小/
		 this.tz.setBounds(10,160,80,20);
		 jp.add(this.tz);   //设置挑战按钮的位置和大小
		 this.rs.setBounds(100,160,80,20);
		 jp.add(this.rs);   //设置认输按钮的位置和大小
		 this.jstz.setBounds(5,190,86,20);
		 jp.add(this.jstz);  //设置接受挑战按钮的位置和大小
		 this.jjtz.setBounds(100,190,86,20);
		 jp.add(this.jjtz);  //设置拒绝挑战按钮的位置和大小
		 jp.setLayout(null);//将棋盘设为空布局
		 jp.setBounds(0,0,700,700);//设置大小
	}
	public void addListener(){
		this.lj.addActionListener(this);   //为“连接”按钮注册事件监听器
		this.dk.addActionListener(this);   //为“断开”按钮注册事件监听器
		this.tz.addActionListener(this);   //为“挑战”按钮注册事件监听器
		this.rs.addActionListener(this);   //为“认输”按钮注册事件监听器
		this.jstz.addActionListener(this); //为“接受挑战”按钮注册事件监听器
		this.jjtz.addActionListener(this); //为“拒绝挑战”按钮注册事件监听器 
	}
	public void initialState(){
		this.dk.setEnabled(false);   //将”断开“按钮设置为不可用
		this.tz.setEnabled(false);   //将”挑战“按钮设置为不可用
		this.rs.setEnabled(false);   //将”认输“按钮设置为不可用
		this.xllb.setEnabled(false); //将下拉列表框设置为不可用
		this.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
		this.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
	}
	public void initialqizi(){
		//初始化各个棋子
		qizi[0][0]=new qizi(color1,"車",0,0);
		qizi[1][0]=new qizi(color1,"馬",1,0);
		qizi[2][0]=new qizi(color1,"象",2,0);
		qizi[3][0]=new qizi(color1,"士",3,0);
		qizi[4][0]=new qizi(color1,"帥",4,0);
		qizi[5][0]=new qizi(color1,"士",5,0);
		qizi[6][0]=new qizi(color1,"象",6,0);
		qizi[7][0]=new qizi(color1,"馬",7,0);
		qizi[8][0]=new qizi(color1,"車",8,0);
		qizi[1][2]=new qizi(color1,"炮",1,2);
		qizi[7][2]=new qizi(color1,"炮",7,2);
		qizi[0][3]=new qizi(color1,"兵",0,4);
		qizi[2][3]=new qizi(color1,"兵",2,4);
		qizi[4][3]=new qizi(color1,"兵",4,4);
		qizi[6][3]=new qizi(color1,"兵",6,4);
		qizi[8][3]=new qizi(color1,"兵",8,4);
		qizi[0][6]=new qizi(color2,"卒",0,6);
		qizi[2][6]=new qizi(color2,"卒",2,6);
		qizi[4][6]=new qizi(color2,"卒",4,6);
		qizi[6][6]=new qizi(color2,"卒",6,6);
		qizi[8][6]=new qizi(color2,"卒",8,6);
		qizi[1][7]=new qizi(color2,"炮",1,7);
		qizi[7][7]=new qizi(color2,"炮",7,7);
		qizi[0][9]=new qizi(color2,"車",0,9);
		qizi[1][9]=new qizi(color2,"馬",1,9);
		qizi[2][9]=new qizi(color2,"象",2,9);
		qizi[3][9]=new qizi(color2,"士",3,9);
		qizi[4][9]=new qizi(color2,"将",4,9);
		qizi[5][9]=new qizi(color2,"士",5,9);
		qizi[6][9]=new qizi(color2,"象",6,9);
		qizi[7][9]=new qizi(color2,"馬",7,9);
		qizi[8][9]=new qizi(color2,"車",8,9);
	}
	public void initialFrame(){
		this.setTitle("暗夜之战———客户端");  //设置窗体标题
		this.add(this.jsp);  //窗体添加左右分隔面板
		jsp.setDividerLocation(698);
		jsp.setDividerSize(7);  //设置分隔线位置和宽度
		this.setBounds(185,0,912,730);  //设置窗体大小
		this.setVisible(true);   //设置窗体可见
		this.addWindowListener(   //给窗体添加监听器
				new WindowAdapter()
				{
					public void windowClosing(WindowEvent e)
					{
						if(cat==null)//客户端代理线程为空，直接退出
						{
							System.exit(0);//退出
							return;
						}
						try
						{
							if(cat.tiaozhanzhe!=null)//正在下棋中
							{
								try
								{
									//发送认输信息
									cat.w.writeUTF("<#RENSHU>"+cat.tiaozhanzhe);
								}
								catch(Exception ee)
								{
									ee.printStackTrace();
								}
							}
							cat.w.writeUTF("<#CLIENT_LEAVE>");//向服务器发送离开信息
							cat.flag=false;//终止客户端代理线程
							cat=null;
							
						}
						catch(Exception ee)
						{
							ee.printStackTrace();
						}
						System.exit(0);//退出
					}
					
				}
				);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==this.lj){  //单击“连接”按钮时
			this.lj_event();
		}else if(e.getSource()==this.dk){  //单击“断开”按钮时
			this.dk_event();
		}else if(e.getSource()==this.tz){  //单击“挑战”按钮时
			this.tz_event();
		}else if(e.getSource()==this.rs){  //单击“认输”按钮时
			this.rs_event();
		}else if(e.getSource()==this.jstz){  //单击“接受挑战”按钮时
			this.jstz_event();
		}else if(e.getSource()==this.jjtz){   //单击“拒绝挑战”按钮时
			this.jjtz_event();
	    }
	}
	public void lj_event(){      //”连接“按钮的业务处理代码
		int port=0;
		try{  //获取用户输入的端口号并转换成整形
			port=Integer.parseInt(this.dkhk.getText().trim());
		}catch(Exception ee){
			//端口号不是整数，给出提示信息（弹出一个对话框）
			JOptionPane.showMessageDialog(this,"         端口号只能为整数","错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(port>65535||port<0){
			//端口号不合法,给出提示信息
			JOptionPane.showMessageDialog(this," 端口号只能为0-65535的整数值","错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String name=this.nck.getText().trim();  //获得昵称
		if(name.length()==0){
			JOptionPane.showMessageDialog(this,"          昵称不能为空","错误0",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		try{//开启socket端口 写出待处理数据 	开启数据流发送和读取获取服务端的信息
			sc=new Socket(this.zjmk.getText().trim(),port);  //主机名和端口号
			//Socket是建立网络连接时使用的。在连接成功时，应用程序两端都会产生一个Socket实例
			cat=new ClientAgentThread(this);//创建客户端代理线程	
			cat.start();//启动客户端代理线程	
			this.zjmk.setEnabled(false);   //将”主机名“文本框设置为不可用
			this.nck.setEnabled(false);    //将”昵称名“文本框设置为不可用
			this.dkhk.setEnabled(false);   //将”端口号“文本框设置为不可用
			this.lj.setEnabled(false);   //将”连接“按钮设置为不可用
			this.dk.setEnabled(true);    //将”断开“按钮设置为可用
			this.xllb.setEnabled(true);  //将下拉列表框设置为可用
			this.tz.setEnabled(true);    //将”挑战“按钮设置为可用
			this.rs.setEnabled(false);   //将”认输“按钮设置为不可用
			this.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
			this.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
			JOptionPane.showMessageDialog(this,"         已连接到服务器","提示",
			        JOptionPane.INFORMATION_MESSAGE);  //给出服务器连接成功的提示信息
		}catch(Exception ee){
			JOptionPane.showMessageDialog(this,"         服务器连接失败","错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	public void dk_event(){  //”断开“按钮的业务处理代码
		try{
			this.cat.w.writeUTF("<#CLIENT_LEAVE>");//向服务器发送离开的信息
			this.cat.flag=false;//终止客户端代理线程
			this.cat=null;
			this.zjmk.setEnabled(true);   //将”主机名“文本框设置为可用
			this.nck.setEnabled(true);    //将”昵称名“文本框设置为可用
			this.dkhk.setEnabled(true);   //将”端口号“文本框设置为可用
			this.lj.setEnabled(true);    //将”连接“按钮设置为可用
			this.dk.setEnabled(false);   //将”断开“按钮设置为不可用
			this.xllb.setEnabled(false); //将下拉列表框设置为不可用
			this.tz.setEnabled(false);   //将”挑战“按钮设置为不可用
			this.rs.setEnabled(false);   //将”认输“按钮设置为不可用
			this.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
			this.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
		}catch(Exception ee){
			ee.printStackTrace();	
		}
	}
	public void tz_event(){   //挑战之后的业务处理代码
		//获得用户选中的挑战对象
		Object o=this.xllb.getSelectedItem();
		if((o==null)&&((String)o).equals("")){
			JOptionPane.showMessageDialog(this,"          请选中对方名字","错误",
					JOptionPane.ERROR_MESSAGE);
		}else{
			String name2=(String)this.xllb.getSelectedItem();  //获得挑战对象
			try{
				this.zjmk.setEnabled(false);  //将”主机名“文本框设置为不可用
				this.nck.setEnabled(false);   //将”昵称名“文本框设置为不可用
				this.dkhk.setEnabled(false);  //将”端口号“文本框设置为不可用
				this.lj.setEnabled(false);   //将”连接“按钮设置为不可用
				this.dk.setEnabled(false);   //将”断开“按钮设置为不可用
				this.xllb.setEnabled(true); //将下拉列表框设置为可用
				this.tz.setEnabled(false);   //将”挑战“按钮设置为不可用
				this.rs.setEnabled(false);   //将”认输“按钮设置为不可用
				this.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
				this.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
				this.cat.tiaozhanzhe=name2;//设置挑战对象
				this.cp=true; //棋盘可用
				this.color=0; //将color设置为0
				this.cat.w.writeUTF("<#TIAO_ZHAN>"+name2);//发送挑战信息
				JOptionPane.showMessageDialog(this,"       已提出挑战，等待对方回应","提示",
						JOptionPane.INFORMATION_MESSAGE);
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
	}
	public void rs_event(){   //发送认输的消息
		try{
			this.cat.w.writeUTF("<#RENSHU>"+this.cat.tiaozhanzhe);
			this.cat.tiaozhanzhe=null;//将tiaoZhanZhe设为空
			this.color=0;//将color设为0
			this.cp=false;//将caiPan设为false
			this.next();//初始化下一局
			this.zjmk.setEnabled(false);  //将”主机名“文本框设置为不可用
			this.nck.setEnabled(false);   //将”昵称名“文本框设置为不可用
			this.dkhk.setEnabled(false);  //将”端口号“文本框设置为不可用
			this.lj.setEnabled(false);    //将”连接“按钮设置为不可用
			this.dk.setEnabled(true);     //将”断开“按钮设置为可用
			this.xllb.setEnabled(true);   //将下拉列表框设置为可用
			this.tz.setEnabled(true);     //将”挑战“按钮设置为可用
			this.rs.setEnabled(false);    //将”认输“按钮设置为不可用
			this.jstz.setEnabled(false);  //将”接受挑战“按钮设置为不可用
			this.jjtz.setEnabled(false);  //将”拒绝挑战“按钮设置为不可用
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public void jstz_event(){  //同意挑战的信息
		try{
			this.cat.w.writeUTF("<#TONG_YI>"+this.cat.tiaozhanzhe);
			this.cp=false;//将caiPan设为false
			this.color=1;//将color设为1
			this.zjmk.setEnabled(false);   //将”主机名“文本框设置为不可用
			this.nck.setEnabled(false);    //将”昵称名“文本框设置为不可用
			this.dkhk.setEnabled(false);   //将”端口号“文本框设置为不可用
			this.lj.setEnabled(false);   //将”连接“按钮设置为不可用
			this.dk.setEnabled(false);   //将”断开“按钮设置为不可用
			this.xllb.setEnabled(true);  //将下拉列表框设置为可用
			this.tz.setEnabled(false);   //将”挑战“按钮设置为不可用
			this.rs.setEnabled(true);    //将”认输“按钮设置为可用
			this.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
			this.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public void jjtz_event(){   //发送拒绝挑战的信息
		try{
			this.cat.w.writeUTF("<#BUTONG_YI>"+this.cat.tiaozhanzhe);
			this.cat.tiaozhanzhe=null;//将tiaoZhanZhe设为空
			this.zjmk.setEnabled(false);   //将”主机名“文本框设置为不可用
			this.nck.setEnabled(false);    //将”昵称名“文本框设置为不可用
			this.dkhk.setEnabled(false);   //将”端口号“文本框设置为不可用
			this.lj.setEnabled(false);   //将”连接“按钮设置为不可用
			this.dk.setEnabled(true);    //将”断开“按钮设置为可用
			this.xllb.setEnabled(true);  //将下拉列表框设置为可用
			this.tz.setEnabled(true);    //将”挑战“按钮设置为可用
			this.rs.setEnabled(false);   //将”认输“按钮设置为可用
			this.jstz.setEnabled(false); //将”接受挑战“按钮设置为不可用
			this.jjtz.setEnabled(false); //将”拒绝挑战“按钮设置为不可用
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public void next(){    //恢复初始化棋盘状态
		for(int i=0;i<9;i++){
			for(int j=0;i<9;i++){
				this.qizi[i][j]=null;
			}
		}
		this.cp=false;   //棋盘不可用
		this.initialqizi();   //重新初始化棋子
		this.repaint();      //重绘（走一步重绘一次棋盘）
	}
	public static void main(String args[]){ 
		new XiangQi();
	}
}
