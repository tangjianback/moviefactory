package com.example.demo;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Operator {
    static IVCompressor compressor = new IVCompressor();
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
    public static void compress_video(String file_name)
    {
        File video_file = new File("/Users/tangjian/Desktop/test/"+file_name);
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
