package com.sinosoft.fragins.framework.utils;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileStorage {

	/**
	 * 存储数据
	 * 
	 * @param fileId 文件ID
	 * @param data   二进制数据
	 */
	public void save(String fileId, byte[] data);

	/**
	 * 复制流模式存储数据，适用于如将现有的磁盘上的文件保存到FileStorage的模式
	 * 
	 * @param fileId 文件ID
	 * @param in     表示文件内容的输入流，注意这个方法会读完整个流，因此输入流本身不能读到一半卡住，否则可能造成死锁
	 */
	public void save(String fileId, InputStream in);

	/**
	 * 流模式存储数据，适用于程序生成文件然后保存到FileStorage的模式
	 * 
	 * @param fileId 文件ID
	 * @return 返回一个供写入文件内容的OutputStream，应用需要自己close这个输出流，在close后才实际完成保存。
	 */
	public OutputStream save(String fileId);

	/**
	 * 读取数据
	 * 
	 * @param fileId 文件ID
	 * @return 二进制数据
	 */
	public byte[] load(String fileId);

	/**
	 * 流模式读取数据，适用于大文件
	 * 
	 * @param fileId 文件ID
	 * @return 用于读取文件内容的输入流
	 */
	public InputStream loadAsStream(String fileId);

	/**
	 * 读取数据并直接写入输出流
	 * 
	 * @param fileId 文件ID
	 * @param out    输出流
	 */
	public void load(String fileId, OutputStream out);

	/**
	 * 存储数据,返回url
	 *
	 * @param fileId 文件ID
	 * @param data   二进制数据
	 */
	public String saveByUrl(String fileId, byte[] data);

	public String saveByUrl(String fileId, InputStream in);

}
