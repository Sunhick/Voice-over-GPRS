import java.io.*;
import java.lang.*;
import java.util.*;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.lang.*;
import java.util.*;
import java.io.IOException;
//import javax.microedition.io.Conneax.microedition.lcdui.*;
import javax.microedition.media.control.*;
import javax.microedition.media.*;
import javax.microedition.io.*;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;
import javax.microedition.io.ConnectionNotFoundException;

/*class myexp extends Exception{

}*/
public class Voicechat extends MIDlet implements CommandListener{
    private Command exitCommand; // The exit command
    private Display display;     // The display for this MIDlet
    private Command record;
    private Command play;
    private Command connect;
    private byte[] recordedSoundArray = null; //recorded voice is stored here
    private Form t;
    StringItem si;
    DatagramConnection dc;
    int sleep_t = 615;
    int max_bytes = 1024;
    InputStream is;
    OutputStream os;
    SocketConnection sc;
    //   String server_ip = "socket://124.247.216.133:4861";
    //String server_ip = "datagram://10.0.0.25:4862";
    String server_ip = "datagram://";
    Buffer buff;
    Recording ThreadRecorder;
    Playout ThreadPlayer;
    boolean Pflag=true,Rflag=true;
    boolean F = true;
    public int start=0,end=0,end1=0;
     private TextField ipaddr;
    public boolean flag=false;
    byte[] header="#!AMR\n".getBytes(); //Encodes this String into a sequence of bytes using the platform's default charset, storing the result into a new byte array.
    private TextField username;
    private TextField password;
    String[] usr={"a","b","c","d","e"};
    String[] pw={"1","2","3","4","5"};

    public boolean ring=false;

    public Voicechat(){
        display = Display.getDisplay(this);
        t = new Form("Voicechat");
        ipaddr = new TextField ("IP Address:","",100, TextField.ANY);
        t.append (ipaddr);

         username = new TextField ("Username:","",100, TextField.ANY);
        t.append (username);

        password = new TextField ("Password:","",100, TextField.PASSWORD);
        t.append (password);

        exitCommand = new Command("Exit", Command.EXIT, 1);
        record = new Command("Record", Command.SCREEN, 2);
        play = new Command("Play", Command.SCREEN, 3);
        connect = new Command("connect", Command.SCREEN, 4);
        t.addCommand(exitCommand);
        //t.addCommand(record);
        si = new StringItem("status","");
        t.append(si);
        //t.addCommand(play);
        t.addCommand(connect);
        t.setCommandListener(this);
         display.setCurrent (t);
    }

    public void startApp(){
       /* String ip=ipaddr.getString();
        server_ip = server_ip+ip+":4862";

        byte[] b = new byte[2]; //Initial 2 bytes sent to server for registration purposes
	b[0] = 111;
	b[1] = 100;
	try
            {
		//            sc = (SocketConnection) Connector.open(server_ip);
		//            os = sc.openOutputStream();
		//            os.write(b);

                dc = (DatagramConnection) Connector.open(server_ip);
		//            os = sc.openOutputStream();
		//            os.write(b);
                Datagram dg = dc.newDatagram(b, b.length, server_ip);
                dc.send(dg);
                buff = new Buffer();
                buff.start();

            }
	catch(IOException ioe)
            {
                t.append("Connection Error:"+ioe.toString());
            }
	catch(SecurityException sexp)
            {
                t.append("Connection Error:"+sexp.toString());
            }
	display.setCurrent(t);*/

    }

    public void pauseApp(){
    }

    public void destroyApp(boolean unconditional){
    }

    public static final byte[] intToByteArray(int value) {
	return new byte[] {
	    (byte)(value >>> 8),
	    (byte)value};
    }

