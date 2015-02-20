package Programming_project_SPECK_v1;

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


public class EncryptFile {

	public static void main(String[] args) throws IOException{


		File plaintext= new File (args[1]);

		File ciphertext= new File( args[2]);
		byte [] key = Hex.toByteArray(args[0]);

		InputStream plain_t = new BufferedInputStream
				(new FileInputStream (plaintext));
		OutputStream cipher_t = new BufferedOutputStream
				(new FileOutputStream (ciphertext));
		
		
		Path path = Paths.get(plaintext.getAbsolutePath());
		byte[] p = Files.readAllBytes(path);
		
		List<Byte> d = new ArrayList<Byte>();
		for( int i=0 ;i< p.length;i++){
			d.add(p[i]);
		}
		d.add((byte) 0x80);

		if(d.size() % 4 !=0){
			while(d.size()%4 !=0 ){
				d.add((byte)0x00);
			}
		}
		
		byte [] padded = (byte[])pad_array(d);
		int len = padded.length;
		Encrypt e = new Encrypt(key, padded);
		e.setKey(key);
		e.key_schedule();
		byte [] hero;
		int iter=0;
		while( iter != padded.length){
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
		int ii=0;
		for( int i=0 ; i<padded.length ; i++){
			if(ii!=0 && ii%16==0)
				System.out.println();
			System.out.print(Hex.toString(padded[i])+" ");
			ii++; 
		}

		int f=0;
		while(f!= padded.length){
			cipher_t.write(padded[f]);
			f++;
		}
		//.close();
		plain_t.close();
		cipher_t.close();

	}

	private static byte[] pad_array(List<Byte> d) {
		byte [] temp = new byte[d.size()];
		for( int i=0; i< temp.length; i++){
			temp[i]= (byte)d.get(i);
		}
		return temp;
	}
}
