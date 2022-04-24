package com.sinosoft.fragins.framework.utils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * FileStorage具体实现类的公共父类，提供一些流转换的功能
 *
 * @author mrpan
 *
 */
public abstract class FileStorageSupport implements FileStorage {

	@Override
	public void save(String fileId, byte[] data) {
		save(fileId, new ByteArrayInputStream(data));
	}

	@Override
	public OutputStream save(String fileId) {
		try {
			File tempFile = File.createTempFile(fileId, null);
			return new TempFileOutputStream(fileId, tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] load(String fileId) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		load(fileId, baos);
		return baos.toByteArray();
	}

	@Override
	public void load(String fileId, OutputStream out) {
		try (InputStream in = loadAsStream(fileId)) {
			IOUtils.copy(in, out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private class TempFileOutputStream extends FilterOutputStream {

		String fileId;
		File tempFile;

		public TempFileOutputStream(String fileId, File tempFile) throws IOException {
			super(new FileOutputStream(tempFile));
			this.fileId = fileId;
			this.tempFile = tempFile;
		}

		@Override
		public void close() throws IOException {
			super.close();
			FileStorageSupport.this.save(fileId, new FileInputStream(tempFile));
		}

	}

    @Override
    public String saveByUrl(String fileId, byte[] data) {
        return saveByUrl(fileId, new ByteArrayInputStream(data));
    }
}
