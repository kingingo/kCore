package eu.epicpvp.kcore.EncryptionUtil.Streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PushbackStream extends OutputStream{
	ByteArrayOutputStream out;
	
	public PushbackStream(){
		out = new ByteArrayOutputStream();
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
	}

	@Override
	public void write(byte[] b) throws IOException {
		out.write(b);
	}
	
	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	public byte[] pushback(){
		return out.toByteArray();
	}
}
