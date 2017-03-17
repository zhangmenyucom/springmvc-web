package com.taylor.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOUtils {
	/**
	 * @desc Byte2Object(字节转换为对象)
	 * @param bytes
	 * @author xiaolu.zhang
	 * @date 2017年3月16日 上午10:57:51
	 */
	public static Object Byte2Object(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);
			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * @desc ObjectToByte(对象转化为字节)
	 * @param obj
	 * @author xiaolu.zhang
	 * @date 2017年3月16日 上午10:59:30
	 */
	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}

}
