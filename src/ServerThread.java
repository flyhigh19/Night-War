import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;

public class ServerThread extends Thread{
	Server father;//声明服务器对象father
	ServerSocket ss;
	//ServerSocket 一般仅用于设置端口号和监听，真正进行通信的是服务器端的Socket与客户端的Socket
	//在ServerSocket 进行accept之后，就将主动权转让了。
	boolean flag=true;//定义线程的生命期
	public ServerThread(Server father){//构造器（实现初始化）
		this.father=father;
		this.ss=father.s;
	}
	public void run(){
		while(flag){
			try{
				Socket sc=ss.accept();//等待客户端链接
				// 接受客户端建立连接的请求，并返回Socket对象，以便和客户端进行交互
				 //交互的方式和客户端相同，也是通过Socket.getInputStream和Socket.getOutputStream来进行读写操作
				  //此方法会一直阻塞到有客户端发送建立连接的请求，如果希望此方法最多阻塞一定的时间
				   //则要在创建ServerSocket后调用其setSoTimeout(以毫秒为单位的超时时间)
				ServerAgentThread sat=new ServerAgentThread(father,sc);
				sat.start();//创建并启动服务器代理线程
			}catch(Exception e){
				e.printStackTrace();//打印异常信息
			}
		}
	}
}
