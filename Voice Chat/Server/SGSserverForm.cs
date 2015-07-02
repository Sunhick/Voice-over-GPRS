using System;
using System.Collections.Generic;
using System.Collections;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets;
using System.Media;
namespace Server
{
    
    public partial class SGSserverForm : Form
    {
        int MAX = 10;
        public class table
        {
            public EndPoint edp;
            public int id;
            public table()
            {
                id = 0;
            }
        }

        table[] tab = new table[10];
        byte[] byteD = new byte[1024];
    //    public EndPoint endpoint1;   //Socket of the client
    //    public EndPoint endpoint2;
        public EndPoint e;
        public EndPoint epSender;
        int iRx = 0;
   //     public string strName;      //Name by which the user logged into the chat room

        int count = 0;

        //The main socket on which the server listens to the clients
        Socket serverSocket;
        byte[] byteData = new byte[1024];
              
        string msgReceived = "";
        string msgToSend = "";
        public SGSserverForm()
        {
            InitializeComponent();
            for (int i = 0; i < 10; i++)
            {
                tab[i] = new table();
            }
        }

        private void Form1_Load(object sender, EventArgs e)
        {            
        try
        {
             
	     //   CheckForIllegalCrossThreadCalls = false;

            //We are using UDP sockets
            
            serverSocket = new Socket(AddressFamily.InterNetwork, 
                SocketType.Dgram, ProtocolType.Udp);

            //Assign the any IP of the machine and listen on port number 1000
            IPEndPoint ipEndPoint = new IPEndPoint(IPAddress.Any, 4862);

            //Bind this address to the server
            serverSocket.Bind(ipEndPoint);
            
            IPEndPoint ipeSender = new IPEndPoint(IPAddress.Any, 0);

            //The epSender identifies the incoming clients
            epSender = (EndPoint) ipeSender;
    
            //Start receiving data
            serverSocket.BeginReceiveFrom (byteData, 0, byteData.Length, 
                SocketFlags.None, ref epSender, new AsyncCallback(OnReceive), epSender);                
            
        }
        catch (Exception ex) 
        { 
            MessageBox.Show(ex.Message, "SGSServerUDP", 
                MessageBoxButtons.OK, MessageBoxIcon.Error); 
        }            
    }

        private int find_loc(EndPoint e)
        {
            for(int i = 0; i < MAX; i++)
            {
                object o = new object();
                o = e;
                if (tab[i].id != 0)
                {
                    if (tab[i].edp.Equals(o))
                    {
                        return i;
                    }
                }
                if(tab[i].id == 0)
                {
                    tab[i].edp = e;
                    tab[i].id++;
                    count++;
                    return -1;
                }
            }
            return -2;
        }

        private void OnReceive(IAsyncResult ar)
        {            
            try
            {
                 
                IPEndPoint ipeSender = new IPEndPoint(IPAddress.Any, 0);
                EndPoint epS = (EndPoint)ipeSender; 
                serverSocket.EndReceiveFrom (ar, ref epS);                
                if (count != MAX)
                {
                    int temp;
                    if ((temp = find_loc(epS)) != -1)
                    {
                        send(temp);
                        /*string t = byteData.ToString();
                        t = t.Trim('\0');
                        txtLog.Text += t;*/
                    }
                }
                try
                {
                    byteData = new byte[1024];
                    serverSocket.BeginReceiveFrom(byteData, 0, byteData.Length, SocketFlags.None, ref epSender,
                            new AsyncCallback(OnReceive), epSender);
                    return;
                }
                catch (Exception ex)
                {

                } 
            }
            catch (Exception ex)
            { 
                MessageBox.Show(ex.Message, "SGSServerUDP", MessageBoxButtons.OK, MessageBoxIcon.Error); 
            }
        }

        private void send(int temp)
        {
            System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
            msgReceived = enc.GetString(byteData);
            Object objData = msgReceived.Trim('\0');
            byte[] message = System.Text.Encoding.ASCII.GetBytes(objData.ToString()); 
            for (int i = 0; i < MAX; i++)
            {
                if (tab[i].id != 0 && i != temp)
                {
                    serverSocket.SendTo(byteData, tab[i].edp);
                }
            }
        }

        private void txtLog_TextChanged(object sender, EventArgs e)
        {

        }
    }
}