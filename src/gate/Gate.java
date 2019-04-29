package gate;

import java.util.Random;

public class Gate 
{
	byte[][] lut=new byte[4][];
	
	public Gate(){}
	
	public Gate(byte[] l1,byte[] l2,byte[] l3,byte[] l4)
	{
		lut[0]=l1;
		lut[1]=l2;
		lut[2]=l3;
		lut[3]=l4;
	}
	
	public Gate(byte[][] lut)
	{
		this.lut=lut;
	}
	
	public byte[] operate(byte[] key1, byte[] key2) throws Exception
	{
		byte[] result1=null;
		byte[] result2=null;
	
		for(int i=0;i<3;i++)
		{
			result1=Utils.AESdecrypt(lut[i],key1);
			result2=Utils.AESdecrypt(result1,key2);
			
			if(result2[0]==0x12&&result2[1]==0x33&&result2[2]==0x21)
				return result2;
			else{
				byte [] zero = new byte[]{0x12,0x33,0x21,0,0,0,0,0,0,0,0,0,0,0,0,0};

				result1=Utils.AESdecrypt(zero,key2);
				result2=Utils.AESdecrypt(result1,key1);
		
				if(result2[0]==0x12&&result2[1]==0x33&&result2[2]==0x21)
						return result2;
			
			}
		}
		
		return null;
	
	}
	
	void genEncryptedLut(int i00,int i01,int i10,int i11,Wire i1, Wire i2, Wire r) throws Exception
	{	
		byte [] zero = new byte[]{0x12,0x33,0x21,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//encrypt
		lut[0]=Utils.AESencrypt(Utils.AESencrypt(r.value[i00],i1.value[0]),i2.value[0]);
		lut[1]=Utils.AESencrypt(Utils.AESencrypt(r.value[i01],i1.value[1]),i2.value[0]);
		lut[2]=Utils.AESencrypt(Utils.AESencrypt(r.value[i10],i1.value[0]),i2.value[1]);
		if(i11 == 0)
			r.setValue0(Utils.AESdecrypt(Utils.AESdecrypt(zero, i2.value[1]), i1.value[1]));
		else if(i11 == 1)
			r.setValue1(Utils.AESdecrypt(Utils.AESdecrypt(zero, i2.value[1]), i1.value[1]));
		//lut[3]=Utils.AESencrypt(Utils.AESencrypt(r.value[i11],i1.value[1]),i2.value[1]);
		
		//shuffle
		Random rand=new Random();
		if(rand.nextBoolean())
			Utils.swap(lut[0],lut[1]);
		if(rand.nextBoolean())
			Utils.swap(lut[2],lut[1]);
		if(rand.nextBoolean())
			Utils.swap(lut[0],lut[2]);
		// if(rand.nextBoolean())
		// 	Utils.swap(lut[1],lut[3]);
	}
	
	public byte[] getLutEntry(int i)
	{
		return lut[i];
	}
	
	public byte[][] getLut()
	{
		return lut;
	}

}
