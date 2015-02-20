/*package Programming_project_SPECK_v1;

import edu.rit.util.Hex;
import edu.rit.util.Packing;

public class Decrypt {

	short [] k0= new short[22];
	short [] l0 =new short [22];
	short [] l1 = new short[22];
	short [] l2 = new short [22];
	byte[] key;
	byte[] plaintext;
	public Decrypt(byte [] key, byte []plaintext) {
		this.key= key;
		this.plaintext=plaintext;
	}


	public void setKey(byte[] key) {
		long key_1= Packing.packLongBigEndian(key, 0);
		k0[0]= (short)(key_1 & 0x000000000000FFFFL);
		l0[0]= (short)((key_1 & 0x00000000FFFF0000L)>>16);
		l1[0]= (short)((key_1 & 0x0000FFFF00000000L)>>32);
		l2[0]= (short)((key_1 & 0xFFFF000000000000L)>>48);
	}


	public int blockSize() {
		return 32;
	}

	public int keySize() {
		return 64;
	}

	public void key_schedule1(){
		int count=1;
		int l=1,k=1,m=1,j=1;
		int first=0, second=0, third=0;
		for(int i=0; i<21;i++){
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

	private short right_rotate_by_2(short s){
		short y= (short)( (s& 0x0000FFFF)>>2);
		short x= (short)((s& 0x0000FFFF)<<14);
		short temp = (short) (y|x);
		return temp;
	}
	
	private short left_rotate_by_7(short s){
			short x= (short) ((s& 0x0000FFFF)<<7);
			short y= (short) ((s& 0x0000FFFF)>>9);
			short temp = (short) (y|x);
			return temp;
	}

	public void decrypt(byte [] text){
		int dec=0;
		int ciphertext = Packing.packIntBigEndian(text, 0);
		short x = (short) ((ciphertext & 0xFFFF0000)>>16);
		short y = (short) ((ciphertext & 0x0000FFFF));

		for(int i=21; i>=0 ; i--){
			short value1= (short)(x ^ y);
			y= right_rotate_by_2((short)(x ^ y));
			short value2= (short) (x ^ k0[i]);
			short value3 = (short)((x ^ k0[i])- y);
			x= (short)(left_rotate_by_7((short)((x ^ k0[i])- y)));
			dec = x<<16 | (  y&0x0000FFFF);

		}
		byte[] temp2 = Hex.toByteArray(Hex.toString(dec));
		text[0]= temp2[0];
		text[1]= temp2[1];
		text[2]= temp2[2];
		text[3]= temp2[3];

	}
	public static void main(String[] args) {
		Decrypt s= new Decrypt();
		byte[] key =Hex.toByteArray(args[0]);
		byte[] plaintext = Hex.toByteArray(args[1]);
		s.setKey(key);
		s.key_schedule1();
		s.decrypt(plaintext);
		System.out.println(Hex.toString(plaintext));

	}

}
*/