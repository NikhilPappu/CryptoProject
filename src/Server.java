import gate.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

public class Server
{
 
    private static Socket socket;
 
    public static void main(String[] args) throws Exception
    {
        Wire a1=new Wire();
		Wire a2=new Wire();
		Wire b1=new Wire();
		Wire b2=new Wire();
		Wire ra=new Wire();
        Wire rb=new Wire();
        Wire rc=new Wire();
        Wire rd=new Wire();
	    Wire re=new Wire();
        Wire rf=new Wire();
        Wire rg=new Wire();
        Wire rh=new Wire();
		Gate g1=new AndGate(a1,b1,ra);
		Gate g2=new AndGate(a2,b1,rb);
        Gate g3=new AndGate(a1,b2,rc);
        Gate g4=new AndGate(a2,b2,rd);
        Gate g5=new XorGate(rb,rc,re);
        Gate g6=new AndGate(rb,rc,rf); 
        Gate g7=new XorGate(rf,rd,rg);
        Gate g8=new AndGate(rf,rd,rh);

		byte[][] lut_g1=g1.getLut();
		byte[][] lut_g2=g2.getLut();
        byte[][] lut_g3=g3.getLut();
        byte[][] lut_g4=g4.getLut();
		byte[][] lut_g5=g5.getLut();
        byte[][] lut_g6=g6.getLut();
        byte[][] lut_g7=g7.getLut();
        byte[][] lut_g8=g8.getLut();

        byte[] in_a2 = {0x12, 0x12}, in_a1 = {0x12, 0x12};
        //-------Inputs------------
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a2:");
        int a2_in = sc.nextInt();
        System.out.println("Enter a1:");
        int a1_in = sc.nextInt();

        if(a2_in == 0)
            in_a2 = a2.getValue0();
        else if(a2_in == 1)
            in_a2 = a2.getValue1();

        if(a1_in == 0)
           in_a1 = a1.getValue0();
        else if(a1_in == 1)
            in_a1 = a1.getValue1();

        //-------------------------

        byte[][][] lut=new byte[8][][];
        lut[0]=lut_g1;
        lut[1]=lut_g2;
        lut[2]=lut_g3;
        lut[3]=lut_g4;
        lut[4]=lut_g5;
        lut[5]=lut_g6;
        lut[6]=lut_g7;
        lut[7]=lut_g8;
        

		String s_in_a1 = Utils.getHex(in_a1);
        String s_in_a2 = Utils.getHex(in_a2);

        try
        {
 
            int port = 25000;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 25000");
 
            while(true)
            {
               
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String publicKey1 = br.readLine();
                String publicKey2 = br.readLine();

                String publicKey3 = br.readLine();
                String publicKey4 = br.readLine();

                PublicKey pk1 = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Utils.hextoByte(publicKey1)));
                PublicKey pk2 = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Utils.hextoByte(publicKey2)));

                PublicKey pk3 = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Utils.hextoByte(publicKey3)));
                PublicKey pk4 = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Utils.hextoByte(publicKey4)));

                byte[] cipher1=Utils.RSAencrypt(b1.getValue0(), pk1);
                byte[] cipher2=Utils.RSAencrypt(b1.getValue1(), pk2);

                byte[] cipher3=Utils.RSAencrypt(b2.getValue0(), pk3);
                byte[] cipher4=Utils.RSAencrypt(b2.getValue1(), pk4);
                

 
                //Sending the response back to the client.
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                
                bw.write(Utils.getHex(cipher1) + "\n");
                bw.write(Utils.getHex(cipher2) + "\n");
                bw.write(Utils.getHex(cipher3) + "\n");
                bw.write(Utils.getHex(cipher4) + "\n");


                String ra0 = Utils.getHex(ra.getValue0());
                String ra1 = Utils.getHex(ra.getValue1());
                String re0 = Utils.getHex(re.getValue0());
                String re1 = Utils.getHex(re.getValue1());
                String rg0 = Utils.getHex(rg.getValue0());
                String rg1 = Utils.getHex(rg.getValue1());
                String rh0 = Utils.getHex(rh.getValue0());
                String rh1 = Utils.getHex(rh.getValue1());
                
                bw.write(s_in_a1 + "\n");
                bw.write(s_in_a2 + "\n");
                
                bw.write(ra0 + "\n");
                bw.write(ra1 + "\n");
                bw.write(re0 + "\n");
                bw.write(re1 + "\n");
                bw.write(rg0 + "\n");
                bw.write(rg1 + "\n");
                bw.write(rh0 + "\n");
                bw.write(rh1 + "\n");

                for(int j=0;j<8;j++){
                for(int i=0;i<3;i++){
                    String s1=Utils.getHex(lut[j][i]);
                    bw.write(s1 + "\n");
                }
			}
			bw.flush();
        }
    }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(Exception e){}
		}

    }
}