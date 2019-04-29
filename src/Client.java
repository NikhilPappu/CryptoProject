import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Scanner;

import gate.*; 


public class Client
{
 
    private static Socket socket;
 
    public static void main(String args[]) throws Exception
    {
        try
        {
            String host = "localhost";
            int port = 25000;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter b2:");
            int b2_in = sc.nextInt();
            System.out.println("Enter b1:");
            int b1_in = sc.nextInt();

            //Send the message to the server
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
 
            String number = "2";
 
            String sendMessage = number + "\n";
            
            System.out.println();    
            KeyPair kp = Utils.genRSAkeypair();
            PrivateKey sk = kp.getPrivate();
            Key pk = kp.getPublic();
            Key pk2 = Utils.genRSAkeypair().getPublic();
            
            byte [] pkBytes = pk.getEncoded();
            byte [] pkBytes2 = pk2.getEncoded();

            String pks = Utils.getHex(pkBytes);
            String pks2 = Utils.getHex(pkBytes2);
            
            if(b1_in == 0){
                bw.write(pks  + "\n"); // b = 0
                bw.write(pks2  + "\n");
            }
            else if(b1_in == 1){
                bw.write(pks2  + "\n"); // b = 1
                bw.write(pks  + "\n");
            }
            KeyPair kp2 = Utils.genRSAkeypair();
            PrivateKey sk2 = kp2.getPrivate();
            Key pkk = kp2.getPublic();
            Key pkk2 = Utils.genRSAkeypair().getPublic();
            
            byte [] pkkBytes = pkk.getEncoded();
            byte [] pkkBytes2 = pkk2.getEncoded();

            String pkks = Utils.getHex(pkkBytes);
            String pkks2 = Utils.getHex(pkkBytes2);
            
            if(b2_in == 0){
                bw.write(pkks  + "\n"); // b = 0
                bw.write(pkks2  + "\n");
            }
            else if(b2_in == 1){
                bw.write(pkks2  + "\n"); // b = 1
                bw.write(pkks  + "\n");
            }

            bw.flush();
           // System.out.println("Message sent to the server : "+sendMessage);
 
            //Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            byte[][] lut_g1= new byte [3][];
		    byte[][] lut_g2= new byte [3][];
            byte[][] lut_g3= new byte [3][];
            byte[][] lut_g4= new byte [3][];
		    byte[][] lut_g5= new byte [3][];
            byte[][] lut_g6= new byte [3][];
            byte[][] lut_g7= new byte [3][];
		    byte[][] lut_g8= new byte [3][];
            byte[][][] lut=new byte[8][][];
            
            
            lut[0]=lut_g1;
            lut[1]=lut_g2;
            lut[2]=lut_g3;
            lut[3]=lut_g4;
            lut[4]=lut_g5;
            lut[5]=lut_g6;
            lut[6]=lut_g7;
            lut[7]=lut_g8;

            String cipher1 = br.readLine();
            String cipher2 = br.readLine();
            String cipher_b1 = "";
            if(b1_in == 0){
               cipher_b1 = cipher1;
            }
            else if(b1_in == 1){
                cipher_b1 = cipher2;
            }
            String cipher3 = br.readLine();
            String cipher4 = br.readLine();
            String cipher_b2 = "";
            if(b2_in == 0){
                cipher_b2 = cipher3;
             }
             else if(b2_in == 1){
                 cipher_b2 = cipher4;
             }

            String s_in_a1 = br.readLine();
            String s_in_a2 = br.readLine();
            String s_in_b1 = Utils.getHex(Utils.RSAdecrypt(Utils.hextoByte(cipher_b1), sk));
            String s_in_b2 = Utils.getHex(Utils.RSAdecrypt(Utils.hextoByte(cipher_b2), sk2));
    
            String ra0 = br.readLine();
            String ra1 = br.readLine();
            String re0 = br.readLine();
            String re1 = br.readLine();
            String rg0 = br.readLine();
            String rg1 = br.readLine();
            String rh0 = br.readLine();
            String rh1 = br.readLine();


            for(int j=0;j<8;j++){
                for(int i=0; i<3; i++){
                String s1= br.readLine();
                lut[j][i] = Utils.hextoByte(s1);
                }
            }

            Gate gate1=new Gate(lut_g1);
            Gate gate2=new Gate(lut_g2);
            Gate gate3=new Gate(lut_g3);
            Gate gate4=new Gate(lut_g4);
            Gate gate5=new Gate(lut_g5);
            Gate gate6=new Gate(lut_g6);
            Gate gate7=new Gate(lut_g7);
            Gate gate8=new Gate(lut_g8);    

            byte[] ra=gate1.operate(Utils.hextoByte(s_in_b1), Utils.hextoByte(s_in_a1));
            byte[] rb=gate2.operate(Utils.hextoByte(s_in_b1), Utils.hextoByte(s_in_a2));
            byte[] rc=gate3.operate(Utils.hextoByte(s_in_b2), Utils.hextoByte(s_in_a1));
            byte[] rd=gate4.operate(Utils.hextoByte(s_in_b2), Utils.hextoByte(s_in_a2));
            byte[] re=gate5.operate(rc, rb);
            byte[] rf=gate6.operate(rc, rb);
            byte[] rg=gate7.operate(rd, rf);
            byte[] rh=gate8.operate(rd, rf);
      
      
            if (Utils.getHex(rh).equals(rh0)){
                System.out.print("0");
            }
            else if (Utils.getHex(rh).equals(rh1)){
                System.out.print("1");
            }
            if (Utils.getHex(rg).equals(rg0)){
                System.out.print("0");
            }
            else if (Utils.getHex(rg).equals(rg1)){
                System.out.print("1");
            }
            if (Utils.getHex(re).equals(re0)){
                System.out.print("0");
            }
            else if (Utils.getHex(re).equals(re1)){
                System.out.print("1");
            }
            if (Utils.getHex(ra).equals(ra0)){
                System.out.print("0");
            }
            else if (Utils.getHex(ra).equals(ra1)){
                System.out.print("1");
            }

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            //Closing the socket
            try
            {
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}