    public void commandAction(Command c, Displayable disp){

        if(c==connect){

        String ip=ipaddr.getString();
        server_ip = server_ip+ip+":4862";
        String usrname=username.getString();
        String pswd=password.getString();
        

        int valid=0;

        for(int i=0;i<usr.length;i++)
        {
            if(usrname.equals(usr[i]) && pswd.equals(pw[i]))
            {
                valid=1;
                t.removeCommand(c);
                t.addCommand(record);
                t.addCommand(play);
                t.deleteAll();
            }
            
        }

        //try{

        if(valid==0) //throw new myexp();}
        //catch(myexp e)
        {
           Display display = Display.getDisplay(this);
           Alert a = new Alert ("Invalid Username or Password",
                "Please enter correctly",
                null, AlertType.ERROR);
            a.setTimeout (Alert.FOREVER);
            display.setCurrent (a);
        }


           

        byte[] b = new byte[2]; //Initial 2 bytes sent to server for registration purposes
	b[0] = 111;
	b[1] = 100;
	try
            {
		//            sc = (SocketConnection) Connector.open(server_ip);
		//            os = sc.openOutputStream();
		//            os.write(b);

                dc = (DatagramConnection) Connector.open(server_ip);
                if(dc!=null)
                    si.setText("Connected to server");
		//            os = sc.openOutputStream();
		//            os.write(b);
                Datagram dg = dc.newDatagram(b, b.length, server_ip);
                dc.send(dg);
                buff = new Buffer();
                buff.start();

            }
        catch(ConnectionNotFoundException cne) {
            Display display = Display.getDisplay(this);
            Alert a = new Alert("Client","Please run the server ",null,AlertType.ERROR);
            a.setTimeout(Alert.FOREVER);
            a.setCommandListener(this);
            display.setCurrent(a);
        }
	catch(IOException ioe)
            {
                t.append("Connection Error:"+ioe.toString());
            }
	catch(SecurityException sexp)
            {
                t.append("Connection Error:"+sexp.toString());
            }
        
	display.setCurrent(t);

        }
	if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        }
        else if (c == record)
	    {
		/*
		  try{
		  if(Rflag==true){
		  if(Pflag==false)
		  {
		  ThreadPlayer.wait();
		  }
		  ThreadRecorder = new Recording();
		  ThreadRecorder.start();
		  Rflag=false;
		  }
		  else
		  {
		  ThreadPlayer.wait();
		  ThreadRecorder.notify();
		  }
		  }
		  catch(InterruptedException ex)
		  {
		  t.append("Recorder"+ex.getMessage().toString());
		  } */
		if(Rflag==true){

                    //ThreadPlayer.wait();

                    ThreadRecorder = new Recording();
                    F = true;
                    ThreadRecorder.start();
                    Rflag=false;
                    return;
		}
		F = true;


	    }
	else if (c == play)
	    {
		/* try{
		   if(Pflag==true)
		   {
		   if(Rflag==false)
		   {
		   ThreadRecorder..wait();
		   }

		   ThreadPlayer = new Playout();
		   ThreadPlayer.start();
		   Pflag=false;
		   }
		   else
		   {
		   ThreadRecorder.wait();
		   ThreadPlayer.notify();
		   }
		   }
		   catch(InterruptedException ex)
		   {
		   t.append("Player"+ex.getMessage().toString());
		   }  */
		if(Pflag==true)
		    {
			F = false;

			ThreadPlayer = new Playout();
			ThreadPlayer.start();
			Pflag=false;
			return;
		    }
		F = false;
	    }
    }

    public class Recording extends Thread{
	private Player p;

        public Recording(){

        }
        public void run(){
            try
		{
		    int i=0;
		    while(true)
			{
			    if(F)
				{
				    i++;
				    p = Manager.createPlayer("capture://audio?encoding=amr");
				    p.realize();
				    RecordControl rc = (RecordControl)p.getControl("RecordControl");
				    ByteArrayOutputStream output = new ByteArrayOutputStream();
				    rc.setRecordStream(output);
				    rc.startRecord();
				    p.start();
				    Thread.currentThread().sleep(500); // record for 20 milliseconds
				    p.stop();
				    rc.stopRecord();
				    rc.commit();
				    int len = output.size();
				    byte[] k =  intToByteArray(len);
				    /*if(output.size() > max_bytes-4)
				      {
				      byte[] tem = output.toByteArray();
				      byte[] t = new byte[max_bytes];
				      for(int cnt = 0; cnt < max_bytes -1; cnt++)
				      t[cnt] = tem[cnt];
				      recordedSoundArray = t;
				      }
				      else
				      {
				      recordedSoundArray = output.toByteArray();
				      }*/
				    int leng =len+k.length;
				    byte[] tem = new byte[leng];
				    System.arraycopy(k, 0, tem, 0, k.length);
				    System.arraycopy(output.toByteArray(), 0, tem,k.length , len);
				    //recordedSoundArray=output.toByteArray();
				    //os.write(recordedSoundArray);
				    if(leng<1022)
					{
					    System.arraycopy(k, 0, tem, 0, k.length);
					    System.arraycopy(output.toByteArray(), 0, tem,k.length , len);
					    Datagram dg = dc.newDatagram(tem, tem.length, server_ip);
					    dc.send(dg);

					}
				    else
					{
					    tem[0]=(byte)3;
					    tem[1]=(byte)254;
					    System.arraycopy(output.toByteArray(), 0, tem,k.length , 1022);
					    Datagram dg = dc.newDatagram(tem, 1024, server_ip);
					    dc.send(dg);
					}
				}
			    else
				Thread.currentThread().sleep(1000);
			}
		}
            catch (IOException ioe)
		{
		    t.append("Error 1:"+ioe.toString());
		}
            catch (MediaException me)
		{
		    t.append("reError 2:"+me.toString());
		}
            catch (InterruptedException ie)
		{
		    t.append("Error 3:"+ie.toString());
		}
	}
    }

    public class Buffer extends Thread {
        int length;
        public  byte[] recd_array=new byte[102400];
        byte[] fetch = new byte[1024];
        public int start=0,end=0,end1=0;
        public boolean flag=false;
        byte[] nullVal = new byte[1];
        byte[] received= new byte[1024];

        public Buffer()
        {

        }

	public void run()
	{
	    int c=0;
	    while(true)
                {
                    try
			{
			    Datagram dg1 = dc.newDatagram(1024);//1024 size of the buffer needed for the datagram.
			    // t.append("buffer");
			    dc.receive(dg1);
			    received = dg1.getData();
			    byte[] len=new byte[2];
			    len[0]=received[0];
			    len[1]=received[1];
			    length=byteArrayToInt(len);
			    length=length-6;

                            if(length!=0 && ring==false)
                            {
                               ring=true;
                               try{
                                   Manager.playTone(60,250,100);
                                   Manager.playTone(70,250,100);
                                   Manager.playTone(80,250,100);
                                   Manager.playTone(90,250,100);
                                   Manager.playTone(100,250,100);
                                   Manager.playTone(90,250,100);
                                   Manager.playTone(80,250,100);
                                   Manager.playTone(70,250,100);
                                   Manager.playTone(60,250,100);
                                  }
                               catch(MediaException m){}


                            }
			    System.arraycopy(received, 8,recd_array , end, length);
			    // t.append(String.valueOf(recd_array));
			    end+=length;
			    t.append("e"+String.valueOf(end));
			    if(end >100000)
				{
				    end1=end;
				    end=0;
				    flag=true;//considerable amount of data has been buffered now play it.
				}
			}
                    catch (IOException ioe)
			{
			    t.append("Error 1:"+ioe.toString());
			}
                    catch (Exception e)
			{
			    t.append("Error "+e.toString());
			}
                }

	}

    }
    public int byteArrayToInt(byte [] b) {
	return  ((b[0] & 0xFF) << 8)
	    + (b[1] & 0xFF);
    }

    public class Playout extends Thread{
        private Player p2;
        public Playout(){

        }

	public void run()
	{

	    try
		{
		    int i=0;
		    int co=0;
		    t.append("Start of do");
		    byte[] recd;
		    int del;
		    int start_p,end_p;
		    while(true)
			{
			    if(!F)	//if F=true then its recording...so can't play the recorded data.
				{
				    if(buff.flag==true)
					{
					    start_p=buff.start;
					    end_p=buff.end1;
					    buff.start=0;
					    buff.flag=false;
					}
				    else
					{
					    start_p=buff.start;
					    end_p=buff.end;
					    buff.start=end_p;
					}
				    int len_toplay=end_p-start_p;
				    byte[] to_play=new byte[len_toplay+6];
				    try
					{
					    t.append(String.valueOf(len_toplay));
					    System.arraycopy(header,0,to_play,0,6);
					    System.arraycopy(buff.recd_array,start_p,to_play,6,len_toplay);
					    if(len_toplay>0)
						{
						    // Datagram dg = dc.newDatagram(to_play,1024, server_ip);
						    //  dc.send(dg);
						    ByteArrayInputStream recordedInputStream = new ByteArrayInputStream(to_play);
						    p2 = Manager.createPlayer(recordedInputStream,"audio/amr");
						    p2.prefetch();
						    p2.start();
						    Thread.currentThread().sleep(601*len_toplay/1000);
						    p2.deallocate();

						}
					    else
						{
						    Thread.currentThread().sleep(2000);
						}
					}
				    catch (MediaException me)
					{
					    t.append("plError 2:"+me.toString());
					}
				    catch (IOException ioe)
					{
					    t.append("Error 1:"+ioe.toString());
					}

				    catch (InterruptedException ie)
					{
					    t.append("Error 3:"+ie.toString());
					}
				}
			    else
				Thread.currentThread().sleep(1000);
			}
		}

            catch (InterruptedException ie)
		{
		    t.append("Error 3:"+ie.toString());
		}
        }

    }
}
