package com.sinosoft.fragins.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

public class VfsFileStorage extends FileStorageSupport {

	private String baseUrl;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public void save(String fileId, InputStream in) {
		FileObject dir = null;
		FileObject file = null;
		OutputStream out = null;
		try {
			FileSystemManager fsManager = VFS.getManager();
			dir = fsManager.resolveFile(baseUrl);
			if (!dir.exists()) {
				dir.createFolder();
			}
			file = dir.resolveFile(fileId);
			file.createFile();
			out = file.getContent().getOutputStream();
			IOUtils.copy(in, out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(file);
			IOUtils.closeQuietly(dir);
		}
	}

	@Override
	public InputStream loadAsStream(String fileId) {
		FileObject dir = null;
		FileObject file = null;
		InputStream in = null;
		try {
			FileSystemManager fsManager = VFS.getManager();
			dir = fsManager.resolveFile(baseUrl);
			file = dir.getChild(fileId);
			if (file == null) {
				throw new RuntimeException("文件不存在：" + fileId);
			}
			in = file.getContent().getInputStream();
			return in;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String saveByUrl(String fileId, InputStream in) {
		FileObject dir = null;
		FileObject file = null;
		OutputStream out = null;
		try {
			FileSystemManager fsManager = VFS.getManager();
			dir = fsManager.resolveFile(baseUrl);
			if (!dir.exists()) {
				dir.createFolder();
			}
			file = dir.resolveFile(fileId);
			file.createFile();
			out = file.getContent().getOutputStream();
			IOUtils.copy(in, out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(file);
			IOUtils.closeQuietly(dir);
		}
		return "";
	}
}
