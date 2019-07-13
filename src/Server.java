import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;
//服务器通过开辟一个端口和socket等待客户端接入，并通过数据流进行接收和发送来自客户端的请求
public class Server extends JFrame implements ActionListener {
	JLabel jb=new JLabel("端口号");//创建提示输入端口号的标签jb
	JTextField jtf=new JTextField("9999");//用于输入端口号的文本框jtf
	JButton jbStart=new JButton("启动");//创建启动按钮jbstart
	JButton jbStop=new JButton("关闭");//创建关闭按钮jbstop
	JPanel jpl=new JPanel();//创建一个JPanel面板对象
	JList jls=new JList();//创建用于显示当前用户的对象列表jls
	JScrollPane jsp=new JScrollPane(jls);//创建滚动条jsp并将对象列表放入其中
	JSplitPane jspx=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp,jpl);//窗体左右分隔
	ServerSocket s;//声明ServerSocket引用
	ServerThread st;//线程类对象st的声明
	Vector onlineList=new Vector();//创建存放当前在线用户的Vector对象
	public Server(){
		this.initialComponent();//初始化控件
		this.addlistener();//为相应的控件注册事件监听器
		this.initialJFrame();//初始化窗体
	}
	public void initialComponent(){//初始化控件方法
		 jpl.setLayout(null);//面板空布局
		 jb.setBounds(27,20,50,20);//标签位置和大小
		 jpl.add(jb);//添加用于提示输入端的标签
		 this.jtf.setBounds(85,20,60,20);
		 jpl.add(this.jtf);//添加用于提示输入端口号的文本框
		 this.jbStart.setBounds(18,50,60,20);
		 jpl.add(this.jbStart);//添加开始按钮
		 this.jbStop.setBounds(85, 50, 60, 20);
		 jpl.add(this.jbStop);//添加关闭按钮
		 this.jbStop.setEnabled(false);//设置关闭按钮不可选
	}
	public void addlistener(){
		this.jbStart.addActionListener(this);//为开始按钮和关闭按钮注册时间监听器
		this.jbStop.addActionListener(this);
	}
	public void initialJFrame(){
		this.setTitle("暗夜之战--服务器端");//设置窗体标题
		this.add(jspx);//将分隔线添加到窗体中
		jspx.setDividerLocation(250);
		jspx.setDividerSize(4);//设置分隔线的位置和宽度
		this.setBounds(500,200,420,320);
		this.setVisible(true);//设置窗体可见性
		this.addWindowListener(  //为窗体关闭注册监听器
			new WindowAdapter()
			{
				public void WindowClosing(WindowEvent e)
				{
					if(st==null)
					{  //当服务器线程为空时直接退出
						System.exit(0); //退出
						return;
					}
					try{
						Vector v=onlineList;
						int size=v.size();
						for(int i=0;i<size;i++){  //当不为空时向在线用户发送离线消息
							ServerAgentThread satemp=(ServerAgentThread)v.get(i);//默认是object类
							satemp.w.writeUTF("<#SERVER_DOWN>");
							satemp.flag=false;//终止服务器代理线程
						}
						st.flag=false; //终止服务器线程
						st=null;
						s.close();//关闭serverSocket
						v.clear();//将在线用户列表清空
						refreshList();//刷新列表
					}catch(Exception ee){
						ee.printStackTrace();
					}
					System.exit(0);  //退出
				}
			}
		);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==this.jbStart){
			//System.out.println("服务器启动成功！！");//单击启动按钮时，服务器启动成功
			this.jbStart_event();
		}else if(e.getSource()==this.jbStop){
			//System.out.println("服务器正常关闭！！");//单击关闭按钮时，服务器正常关闭
			this.jbStop_event();
		}
	}
	public void jbStart_event(){ //单击“启动”按钮的业务处理代码
		int port=0;
		try{  //管理者从服务器端输入的端口号并转换成整形
			port=Integer.parseInt(this.jtf.getText().trim());
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
		try{
			this.jbStart.setEnabled(false);//将开始按钮设置为不可用
			this.jtf.setEnabled(false);//将输入端口号的文本框设为不可用
			this.jbStop.setEnabled(true);//将关闭按钮设置为可用
			s=new ServerSocket(port);//创建serversocket对象s唯一标示
			st=new ServerThread(this);//创建服务器线程
			st.start();//启动服务器线程
			JOptionPane.showMessageDialog(this,"         服务器启动成功","提示",
					        JOptionPane.INFORMATION_MESSAGE);  //给出服务器启动的提示信息
		}catch(Exception ee){
			//给出服务器启动失败的提示信息
			JOptionPane.showMessageDialog(this,"         服务器启动失败","错误",
					JOptionPane.ERROR_MESSAGE);
			this.jbStart.setEnabled(true);//将开始按钮设置为可用
			this.jtf.setEnabled(true);//将输入端口号的文本框设为可用
			this.jbStop.setEnabled(false);//将关闭按钮设置为不可用
		}
	}
	public void jbStop_event(){
		  //单击”关闭“按钮之后的业务处理代码
		try{
			Vector v=onlineList;
			int size=v.size();
			for(int i=0;i<size;i++){
				ServerAgentThread satemp=(ServerAgentThread)v.get(i);//默认是object类
				satemp.w.writeUTF("<#SERVER_DOWN>");
				satemp.flag=false;//终止服务器代理线程
			}
			st.flag=false; //终止服务器线程
			st=null;
			s.close();//关闭Socket
			v.clear();//将在线用户列表清空
			refreshList();//刷新列表
			this.jbStart.setEnabled(true);//将开始按钮设置为可用
			this.jtf.setEnabled(true);//将输入端口号的文本框设为可用
			this.jbStop.setEnabled(false);//将关闭按钮设置为不可用
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public void refreshList(){
		 //更新在线用户列表的业务处理代码
		Vector v=new Vector();
		int size=this.onlineList.size();
		for(int i=0;i<size;i++){
			ServerAgentThread satemp1=(ServerAgentThread)this.onlineList.get(i);//默认是object类
	        String temps=satemp1.sc.getInetAddress().toString();
	        temps=temps+"|"+satemp1.getName();  //获取所需信息
	        v.add(temps); //将信息添加到Vector中
		}
		this.jls.setListData(v);//更新列表数据
	}
	public static void main(String args[]){
		new Server();
	}
	
}
