import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;

public class ServerThread extends Thread{
	Server father;//��������������father
	ServerSocket ss;
	//ServerSocket һ����������ö˿ںźͼ�������������ͨ�ŵ��Ƿ������˵�Socket��ͻ��˵�Socket
	//��ServerSocket ����accept֮�󣬾ͽ�����Ȩת���ˡ�
	boolean flag=true;//�����̵߳�������
	public ServerThread(Server father){//��������ʵ�ֳ�ʼ����
		this.father=father;
		this.ss=father.s;
	}
	public void run(){
		while(flag){
			try{
				Socket sc=ss.accept();//�ȴ��ͻ�������
				// ���ܿͻ��˽������ӵ����󣬲�����Socket�����Ա�Ϳͻ��˽��н���
				 //�����ķ�ʽ�Ϳͻ�����ͬ��Ҳ��ͨ��Socket.getInputStream��Socket.getOutputStream�����ж�д����
				  //�˷�����һֱ�������пͻ��˷��ͽ������ӵ��������ϣ���˷����������һ����ʱ��
				   //��Ҫ�ڴ���ServerSocket�������setSoTimeout(�Ժ���Ϊ��λ�ĳ�ʱʱ��)
				ServerAgentThread sat=new ServerAgentThread(father,sc);
				sat.start();//���������������������߳�
			}catch(Exception e){
				e.printStackTrace();//��ӡ�쳣��Ϣ
			}
		}
	}
}
