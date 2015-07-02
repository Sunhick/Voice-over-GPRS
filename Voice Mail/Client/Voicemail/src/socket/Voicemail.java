package socket;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.lang.*;
import java.util.*;
import java.io.IOException;
import javax.microedition.io.*;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;
import javax.microedition.io.ConnectionNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;
import javax.microedition.media.control.*;
import javax.microedition.media.*;

public class Voicemail extends MIDlet implements CommandListener {

    private Display display;

    private Form f;

    private StringItem si;

    private TextField tf;
    private TextField us;
    private TextField ps;
    private List lb;
    private boolean stop;
private Command selCommand = new Command("Select", Command.ITEM, 1);
    private Command startCommand = new Command("Send VM", Command.ITEM, 1);
    private Command recvCommand = new Command("Receive VM", Command.ITEM, 1);
    private Command stopCommand = new Command("Stop", Command.ITEM, 1);
    private Command exitCommand = new Command("Exit", Command.EXIT, 1);
    private Command connCommand = new Command("Connect", Command.ITEM, 1);
 //   private Command exitCommand = new Command("E", Command.ITEM, 1);
//    InputStream is;
    byte[] to_send;
    OutputStream os;
    InputStream is;
    String dest;
    String user_name;

    SocketConnection sc;
    String server_ip="socket://27.61.242.219:4861";

 //   Sender sender;

//    private int port;

    private Player p;

    ByteArrayOutputStream op = new ByteArrayOutputStream();
    RecordControl rc;

    public Voicemail() {
    try
    {
    sc = (SocketConnection) Connector.open(server_ip);//("socket://124.247.216.133:4861");
    }catch (ConnectionNotFoundException cnfe) {

	} catch (IOException ioe) {

	} 
    display = Display.getDisplay(this);
	f = new Form("Voice Mail");
 //   si = new StringItem("To:", " ");
    tf = new TextField("To User", "", 30, TextField.ANY);
    us = new TextField("User Name", "", 30, TextField.ANY);
    ps = new TextField("Password", "", 30, TextField.ANY);
  //  f.append(si);
    f.append(us);
    f.append(ps);
    f.addCommand(connCommand);
	f.addCommand(exitCommand);
//	f.addCommand(startCommand);
	f.setCommandListener(this);
	display.setCurrent(f);
    }

    /**
         * Start the client thread
         */
    public void isPaused() {
//	return isPaused;
    }

    public void startApp() {
//	isPaused = false;
    }

    public void pauseApp() {
//	isPaused = true;
    }

    public void destroyApp(boolean unconditional) {

	//if (client != null) {
	//    client.stop();
//	}
    }


