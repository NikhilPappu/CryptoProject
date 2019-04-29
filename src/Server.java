import yao.gate.AndGate;
import yao.gate.Gate;
import yao.gate.Wire;
import yao.gate.XorGate;
import yao.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    public static void main(String[] args) throws Exception{
        Wire a1=new Wire();
		Wire a2=new Wire();
		Wire b1=new Wire();
		Wire b2=new Wire();
		Wire ra=new Wire();
		Wire rb=new Wire();
		
		Gate g1=new XorGate(a1,a2,ra);
		Gate g2=new AndGate(b1,b2,rb);
        //Gate g3=new XorGate(ra,rb,r);
    
        byte[][] lut_g1=g1.getLut();
		byte[][] lut_g2=g2.getLut();
	//	byte[][] lut_g3=g3.getLut();
		byte[] in_a1=a1.getValue1();
		byte[] in_a2=a2.getValue0();
		byte[] in_b1=b1.getValue1();
        byte[] in_b2=b2.getValue1();
    
        Gate gate1=new Gate(lut_g1);
		Gate gate2=new Gate(lut_g2);
	//	Gate gate3=new Gate(lut_g3);
				
        byte[] r1=gate1.operate(in_a2, in_a1);
        System.out.println(Utils.getHex(r1));
    }
}