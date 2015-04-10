/*
 * Encrypt.java
 * Version :  $ak$
 * Revision: log $ak$
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import edu.rit.util.Hex;
import edu.rit.util.Packing;

/**
 * This program Decrypts the file, in ECB mode.
 * This is an implementation of block cipher SPECK 
 * @author Ajinkya Kale
 *
 */

public class DecryptFile {
	/**
	 * This is main program
	 * @param args commandline arguments
	 */
	public static void main(String[] args) throws IOException{
		if(args.length!=3){
			usage();
		}


		File plaintext= new File (args[1]);  // plaintext file
		File ciphertext= new File( args[2]); // cipher text file
		byte [] key = Hex.toByteArray(args[0]); // key
		InputStream  cipher_t= new BufferedInputStream
				(new FileInputStream (plaintext));
		OutputStream plain_t = new BufferedOutputStream
				(new FileOutputStream (ciphertext));

		Path path = Paths.get(plaintext.getAbsolutePath());
		byte [] p = Files.readAllBytes(path); // reads all the bytes of file

		Decrypt e = new Decrypt(key, p);
		e.setKey(key); // sets the key
		e.key_schedule1(); // generates subkeys

		int iter=0;
		while( iter != p.length){  // decrypts the 4 bytes at a time
			int prev= iter;
			byte [] temp = {(byte) (p[iter]) ,(byte)(p[++iter]), (byte)(p[++iter]), (byte)(p[++iter])};
			int temp2= Packing.packIntBigEndian(temp, 0);
			Packing.unpackIntBigEndian(temp2, temp, 0);
			e.decrypt(temp);
			p[prev]= temp[0];
			p[++prev]= temp[1];
			p[++prev]= temp[2];
			p[++prev]= temp[3];
			prev=0;
			iter++;
		}

		byte [] t = remove_padding(p); // orginaml plaintext without padding
		// writies plaintext without padding to the file
		int c=0;
		while(c!= t.length){
			plain_t.write(t[c]);
			c++;
		}
		plain_t.close();
		cipher_t.close();

	}

	/**
	 * This method removes padding from ciphertext
	 * @param ciphertext byte array
	 * @return temp
	 */
	public static byte[] remove_padding(byte [] ciphertext_ ){
		int i=ciphertext_.length-1;
		int counter=0;
		List<Byte> u = new ArrayList<Byte>();
		byte[] temp;

		while(ciphertext_[i]== 0x0000){
			counter++;
			i--;
		}


		for( int j=0; j<(ciphertext_.length-(counter+1)); j++){
			u.add(ciphertext_[j]);
		}

		temp= new byte[ciphertext_.length-(counter+1)];

		for(int k=0; k< temp.length; k++){
			temp[k]= u.get(k);
		}
		return temp;

	}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage() {
		System.err.println ("Usage: java DecryptFile <key> <ctfile> <plaintext>");
		System.err.println ("<ptfile> = Plaintext file name");
		System.err.println ("<ctfile> = Ciphertext file name");
		System.err.println ("<key> = Key(64 hex digits)");
		System.exit (1);
	}
}
