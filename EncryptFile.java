/*
 * Encrypt.java
 * Version :  $ak$
 * Revision: log $ak$
*/

import edu.rit.util.Packing;
import edu.rit.util.Hex;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This program encrypts the file, in ECB mode.
 * This is an implementation of bloack cipher SPECK 
 * @author Ajinkya Kale
 *
 */

public class EncryptFile {
	/**
	 * This is main program
	 * @param args commandline arguments
	*/
	public static void main(String[] args) throws IOException{

		if( args.length!=3){
		usage();
		}
		File plaintext= new File (args[1]); // plaintext file

		File ciphertext= new File( args[2]); // ciphertext file in which ciphertext will be stored
		byte [] key = Hex.toByteArray(args[0]); // key for encryption

		InputStream plain_t = new BufferedInputStream
				(new FileInputStream (plaintext));
		OutputStream cipher_t = new BufferedOutputStream
				(new FileOutputStream (ciphertext));

		Path path = Paths.get(plaintext.getAbsolutePath());
		byte[] p = Files.readAllBytes(path); // reads all bytes from file

		List<Byte> d = new ArrayList<Byte>(); 
		for( int i=0 ;i< p.length;i++){
			d.add(p[i]);
		}
		d.add((byte) 0x80); // adding padding

		if(d.size() % 4 !=0){ // padding 
			while(d.size()%4 !=0 ){
				d.add((byte)0x00);
			}
		}
		byte [] padded = (byte[])pad_array(d);  // padded array of bytes
		int len = padded.length;
		Encrypt e = new Encrypt(key, padded);
		e.setKey(key); // sets the key
		e.key_schedule(); // generates 22 subkyes
		int iter=0;  
		while( iter != padded.length){ // encrytion of 4 bytes ata time
			int prev= iter;
			byte [] temp = {(byte) (padded[iter]) ,(byte)(padded[++iter]), (byte)(padded[++iter]), (byte)(padded[++iter])};
			int temp2= Packing.packIntBigEndian(temp, 0);
			Packing.unpackIntBigEndian(temp2, temp, 0);
			e.encrypt(temp);
			padded[prev]= temp[0];
			padded[++prev]= temp[1];
			padded[++prev]= temp[2];
			padded[++prev]= temp[3];
			prev=0;
			iter++;
		}
			
				
		int f=0;
		// writing encrypted ciphertex to file
		while(f!= padded.length){
			cipher_t.write(padded[f]);
			f++;
		}

		plain_t.close();
		cipher_t.close();

	}
	
	/**
	 * This method converts the padded list to array of byte with padding
	 * @param d      List of Bytes
	 * @return temp
	*/
	private static byte[] pad_array(List<Byte> d) {
		byte [] temp = new byte[d.size()];
		for( int i=0; i< temp.length; i++){
			temp[i]= (byte)d.get(i);
		}
		return temp;
	}	
	private static void usage() {
      		System.err.println ("Usage: java EncryptFile <key> <plaintext> <ciphertext>");
      		System.err.println ("<ptfile> = Plaintext file name");
      		System.err.println ("<ctfile> = Ciphertext file name");
      		System.err.println ("<key> = Key (64 hex digits)");
      		System.exit (1);
      }
}
