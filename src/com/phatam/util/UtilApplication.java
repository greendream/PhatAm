/*
 * Copyright (C) 2014  Le Ngoc Anh <greendream.ait@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 */


package com.phatam.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONObject;

import android.content.Context;
import android.os.Build;

public class UtilApplication {

	/**
	 * Save JSON to Internal memory
	 * 
	 * @param json
	 * @param fileName
	 * @param ctx
	 */
	public static void saveJsonToInternalMemory(String json, String fileName, Context ctx) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(
												new FileWriter(
														new File(ctx.getFilesDir() + File.separator + fileName)
												)
											);
			
			bufferedWriter.write(json);
			bufferedWriter.close();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Load JSON from Internal memory
	 * 
	 * @param fileName
	 * @param ctx
	 * @return
	 */
	public static JSONObject loadJsonFromInternalMemory(String fileName, Context ctx) {
		JSONObject json = new JSONObject();
		try {
			BufferedReader bufferedReader = new BufferedReader(
												new FileReader(
														new File(ctx.getFilesDir() + File.separator + fileName)
												)
											);
			
			StringBuilder builder = new StringBuilder("");
			String read = "";
			while((read = bufferedReader.readLine()) != null){
				builder.append(read);
			}
			bufferedReader.close();
			
			json = new JSONObject(builder.toString());
		} catch (Exception e) {
			
		}
		
		return json;
	}
	
	/**
	 * Copy stream
	 * 
	 * @param iStream
	 * @param oStream
	 */
	public static void CopyStream(InputStream iStream, OutputStream oStream) {

		final int buffer_size = 1024;

		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = iStream.read(bytes, 0, buffer_size);
				if (count == -1) {
					break;
				}
				oStream.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

}