    public static final byte[] intToByteArray1(int value) {
        return new byte[]{(byte)(value >> 16 & 0xff),
        (byte)(value >> 8 & 0xff), (byte)(value & 0xff) };
    }
    public void commandAction(Command c, Displayable s) {
        if(c == connCommand)
        {
            user_name = us.getString();
            byte[] mess = new byte[2+us.size()+1+ps.size()];
            mess[0]= (byte)250;
            mess[1] = (byte)255;
            System.arraycopy(us.getString().getBytes(), 0, mess, 2, us.size());
            System.arraycopy(ps.getString().getBytes(), 0, mess, 3+us.size(), ps.size());
            mess[2+us.size()] = (byte)58;   // ascii for :

            try {
	    
	    os = sc.openOutputStream();
        os.write(mess);
    //    os.close();
        mess = new byte[2];
        is = sc.openInputStream();
        is.read(mess);
        int ty = (int)mess[0];
        f.append(String.valueOf(ty));
        if(mess[0] == (byte)249)  
        {
            f.deleteAll();
            f.removeCommand(connCommand);
            f.addCommand(startCommand);
            f.addCommand(recvCommand);
            f.append(tf);
   //         is.close();
          //  sc.close();
        }
        else
        {
            // alert for negative auth
            
        }

        
	} catch (ConnectionNotFoundException cnfe) {

	} catch (IOException ioe) {

	} catch (Exception e) {

	}
        }
        if(c == recvCommand)
        {
            try {
            byte[] mess = new byte[2+user_name.length()];
            mess[0] = (byte)247;
            mess[1] = (byte)255;
            System.arraycopy(user_name.getBytes(), 0, mess, 2, user_name.length());
         //   os = sc.openOutputStream();
            os.write(mess);

        //    os.close();
            mess = new byte[102];
        //    is = sc.openInputStream();
            is.read(mess);
         //   int ty = (int)mess[0];
         //   f.append(String.valueOf(ty));
            if(mess[0] == (byte)246)
            {
                // alert no voice mail
          //  sc.close();
            }
            if(mess[0] ==(byte)245)
            {
                f.deleteAll();
                f.removeCommand(startCommand);
              //  f.addCommand(selCommand);
                f.removeCommand(recvCommand);
                
                byte[] temp =new byte[100];
                System.arraycopy(mess, 2, temp, 0, 98);
                String tem = new String(temp);
                tem = tem.trim();
          //      f.append(tem);
                String[] arr = new String[10];
                arr = split(tem,":");
                
                lb = new List("Files", Choice.IMPLICIT, arr, null);
                lb.addCommand(selCommand);
                lb.addCommand(exitCommand);
                lb.setCommandListener(this);
                display.setCurrent(lb);
              
    //            is.close();
            }
            } catch (ConnectionNotFoundException cnfe) {
            f.append("e1");
	} catch (IOException ioe) {
f.append("e2");
	} catch (Exception e) {
f.append("e3: " + e.toString());
	}
        }
        if(c == selCommand)
        {
            int len_fl = 0;
            try{
            if(lb.getSelectedIndex() == -1)
            {
                // alert no file selected
            }
            else
            {
            //  f.removeCommand(selCommand);
                lb.removeCommand(selCommand);
                String s_t = lb.getString(lb.getSelectedIndex());
                byte[] se = new byte[2+s_t.length()];
                se[0] = (byte)244;
                se[1] = (byte)255;
                System.arraycopy(s_t.getBytes(), 0, se, 2, s_t.length());
        //        os.flush();
       //         os = sc.openOutputStream();
                os.write(se);
        //        os.close();
                byte[] array = new byte[100000];
                se = new byte[7];
                //len_fl = is.read(se);
                len_fl = is.read(se);
                int r_len = ((se[2] & 0xFF) << 16) + ((se[3] & 0xFF) << 8) + (se[4] & 0xFF);
                f.append(String.valueOf(len_fl)+"read ");
                f.append(String.valueOf(r_len)+"asd");
                int cnt =0;
                while(cnt < r_len)
                {
                se = new byte[10000];
       //         is = sc.openInputStream();
               // is.reset();
                len_fl = is.read(se);
                

                if(cnt == 0)
                    System.arraycopy(se, 2, array, cnt, len_fl-2);
                else
                    System.arraycopy(se, 0, array, cnt, len_fl);
                cnt += len_fl;
                }
                f.append(String.valueOf(cnt) + "here ");
                ByteArrayInputStream recordedInputStream = new ByteArrayInputStream(array);
                p = Manager.createPlayer(recordedInputStream,"audio/amr");
                p.prefetch();
                p.start();
                Thread.currentThread().sleep(600*cnt/1000);
                p.deallocate();
           //     f.addCommand(selCommand);
                lb.addCommand(selCommand);
                lb.delete(lb.getSelectedIndex());
            }
            } catch (ConnectionNotFoundException cnfe) {
                display.setCurrent(f);
            f.append("1" + cnfe.toString());
	} catch (IOException ioe) {
        display.setCurrent(f);
f.append("2" + ioe.toString());
	} catch (Exception e) {
        display.setCurrent(f);
        f.append(String.valueOf(len_fl));
f.append("3" + e.toString());
	}
        }
	if (c == startCommand) {
	    dest = tf.getString();
        if(dest.equals(""))
        {
            // create alert to display msg
            f.append("Enter Receiver's User name");
            return;
        }
        f.deleteAll();
        f.removeCommand(startCommand);
        f.removeCommand(recvCommand);
        f.addCommand(stopCommand);
        Thread q =new Thread()
            {
                public void run(){
                
            try
            {

                p = Manager.createPlayer("capture://audio?encoding=amr");
                p.realize();
                rc = (RecordControl)p.getControl("RecordControl");

                rc.setRecordStream(op);

                rc.startRecord();
                p.start();
               // Thread.currentThread().sleep(8000);
                
            }
            catch (IOException ioe)
            {
                f.append("Error 1:"+ioe.toString());
            }
           catch (MediaException me)
            {
               f.append("reError 2:"+me.toString());
            }
            
            
                }
            };q.start();
	}

	if (c == stopCommand) {
        int length = 0;

        try
            {
                p.stop();
                rc.stopRecord();
                rc.commit();
                length=op.size();


          }
            catch (IOException ioe)
            {
                f.append("stopError 1:"+ioe.toString());
            }
           catch (MediaException me)
            {
               f.append("stopError 2:"+me.toString());
            }
            
                try {
	 //   sc = (SocketConnection) Connector.open("socket://124.247.216.133:4861");


	//    os = sc.openOutputStream();

        byte[] len=intToByteArray1(length);
        byte[] c_data = new byte[5 + dest.length()];
        c_data[0] = (byte)240;
        c_data[1] = (byte)255;
        c_data[2] = len[0];
        c_data[3] = len[1];
        c_data[4] = len[2];
        System.arraycopy(dest.getBytes(), 0, c_data, 5, dest.length());
	    os.write(c_data);
        Thread.currentThread().sleep(6000);
        f.removeCommand(stopCommand);
//create an alert saying msg sent
        os.write(op.toByteArray());
        f.addCommand(startCommand);
        f.addCommand(recvCommand);

        //f.deleteAll();

	  //  stop();

	} catch (ConnectionNotFoundException cnfe) {
        f.append("stopconn 1");
	} catch (IOException ioe) {
        f.append("stopconn 2");
	} catch (Exception e) {
        f.append("stopconn 3");
	}
	}
    if(c == exitCommand)
    {
        destroyApp(false);
        notifyDestroyed();

    }
    }

    private String[] split(String original, String separator) {
Vector nodes = new Vector();

// Parse nodes into vector
int index = original.indexOf(separator);
while (index >= 0) {
nodes.addElement(original.substring(0, index));
original = original.substring(index + separator.length());
index = original.indexOf(separator);
}
// Get the last node
nodes.addElement(original);

// Create splitted string array
String[] result = new String[nodes.size()];
if (nodes.size() > 0) {
for (int loop = 0; loop < nodes.size(); loop++) {
result[loop] = (String) nodes.elementAt(loop);
}
}
return result;
}
    /**
         * Close all open streams
         */
 /*   public void stop() {
	try {
	    stop = true;

	    if (sender != null) {
		sender.stop();
	    }

	    if (is != null) {
		is.close();
	    }

	    if (os != null) {
		os.close();
	    }

	    if (sc != null) {
		sc.close();
	    }
	} catch (IOException ioe) {
	}
    } */
}
