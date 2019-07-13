import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.net.*;

public class ClientAgentThread extends Thread {   //��������һ���ͻ����߳���������ÿһ�����ߵ��û�
	XiangQi father;  //�ͻ�����������
	DataInputStream  r;//��������������
	DataOutputStream w;//�������������
	boolean flag=true;  //�����̵߳ı�־λ
	String tiaozhanzhe=null; //����һ����ս�߱�ʾ�Ƿ����
	public ClientAgentThread(XiangQi father){  //������
		this.father=father;
		try{
			r=new DataInputStream(father.sc.getInputStream());  //��������������
			w=new DataOutputStream(father.sc.getOutputStream()); //�������������
			String name=father.nck.getText().trim();    //����ǳ�
			w.writeUTF("<#NICK_NAME>"+name);     //������������ǳ�
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void run(){
		while(flag){
			try{
				String msg=r.readUTF().trim();   //��msg�����շ�������������Ϣ
				if(msg.startsWith("<#CHONG_MING>")){   //�������
					this.chong_ming();
				}else if(msg.startsWith("<#NICK_LIST>")){ //����ǳ��б�
					this.nc_list(msg);
				}else if(msg.startsWith("<#SERVER_DOWN>")){  //��������Ϊ�ر�
					this.server_down();
				}else if(msg.startsWith("<#TIAO_ZHAN>")){    //XXX���㷢����ս
					this.tiao_zhan(msg);
				}else if(msg.startsWith("<#TONG_YI>")){      //ͬ��XXX����ս
					this.tong_yi();
				}else if(msg.startsWith("<#BUTONG_YI>")){    //��ͬ��XXX����ս
					this.butong_yi();
				}else if(msg.startsWith("<#BUSY>")){     //�Է���æ
					this.busy();
				}else if(msg.startsWith("<#RENSHU>")){    //����ʤ���Է�����
					this.renshu();
				}else if(msg.startsWith("<#MOVE>")){      //��ʼ����
					this.move(msg);
				}
				}catch(Exception ee){
					ee.printStackTrace();
			}
		}
	}
	public void chong_ming(){   //����������¼��Ĵ������
		try{
			JOptionPane.showMessageDialog(this.father,"������Ѿ����ڣ���������д��","����",
					JOptionPane.ERROR_MESSAGE);  //����������Ѵ��ڵ���ʾ��Ϣ
			r.close();  //�ر����������ͻ�����Դ�رգ�
			w.close();
			this.father.zjmk.setEnabled(true);   //�������������ı�������Ϊ����
			this.father.nck.setEnabled(true);    //�����ǳ������ı�������Ϊ����
			this.father.dkhk.setEnabled(true);   //�����˿ںš��ı�������Ϊ����
			this.father.lj.setEnabled(true);    //�������ӡ���ť����Ϊ����
			this.father.dk.setEnabled(false);   //�����Ͽ�����ť����Ϊ������
			this.father.tz.setEnabled(false);   //������ս����ť����Ϊ������
			this.father.rs.setEnabled(false);   //�������䡰��ť����Ϊ������
			this.father.jstz.setEnabled(false); //����������ս����ť����Ϊ������
			this.father.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
			father.sc.close();  //�ر�socket����
			father.sc=null;    
			father.cat=null;
			flag=false;  //��ֹ�ÿͻ����߳�
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public void nc_list(String msg){     //����ǳ��б�(�вη���)
		String s=msg.substring(12);  //����û���Ϣ
		String fj[]=s.split("\\|");  //��\\|���зֽ�õ����������ǳ�
		Vector v=new Vector();  //����һ�����������v
		for(int i=0;i<fj.length;i++){
			if(fj[i].trim().length()!=0&&(!fj[i].trim().equals(father.nck.getText().trim()))){
				v.add(fj[i]);      //���ǳ�ȫ����ӵ�Vector��
			}
		}
		father.xllb.setModel(new DefaultComboBoxModel(v));   //���������б��ֵ
	}
	public void server_down(){    //��������Ϊ�Ĺرպ��¼��������
		this.father.zjmk.setEnabled(true);   //�������������ı�������Ϊ����
		this.father.nck.setEnabled(true);    //�����ǳ������ı�������Ϊ����
		this.father.dkhk.setEnabled(true);   //�����˿ںš��ı�������Ϊ����
		this.father.lj.setEnabled(true);    //�������ӡ���ť����Ϊ����
		this.father.dk.setEnabled(false);   //�����Ͽ�����ť����Ϊ������
		this.father.tz.setEnabled(false);   //������ս����ť����Ϊ������
		this.father.rs.setEnabled(false);   //�������䡰��ť����Ϊ������
		this.father.jstz.setEnabled(false); //����������ս����ť����Ϊ������
		this.father.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
		this.flag=false;    
		this.father.cat=null;//��ֹ�ͻ��˴����߳�
		JOptionPane.showMessageDialog(this.father,"��������ֹͣ������","��ʾ",
				JOptionPane.INFORMATION_MESSAGE);  //������������ֹͣ����ʾ��Ϣ
	}
	public void tiao_zhan(String msg){
		try{
			String name=msg.substring(12);   //�����ս�ߵ��ǳ�
			if(this.tiaozhanzhe==null){
				    tiaozhanzhe=msg.substring(12);   //�����ս�ߵ��ǳ�
				    this.father.zjmk.setEnabled(false);   //�������������ı�������Ϊ������
					this.father.nck.setEnabled(false);    //�����ǳ������ı�������Ϊ������
					this.father.dkhk.setEnabled(false);   //�����˿ںš��ı�������Ϊ������
					this.father.lj.setEnabled(false);    //�������ӡ���ť����Ϊ������
					this.father.dk.setEnabled(false);   //�����Ͽ�����ť����Ϊ������
					this.father.tz.setEnabled(false);   //������ս����ť����Ϊ������
					this.father.rs.setEnabled(false);   //�������䡰��ť����Ϊ������
					this.father.jstz.setEnabled(true); //����������ս����ť����Ϊ����
					this.father.jjtz.setEnabled(true); //�����ܾ���ս����ť����Ϊ����
					JOptionPane.showMessageDialog(this.father,tiaozhanzhe+"������ս","��ʾ",
							JOptionPane.INFORMATION_MESSAGE);  //����XXX��ս��������ս����ʾ��Ϣ
			}
			else{   //��������æµ�У��򷵻�һ����<#BUSY>Ϊ��ͷ����Ϣ
				this.w.writeUTF("<#BUSY>"+name);
			}
		}catch(IOException ee){
			ee.printStackTrace();
		}
	}
	public void tong_yi(){   //ͬ����ս����¼��������
		this.father.nck.setEnabled(false);    //�����ǳ������ı�������Ϊ������
		this.father.dkhk.setEnabled(false);   //�����˿ںš��ı�������Ϊ������
		this.father.lj.setEnabled(false);    //�������ӡ���ť����Ϊ������
		this.father.dk.setEnabled(true);   //�����Ͽ�����ť����Ϊ����
		this.father.tz.setEnabled(false);   //������ս����ť����Ϊ������
		this.father.rs.setEnabled(true);   //�������䡰��ť����Ϊ����
		this.father.jstz.setEnabled(false); //����������ս����ť����Ϊ������
		this.father.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
		JOptionPane.showMessageDialog(this.father,"�Է�ͬ�����������ս�������ߣ����壩","��ʾ",
				JOptionPane.INFORMATION_MESSAGE);  //����XXX��ս��������ս����ʾ��Ϣ
	}
	public void butong_yi(){   //�Է���ͬ��������ս�¼���ҵ�������
		this.father.cp=false;  //���̲�����
		this.father.color=0;   //����Ϊ�췽
		this.father.nck.setEnabled(false);    //�����ǳ������ı�������Ϊ������
		this.father.dkhk.setEnabled(false);   //�����˿ںš��ı�������Ϊ������
		this.father.lj.setEnabled(false);    //�������ӡ���ť����Ϊ������
		this.father.dk.setEnabled(true);   //�����Ͽ�����ť����Ϊ����
		this.father.tz.setEnabled(true);   //������ս����ť����Ϊ����
		this.father.rs.setEnabled(false);   //�������䡰��ť����Ϊ������
		this.father.jstz.setEnabled(false); //����������ս����ť����Ϊ������
		this.father.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
		JOptionPane.showMessageDialog(this.father,"�Է���ͬ�����������ս","��ʾ",
				JOptionPane.INFORMATION_MESSAGE);  //����XXX��ս��������ս����ʾ��Ϣ
		this.tiaozhanzhe=null;
	}
	public void busy(){
		this.father.cp=false;  //���̲�����
		this.father.color=0;   //����Ϊ�췽
		this.father.nck.setEnabled(false);    //�����ǳ������ı�������Ϊ������
		this.father.dkhk.setEnabled(false);   //�����˿ںš��ı�������Ϊ������
		this.father.lj.setEnabled(false);    //�������ӡ���ť����Ϊ������
		this.father.dk.setEnabled(true);   //�����Ͽ�����ť����Ϊ����
		this.father.tz.setEnabled(true);   //������ս����ť����Ϊ����
		this.father.rs.setEnabled(false);   //�������䡰��ť����Ϊ������
		this.father.jstz.setEnabled(false); //����������ս����ť����Ϊ������
		this.father.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
		JOptionPane.showMessageDialog(this.father,"�Է�æµ�У���","��ʾ",
				JOptionPane.INFORMATION_MESSAGE);  //����XXX��ս��������ս����ʾ��Ϣ
		this.tiaozhanzhe=null;
	}
	public void renshu(){
		JOptionPane.showMessageDialog(this.father,"��ϲ�㣬���ʤ���Է����䣡��","��ʾ",
				JOptionPane.INFORMATION_MESSAGE);  //����XXX��ս��������ս����ʾ��Ϣ	
		this.tiaozhanzhe=null;
		this.father.cp=false;  //���̲�����
		this.father.color=0;   //����Ϊ�췽
		this.father.next();    //��ʼ������һ��
		this.father.nck.setEnabled(false);    //�����ǳ������ı�������Ϊ������
		this.father.dkhk.setEnabled(false);   //�����˿ںš��ı�������Ϊ������
		this.father.lj.setEnabled(false);    //�������ӡ���ť����Ϊ������
		this.father.dk.setEnabled(true);   //�����Ͽ�����ť����Ϊ����
		this.father.tz.setEnabled(true);   //������ս����ť����Ϊ����
		this.father.rs.setEnabled(false);   //�������䡰��ť����Ϊ������
		this.father.jstz.setEnabled(false); //����������ս����ť����Ϊ������
		this.father.jjtz.setEnabled(false); //�����ܾ���ս����ť����Ϊ������
	}
	public void move(String msg){
		int length=msg.length();
		int startI=Integer.parseInt(msg.substring(length-4,length-3));  //������ӵ�ԭʼλ��
		int startJ=Integer.parseInt(msg.substring(length-3,length-2));
		int endI=Integer.parseInt(msg.substring(length-2,length-1));  //��������ߺ��λ��
		int endJ=Integer.parseInt(msg.substring(length-1));
		this.father.jpz.move(startI,startJ,endI,endJ);
		this.father.cp=true;   //��������true
	}
}
