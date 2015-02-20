package Programming_project_SPECK_v1;

import Programming_project_SPECK.Speck;
import edu.rit.util.Packing;
import edu.rit.util.Hex;

public class Encrypt implements BlockCipher{

	short [] k0= new short[22];
	short [] l0 =new short [22];
	short [] l1 = new short[22];
	short [] l2 = new short [22];
	byte [] plaintext;
	byte[] key;

	public Encrypt(byte [] key, byte []plaintext) {
		this.key= key;
		this.plaintext=plaintext;
	}



	@Override
	public int blockSize() {
		return 32;
	}

	@Override
	public int keySize() {
		return 64;
	}

	@Override
	public void setKey(byte[] key) {
		long key_1= Packing.packLongBigEndian(key, 0);
		k0[0]= (short)(key_1 & 0x000000000000FFFFL);
		l0[0]= (short)((key_1 & 0x00000000FFFF0000L)>>16);
		l1[0]= (short)((key_1 & 0x0000FFFF00000000L)>>32);
		l2[0]= (short)((key_1 & 0xFFFF000000000000L)>>48);
		
		
	}

	@Override
	public void encrypt(byte[] text) {
		/*k0[0]=(short) 0x853a;
		k0[1]=(short) 0x1586;
		k0[2]=(short) 0x0424;*/
		int  you =0;
		int plaintext = Packing.packIntBigEndian(text, 0);
		short  x =(short)((plaintext &  0xFFFF0000)>>16) ;
		short y =(short)((plaintext & 0x0000FFFF));
		for(int i=0; i<3 ;i++){
			x= (short) ((( l_right_rotate(x) + y)^ this.k0[i]) );
			y = (short) (k_left_rotate(y)^x);
			you = x<<16|(y & 0x0000FFFF);
			//System.out.println(Hex.toString(you));
		}
		Packing.unpackIntBigEndian (you, text, 0);
	}

	public void key_schedule(){

		int count=1;
		int l=1,k=1,m=1,j=1;
		int first=0, second=0, third=0;
		for(int i=0; i<3;i++){
			if( count==1){
				l0[l]= (short)((k0[i] + l_right_rotate(l0[first]))^ (short)i);// GF addition is nothing but XOR
				k0[j] = (short)(  k_left_rotate(k0[i])^ l0[l]);
				l++;
				j++;
				first++;
			}

			if(count ==2){
				l1[k]= (short) ((k0[i] +  l_right_rotate(l1[second]))^ (short)i);
				k0[j] =(short) ( k_left_rotate(k0[i]) ^ l1[k]);
				k++;
				j++;
				second++;
			}
			if(count == 3){
				l2[m]= (short) ((k0[i] +l_right_rotate(l2[third]))^(short)i);
				k0[j]= (short) ((k_left_rotate(k0[i])) ^ l2[m]);
				m++;
				j++;
				third++;
			}
			count++;
			if(count>3){
				count=1;
			}
		}
		for( int i=0;i<k0.length;i++)
		System.out.println(Hex.toString(k0[i] ));
	}

	private short l_right_rotate(short s) {
		short x= (short) ((s& 0x0000FFFF)>>7);
		short y= (short) ((s& 0x0000FFFF)<<9);
		short temp = (short) (y|x);
		return temp;
	}

	private short k_left_rotate(short s){
		short y= (short)( (s& 0x0000FFFF)<<2);
		short x= (short)((s& 0x0000FFFF)>>14);
		short temp = (short) (y|x);
		return temp;
	}

	public static void main(String[] args) {
		;

		byte[] key =Hex.toByteArray(args[0]);
		byte[] plaintext =Hex.toByteArray(args[1]);
		Encrypt s= new Encrypt(key, plaintext);
		s.setKey(key);
		s.key_schedule();
		s.encrypt(plaintext);
		System.out.println(Hex.toString(plaintext));

	}

}
