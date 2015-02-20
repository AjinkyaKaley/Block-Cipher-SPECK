/*package Programming_project_SPECK_v1;

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

public class DecryptFile {

	public static void main(String[] args) throws IOException{
		File plaintext= new File (args[1]);
		File ciphertext= new File( args[2]);
		byte [] key = Hex.toByteArray(args[0]);
		InputStream  cipher_t= new BufferedInputStream
				(new FileInputStream (plaintext));
		OutputStream plain_t = new BufferedOutputStream
				(new FileOutputStream (ciphertext));

		Path path = Paths.get(plaintext.getAbsolutePath());
		byte [] p = Files.readAllBytes(path);
		int jj=0;
		for( int i=0 ;i<p.length;i++){
			if(jj!=0 && jj%16==0)
				System.out.println();
			System.out.println(p[i] + " ");

			System.out.println();
		}
		Decrypt e = new Decrypt(key, p);
		e.setKey(key);
		e.key_schedule1();
		
		int iter=0;
		while( iter != p.length){
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
		
		
		
		byte [] t = remove_padding(p);
		int ii=0;
		for( int i=0 ; i<t.length ; i++){
			if(ii!=0 && ii%16==0)
				System.out.println();
			System.out.print(Hex.toString(t[i])+" ");
			ii++; 
		}
		//	e.setKey(key);

		
		//e.decrypt(p);


		// remove padding
		int c=0;
		while(c!= t.length){
			plain_t.write(t[c]);
			c++;
		}
		plain_t.close();
		cipher_t.close();

	}

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
}*/