using System;
using System.IO;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets;
using System.Data.SqlTypes;
using System.Data.SqlClient;
using System.Data.Common;
using System.Collections;
using System.Globalization;
using System.Reflection;
using System.Threading;

namespace SocketServer
{
    public partial class SocketServer : Form
    {
        const int MAX_CLIENTS = 100;
        int MAX = 100;
        public table[] tab = new table[10];
        String PORT = "5000";
        public class ep
        {
            public string usr;
            public EndPoint e;
            //    public int index;
        }

        public static Hashtable ep_tab = new Hashtable();

        //   public ep[] ep_tab = new ep[10];  

        private Socket m_mainSocket;
        //    private SocketPacket[] m_workersocpac = new SocketPacket[MAX_CLIENTS];  
        private int m_clientCount = 0;
        private int max_p_i = 0;
        public int count = 0;
        //   System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
        public class table
        {
            public ArrayList l = new ArrayList();
            public Socket soc;
            public int id;
            public string user;
            public byte[] recd;
            public byte[] res;

            public table()
            {
                id = 0;
                recd = new byte[100];
            }

            // Start waiting for data from the client
            public void WaitForData()
            {
                try
                {
                    recd = new byte[100];
                    soc.Receive(recd);
                    process();
                    WaitForData();
                }
                catch (SocketException se)
                {
                    MessageBox.Show(se.ToString());
                    /*soc.logoutUser(soc.username);
                    soc.state = 0;
                    return;*/
                }

            }

            public void process()
            {
                //args = msg;
                execute_sp();
                if (res != null)
                    soc.Send(res);
                res = null;
            }

