package com.ramostear.application;

//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class FileHandlingApplicationTests {
//
//	@Test
//	public void contextLoads() {
//		System.out.println("hellow");
//	}
//}




import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.System.exit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.naming.*;
import javax.jms.*;

@SpringBootTest
class DemoApplicationTests {

	@Test
	public void queue_test() {
		try
		{   //Create and start connection
			InitialContext ctx=new InitialContext();
			QueueConnectionFactory f=(QueueConnectionFactory)ctx.lookup("myQueueConnectionFactory");
			QueueConnection con=f.createQueueConnection();
			con.start();
			//2) create queue session
			QueueSession ses=con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			//3) get the Queue object
			Queue t=(Queue)ctx.lookup("myQueue");
			//4)create QueueSender object
			QueueSender sender=ses.createSender(t);
			//5) create TextMessage object
			TextMessage msg=ses.createTextMessage();

			//6) write message
			BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				System.out.println("Enter Msg, end to terminate:");
				String s=b.readLine();
				if (s.equals("end"))
					break;
				msg.setText(s);
				//7) send message
				sender.send(msg);
				System.out.println("Message successfully sent.");
			}
			//8) connection close
			con.close();
		}catch(Exception e){System.out.println(e);}
	}


	/**
	 * 将字节流转换成文件
	 * @param filename
	 * @param data
	 * @throws Exception
	 */
	public static void saveFile(String filename,byte [] data)throws Exception{
		if(data != null){
			String filepath ="/Users/tangjian/Desktop/test/" + filename;
			File file  = new File(filepath);
			if(file.exists()){
				file.delete();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data,0,data.length);
			fos.flush();
			fos.close();
		}
	}

	@Test
	void daoTest(){
		System.out.println("hellow");
		IVCompressor compressor = new IVCompressor();

		File video_file = new File("/Users/tangjian/Desktop/test/1.mp4");
		try {
			byte[] arr = compressor.reduceVideoSize(video_file, VideoFormats.MP4, ResizeResolution.R480P);
			saveFile("1test.mp4",arr);
		} catch (VideoException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

