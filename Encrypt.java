/*
 * Encrypt.java
 * Version :  $ak$
 * Revision: log $ak$
 */


import edu.rit.util.Packing;
import edu.rit.util.Hex;
import java.io.IOException;
/**
 * This program is an implementation of block cipher Speck.
 * It encryptes the given plaintext by XORing with key generated 
 * from 22 round key scheduler
 * 
 * @author  Ajinkya Kale
 *
 */
public class Encrypt implements BlockCipher{

	short [] k0= new short[22];  // stores subkeys Ki
	short [] l0 =new short [22]; // stores L0 values, 16 bits of key
	short [] l1 = new short[22]; // stores L1 values, 16 bits of key
	short [] l2 = new short [22];// stores L2 values, 16 bits of key
	byte [] plaintext; // stores plaintext 
	byte[] key;  // stores key 

	/**
	 * consturctor initializes the key and plaintext
	 */
	public Encrypt(byte [] key, byte []plaintext) {
		this.key= key;
		this.plaintext=plaintext;
	}

	/**
	 * returns blocksize 
	 */
	public int blockSize() {
		return 32;
	}
	/**
	 * returns keysize 
	 */
	public int keySize() {
		return 64;
	}

	/**
	 * This method sets the initial values of the key K0,L0,L1,L2
	 */

	public void setKey(byte[] key) {
		long key_1= Packing.packLongBigEndian(key, 0); 
		k0[0]= (short)(key_1 & 0x000000000000FFFFL);
		l0[0]= (short)((key_1 & 0x00000000FFFF0000L)>>16);
		l1[0]= (short)((key_1 & 0x0000FFFF00000000L)>>32);
		l2[0]= (short)((key_1 & 0xFFFF000000000000L)>>48);
	}


	/**
	 * This method encrypts the plaintext using the subkey.
	 * Encryption consists of 22 rounds
	 */
	public void encrypt(byte[] text) {
		int  you =0;
		int plaintext = Packing.packIntBigEndian(text, 0);
		short  x =(short)((plaintext &  0xFFFF0000)>>16) ;
		short y =(short)((plaintext & 0x0000FFFF));
		for(int i=0; i<22 ;i++){
			x= (short) ((( l_right_rotate(x) + y)^ this.k0[i]) );
			y = (short) (k_left_rotate(y)^x);
			you = x<<16|(y & 0x0000FFFF);
		}
		Packing.unpackIntBigEndian (you, text, 0);

	}

	/**
	 * This method produceds the 22 subkeys required to generate ciphertext
	 * 
	 */
	public void key_schedule(){

		int count=1; // marker 
		int l=1,k=1,m=1,j=1; // index for l0, l1, l2 and k respetively
		int first=0, second=0, third=0; 
		for(int i=0; i<21;i++){ // rounds 
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

	/**
	 * This method performs right rotation.
	 * @param s    
	 * @return temp  
	 */

	private short l_right_rotate(short s) {
		short x= (short) ((s& 0x0000FFFF)>>7);
		short y= (short) ((s& 0x0000FFFF)<<9);
		short temp = (short) (y|x);
		return temp;
	}

	/**
	 * This method perform left rotation.
	 * @param s
	 * @return temp
	 */
	private short k_left_rotate(short s){
		short y= (short)( (s& 0x0000FFFF)<<2);
		short x= (short)((s& 0x0000FFFF)>>14);
		short temp = (short) (y|x);
		return temp;
	}

	/**
	 * Prints the useage error message
	 */
	private static void usage(){
		System.err.println ("Usage: java EncryptFile <key> <plaintext>");
		System.err.println ("<ptfile> = Plaintext file name"); 
		System.err.println ("<key> = Key (64 hex digits)");
		System.exit (1);
	}

	/**
	 * This is main program
	 * @param args commandline arguments 
	 */
	public static void main(String[] args) {
		if(args.length !=2){
			usage();	
		}		
		byte[] key =Hex.toByteArray(args[0]);
		byte[] plaintext = Hex.toByteArray(args[1]);
		Encrypt s= new Encrypt(key, plaintext);
		s.setKey(key);
		s.key_schedule();
		s.encrypt(plaintext);
		System.out.println(Hex.toString(plaintext)); // printing the ciphertext  output

	}

}