            public void execute_sp()
            {
                if (recd[1] == 255)
                {
                    switch (recd[0])
                    {
                        case 250:
                            {
                                /* get the username n pass n auth by querying db */
                                int r = 1;
                                if (r == 0)
                                {

                                }
                                else if (r == 1)
                                {
                                    res = new byte[2];
                                    res[0] = 249;
                                    res[1] = 255;
                                    System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
                                    string t = enc.GetString(recd);
                                    t = t.Remove(0, 2).TrimEnd('\0');
                                    string[] t_arr = t.Split(':');
                                    user = t_arr[0];
                                    ep e_tem = new ep();
                                    e_tem.e = soc.RemoteEndPoint;
                                    e_tem.usr = user;
                                    ep_tab.Add(user, e_tem);
                                }
                            }
                            break;
                        case 247:
                            {
                                System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
                                string t = enc.GetString(recd);
                                t = t.Remove(0, 2).TrimEnd('\0');
                                user = t;

                                BinaryReader binr_list;
                                byte[] buf_list = new byte[100];
                                binr_list = new BinaryReader(File.Open("D:\\" + t + "\\list.txt", FileMode.Open));
                                int r_cnt = binr_list.Read(buf_list, 0, 100);
                                binr_list.Close();
                                if (r_cnt == 0)
                                {
                                    res = new byte[2];
                                    res[0] = 246;
                                    res[1] = 255;
                                }
                                else
                                {
                                    res = new byte[r_cnt + 2];
                                    res = System.Text.Encoding.ASCII.GetBytes("  " + enc.GetString(buf_list));
                                    res[0] = 245;
                                    res[1] = 255;
                                }
                            }
                            break;
                        case 244:
                            {
                                System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
                                string t = enc.GetString(recd);
                                t = t.Remove(0, 2);
                                BinaryReader binr_list;
                                byte[] buf_list = new byte[100000];
                                t = t.TrimEnd('\0');
                                binr_list = new BinaryReader(File.Open(@"D:\" + user + "\\" + t, FileMode.Open));
                                int r_cnt = binr_list.Read(buf_list, 2, 100000 - 2);
                                binr_list.Close();

                                byte[] b = new byte[3];
                                b = intToByteArray1(r_cnt);
                                byte[] bs = new byte[5];
                                bs[0] = 233;
                                bs[1] = 255;
                                bs[2] = b[0];
                                bs[3] = b[1];
                                bs[4] = b[2];
                                soc.Send(bs);

                                res = new byte[r_cnt + 2];
                                for (int t_cnt = 2; t_cnt < r_cnt; t_cnt++)
                                    res[t_cnt] = buf_list[t_cnt];

                                res[0] = 243;
                                res[1] = 255;
                            }
                            break;
                        // control cha 242 n 241 reserved
                        case 240:
                            {
                                System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
                                byte[] len = new byte[3];
                                len[0] = recd[2];
                                len[1] = recd[3];
                                len[2] = recd[4];
                                int length = ((len[0] & 0xFF) << 16) + ((len[1] & 0xFF) << 8) + (len[2] & 0xFF);
                                string t = enc.GetString(recd);
                                t = t.Remove(0, 5);
                                t = t.TrimEnd('\0');
                                int cnt = 0;
                                byte[] to_wr = new byte[2000000];
                                while (cnt < length)
                                {
                                    recd = new byte[1000000];
                                    int c = soc.Receive(recd);
                                    //  string t = enc.GetString(recd);
                                    //  t.TrimEnd('\0');
                                    for (int temp = 0; temp < c; temp++)
                                        to_wr[cnt + temp] = recd[temp];
                                    cnt += c;
                                }
                                //    user = "vin";
                                BinaryWriter bin = new BinaryWriter(File.Open("D:\\" + t + "\\" + user + ".amr", FileMode.Create));
                                bin.Write(to_wr, 0, length);
                                bin.Close();
                                bin = new BinaryWriter(File.Open("D:\\" + t + "\\list.txt", FileMode.Append));
                                string g = ":" + user + ".amr";
                                byte[] b_list = System.Text.Encoding.ASCII.GetBytes(g);
                                bin.Write(b_list);
                                bin.Close();
                            }
                            break;
                        case 239:
                            {
                                System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
                                string t = enc.GetString(recd);
                                t = t.Remove(0, 2);
                                t = t.TrimEnd('\0');
                                //    int pos = find(t);
                                EndPoint rp = get_rep(t);
                                if (rp == null)
                                {
                                    // return negative code.. user not online..

                                }
                                else
                                {
                                    l.Add(rp);
                                    // some handshake bet sender n receiver reqd..

                                }
                            }
                            break;
                        default:
                            {
                                if (l.Count > 0)
                                    forward();
                            }
                            break;

                    }
                }
                //    recd = new byte[100];
            }

            void forward()
            {
                foreach (EndPoint p in l)
                {
                    soc.SendTo(recd, p);
                }
            }

            public EndPoint get_rep(string s)
            {
                /*
                for (int i = 0; i < 10; i++)
                {
                    if (ep_tab[i].usr != null)
                        if (s.Equals(ep_tab[i].usr))
                            return ep_tab[i].e;
                }
                return null;*/
                if (ep_tab.ContainsKey(s))
                {
                    ep r = (ep)ep_tab[s];
                    return r.e;
                }
                return null;
            }
        }



        public int hash(int i, int x)
        {
            return ((x + i) % MAX_CLIENTS);
        }

        /*    public int find(string s)
            {
                /*for (int i = 0; i < 10; i++)
                {
                    if (ep_tab[i].usr != null)
                        if (s.Equals(ep_tab[i].usr))
                            return ep_tab[i].index;
                }
                return -1;
                if (ep_tab.ContainsKey(s))
                {
                    ep e = ep_tab[s];
                    return e.index;
                }
                return -1;
            } */


        public SocketServer()
        {
            InitializeComponent();
            for (int i = 0; i < 10; i++)
                tab[i] = new table();
        }

        private void SocketServer_Load(object sender, EventArgs e)
        {
            try
            {
                string portStr = PORT;
                int port = System.Convert.ToInt32("4861");

                // Create the listening socket...
                m_mainSocket = new Socket(AddressFamily.InterNetwork,
                                          SocketType.Stream,
                                          ProtocolType.Tcp);
                IPEndPoint ipLocal = new IPEndPoint(IPAddress.Any, port);
                // Bind to local IP Address...
                m_mainSocket.Bind(ipLocal);
                // Start listening...
                m_mainSocket.Listen(4);
                // Create the call back for any client connections...
                m_mainSocket.BeginAccept(new AsyncCallback(OnClientConnect), null);

            }
            catch (SocketException se)
            {
                MessageBox.Show(se.Message);
            }
        }

        public static byte[] intToByteArray1(int value)
        {
            return new byte[]{(byte)(value >> 16 & 0xff),
        (byte)(value >> 8 & 0xff), (byte)(value & 0xff) };
        }

        public void OnClientConnect(IAsyncResult asyn)
        {
            try
            {

                //MessageBox.Show("Connected");
                // Here we complete/end the BeginAccept() asynchronous call
                // by calling EndAccept() - which returns the reference to
                // a new Socket object
                int i = 0, new_index = 0;
                if (m_clientCount == MAX_CLIENTS)
                {
                    // here code should be included to send msg to client that 
                    // server is busy. on the server all the available sockets are being used

                    Socket rsoc = m_mainSocket.EndAccept(asyn);
                    m_mainSocket.BeginAccept(new AsyncCallback(OnClientConnect), null);
                    rsoc.Close();
                    return;
                }

                while (true)
                {
                    new_index = hash(i, m_clientCount);

                    if (tab[new_index].id == 0)
                        break;
                    i++;
                }

                tab[new_index].soc = m_mainSocket.EndAccept(asyn);
                tab[new_index].id = 1;

                // Let the worker Socket do the further processing for the 
                // just connected client
                //     tab[new_index].recd = new byte[100];

                // Now increment the client count

                ++m_clientCount;

                // Since the main Socket is now free, it can go back and wait for
                // other clients who are attempting to connect
                m_mainSocket.BeginAccept(new AsyncCallback(OnClientConnect), null);

                //    WaitForData(tab[new_index]);
                //     new Thread(tab[new_index].WaitForData);
                tab[new_index].WaitForData();


            }
            catch (ObjectDisposedException)
            {
                System.Diagnostics.Debugger.Log(0, "1", "\n OnClientConnection: Socket has been closed\n");
            }
            catch (SocketException se)
            {
                MessageBox.Show(se.Message);
            }

        }



    }
}